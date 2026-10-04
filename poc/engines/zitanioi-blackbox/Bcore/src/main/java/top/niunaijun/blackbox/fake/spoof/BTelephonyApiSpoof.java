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
                // Review 2026-10-05: defense-in-depth. The SIM operator and
                // country-ISO getters currently rely on the ITelephony binder
                // proxy — but the network-operator getters proved
                // TelephonyManager methods can bypass the proxy entirely on
                // some framework builds. Hook the public API directly too. A
                // fresh identity's SIM belongs to its spoofed operator, so
                // the per-identity operator/country values are coherent here.
                hookOperatorGetter("getSimOperator", new Class<?>[0], true);
                hookOperatorGetter("getSimOperator", new Class<?>[]{int.class}, true);
                hookOperatorGetter("getSimOperatorName", new Class<?>[0], false);
                hookOperatorGetter("getSimOperatorName", new Class<?>[]{int.class}, false);
                hookCountryIsoGetter("getSimCountryIso", new Class<?>[0]);
                hookCountryIsoGetter("getSimCountryIso", new Class<?>[]{int.class});
                hookCountryIsoGetter("getNetworkCountryIso", new Class<?>[0]);
                hookCountryIsoGetter("getNetworkCountryIso", new Class<?>[]{int.class});
                // R3 audit 2026-10-05: ServiceState carries its own operator
                // fields (alpha long/short, numeric) that bypass the
                // TelephonyManager Pine getters — a guest reading
                // getServiceState() sees the real operator. Hook the
                // ServiceState getters directly (lower layer, covers every
                // path that produces a ServiceState).
                hookServiceStateOperator();
                // R3 audit 2026-10-05: public getNetworkType() is
                // permissionless and was served the real value (the binder
                // hook name was dead). Belt-and-suspenders Pine hook at the
                // public API alongside the fixed binder hook.
                hookNetworkType();
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
                // Re-audit 2026-10-04 (N8): getNai() returns a SIM-derived
                // Network Access Identifier — stable across identities,
                // READ_PHONE_STATE-gated. No per-identity NAI in the profile;
                // fail closed to null.
                hookStringGetter("getNai", new Class<?>[0], ValueKind.NULL_ALWAYS);
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
                    // R3 audit 2026-10-05: fail closed. An active profile
                    // always overrides — even when the field is missing (a
                    // null/empty operator is plausible; the real operator is
                    // a cross-identity link). Previously fell through to the
                    // real value when the profile lacked the field.
                    callFrame.setResult(numeric
                            ? spoof.getOperatorNumeric() : spoof.getOperatorName());
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

    /**
     * R3 audit 2026-10-05: hooks the {@code ServiceState} operator getters
     * to the per-identity operator. Fail-closed when a profile is active
     * (null when the profile lacks the field); inactive profile passes
     * through, matching the sibling operator hooks.
     */
    private static void hookServiceStateOperator() {
        hookServiceStateGetter("getOperatorAlphaLong", false);
        hookServiceStateGetter("getOperatorAlphaShort", false);
        hookServiceStateGetter("getOperatorNumeric", true);
    }

    private static void hookServiceStateGetter(String name, final boolean numeric) {
        final Method target;
        try {
            target = android.telephony.ServiceState.class.getDeclaredMethod(name);
        } catch (Throwable t) {
            Slog.e(TAG, "ServiceState." + name + " not found; skipping", t);
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
                    // Fail closed: active profile always overrides, even
                    // when the field is missing (null is plausible).
                    callFrame.setResult(numeric
                            ? spoof.getOperatorNumeric() : spoof.getOperatorName());
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on ServiceState." + name + " failed", t);
        }
    }

    /**
     * R3 audit 2026-10-05: {@code TelephonyManager.getNetworkType()} is
     * permissionless. Serves the per-identity network type when active;
     * inactive profile passes through (matches the binder hook shape).
     */
    private static void hookNetworkType() {
        final Method target;
        try {
            target = TelephonyManager.class.getDeclaredMethod("getNetworkType");
        } catch (Throwable t) {
            Slog.e(TAG, "TelephonyManager.getNetworkType not found; skipping", t);
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
                    int nt = spoof.getNetworkType();
                    if (nt >= 0) {
                        callFrame.setResult(nt);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on TelephonyManager.getNetworkType failed", t);
        }
    }

    /**
     * Hooks a no/slot-arg {@code TelephonyManager} country-ISO getter to the
     * per-identity country (e.g. "nl"). Same fail-open shape as
     * {@link #hookOperatorGetter}: inactive profile passes through.
     */
    private static void hookCountryIsoGetter(String name, Class<?>[] params) {
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
                    // R3 audit 2026-10-05: fail closed — see hookOperatorGetter.
                    callFrame.setResult(spoof.getCountryIso());
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on TelephonyManager." + name + " failed", t);
        }
    }

    /** Which per-identity profile value a hooked string getter serves. */
    private enum ValueKind {
        SUBSCRIBER_ID, SIM_SERIAL, DEVICE_ID, PHONE_NUMBER, NULL_ALWAYS
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
                        case NULL_ALWAYS:
                            // No per-identity value exists; the real value is
                            // a stable cross-identity link, so always null.
                            failClosedNull = true;
                            break;
                        case DEVICE_ID:
                        default:
                            if (!hasPrivilegedPhoneState()) {
                                // R3 audit 2026-10-05: on API 29+ a real
                                // device returns null here without
                                // READ_PRIVILEGED_PHONE_STATE. Serving the
                                // spoofed IMEI anyway is a behavioral tell —
                                // fail closed to null, matching the framework.
                                value = null;
                                failClosedNull = true;
                            } else if (spoof.isSpoofActive()
                                    && spoof.getTelephonyDeviceId() != null) {
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

    /**
     * R3 audit 2026-10-05: whether the guest package holds
     * {@code READ_PRIVILEGED_PHONE_STATE}. Cached per process (install-time
     * permission; never changes at runtime for normal apps). On failure the
     * check assumes granted — serving the spoofed value (the old behavior)
     * is preferable to wrongly nulling a privileged caller.
     */
    private static volatile Boolean sHasPrivilegedPhoneState;

    private static boolean hasPrivilegedPhoneState() {
        Boolean cached = sHasPrivilegedPhoneState;
        if (cached != null) {
            return cached;
        }
        boolean granted = true;
        try {
            android.content.Context ctx =
                    top.niunaijun.blackbox.BlackBoxCore.getContext();
            String pkg = top.niunaijun.blackbox.app.BActivityThread.getAppPackageName();
            if (ctx != null && pkg != null) {
                // Literal: android.Manifest.permission.READ_PRIVILEGED_PHONE_STATE
                // — the constant is missing from the compile SDK's android.jar
                // stubs, but the framework string is stable.
                granted = ctx.getPackageManager().checkPermission(
                        "android.permission.READ_PRIVILEGED_PHONE_STATE",
                        pkg) == android.content.pm.PackageManager.PERMISSION_GRANTED;
            }
        } catch (Throwable t) {
            Slog.w(TAG, "privileged-permission check failed; assuming granted", t);
        }
        sHasPrivilegedPhoneState = granted;
        return granted;
    }
}
