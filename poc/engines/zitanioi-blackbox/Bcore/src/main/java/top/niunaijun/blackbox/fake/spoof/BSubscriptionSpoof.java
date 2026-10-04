package top.niunaijun.blackbox.fake.spoof;

import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

import java.lang.reflect.Method;
import java.util.Collections;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;
import top.niunaijun.blackbox.utils.Slog;

/**
 * Closes the {@code SubscriptionManager} bypass around the per-identity
 * MSISDN/ICCID spoofing.
 *
 * <p>There is no {@code ISub} binder proxy in the engine, so a guest calling
 * {@code SubscriptionManager.getActiveSubscriptionInfoList()} gets the real
 * subscription list, and {@code SubscriptionInfo.getNumber()} /
 * {@code getIccId()} hand back the <b>real SIM number and real ICCID</b> —
 * identical across every identity, completely defeating the per-identity
 * {@code getLine1Number()} fix. Any backend keying accounts by
 * device-reported number links the identities.
 *
 * <p>Hooks (all Pine, public-API choke point):
 * <ul>
 *   <li>{@code SubscriptionManager.getActiveSubscriptionInfoList()} →
 *       empty list when a profile is active (mirrors the cell-privacy
 *       treatment of {@code getAllCellInfo}).</li>
 *   <li>{@code SubscriptionManager.getActiveSubscriptionInfo(int)} and
 *       {@code getActiveSubscriptionInfoForSimSlotIndex(int)} → null.</li>
 *   <li>{@code SubscriptionInfo.getNumber()} → per-identity MSISDN.</li>
 *   <li>{@code SubscriptionInfo.getIccId()} → per-identity ICCID.</li>
 * </ul>
 *
 * <p>Fail-closed for number-like values: null when no profile is active (a
 * null MSISDN/ICCID is plausible on real devices; the real SIM's would be a
 * cross-identity link).
 */
public final class BSubscriptionSpoof {
    private static final String TAG = "BSubscriptionSpoof";
    private static volatile boolean sInstalled = false;

    private BSubscriptionSpoof() {
    }

    /** Installs the Pine hooks. Safe to call repeatedly; failures never throw. */
    public static void install() {
        if (sInstalled) {
            return;
        }
        synchronized (BSubscriptionSpoof.class) {
            if (sInstalled) {
                return;
            }
            try {
                hookManagerMethod("getActiveSubscriptionInfoList", new Class<?>[0], true);
                hookManagerMethod("getActiveSubscriptionInfo", new Class<?>[]{int.class}, false);
                hookManagerMethod("getActiveSubscriptionInfoForSimSlotIndex",
                        new Class<?>[]{int.class}, false);
                hookInfoGetter("getNumber", true);
                hookInfoGetter("getIccId", false);
                sInstalled = true;
                Slog.d(TAG, "SubscriptionManager/SubscriptionInfo hooks installed");
            } catch (Throwable t) {
                // Fail open: real subscription values remain visible.
                Slog.e(TAG, "SubscriptionManager hook install failed", t);
            }
        }
    }

    /**
     * @param list true for the list getter (→ empty list), false for the
     *             single-subscription getters (→ null).
     */
    private static void hookManagerMethod(String name, Class<?>[] params, final boolean list) {
        final Method target;
        try {
            target = SubscriptionManager.class.getDeclaredMethod(name, params);
        } catch (Throwable t) {
            Slog.e(TAG, "SubscriptionManager." + name + " not found; skipping", t);
            return;
        }
        try {
            Pine.hook(target, new MethodHook() {
                @Override
                public void beforeCall(Pine.CallFrame callFrame) {
                    if (!BSpoofManager.get().isSpoofActive()) {
                        return;
                    }
                    callFrame.setResult(list ? Collections.emptyList() : null);
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on SubscriptionManager." + name + " failed", t);
        }
    }

    /** @param number true for getNumber (→ MSISDN), false for getIccId (→ ICCID). */
    private static void hookInfoGetter(String name, final boolean number) {
        final Method target;
        try {
            target = SubscriptionInfo.class.getDeclaredMethod(name);
        } catch (Throwable t) {
            Slog.e(TAG, "SubscriptionInfo." + name + " not found; skipping", t);
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
                    String value = number ? spoof.getPhoneNumber() : spoof.getSimSerial();
                    // Fail closed: null, never the real SIM value.
                    callFrame.setResult(value);
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on SubscriptionInfo." + name + " failed", t);
        }
    }
}
