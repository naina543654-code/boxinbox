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
}
