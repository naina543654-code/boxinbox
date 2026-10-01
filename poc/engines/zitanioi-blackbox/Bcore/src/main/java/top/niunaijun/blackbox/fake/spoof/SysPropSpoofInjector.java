package top.niunaijun.blackbox.fake.spoof;

import top.niunaijun.blackbox.fake.hook.IInjectHook;

/**
 * HookManager injector for telephony operator identity spoofing at the
 * SystemProperties choke point.
 *
 * <p>Registered via {@code addInjector(new SysPropSpoofInjector());} —
 * installs the Pine hooks in {@link BSpoofSystemProps}. Install failures are
 * fail-open (properties pass through unspoofed) and never throw out of
 * {@link #injectHook()}.
 */
public class SysPropSpoofInjector implements IInjectHook {

    @Override
    public void injectHook() {
        BSpoofSystemProps.install();
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
