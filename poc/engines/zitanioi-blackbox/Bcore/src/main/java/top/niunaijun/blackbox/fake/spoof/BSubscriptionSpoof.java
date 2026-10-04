package top.niunaijun.blackbox.fake.spoof;

import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

import java.lang.reflect.Method;
import java.util.ArrayList;

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
 *       empty list (mirrors the cell-privacy treatment of
 *       {@code getAllCellInfo}).</li>
 *   <li>{@code SubscriptionManager.getActiveSubscriptionInfo(int)} and
 *       {@code getActiveSubscriptionInfoForSimSlotIndex(int)} → null.</li>
 *   <li>{@code SubscriptionManager.getPhoneNumber(int)} and
 *       {@code getPhoneNumber(int, int)} → per-identity MSISDN
 *       (re-audit 2026-10-04: public-API bypass of the line1Number hooks).</li>
 *   <li>{@code SubscriptionInfo.getNumber()} → per-identity MSISDN.</li>
 *   <li>{@code SubscriptionInfo.getIccId()} → per-identity ICCID.</li>
 *   <li>{@code SubscriptionInfo.getGroupUuid()} → null (stable SIM link
 *       otherwise).</li>
 *   <li>{@code SubscriptionInfo.getCardId()} → per-identity deterministic
 *       int (primitive; cannot return null, so a derived value replaces
 *       the hardware-tied one).</li>
 * </ul>
 *
 * <p>Fully fail-closed: the spoofed values are served unconditionally.
 * Guests never run without an active identity, and a null/empty/passthrough
 * subscription state is plausible on real devices (no-SIM, airplane mode).
 * The real SIM values are never visible to a guest, active profile or not.
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
                hookManagerNumberMethod("getPhoneNumber", new Class<?>[]{int.class});
                hookManagerNumberMethod("getPhoneNumber",
                        new Class<?>[]{int.class, int.class});
                hookInfoGetter("getNumber", true);
                hookInfoGetter("getIccId", false);
                hookInfoGetter("getGroupUuid", false, true);
                hookInfoCardId();
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
                    // Fail closed: never expose the real subscription state.
                    // Re-audit 2026-10-04 (N9): a mutable ArrayList, not
                    // Collections.emptyList() — a caller that mutates the
                    // result would get UnsupportedOperationException
                    // (guest crash + a spoofing tell).
                    callFrame.setResult(list ? new ArrayList<>() : null);
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on SubscriptionManager." + name + " failed", t);
        }
    }

    /**
     * SubscriptionManager.getPhoneNumber(...) → per-identity MSISDN, fail closed
     * (null when no profile is active rather than the real SIM number).
     */
    private static void hookManagerNumberMethod(String name, Class<?>[] params) {
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
                    BSpoofManager spoof = BSpoofManager.get();
                    if (!spoof.isSpoofActive()) {
                        // Fail closed: null, never the real SIM number.
                        callFrame.setResult(null);
                        return;
                    }
                    callFrame.setResult(spoof.getPhoneNumber());
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on SubscriptionManager." + name + " failed", t);
        }
    }

    /**
     * @param number    true for getNumber (→ MSISDN), false for getIccId (→ ICCID).
     * @param nullValue when true, always serve null instead of a spoofed value.
     */
    private static void hookInfoGetter(String name, final boolean number, final boolean nullValue) {
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
                    if (nullValue) {
                        callFrame.setResult(null);
                        return;
                    }
                    BSpoofManager spoof = BSpoofManager.get();
                    if (!spoof.isSpoofActive()) {
                        // Fail closed: null, never the real SIM value.
                        callFrame.setResult(null);
                        return;
                    }
                    String value = number ? spoof.getPhoneNumber() : spoof.getSimSerial();
                    callFrame.setResult(value);
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on SubscriptionInfo." + name + " failed", t);
        }
    }

    /** @param number true for getNumber (→ MSISDN), false for getIccId (→ ICCID). */
    private static void hookInfoGetter(String name, final boolean number) {
        hookInfoGetter(name, number, false);
    }

    /**
     * SubscriptionInfo.getCardId() returns a primitive int, so null is not an
     * option. Serve a per-identity deterministic value derived from the
     * Android ID instead of the hardware-tied card id.
     */
    private static void hookInfoCardId() {
        final Method target;
        try {
            target = SubscriptionInfo.class.getDeclaredMethod("getCardId");
        } catch (Throwable t) {
            Slog.e(TAG, "SubscriptionInfo.getCardId not found; skipping", t);
            return;
        }
        try {
            Pine.hook(target, new MethodHook() {
                @Override
                public void beforeCall(Pine.CallFrame callFrame) {
                    BSpoofManager spoof = BSpoofManager.get();
                    String seed = spoof.isSpoofActive() ? spoof.getAndroidId() : "inactive";
                    int fake = Math.abs(("cardid-" + seed).hashCode());
                    callFrame.setResult(fake);
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on SubscriptionInfo.getCardId failed", t);
        }
    }
}
