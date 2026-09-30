package top.niunaijun.blackbox.fake.spoof;

import top.niunaijun.blackbox.fake.hook.IInjectHook;

/**
 * HookManager injector for profile-driven sensor visibility filtering.
 *
 * <p>Registered by the coordinator via
 * {@code addInjector(new SensorSpoofInjector());} — installs the Pine hooks in
 * {@link BSpoofSensors}. Install failures are fail-open (sensors pass through
 * unfiltered) and never throw out of {@link #injectHook()}.
 */
public class SensorSpoofInjector implements IInjectHook {

    @Override
    public void injectHook() {
        BSpoofSensors.install();
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
