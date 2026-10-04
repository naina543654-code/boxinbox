package top.niunaijun.blackbox.fake.service;


import java.lang.reflect.Method;

import black.android.os.BRIDeviceIdentifiersPolicyServiceStub;
import black.android.os.BRServiceManager;
import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.fake.hook.BinderInvocationStub;
import top.niunaijun.blackbox.fake.hook.MethodHook;
import top.niunaijun.blackbox.fake.hook.ProxyMethod;
import top.niunaijun.blackbox.utils.Md5Utils;

/**
 * Created by Milk on 4/3/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class IDeviceIdentifiersPolicyProxy extends BinderInvocationStub {

    public IDeviceIdentifiersPolicyProxy() {
        super(BRServiceManager.get().getService("device_identifiers"));
    }

    @Override
    protected Object getWho() {
        return BRIDeviceIdentifiersPolicyServiceStub.get().asInterface(BRServiceManager.get().getService("device_identifiers"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("device_identifiers");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @ProxyMethod("getSerialForPackage")
    public static class x extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
//                args[0] = BlackBoxCore.getHostPkg();
//                return method.invoke(who, args);
            // Audit fix 2026-10-04: the md5(hostPkg) fake was identical across
            // identities. Gate on the profile: per-identity serial when
            // active, legacy stable fake when not (never the real serial).
            top.niunaijun.blackbox.fake.spoof.BSpoofManager spoof =
                    top.niunaijun.blackbox.fake.spoof.BSpoofManager.get();
            if (spoof.isSpoofActive() && spoof.getSerial() != null) {
                return spoof.getSerial();
            }
            return Md5Utils.md5(BlackBoxCore.getHostPkg());
        }
    }

    /**
     * R3 audit 2026-10-05: the no-arg {@code getSerial()} (AIDL line 40) was
     * unhooked — a direct-binder caller bypasses the {@code getSerialForPackage}
     * hook above and reads the real hardware serial. Same treatment.
     */
    @ProxyMethod("getSerial")
    public static class GetSerial extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            top.niunaijun.blackbox.fake.spoof.BSpoofManager spoof =
                    top.niunaijun.blackbox.fake.spoof.BSpoofManager.get();
            if (spoof.isSpoofActive() && spoof.getSerial() != null) {
                return spoof.getSerial();
            }
            return Md5Utils.md5(BlackBoxCore.getHostPkg());
        }
    }
}
