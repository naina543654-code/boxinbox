package top.niunaijun.blackbox.fake.spoof;

import java.lang.reflect.Method;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;
import top.niunaijun.blackbox.utils.Slog;

/**
 * Spoofs JVM-level properties that leak host hardware/firmware identity.
 *
 * <p>Why this exists: {@code System.getProperty("os.version")} returns the
 * host kernel version (e.g. {@code 4.19.304-perf+}) straight from the JVM —
 * no binder call, no {@code android.os.SystemProperties} read, so neither
 * the binder proxies nor {@link BSpoofSystemProps} can intercept it. A guest
 * comparing the kernel against the spoofed device profile (a 2023 phone
 * would never ship a 4.19 kernel) immediately sees through the sandbox.
 * Hooking {@code java.lang.System.getProperty} catches every reader.
 *
 * <p>Only the {@code os.version} key is rewritten, and only to the profile's
 * per-API plausible kernel (5.10.x for API 33, 6.1.x for API 34). Every
 * other key passes through untouched.
 *
 * <p>Recursion safety: the hook body calls
 * {@code BSpoofManager.isSpoofActive()} → {@code ensureLoaded()}, which
 * resolves the profile path via {@code applicationInfo.dataDir} — a plain
 * field read, never {@code System.getProperty} — so the hook cannot
 * re-enter itself. {@code System.getProperty} is extremely hot; the
 * inactive path is a single synchronized boolean check plus one string
 * comparison.
 *
 * <p>Fail-open: if the Pine hook cannot be installed, or no spoof profile is
 * active, property reads pass through untouched.
 *
 * <p>Scope note: Pine hooks are process-wide. This installer runs only in
 * guest/server processes (see {@code JvmPropSpoofInjector}, registered in
 * {@code HookManager}), and every call additionally gates on
 * {@code BSpoofManager.isSpoofActive()}.
 */
public final class BSpoofJvmProps {
    private static final String TAG = "BSpoofJvmProps";
    private static volatile boolean sInstalled = false;

    private BSpoofJvmProps() {
    }

    /** Installs the Pine hooks. Safe to call repeatedly; failures never throw. */
    public static void install() {
        if (sInstalled) {
            return;
        }
        synchronized (BSpoofJvmProps.class) {
            if (sInstalled) {
                return;
            }
            try {
                hookGetProperty(new Class<?>[]{String.class});
                hookGetProperty(new Class<?>[]{String.class, String.class});
                sInstalled = true;
                Slog.d(TAG, "System.getProperty os.version hook installed");
            } catch (Throwable t) {
                // Fail open: JVM properties pass through unspoofed.
                Slog.e(TAG, "System.getProperty hook install failed; props pass through", t);
            }
        }
    }

    private static void hookGetProperty(Class<?>[] params) {
        final Method target;
        try {
            target = System.class.getDeclaredMethod("getProperty", params);
        } catch (Throwable t) {
            Slog.e(TAG, "System.getProperty/" + params.length + " not found; skipping", t);
            return;
        }
        try {
            Pine.hook(target, new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable()) {
                        return;
                    }
                    Object[] args = callFrame.args;
                    if (args == null || args.length == 0
                            || !"os.version".equals(args[0])) {
                        return;
                    }
                    BSpoofManager spoof = BSpoofManager.get();
                    if (spoof == null || !spoof.isSpoofActive()) {
                        return;
                    }
                    String kernel = spoof.getKernelVersion();
                    if (kernel != null) {
                        callFrame.setResult(kernel);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on System.getProperty failed", t);
        }
    }
}
