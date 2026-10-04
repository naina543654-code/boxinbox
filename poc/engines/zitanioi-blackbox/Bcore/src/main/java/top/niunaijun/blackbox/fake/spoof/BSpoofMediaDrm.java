package top.niunaijun.blackbox.fake.spoof;

import android.media.MediaDrm;

import java.lang.reflect.Method;
import java.security.MessageDigest;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;
import top.niunaijun.blackbox.utils.Slog;

/**
 * Spoofs {@code MediaDrm.getPropertyByteArray("deviceUniqueId")} (Widevine).
 *
 * <p>The real Widevine device ID is stable across identities <b>and</b>
 * factory resets, requires no permission, and is a favorite fingerprinting
 * input for anti-fraud SDKs. Nothing in the engine hooked it.
 *
 * <p>Served value: 32 bytes of SHA-256 over {@code "widevine-" + androidId},
 * so it is stable within an identity, unique across identities, and shaped
 * like a real device ID. Inactive profile → genuine passthrough (guests are
 * not expected to run without an identity).
 */
public final class BSpoofMediaDrm {
    private static final String TAG = "BSpoofMediaDrm";
    private static volatile boolean sInstalled = false;

    private BSpoofMediaDrm() {
    }

    /** Installs the Pine hook. Safe to call repeatedly; failures never throw. */
    public static void install() {
        if (sInstalled) {
            return;
        }
        synchronized (BSpoofMediaDrm.class) {
            if (sInstalled) {
                return;
            }
            try {
                final Method target;
                try {
                    target = MediaDrm.class.getDeclaredMethod(
                            "getPropertyByteArray", String.class);
                } catch (Throwable t) {
                    Slog.e(TAG, "MediaDrm.getPropertyByteArray not found; skipping", t);
                    return;
                }
                Pine.hook(target, new MethodHook() {
                    @Override
                    public void beforeCall(Pine.CallFrame callFrame) {
                        Object[] args = callFrame.args;
                        if (args == null || args.length == 0
                                || !"deviceUniqueId".equals(args[0])) {
                            return;
                        }
                        BSpoofManager spoof = BSpoofManager.get();
                        if (!spoof.isSpoofActive()) {
                            return;
                        }
                        byte[] fake = widevineIdFor(spoof);
                        if (fake != null) {
                            callFrame.setResult(fake);
                        }
                    }
                });
                sInstalled = true;
                Slog.d(TAG, "MediaDrm.getPropertyByteArray hook installed");
            } catch (Throwable t) {
                // Fail open: real Widevine ID remains visible.
                Slog.e(TAG, "MediaDrm hook install failed", t);
            }
        }
    }

    /** Deterministic per-identity 32-byte Widevine-style device ID. */
    private static byte[] widevineIdFor(BSpoofManager spoof) {
        try {
            String androidId = spoof.getAndroidId();
            if (androidId == null) {
                return null;
            }
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            return sha256.digest(("widevine-" + androidId).getBytes("UTF-8"));
        } catch (Throwable t) {
            return null;
        }
    }
}
