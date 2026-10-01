package top.niunaijun.blackbox.fake.spoof;

import top.niunaijun.blackbox.fake.hook.IInjectHook;

/**
 * HookManager injector for root/debugger hiding in guest processes.
 *
 * <p>Registered via {@code addInjector(new RootHideInjector());} —
 * installs the Pine hooks in {@link BRootHide}. Install failures are
 * fail-open (root artifacts remain visible) and never throw out of
 * {@link #injectHook()}.
 */
public class RootHideInjector implements IInjectHook {

    @Override
    public void injectHook() {
        BRootHide.install();
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
