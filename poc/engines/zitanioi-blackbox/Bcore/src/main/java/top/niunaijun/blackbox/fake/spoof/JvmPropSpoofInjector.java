package top.niunaijun.blackbox.fake.spoof;

import top.niunaijun.blackbox.fake.hook.IInjectHook;

/**
 * HookManager injector for JVM property spoofing (os.version kernel).
 *
 * <p>Registered via {@code addInjector(new JvmPropSpoofInjector());} —
 * installs the Pine hooks in {@link BSpoofJvmProps}. Install failures are
 * fail-open (properties pass through unspoofed) and never throw out of
 * {@link #injectHook()}.
 */
public class JvmPropSpoofInjector implements IInjectHook {

    @Override
    public void injectHook() {
        BSpoofJvmProps.install();
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
