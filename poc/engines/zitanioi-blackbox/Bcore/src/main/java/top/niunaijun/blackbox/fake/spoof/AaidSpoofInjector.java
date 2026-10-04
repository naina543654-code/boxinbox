package top.niunaijun.blackbox.fake.spoof;

import top.niunaijun.blackbox.fake.hook.IInjectHook;

/**
 * HookManager injector for per-identity advertising ID spoofing.
 *
 * <p>Registered via {@code addInjector(new AaidSpoofInjector());} —
 * installs the Pine hook in {@link BSpoofAaid}. Install failures are
 * fail-open (the real AAID passes through) and never throw out of
 * {@link #injectHook()}.
 */
public class AaidSpoofInjector implements IInjectHook {

    @Override
    public void injectHook() {
        BSpoofAaid.install();
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
