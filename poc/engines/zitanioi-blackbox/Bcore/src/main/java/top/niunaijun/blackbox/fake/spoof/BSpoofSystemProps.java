package top.niunaijun.blackbox.fake.spoof;

import java.lang.reflect.Method;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;
import top.niunaijun.blackbox.utils.Slog;

/**
 * Spoofs telephony operator identity at the {@code android.os.SystemProperties}
 * choke point.
 *
 * <p>Why this exists: on modern Android (API 30+) several
 * {@code TelephonyManager} getters — {@code getNetworkOperatorName()},
 * {@code getNetworkOperator()}, {@code getNetworkCountryIso()} and their
 * slot-index overloads — do <b>not</b> make a binder call. They read the
 * {@code gsm.operator.*} / {@code gsm.sim.operator.*} system properties
 * directly (via {@code TelephonyProperties}). The binder-level hooks in
 * {@code ITelephonyManagerProxy} therefore never fire for these getters, and
 * guests observe the real host operator values. Hooking
 * {@code SystemProperties.get()} catches every reader regardless of API level
 * or overload.
 *
 * <p>Fail-open: if the Pine hook cannot be installed, or no spoof profile is
 * active, property reads pass through untouched. The hook is a no-op for keys
 * outside the telephony operator set.
 *
 * <p>Scope note: Pine hooks are process-wide. This installer runs only in
 * guest/server processes (see {@code SysPropSpoofInjector}, registered in
 * {@code HookManager}), and every call additionally gates on
 * {@code BSpoofManager.isSpoofActive()}.
 */
public final class BSpoofSystemProps {
    private static final String TAG = "BSpoofSysProps";
    private static volatile boolean sInstalled = false;

    private BSpoofSystemProps() {
    }

    /** Installs the Pine hooks. Safe to call repeatedly; failures never throw. */
    public static void install() {
        if (sInstalled) {
            return;
        }
        synchronized (BSpoofSystemProps.class) {
            if (sInstalled) {
                return;
            }
            try {
                // SystemProperties is a hidden class: not in android.jar, so
                // resolve it reflectively. We never *invoke* it via reflection
                // (hidden-API restricted); Pine hooks the ArtMethod natively.
                Class<?> sp = Class.forName("android.os.SystemProperties");
                hookGet(sp, new Class<?>[]{String.class});
                hookGet(sp, new Class<?>[]{String.class, String.class});
                // Typed overloads: detectors read ro.debuggable/ro.secure
                // through getInt/getBoolean, which the String hooks never see.
                hookTypedGet(sp, "getInt", new Class<?>[]{String.class, int.class});
                hookTypedGet(sp, "getLong", new Class<?>[]{String.class, long.class});
                hookTypedGet(sp, "getBoolean", new Class<?>[]{String.class, boolean.class});
                sInstalled = true;
                Slog.d(TAG, "SystemProperties telephony hooks installed");
            } catch (Throwable t) {
                // Fail open: telephony properties pass through unspoofed.
                Slog.e(TAG, "SystemProperties hook install failed; props pass through", t);
            }
        }
    }

    private static void hookGet(Class<?> sp, Class<?>[] params) {
        final Method target;
        try {
            target = sp.getDeclaredMethod("get", params);
        } catch (Throwable t) {
            Slog.e(TAG, "SystemProperties.get/" + params.length + " not found; skipping", t);
            return;
        }
        try {
            Pine.hook(target, new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable()) {
                        return;
                    }
                    // Fast bail: single volatile boolean once the profile state
                    // is cached. This method is extremely hot; keep the
                    // inactive path as cheap as possible.
                    BSpoofManager spoof = BSpoofManager.get();
                    if (!spoof.isSpoofActive()) {
                        return;
                    }
                    Object[] args = callFrame.args;
                    if (args == null || args.length == 0 || !(args[0] instanceof String)) {
                        return;
                    }
                    String spoofed = spoofedValueFor(spoof, (String) args[0]);
                    if (spoofed != null) {
                        callFrame.setResult(spoofed);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on SystemProperties.get failed", t);
        }
    }

    /**
     * Returns the spoofed value for a telephony operator property key, or
     * null to pass through. Prefix matching covers per-subscription suffixed
     * variants (e.g. {@code gsm.operator.alpha.2}).
     */
    private static String spoofedValueFor(BSpoofManager spoof, String key) {
        if (key == null) {
            return null;
        }
        if (startsWithAny(key, "gsm.operator.alpha", "gsm.sim.operator.alpha")) {
            return spoof.getOperatorName();
        }
        if (startsWithAny(key, "gsm.operator.numeric", "gsm.sim.operator.numeric")) {
            return spoof.getOperatorNumeric();
        }
        if (startsWithAny(key, "gsm.operator.iso-country", "gsm.sim.operator.iso-country")) {
            return spoof.getCountryIso();
        }
        // Root-hiding: report a locked-down, non-debuggable production
        // device. Part of BRootHide's choke-point coverage (see its docs).
        if (key.equals("ro.debuggable")) {
            return "0";
        }
        if (key.equals("ro.secure")) {
            return "1";
        }
        if (key.equals("ro.build.tags")) {
            return "release-keys";
        }
        if (key.equals("ro.build.type")) {
            return "user";
        }
        // Audit fix 2026-10-04: the real serial is stable across identities
        // and readable with zero permission — never pass it through.
        if (key.equals("ro.serialno")) {
            return spoof.getSerial();
        }
        // Re-audit 2026-10-04: ro.boot.serialno exposes the same hardware
        // serial (boot properties are readable with zero permission).
        if (key.equals("ro.boot.serialno")) {
            return spoof.getSerial();
        }
        // Re-audit 2026-10-04: gsm.version.baseband leaked at this Java layer
        // (the BSpoofManager table only covered the getprop/exec path).
        if (key.equals("gsm.version.baseband")) {
            return spoof.getBuildField("RADIO");
        }
        return null;
    }

    private static boolean startsWithAny(String key, String... prefixes) {
        for (String p : prefixes) {
            if (key.startsWith(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Hooks a typed overload ({@code getInt(String,int)},
     * {@code getLong(String,long)}, {@code getBoolean(String,boolean)}).
     * Only keys our spoof table covers are rewritten; everything else
     * passes through to the real implementation.
     */
    private static void hookTypedGet(Class<?> sp, final String methodName, Class<?>[] params) {
        final Method target;
        try {
            target = sp.getDeclaredMethod(methodName, params);
        } catch (Throwable t) {
            Slog.e(TAG, "SystemProperties." + methodName + " not found; skipping", t);
            return;
        }
        try {
            Pine.hook(target, new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable()) {
                        return;
                    }
                    BSpoofManager spoof = BSpoofManager.get();
                    if (!spoof.isSpoofActive()) {
                        return;
                    }
                    Object[] args = callFrame.args;
                    if (args == null || args.length == 0 || !(args[0] instanceof String)) {
                        return;
                    }
                    String spoofed = spoofedValueFor(spoof, (String) args[0]);
                    if (spoofed == null) {
                        return;
                    }
                    try {
                        if ("getInt".equals(methodName)) {
                            callFrame.setResult(Integer.parseInt(spoofed));
                        } else if ("getLong".equals(methodName)) {
                            callFrame.setResult(Long.parseLong(spoofed));
                        } else {
                            callFrame.setResult("1".equals(spoofed)
                                    || "true".equalsIgnoreCase(spoofed));
                        }
                    } catch (NumberFormatException e) {
                        // Spoof value isn't numeric for this overload; leave
                        // the real value rather than crash the caller.
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on SystemProperties." + methodName + " failed", t);
        }
    }
}
