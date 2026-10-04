package top.niunaijun.blackbox.fake.service;

import android.content.Context;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import black.android.os.BRServiceManager;
import black.com.android.internal.telephony.BRITelephonyStub;
import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.app.BActivityThread;
import top.niunaijun.blackbox.entity.location.BCell;
import top.niunaijun.blackbox.fake.frameworks.BLocationManager;
import top.niunaijun.blackbox.fake.hook.BinderInvocationStub;
import top.niunaijun.blackbox.fake.hook.MethodHook;
import top.niunaijun.blackbox.fake.hook.ProxyMethod;
import top.niunaijun.blackbox.fake.hook.ProxyMethods;
import top.niunaijun.blackbox.fake.spoof.BSpoofManager;
import top.niunaijun.blackbox.utils.Md5Utils;

/**
 * Created by Milk on 4/2/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 *
 * Track B: device/subscriber IDs and operator identity are per-identity values from
 * BSpoofManager when a spoof profile is active. Platform restriction: on API 33+
 * the IMEI/MEID getters (getDeviceId/getImeiForSlot/getMeidForSlot) require a
 * privileged permission, so a guest calling them directly may get a
 * SecurityException from the framework before/around the proxy — the proxy still
 * returns the profile value whenever it is actually invoked.
 */
public class ITelephonyManagerProxy extends BinderInvocationStub {
    public static final String TAG = "ITelephonyManagerProxy";

    public ITelephonyManagerProxy() {
        super(BRServiceManager.get().getService(Context.TELEPHONY_SERVICE));
    }

    @Override
    protected Object getWho() {
        IBinder telephony = BRServiceManager.get().getService(Context.TELEPHONY_SERVICE);
        return BRITelephonyStub.get().asInterface(telephony);
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    /**
     * Track B: per-identity telephony IDs from the spoof profile when active.
     * When no profile is active the legacy constant fake (md5 of host pkg) is kept
     * for the device/subscriber ID getters: passing through to the real
     * implementation would either crash (privileged-permission SecurityException
     * on API 33+) or leak the host's real IMEI/IMSI to the guest — both worse
     * than the stable fake the engine has always returned.
     */
    private static String spoofedOrLegacyDeviceId() {
        BSpoofManager spoof = BSpoofManager.get();
        if (spoof.isSpoofActive() && spoof.getTelephonyDeviceId() != null) {
            return spoof.getTelephonyDeviceId();
        }
        return Md5Utils.md5(BlackBoxCore.getHostPkg());
    }

    @ProxyMethod("getDeviceId")
    public static class GetDeviceId extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
//                MethodParameterUtils.replaceFirstAppPkg(args);
//                return method.invoke(who, args);
            return spoofedOrLegacyDeviceId();
        }
    }

    @ProxyMethod("getImeiForSlot")
    public static class getImeiForSlot extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
//                MethodParameterUtils.replaceFirstAppPkg(args);
//                return method.invoke(who, args);
            return spoofedOrLegacyDeviceId();
        }
    }

    @ProxyMethod("getMeidForSlot")
    public static class GetMeidForSlot extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
