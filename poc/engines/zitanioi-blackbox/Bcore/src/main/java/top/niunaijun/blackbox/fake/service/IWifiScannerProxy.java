package top.niunaijun.blackbox.fake.service;

import android.content.Context;
import android.os.IBinder;

import java.lang.reflect.Method;
import java.util.ArrayList;

import black.android.net.wifi.BRIWifiManagerStub;
import black.android.os.BRServiceManager;
import top.niunaijun.blackbox.fake.hook.BinderInvocationStub;
import top.niunaijun.blackbox.fake.hook.MethodHook;
import top.niunaijun.blackbox.fake.hook.ProxyMethod;
import top.niunaijun.blackbox.fake.hook.ProxyMethods;
import top.niunaijun.blackbox.fake.spoof.BSpoofManager;

/**
 * @author Findger
 * @function
 * @date :2022/4/3 13:05
 **/
public class IWifiScannerProxy extends BinderInvocationStub {

    public IWifiScannerProxy() {
        super(BRServiceManager.get().getService("wifiscanner"));
    }

    @Override
    protected Object getWho() {
        return BRIWifiManagerStub.get().asInterface(BRServiceManager.get().getService("wifiscanner"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("wifiscanner");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    /**
     * R4 audit 2026-10-05 (R4-12): the normal scan-result path is emptied at
     * {@code IWifiManagerProxy.getScanResults}, but direct access to the
     * {@code wifiscanner} binder (hidden-API reflection) would expose real
     * nearby BSSIDs — a strong geolocation/cross-identity signal. Empty list
     * when active, mirroring GetScanResults. Defense in depth: the normal
     * app path never reaches here.
     */
    @ProxyMethods({"getScanResults", "getSingleScanResults"})
    public static class GetScanResults extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (BSpoofManager.get().isSpoofActive()) {
                return new ArrayList<>();
            }
            return method.invoke(who, args);
        }
    }
}
