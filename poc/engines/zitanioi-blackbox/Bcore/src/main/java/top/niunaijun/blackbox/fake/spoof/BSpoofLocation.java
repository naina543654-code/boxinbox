package top.niunaijun.blackbox.fake.spoof;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import top.niunaijun.blackbox.app.BActivityThread;
import top.niunaijun.blackbox.entity.location.BLocation;
import top.niunaijun.blackbox.fake.frameworks.BLocationManager;
import top.niunaijun.blackbox.utils.Slog;

/**
 * Profile-driven fake location for the PoC spoofing build.
 *
 * <p>Flow: profile JSON (written by the host app, read by {@link BSpoofManager})
 * -&gt; {@link #ensureSeeded()} builds a {@link BLocation} and stores it via
 * {@link BLocationManager#setLocation(int, String, BLocation)} in
 * {@link BLocationManager#OWN_MODE} -&gt; the existing
 * {@code ILocationManagerProxy} hooks serve it unchanged through
 * {@code BLocation.convert2SystemLocation()}.
 *
 * <p>Movement simulation: when {@code movement.enabled} is set in the profile,
 * a single static daemon scheduler advances the stored fix every
 * {@link #MOVE_TICK_MS} ms (equirectangular projection, speed m/s along
 * bearing). The engine's listener loop
 * ({@code BLocationManagerService.addTask}) polls the stored location, so
 * registered {@code LocationListener}s observe the moving fix with no further
 * wiring.
 */
public class BSpoofLocation {
    private static final String TAG = "BSpoofLocation";

    /** Mean Earth radius, metres (equirectangular projection). */
    private static final double EARTH_RADIUS_M = 6371000.0;
    /** Movement tick interval. */
    private static final long MOVE_TICK_MS = 2000L;

    /** "userId:pkg" keys already seeded in this process. */
    private static final Set<String> sSeeded =
            Collections.synchronizedSet(new HashSet<String>());

    private static final Object sMoverLock = new Object();
    private static ScheduledExecutorService sMover;

    private BSpoofLocation() {
    }

    private static String key(int userId, String pkg) {
        return userId + ":" + pkg;
    }

    /**
     * Called at the top of the spoofed {@code ILocationManagerProxy} hooks.
     * When spoofing is active, seeds this guest's fix from the profile once per
     * process and starts movement simulation if the profile enables it. When
     * spoofing is inactive, restores passthrough by clearing any seed left by a
     * previous identity.
     */
    public static void ensureSeeded() {
        final int userId = BActivityThread.getUserId();
        final String pkg = BActivityThread.getAppPackageName();
        final String key = key(userId, pkg);

        BSpoofManager spoof;
        boolean active;
        try {
            spoof = BSpoofManager.get();
            active = spoof != null && spoof.isSpoofActive();
        } catch (Throwable t) {
            Slog.e(TAG, "ensureSeeded: BSpoofManager unavailable", t);
            return;
        }

        if (!active) {
            if (sSeeded.remove(key)) {
                BLocationManager.disableFakeLocation(userId, pkg);
            }
            return;
        }

        if (sSeeded.contains(key)) {
            return;
        }

        try {
            BLocation fix = buildFix(spoof);
            BLocationManager lm = BLocationManager.get();
            lm.setLocation(userId, pkg, fix);
            lm.setPattern(userId, pkg, BLocationManager.OWN_MODE);
            sSeeded.add(key);
            Slog.d(TAG, "seeded profile fix for " + key + ": " + fix);
            if (spoof.isMovementEnabled()) {
                startMovement();
            }
        } catch (Throwable t) {
            Slog.e(TAG, "ensureSeeded failed for " + key, t);
        }
    }

