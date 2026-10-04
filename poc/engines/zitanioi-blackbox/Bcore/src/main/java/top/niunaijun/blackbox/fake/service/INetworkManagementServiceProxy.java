package top.niunaijun.blackbox.fake.service;

import static top.niunaijun.blackbox.app.BActivityThread.getUid;

import java.lang.reflect.Method;

import black.android.os.BRINetworkManagementServiceStub;
import black.android.os.BRServiceManager;
import top.niunaijun.blackbox.fake.hook.BinderInvocationStub;
import top.niunaijun.blackbox.fake.hook.MethodHook;
import top.niunaijun.blackbox.fake.hook.ProxyMethod;
import top.niunaijun.blackbox.fake.service.base.UidMethodProxy;
import top.niunaijun.blackbox.utils.MethodParameterUtils;

/**
 * Created by BlackBox on 2022/3/5.
 */
public class INetworkManagementServiceProxy extends BinderInvocationStub {
    public static final String NAME = "network_management";

    public INetworkManagementServiceProxy() {
        super(BRServiceManager.get().getService(NAME));
    }

    @Override
    protected Object getWho() {
        return BRINetworkManagementServiceStub.get().asInterface(BRServiceManager.get().getService(NAME));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(NAME);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        super.onBindMethod();
        addMethodHook(new UidMethodProxy("setUidCleartextNetworkPolicy", 0));
        addMethodHook(new UidMethodProxy("setUidMeteredNetworkBlacklist", 0));
        addMethodHook(new UidMethodProxy("setUidMeteredNetworkWhitelist", 0));
    }

    @ProxyMethod("getNetworkStatsUidDetail")
    public static class getNetworkStatsUidDetail extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstUid(args);
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }

    /**
     * R4 audit 2026-10-05 (R4-11): {@code getInterfaceConfig("wlan0")} carries
     * no permission enforcement — a guest reaching the
     * {@code network_management} binder via hidden-API ServiceManager
     * reflection could read the real Wi-Fi MAC, bypassing the
     * {@code NetworkInterface.getHardwareAddress()} Pine hook. Rewrite the
     * hardware address to the per-identity MAC for wlan0 when active.
     * (InterfaceConfiguration is @hide — field set reflectively.)
     */
    @ProxyMethod("getInterfaceConfig")
    public static class GetInterfaceConfig extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Object config = method.invoke(who, args);
            try {
                top.niunaijun.blackbox.fake.spoof.BSpoofManager spoof =
                        top.niunaijun.blackbox.fake.spoof.BSpoofManager.get();
                if (config != null && spoof.isSpoofActive()
                        && args != null && args.length > 0
                        && "wlan0".equals(args[0])) {
                    String mac = spoof.getWifiMac();
                    if (mac != null) {
                        java.lang.reflect.Field f =
                                config.getClass().getField("hardwareAddress");
                        f.set(config, mac);
                    }
                }
            } catch (Throwable ignored) {
                // Never break the call on a rewrite failure.
            }
            return config;
        }
    }
}
