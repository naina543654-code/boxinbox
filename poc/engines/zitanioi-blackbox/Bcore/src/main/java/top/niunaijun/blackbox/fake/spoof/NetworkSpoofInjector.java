package top.niunaijun.blackbox.fake.spoof;

import top.niunaijun.blackbox.fake.hook.IInjectHook;

/**
 * HookManager injector for hiding the host's real VPN from guests.
 *
 * <p>Registered via {@code addInjector(new NetworkSpoofInjector());} —
 * installs the Pine hooks in {@link BSpoofNetwork}. Install failures are
 * fail-open (the real network description passes through) and never throw
 * out of {@link #injectHook()}.
 */
public class NetworkSpoofInjector implements IInjectHook {

    @Override
    public void injectHook() {
        BSpoofNetwork.install();
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
