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
            // Re-audit 2026-10-04 (N12): was returning null while
            // getMsisdnForSubscriber returned the per-identity number — an
            // incoherent pair on direct-binder reads (a spoofing tell).
            // Line 1 IS the MSISDN: serve the same per-identity value.
            BSpoofManager spoof = BSpoofManager.get();
            if (spoof.isSpoofActive() && spoof.getPhoneNumber() != null) {
                return spoof.getPhoneNumber();
            }
            return null;
        }
    }

    /**
     * Re-audit 2026-10-04 (N6): the bare (non-subscriber) AIDL variants were
     * unhooked — reachable via direct binder reflection even though the
     * public TelephonyManager APIs are Pine-covered. All names verified
     * against AOSP android14-release IPhoneSubInfo.aidl.
     */
    @ProxyMethod("getLine1Number")
    public static class getLine1Number extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            BSpoofManager spoof = BSpoofManager.get();
            if (spoof.isSpoofActive() && spoof.getPhoneNumber() != null) {
                return spoof.getPhoneNumber();
            }
            return null;
        }
    }

    @ProxyMethod("getMsisdn")
    public static class getMsisdn extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            BSpoofManager spoof = BSpoofManager.get();
            if (spoof.isSpoofActive() && spoof.getPhoneNumber() != null) {
                return spoof.getPhoneNumber();
            }
            return null;
        }
    }

    @ProxyMethod("getVoiceMailNumber")
    public static class getVoiceMailNumber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            BSpoofManager spoof = BSpoofManager.get();
            if (spoof.isSpoofActive() && spoof.getPhoneNumber() != null) {
                return spoof.getPhoneNumber();
            }
            return null;
        }
    }

    @ProxyMethod("getDeviceIdWithFeature")
    public static class getDeviceIdWithFeature extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return spoofedOrLegacyDeviceId();
        }
    }

    @ProxyMethod("getSubscriberIdWithFeature")
    public static class getSubscriberIdWithFeature extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return spoofedOrLegacySubscriberId();
        }
    }

    @ProxyMethod("getIccSerialNumberWithFeature")
    public static class getIccSerialNumberWithFeature extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            BSpoofManager spoof = BSpoofManager.get();
            if (spoof.isSpoofActive() && spoof.getSimSerial() != null) {
                return spoof.getSimSerial();
            }
            return null;
        }
    }

    @ProxyMethod("getNaiForSubscriber")
    public static class getNaiForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            // SIM-derived Network Access Identifier — stable cross-identity
            // link; no per-identity NAI exists. Fail closed to null.
            return null;
        }
    }

    @ProxyMethod("getGroupIdLevel1ForSubscriber")
    public static class getGroupIdLevel1ForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            // GID1 is a stable SIM group identifier — fail closed to null.
            return null;
        }
    }

    @ProxyMethod("getLine1AlphaTag")
    public static class getLine1AlphaTag extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @ProxyMethod("getLine1AlphaTagForSubscriber")
    public static class getLine1AlphaTagForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @ProxyMethod("getVoiceMailAlphaTag")
    public static class getVoiceMailAlphaTag extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @ProxyMethod("getVoiceMailAlphaTagForSubscriber")
    public static class getVoiceMailAlphaTagForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @ProxyMethod("getDeviceSvnUsingSubId")
    public static class getDeviceSvnUsingSubId extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            // Device software version (IMEI/SV) — stable device identifier.
            return spoofedOrLegacyDeviceId();
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

    /**
     * Review 2026-10-05: the bare deprecated variants were still open on the
     * direct-binder path (a guest reflecting on {@code iphonesubinfo} can
     * call them even though the SDK hides them). Same treatment as their
     * WithFeature/ForSubscriber siblings. Names verified against AOSP
     * android14-release IPhoneSubInfo.aidl.
     */
    @ProxyMethod("getDeviceSvn")
    public static class getDeviceSvn extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return spoofedOrLegacyDeviceId();
        }
    }

    @ProxyMethod("getSubscriberId")
    public static class getSubscriberId extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return spoofedOrLegacySubscriberId();
        }
    }

    @ProxyMethod("getIccSerialNumber")
    public static class getIccSerialNumber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            BSpoofManager spoof = BSpoofManager.get();
            if (spoof.isSpoofActive() && spoof.getSimSerial() != null) {
                return spoof.getSimSerial();
            }
            return null;
        }
    }

    /**
     * Review 2026-10-05: ISIM/IMS identifiers are SIM-derived stable values
     * (IMPI/IMPU/domain/IST/PCSCF). No per-identity IMS profile exists;
     * fail closed to null ("not present"), which is the normal state on
     * devices without an ISIM app.
     */
    @ProxyMethod("getIsimImpi")
    public static class getIsimImpi extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @ProxyMethod("getImsPrivateUserIdentity")
    public static class getImsPrivateUserIdentity extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @ProxyMethod("getIsimDomain")
    public static class getIsimDomain extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @ProxyMethod("getIsimImpu")
    public static class getIsimImpu extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @ProxyMethod("getImsPublicUserIdentities")
    public static class getImsPublicUserIdentities extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @ProxyMethod("getIsimIst")
    public static class getIsimIst extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @ProxyMethod("getIsimPcscf")
    public static class getIsimPcscf extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }
}
