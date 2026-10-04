package top.niunaijun.blackbox.fake.spoof;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;
import top.niunaijun.blackbox.utils.Slog;

/**
 * Track F: per-identity advertising ID.
 *
 * <p>Why this exists: the cloned GMS hands every guest the host's real
 * Advertising ID. Unlike most device signals — which change with each
 * generated identity — the AAID would stay identical across identities,
 * giving any backend a stable cross-identity link. Hooking
 * {@code AdvertisingIdClient.getAdvertisingIdInfo()} at the client-library
 * choke point returns a deterministic per-identity UUID (derived from the
 * identity's own Android ID) instead, so each identity advertises a
 * different, self-consistent AAID.
 *
 * <p>Fail-open: if the GMS ads-identifier client library isn't present in
 * the guest, if the profile is inactive, or if the {@code Info}
 * constructor can't be reached, the real implementation runs untouched.
 */
public final class BSpoofAaid {
    private static final String TAG = "BSpoofAaid";
    private static final String CLIENT_CLASS =
            "com.google.android.gms.ads.identifier.AdvertisingIdClient";
    private static volatile boolean sInstalled = false;

    private BSpoofAaid() {
    }

    /** Installs the Pine hook. Safe to call repeatedly; failures never throw. */
    public static void install() {
        if (sInstalled) {
            return;
        }
        synchronized (BSpoofAaid.class) {
            if (sInstalled) {
                return;
            }
            try {
                hookGetAdvertisingIdInfo();
                sInstalled = true;
            } catch (Throwable t) {
                Slog.e(TAG, "AAID hook install failed; real AAID visible", t);
            }
        }
    }

    private static void hookGetAdvertisingIdInfo() {
        final Class<?> clientClass;
        try {
            clientClass = Class.forName(CLIENT_CLASS);
        } catch (Throwable t) {
            Slog.d(TAG, "AdvertisingIdClient not in guest; skipping");
            return;
        }
        final Method target;
        try {
            target = clientClass.getDeclaredMethod("getAdvertisingIdInfo", Context.class);
        } catch (Throwable t) {
            Slog.e(TAG, "getAdvertisingIdInfo not found; skipping", t);
            return;
        }
        try {
            Pine.hook(target, new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable()) {
                        return;
                    }
                    BSpoofManager spoof = BSpoofManager.get();
                    if (spoof == null || !spoof.isSpoofActive()) {
                        return;
                    }
                    String aaid = spoof.getAdvertisingId();
                    if (aaid == null) {
                        return;
                    }
                    Object info = newInfo(clientClass, aaid);
                    if (info != null) {
                        callFrame.setResult(info);
                    }
                }
            });
            Slog.d(TAG, "AdvertisingIdClient hook installed");
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on getAdvertisingIdInfo failed", t);
        }
    }

    /** Builds {@code AdvertisingIdClient$Info(aaid, false)}; null on any failure. */
    private static Object newInfo(Class<?> clientClass, String aaid) {
        try {
            Class<?> infoClass = Class.forName(CLIENT_CLASS + "$Info");
            Constructor<?> ctor = infoClass.getDeclaredConstructor(String.class, boolean.class);
            ctor.setAccessible(true);
            return ctor.newInstance(aaid, false);
        } catch (Throwable t) {
            Slog.w(TAG, "cannot build AdvertisingId Info; passing through", t);
            return null;
        }
    }
}
