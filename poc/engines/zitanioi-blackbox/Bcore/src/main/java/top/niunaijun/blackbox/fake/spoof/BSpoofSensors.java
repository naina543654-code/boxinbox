package top.niunaijun.blackbox.fake.spoof;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;
import top.niunaijun.blackbox.utils.Slog;

/**
 * Profile-driven sensor visibility filtering for the PoC spoofing build.
 *
 * <p>{@code android.hardware.SensorManager} is NOT a binder service — it is
 * client-side Java over native code, so the engine's binder-proxy mechanism
 * cannot intercept it. Instead we use Pine (the ART inline-hook engine that
 * ships with this engine for Xposed support) to hook
 * {@code SensorManager.getSensorList(int)}, {@code getDefaultSensor(int)} and
 * {@code getDefaultSensor(int, boolean)} directly.
 *
 * <p>ENFORCED: the guest only sees sensors whose type is in the profile's
 * {@code sensors[]} list ({@link BSpoofManager#getSensorTypes()}). No
 * {@code Sensor} shadows are constructed — {@code android.hardware.Sensor} has
 * no public constructor and synthesising one via {@code Parcel} would be
 * version-fragile — so filtering operates on the real sensor objects.
 *
 * <p>NOT IMPLEMENTED (stretch goal, see class javadoc note): synthetic live
 * readings via {@code registerListener} interception. Delivering fake
 * {@code SensorEvent}s would require constructing {@code SensorEvent} (no
 * public constructor; hidden constructor + field injection, version-fragile),
 * intercepting 6+ {@code registerListener} overloads, and emulating flush /
 * accuracy-change semantics. List filtering covers the profile contract.
 *
 * <p>All hooks are fail-open: any failure leaves the real sensor list
 * untouched, and when spoofing is inactive (or the profile has no sensor
 * list) every call passes through unmodified.
 *
 * <p>Scope note: Pine hooks are process-wide. Injectors run in guest
 * (BAppClient) processes only, so the host app's own process is unaffected —
 * but within a guest process the filter applies to every
 * {@code SensorManager} caller while spoofing is active.
 */
public class BSpoofSensors {
    private static final String TAG = "BSpoofSensors";

    private static volatile boolean sInstalled;

    private BSpoofSensors() {
    }

    /** Installs the Pine hooks. Safe to call repeatedly; failures never throw. */
    public static void install() {
        if (sInstalled) {
            return;
        }
        synchronized (BSpoofSensors.class) {
            if (sInstalled) {
                return;
            }
            try {
                hookGetSensorList();
                hookGetDefaultSensor();
                sInstalled = true;
                Slog.d(TAG, "sensor visibility hooks installed");
            } catch (Throwable t) {
                // Fail open: sensors pass through unfiltered.
                Slog.e(TAG, "sensor spoof install failed; sensors pass through", t);
            }
        }
    }

    private static void hookGetSensorList() {
        final Method target;
        try {
            target = SensorManager.class.getDeclaredMethod("getSensorList", int.class);
        } catch (Throwable t) {
            Slog.e(TAG, "getSensorList(int) not found; skipping", t);
            return;
        }
        try {
            Pine.hook(target, new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable()) {
                        return;
                    }
                    Set<Integer> allowed = allowedTypes();
                    if (allowed == null) {
                        return; // spoof inactive / no profile list -> passthrough
                    }
                    Object result = callFrame.getResult();
                    if (!(result instanceof List)) {
                        return;
                    }
                    List<?> sensors = (List<?>) result;
                    List<Sensor> filtered = new ArrayList<>(sensors.size());
                    for (Object o : sensors) {
                        if (o instanceof Sensor && allowed.contains(((Sensor) o).getType())) {
                            filtered.add((Sensor) o);
                        }
                    }
                    callFrame.setResult(filtered);
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on getSensorList failed", t);
        }
    }

    private static void hookGetDefaultSensor() {
        try {
            Pine.hook(SensorManager.class.getDeclaredMethod("getDefaultSensor", int.class),
                    defaultSensorFilter());
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on getDefaultSensor(int) failed", t);
        }
        try {
            Pine.hook(SensorManager.class.getDeclaredMethod(
                    "getDefaultSensor", int.class, boolean.class), defaultSensorFilter());
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on getDefaultSensor(int,boolean) failed", t);
        }
    }

    private static MethodHook defaultSensorFilter() {
        return new MethodHook() {
            @Override
            public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                if (callFrame.hasThrowable()) {
                    return;
                }
                Set<Integer> allowed = allowedTypes();
                if (allowed == null) {
                    return; // passthrough
                }
                Object result = callFrame.getResult();
                if (result instanceof Sensor
                        && !allowed.contains(((Sensor) result).getType())) {
                    callFrame.setResult(null);
                }
            }
        };
    }

    /**
     * @return the profile's allowed sensor types, or {@code null} when the
     *         filter must not apply (spoof inactive, no profile list, or any
     *         error) — callers treat {@code null} as passthrough.
     */
    private static Set<Integer> allowedTypes() {
        try {
            BSpoofManager spoof = BSpoofManager.get();
            if (spoof == null || !spoof.isSpoofActive()) {
                return null;
            }
            int[] types = spoof.getSensorTypes();
            if (types == null || types.length == 0) {
                return null;
            }
            Set<Integer> set = new HashSet<>(types.length * 2);
            for (int type : types) {
                set.add(type);
            }
            return set;
        } catch (Throwable t) {
            return null;
        }
    }
}
