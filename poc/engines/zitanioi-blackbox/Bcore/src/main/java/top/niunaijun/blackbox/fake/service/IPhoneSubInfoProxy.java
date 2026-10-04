package top.niunaijun.blackbox.fake.service;

import java.lang.reflect.Method;

import black.android.telephony.BRTelephonyManager;
import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.fake.hook.ClassInvocationStub;
import top.niunaijun.blackbox.fake.hook.MethodHook;
import top.niunaijun.blackbox.fake.hook.ProxyMethod;
import top.niunaijun.blackbox.fake.spoof.BSpoofManager;
import top.niunaijun.blackbox.utils.Md5Utils;
import top.niunaijun.blackbox.utils.MethodParameterUtils;

/**
 * Created by BlackBox on 2022/2/26.
 */
public class IPhoneSubInfoProxy extends ClassInvocationStub {
    public static final String TAG = "IPhoneSubInfoProxy";

    public IPhoneSubInfoProxy() {
        if (BRTelephonyManager.get()._check_sServiceHandleCacheEnabled() != null) {
            BRTelephonyManager.get()._set_sServiceHandleCacheEnabled(true);
        }
        if (BRTelephonyManager.get()._check_getSubscriberInfoService() != null) {
            BRTelephonyManager.get().getSubscriberInfoService();
        }
    }

    @Override
    protected Object getWho() {
        return BRTelephonyManager.get().sIPhoneSubInfo();
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        BRTelephonyManager.get()._set_sIPhoneSubInfo(proxyInvocation);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodParameterUtils.replaceFirstAppPkg(args);
        return super.invoke(proxy, method, args);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }


    @ProxyMethod("getLine1NumberForSubscriber")
    public static class getLine1NumberForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    /**
     * The subscriber-info service is a second path to the same identifiers
     * the ITelephony hooks cover. A guest reaching {@code iphonesubinfo}
     * directly must get the per-identity values, never the real SIM's.
     * Fail-closed: null when no profile is active (a null MSISDN/IMSI is
     * plausible; the real one would be a cross-identity link).
     */
    private static String spoofedOrLegacyDeviceId() {
        BSpoofManager spoof = BSpoofManager.get();
        if (spoof.isSpoofActive() && spoof.getTelephonyDeviceId() != null) {
            return spoof.getTelephonyDeviceId();
        }
        return Md5Utils.md5(BlackBoxCore.getHostPkg());
    }

    private static String spoofedOrLegacySubscriberId() {
        BSpoofManager spoof = BSpoofManager.get();
        if (spoof.isSpoofActive() && spoof.getSubscriberId() != null) {
            return spoof.getSubscriberId();
        }
        return Md5Utils.md5(BlackBoxCore.getHostPkg());
    }

    @ProxyMethod("getDeviceId")
    public static class getDeviceId extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return spoofedOrLegacyDeviceId();
        }
    }

    @ProxyMethod("getImeiForSubscriber")
    public static class getImeiForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return spoofedOrLegacyDeviceId();
        }
    }

    @ProxyMethod("getDeviceIdForPhone")
    public static class getDeviceIdForPhone extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return spoofedOrLegacyDeviceId();
        }
    }

    @ProxyMethod("getSubscriberIdForSubscriber")
    public static class getSubscriberIdForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return spoofedOrLegacySubscriberId();
        }
    }

    @ProxyMethod("getIccSerialNumberForSubscriber")
    public static class getIccSerialNumberForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            BSpoofManager spoof = BSpoofManager.get();
            if (spoof.isSpoofActive() && spoof.getSimSerial() != null) {
                return spoof.getSimSerial();
            }
            return null;
        }
    }

    @ProxyMethod("getMsisdnForSubscriber")
    public static class getMsisdnForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            BSpoofManager spoof = BSpoofManager.get();
            if (spoof.isSpoofActive() && spoof.getPhoneNumber() != null) {
                return spoof.getPhoneNumber();
            }
            return null;
        }
    }

    @ProxyMethod("getVoiceMailNumberForSubscriber")
    public static class getVoiceMailNumberForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            BSpoofManager spoof = BSpoofManager.get();
            if (spoof.isSpoofActive() && spoof.getPhoneNumber() != null) {
                return spoof.getPhoneNumber();
            }
            return null;
        }
    }
}
