package top.niunaijun.blackbox.fake.spoof;

import top.niunaijun.blackbox.fake.hook.IInjectHook;

/**
 * HookManager injector for the SubscriptionManager/SubscriptionInfo Pine
 * hooks in {@link BSubscriptionSpoof}. Registered via
 * {@code addInjector(new SubscriptionSpoofInjector());}.
 */
public class SubscriptionSpoofInjector implements IInjectHook {

    @Override
    public void injectHook() {
        BSubscriptionSpoof.install();
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