    /** Clears all seeds in this process (test hook / identity teardown). */
    public static void reset() {
        synchronized (sMoverLock) {
            if (sMover != null) {
                sMover.shutdownNow();
                sMover = null;
            }
        }
        synchronized (sSeeded) {
            for (String k : sSeeded) {
                int idx = k.indexOf(':');
                try {
                    BLocationManager.disableFakeLocation(
                            Integer.parseInt(k.substring(0, idx)), k.substring(idx + 1));
                } catch (Throwable ignored) {
                }
            }
            sSeeded.clear();
        }
    }

    private static BLocation buildFix(BSpoofManager spoof) {
        BLocation fix = new BLocation(spoof.getLatitude(), spoof.getLongitude());
        fix.setAltitude(spoof.getAltitude());
        fix.setAccuracy(spoof.getLocationAccuracy());
        float speed = spoof.isMovementEnabled()
                ? (float) spoof.getMovementSpeedMps() : 0f;
        float bearing = spoof.isMovementEnabled()
                ? (float) spoof.getMovementBearingDeg() : 0f;
        fix.setSpeed(speed);
        fix.setBearing(bearing);
        return fix;
    }

    private static void startMovement() {
        synchronized (sMoverLock) {
            if (sMover != null) {
                return;
            }
            sMover = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "spoof-location-mover");
                    t.setDaemon(true);
                    return t;
                }
            });
            sMover.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    movementTick();
                }
            }, MOVE_TICK_MS, MOVE_TICK_MS, TimeUnit.MILLISECONDS);
            Slog.d(TAG, "movement simulation started");
        }
    }

    private static void stopMovement() {
        synchronized (sMoverLock) {
            if (sMover != null) {
                sMover.shutdownNow();
                sMover = null;
                Slog.d(TAG, "movement simulation stopped");
            }
        }
    }

    private static void movementTick() {
        try {
            BSpoofManager spoof = BSpoofManager.get();
            if (spoof == null || !spoof.isSpoofActive() || !spoof.isMovementEnabled()) {
                stopMovement();
                return;
            }
            final double speedMps = spoof.getMovementSpeedMps();
            final double bearingDeg = spoof.getMovementBearingDeg();
            final double dtSec = MOVE_TICK_MS / 1000.0;
            final BLocationManager lm = BLocationManager.get();

            synchronized (sSeeded) {
                for (String k : sSeeded) {
                    int idx = k.indexOf(':');
                    int userId;
                    String pkg;
                    try {
                        userId = Integer.parseInt(k.substring(0, idx));
                        pkg = k.substring(idx + 1);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    BLocation cur = lm.getLocation(userId, pkg);
                    if (cur == null) {
                        continue;
                    }
                    double[] ll = advance(cur.getLatitude(), cur.getLongitude(),
                            speedMps, bearingDeg, dtSec);
                    BLocation next = new BLocation(ll[0], ll[1]);
                    next.setAltitude(spoof.getAltitude());
                    next.setAccuracy(spoof.getLocationAccuracy());
                    next.setSpeed((float) speedMps);
                    next.setBearing((float) bearingDeg);
                    lm.setLocation(userId, pkg, next);
                }
            }
        } catch (Throwable t) {
            Slog.e(TAG, "movementTick failed", t);
        }
    }

    /**
     * Advances a fix by {@code speedMps * dtSec} metres along
     * {@code bearingDeg} using a simple equirectangular projection.
     */
    static double[] advance(double latDeg, double lonDeg,
                            double speedMps, double bearingDeg, double dtSec) {
        double distanceM = speedMps * dtSec;
        double bearingRad = Math.toRadians(bearingDeg);
        double latRad = Math.toRadians(latDeg);
        double dLat = distanceM * Math.cos(bearingRad) / EARTH_RADIUS_M;
        double cosLat = Math.cos(latRad);
        // Guard the poles where longitude is degenerate.
        double dLon = Math.abs(cosLat) < 1e-9 ? 0
                : distanceM * Math.sin(bearingRad) / (EARTH_RADIUS_M * cosLat);
        return new double[]{latDeg + Math.toDegrees(dLat), lonDeg + Math.toDegrees(dLon)};
    }
}
