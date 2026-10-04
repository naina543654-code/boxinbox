package top.niunaijun.blackbox.fake.spoof;

import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;
import top.niunaijun.blackbox.utils.Slog;

/**
 * Spoofs telephony operator identity at the {@link TelephonyManager} public-API
 * choke point.
 *
 * <p>Why this exists: on the guest's framework build,
 * {@code TelephonyManager.getNetworkOperatorName()} and
 * {@code getNetworkOperator()} do <b>not</b> go through the
 * {@code ITelephony} binder proxy (no {@code ITelephonyManagerProxy} hook ever
 * fires for them — verified on-device: {@code getNetworkCountryIso} and
 * {@code getDataNetworkType} hit the proxy in the same process at the same
 * time while the operator getters never do). They also bypass the hooked
 * {@code SystemProperties.get()} overloads covered by
 * {@link BSpoofSystemProps} (hook installed in every guest process, yet the
 * real values still leak). Hooking the public {@code TelephonyManager} methods
 * directly with Pine catches the value at the exact API the probe and real
 * apps call, regardless of which internal path the framework uses
 * (system-property read, unhooked binder method, or cached value).
 *
 * <p>Fail-open: install failures never throw; every hook additionally gates on
 * {@code BSpoofManager.isSpoofActive()} and on the profile actually carrying a
 * value, so an inactive profile or a missing field passes through untouched.
 */
public final class BTelephonyApiSpoof {
    private static final String TAG = "BTelephonyApiSpoof";
    private static volatile boolean sInstalled = false;

    private BTelephonyApiSpoof() {
    }

    /** Installs the Pine hooks. Safe to call repeatedly; failures never throw. */
    public static void install() {
        if (sInstalled) {
            return;
        }
        synchronized (BTelephonyApiSpoof.class) {
            if (sInstalled) {
                return;
            }
            try {
                // Network operator alpha (long) name, e.g. "Ooredoo".
                hookOperatorGetter("getNetworkOperatorName", new Class<?>[0], false);
                hookOperatorGetter("getNetworkOperatorName", new Class<?>[]{int.class}, false);
                // Network operator numeric (MCC+MNC), e.g. "42701".
                hookOperatorGetter("getNetworkOperator", new Class<?>[0], true);
                hookOperatorGetter("getNetworkOperator", new Class<?>[]{int.class}, true);
                // Line-1 (MSISDN) number: the ITelephony binder hook above
                // covers the service path, but hook the public API too — the
                // operator getters proved these TelephonyManager methods can
                // bypass both the binder proxy and the SystemProperties hooks
                // on some framework builds. The real SIM number must never
                // leak: it is identical across identities.
                hookLine1Number();
                // Device/subscriber identifiers at the public-API choke point.
                // The ITelephony @ProxyMethod("getSubscriberId") and
                // @ProxyMethod("getSimSerialNumber") names do not exist on the
                // API 34 AIDL (verified against AOSP android14-release), so
                // those binder hooks are dead — the Pine hooks below are the
                // live enforcement for the exact APIs apps call.
                hookStringGetter("getSubscriberId", new Class<?>[0], ValueKind.SUBSCRIBER_ID);
                hookStringGetter("getSubscriberId", new Class<?>[]{int.class}, ValueKind.SUBSCRIBER_ID);
                hookStringGetter("getSimSerialNumber", new Class<?>[0], ValueKind.SIM_SERIAL);
                hookStringGetter("getSimSerialNumber", new Class<?>[]{int.class}, ValueKind.SIM_SERIAL);
                hookStringGetter("getImei", new Class<?>[0], ValueKind.DEVICE_ID);
                hookStringGetter("getImei", new Class<?>[]{int.class}, ValueKind.DEVICE_ID);
                hookStringGetter("getMeid", new Class<?>[0], ValueKind.DEVICE_ID);
                hookStringGetter("getMeid", new Class<?>[]{int.class}, ValueKind.DEVICE_ID);
                hookStringGetter("getDeviceId", new Class<?>[0], ValueKind.DEVICE_ID);
                hookStringGetter("getDeviceId", new Class<?>[]{int.class}, ValueKind.DEVICE_ID);
                // Voicemail number often equals the MSISDN — serve the
                // per-identity number, fail closed to null when inactive.
                hookStringGetter("getVoiceMailNumber", new Class<?>[0], ValueKind.PHONE_NUMBER);
                hookStringGetter("getVoiceMailNumber", new Class<?>[]{int.class}, ValueKind.PHONE_NUMBER);
                sInstalled = true;
                Slog.d(TAG, "TelephonyManager operator API hooks installed");
            } catch (Throwable t) {
                // Fail open: real operator values remain visible.
                Slog.e(TAG, "TelephonyManager hook install failed; operator visible", t);
            }
        }
    }

