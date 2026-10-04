package top.niunaijun.blackbox.fake.spoof;

import top.niunaijun.blackbox.fake.hook.IInjectHook;

/**
 * HookManager injector for the Widevine deviceUniqueId Pine hook in
 * {@link BSpoofMediaDrm}. Registered via
 * {@code addInjector(new MediaDrmSpoofInjector());}.
 */
public class MediaDrmSpoofInjector implements IInjectHook {

    @Override
    public void injectHook() {
        BSpoofMediaDrm.install();
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
