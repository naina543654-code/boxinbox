package top.niunaijun.blackbox.fake.spoof;

import top.niunaijun.blackbox.fake.hook.IInjectHook;

/**
 * HookManager injector for OS-level identity Pine hooks.
 *
 * <p>Registered by the coordinator via
 * {@code addInjector(new OsIdentitySpoofInjector());} — installs the Pine hooks in
 * {@link BSpoofOsIdentity}. Install failures are fail-open (values pass through
 * unspoofed) and never throw out of {@link #injectHook()}.
 */
public class OsIdentitySpoofInjector implements IInjectHook {

    @Override
    public void injectHook() {
        BSpoofOsIdentity.install();
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