    /**
     * @param numeric true for the MCC+MNC numeric getter
     *                ({@code getNetworkOperator}), false for the alpha name
     *                getter ({@code getNetworkOperatorName}).
     */
    private static void hookOperatorGetter(String name, Class<?>[] params, final boolean numeric) {
        final Method target;
        try {
            target = TelephonyManager.class.getDeclaredMethod(name, params);
        } catch (Throwable t) {
            Slog.e(TAG, "TelephonyManager." + name + "/" + params.length + " not found; skipping", t);
            return;
        }
        try {
            Pine.hook(target, new MethodHook() {
                @Override
                public void beforeCall(Pine.CallFrame callFrame) {
                    BSpoofManager spoof = BSpoofManager.get();
                    if (!spoof.isSpoofActive()) {
                        return;
                    }
                    String value = numeric ? spoof.getOperatorNumeric() : spoof.getOperatorName();
                    if (value != null) {
                        callFrame.setResult(value);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on TelephonyManager." + name + " failed", t);
        }
    }

    /** Hooks {@code TelephonyManager.getLine1Number()} to the per-identity MSISDN. */
    private static void hookLine1Number() {
        hookStringGetter("getLine1Number", new Class<?>[0], ValueKind.PHONE_NUMBER);
    }

    /** Which per-identity profile value a hooked string getter serves. */
    private enum ValueKind {
        SUBSCRIBER_ID, SIM_SERIAL, DEVICE_ID, PHONE_NUMBER
    }

    /**
     * Hooks a no/slot-arg {@code TelephonyManager} string getter to the
     * per-identity profile value. Fail-closed for number-like values (null
     * when inactive — a null MSISDN/IMSI/ICCID is plausible); device IDs keep
     * the engine's legacy stable fake when no profile is active (the real
     * IMEI must never leak, and a null there is less plausible).
     */
    private static void hookStringGetter(String name, Class<?>[] params, final ValueKind kind) {
        final Method target;
        try {
            target = TelephonyManager.class.getDeclaredMethod(name, params);
        } catch (Throwable t) {
            Slog.e(TAG, "TelephonyManager." + name + "/" + params.length + " not found; skipping", t);
            return;
        }
        try {
            Pine.hook(target, new MethodHook() {
                @Override
                public void beforeCall(Pine.CallFrame callFrame) {
                    BSpoofManager spoof = BSpoofManager.get();
                    String value = null;
                    boolean failClosedNull = false;
                    switch (kind) {
                        case SUBSCRIBER_ID:
                            if (spoof.isSpoofActive()) {
                                value = spoof.getSubscriberId();
                            }
                            failClosedNull = true;
                            break;
                        case SIM_SERIAL:
                            if (spoof.isSpoofActive()) {
                                value = spoof.getSimSerial();
                            }
                            failClosedNull = true;
                            break;
                        case PHONE_NUMBER:
                            if (spoof.isSpoofActive()) {
                                value = spoof.getPhoneNumber();
                            }
                            failClosedNull = true;
                            break;
                        case DEVICE_ID:
                        default:
                            if (spoof.isSpoofActive() && spoof.getTelephonyDeviceId() != null) {
                                value = spoof.getTelephonyDeviceId();
                            } else {
                                value = md5HostPkg();
                            }
                            break;
                    }
                    if (value != null || failClosedNull) {
                        callFrame.setResult(value);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on TelephonyManager." + name + " failed", t);
        }
    }

    private static String md5HostPkg() {
        try {
            return top.niunaijun.blackbox.utils.Md5Utils.md5(
                    top.niunaijun.blackbox.BlackBoxCore.getHostPkg());
        } catch (Throwable t) {
            return "000000000000000";
        }
    }
}