//                MethodParameterUtils.replaceFirstAppPkg(args);
//                return method.invoke(who, args);
            return spoofedOrLegacyDeviceId();
        }
    }

    /**
     * R3 audit 2026-10-05: {@code getPrimaryImei} (ITelephony.aidl) returns
     * the real IMEI via direct binder and has no public-API equivalent —
     * the Pine hooks can't reach it. Same treatment as the other IMEI paths.
     */
    @ProxyMethod("getPrimaryImei")
    public static class GetPrimaryImei extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return spoofedOrLegacyDeviceId();
        }
    }

    /**
     * R3 audit 2026-10-05: CDMA number identifiers. Serve the per-identity
     * MSISDN when active; null when inactive (fail-closed — a null MDN/MIN
     * is plausible, the real SIM's number is a cross-identity link).
     */
    @ProxyMethods({"getCdmaMdn", "getCdmaMin"})
    public static class GetCdmaMdnMin extends MethodHook {
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
     * R4 audit 2026-10-05 (R4-4): {@code getDeviceSoftwareVersionForSlot} is
     * reachable via the public {@code TelephonyManager.getDeviceSoftwareVersion()}
     * and returns the IMEI/SV software version (stable per device). The
     * IPhoneSubInfo twin is hooked to null — this ITelephony twin must match.
     */
    @ProxyMethod("getDeviceSoftwareVersionForSlot")
    public static class GetDeviceSoftwareVersionForSlot extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (BSpoofManager.get().isSpoofActive()) {
                return null;
            }
            return method.invoke(who, args);
        }
    }

    /**
     * R4 audit 2026-10-05 (R4-5): {@code getServiceStateForSubscriber} returns
     * a ServiceState carrying operator numeric/alpha and roaming state via
     * direct binder. The public path is Pine-covered; the binder path must
     * not leak the real one. Null when active (no service state is a
     * plausible guest-visible state); passthrough when inactive.
     */
    @ProxyMethod("getServiceStateForSubscriber")
    public static class GetServiceStateForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (BSpoofManager.get().isSpoofActive()) {
                return null;
            }
            return method.invoke(who, args);
        }
    }

    /**
     * R3 audit 2026-10-05: merged-subscription IMSI arrays — stable
     * cross-identity links via direct binder. Null = no merged
     * subscriptions, the normal state.
     */
    @ProxyMethods({"getMergedSubscriberIds", "getMergedImsisFromGroup"})
    public static class GetMergedSubscriberIds extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @ProxyMethod("isUserDataEnabled")
    public static class IsUserDataEnabled extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return true;
        }
    }


    @ProxyMethod("getLine1NumberForDisplay")
    public static class getLine1NumberForDisplay extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    // NOTE: the @ProxyMethod("getSubscriberId"), @ProxyMethod("getSimSerialNumber")
    // and @ProxyMethod("getLine1Number") hooks were removed 2026-10-04: those
    // bare names do not exist on ITelephony in API 33/34 (verified against the
    // AOSP android14-release AIDL — the API 30 feature-id refactor renamed
    // them, and SIM-serial always lived on IPhoneSubInfo). Dead names fall
    // through to the real method silently, so keeping them was worse than
    // useless. Live enforcement is now: BTelephonyApiSpoof Pine hooks on the
    // TelephonyManager public APIs + IPhoneSubInfoProxy hooks below.

    @ProxyMethod("getDeviceIdWithFeature")
    public static class GetDeviceIdWithFeature extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return spoofedOrLegacyDeviceId();
        }
    }

    @ProxyMethod("getCellLocation")
    public static class GetCellLocation extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Log.d(TAG, "getCellLocation");
            if (BSpoofManager.get().isSpoofActive()) {
                // A spoofed identity must never see the host's real
                // serving-cell location.
                return null;
            }
            if (BLocationManager.isFakeLocationEnable()) {
                BCell cell = BLocationManager.get().getCell(BActivityThread.getUserId(), BActivityThread.getAppPackageName());
                if (cell != null) {
                    // TODO Transfer BCell to CdmaCellLocation/GsmCellLocation
                    return null;
                }
            }
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getAllCellInfo")
    public static class GetAllCellInfo extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (BSpoofManager.get().isSpoofActive()) {
                // A spoofed identity must never see the host's real cell
                // towers: a phone in the profile's city would not be near
                // these masts. Return an empty list (no visible cells).
                return new ArrayList<>();
            }
            if (BLocationManager.isFakeLocationEnable()) {
                List<BCell> cell = BLocationManager.get().getAllCell(BActivityThread.getUserId(), BActivityThread.getAppPackageName());
                // TODO Transfer BCell to CdmaCellLocation/GsmCellLocation
                return cell;
            }
            try {
                return method.invoke(who, args);
            } catch (Throwable e) {
                return null;
            }
        }
    }

    /**
     * R4 audit 2026-10-05: one-shot cell-info refresh requests. When a spoof
     * profile is active the guest must not trigger (or receive, via the
     * listen() path also filtered in R4) real cell updates — no-op, mirroring
     * the empty-list semantics of getAllCellInfo.
     */
    @ProxyMethods({"requestCellInfoUpdate", "requestCellInfoUpdateWithWorkSource"})
    public static class RequestCellInfoUpdate extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (BSpoofManager.get().isSpoofActive()) {
                return null;
            }
            return method.invoke(who, args);
        }
    }

    /**
     * R4 audit 2026-10-05 (R4-3): the operator/country binder hooks below were
     * DELETED. On API 33/34 these values never traverse the ITelephony binder:
     * TelephonyManager.getNetworkOperator() → getNetworkOperatorForPhone() →
     * client-side sysprop read of gsm.operator.numeric (same shape for the
     * alpha/SIM variants: gsm.operator.alpha, gsm.sim.operator.numeric/
     * alpha/iso-country). The binder names ({@code getNetworkOperator},
     * {@code getNetworkOperatorForPhone}, {@code getNetworkOperatorName}(+ForPhone),
     * {@code getSimOperator}(+ForPhone), {@code getSimOperatorName}(+ForPhone),
     * {@code getSimCountryIso}(+ForPhone), {@code getNetworkCountryIso})
     * never fired — inert registrations that created a false impression of
     * binder-level defense in depth. The real coverage is the Pine hooks on
     * the TelephonyManager getters (BTelephonyApiSpoof) plus the getprop
     * interception table (BSpoofSystemProps), both verified present.
     *
     * <p>EXCEPTION: {@code getNetworkCountryIsoForPhone(int)} IS a real binder
     * method (ITelephony.aidl; TM.java calls it) — that hook is live and
     * kept below for direct-binder callers.
     */
    @ProxyMethod("getNetworkCountryIsoForPhone")
    public static class GetNetworkCountryIsoForPhone extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            BSpoofManager spoof = BSpoofManager.get();
            boolean active = spoof.isSpoofActive();
            if (active && spoof.getCountryIso() != null) {
                Log.d(TAG, "getNetworkCountryIsoForPhone: spoofed=" + spoof.getCountryIso());
                return spoof.getCountryIso();
            }
            Log.d(TAG, "getNetworkCountryIsoForPhone: passthrough (spoofActive=" + active + ")");
            return method.invoke(who, args);
        }
    }

    /**
     * Track B: data/voice network type. On API 24+ TelephonyManager
     * getDataNetworkType() calls ITelephony.getDataNetworkTypeForSubscriber,
     * which had no hook at all, so the guest always saw the real type.
     * Spoofed value is the per-identity profile's networkType (randomized at
     * identity creation). Inactive profile -> pass through.
     */
    @ProxyMethods({"getDataNetworkType", "getDataNetworkTypeForSubscriber"})
    public static class GetDataNetworkType extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            BSpoofManager spoof = BSpoofManager.get();
            boolean active = spoof.isSpoofActive();
            if (active) {
                int nt = spoof.getNetworkType();
                if (nt >= 0) {
                    Log.d(TAG, "getDataNetworkType: spoofed=" + nt);
                    return nt;
                }
            }
            Log.d(TAG, "getDataNetworkType: passthrough (spoofActive=" + active + ")");
            try {
                return method.invoke(who, args);
            } catch (Throwable e) {
                return TelephonyManager.NETWORK_TYPE_UNKNOWN;
            }
        }
    }

    // R3 audit 2026-10-05: the "getNetworkType" name never existed on
    // ITelephony (dead hook); the real AIDL method is
    // getNetworkTypeForSubscriber. Both names route to the per-identity
    // network type here.
    @ProxyMethods({"getNetworkTypeForSubscriber", "getVoiceNetworkTypeForSubscriber"})
    public static class GetVoiceNetworkType extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            BSpoofManager spoof = BSpoofManager.get();
            boolean active = spoof.isSpoofActive();
            if (active) {
                int nt = spoof.getNetworkType();
                if (nt >= 0) {
                    Log.d(TAG, "getVoiceNetworkType: spoofed=" + nt);
                    return nt;
                }
            }
            Log.d(TAG, "getVoiceNetworkType: passthrough (spoofActive=" + active + ")");
            try {
                return method.invoke(who, args);
            } catch (Throwable e) {
                return TelephonyManager.NETWORK_TYPE_UNKNOWN;
            }
        }
    }

    @ProxyMethod("getNeighboringCellInfo")
    public static class GetNeighboringCellInfo extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Log.d(TAG, "getNeighboringCellInfo");
            if (BSpoofManager.get().isSpoofActive()) {
                // Never leak the host's real neighboring cells.
                return new ArrayList<>();
            }
            if (BLocationManager.isFakeLocationEnable()) {
                List<BCell> cell = BLocationManager.get().getNeighboringCell(BActivityThread.getUserId(), BActivityThread.getAppPackageName());
                // TODO Transfer BCell to CdmaCellLocation/GsmCellLocation
                return null;
            }
            return method.invoke(who, args);
        }
    }
}
