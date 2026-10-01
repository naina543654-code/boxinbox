package top.niunaijun.blackbox.fake.spoof;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.webkit.WebSettings;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Locale;
import java.util.TimeZone;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;
import top.niunaijun.blackbox.utils.Slog;

/**
 * Track E: Pine ART hooks for OS-level identity that the binder proxies and
 * Build reflection cannot cover.
 *
 * <ul>
 *   <li>{@code TimeZone.getDefault()} → profile timezone (coherent with the
 *       GPS city; the host's real zone would contradict the spoofed
 *       location).</li>
 *   <li>{@code Locale.getDefault()} → profile locale tag (coherent with the
 *       profile country).</li>
 *   <li>{@code BluetoothAdapter.getName()} → spoofed device model.</li>
 *   <li>{@code BluetoothAdapter.getAddress()} → per-identity MAC.</li>
 *   <li>{@code NetworkInterface.getHardwareAddress()} → per-identity MAC,
 *       but ONLY for {@code wlan0} — other interfaces pass through, so we
 *       don't break loopback/rmnet handling.</li>
 *   <li>{@code WebSettings.getDefaultUserAgent(Context)} → prebuilt UA with
 *       the spoofed model/build ID (the UA embeds the real ones otherwise).</li>
 * </ul>
 *
 * <p>All hooks are fail-open: an inactive profile, a missing profile value,
 * or any exception leaves the real value untouched. Recursion safety:
 * the hook bodies only call {@code BSpoofManager} getters, which route
 * through the {@code mLoading} re-entrancy guard in
 * {@link BSpoofManager#ensureLoaded()} — none of them call the hooked
 * methods themselves (no TimeZone/Locale/NetworkInterface use in the
 * profile loader).
 */
public final class BSpoofOsIdentity {
    private static final String TAG = "BSpoofOsIdentity";

    private BSpoofOsIdentity() {
    }

    public static void install() {
        hookTimeZone();
        hookLocale();
        hookBluetooth();
        hookWlan0Mac();
        hookWebViewUa();
    }

    private static void hookTimeZone() {
        try {
            Pine.hook(TimeZone.class.getDeclaredMethod("getDefault"), new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable()) {
                        return;
                    }
                    BSpoofManager spoof = BSpoofManager.get();
                    if (spoof == null || !spoof.isSpoofActive()) {
                        return;
                    }
                    String tz = spoof.getTimezoneId();
                    if (tz != null && !tz.isEmpty()) {
                        callFrame.setResult(TimeZone.getTimeZone(tz));
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on TimeZone.getDefault() failed", t);
        }
    }

    private static void hookLocale() {
        try {
            Pine.hook(Locale.class.getDeclaredMethod("getDefault"), new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable()) {
                        return;
                    }
                    BSpoofManager spoof = BSpoofManager.get();
                    if (spoof == null || !spoof.isSpoofActive()) {
                        return;
                    }
                    String tag = spoof.getLocaleTag();
                    if (tag != null && !tag.isEmpty()) {
                        callFrame.setResult(Locale.forLanguageTag(tag));
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on Locale.getDefault() failed", t);
        }
    }

    private static void hookBluetooth() {
        try {
            Pine.hook(BluetoothAdapter.class.getDeclaredMethod("getName"), new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable()) {
                        return;
                    }
                    BSpoofManager spoof = BSpoofManager.get();
                    if (spoof == null || !spoof.isSpoofActive()) {
                        return;
                    }
                    // The Bluetooth name is the device model on real phones.
                    String model = spoof.getBuildField("MODEL");
                    if (model != null && !model.isEmpty()) {
                        callFrame.setResult(model);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on BluetoothAdapter.getName() failed", t);
        }
        try {
            Pine.hook(BluetoothAdapter.class.getDeclaredMethod("getAddress"), new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable()) {
                        return;
                    }
                    BSpoofManager spoof = BSpoofManager.get();
                    if (spoof == null || !spoof.isSpoofActive()) {
                        return;
                    }
                    String mac = spoof.getBluetoothMac();
                    if (mac != null && !mac.isEmpty()) {
                        callFrame.setResult(mac);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on BluetoothAdapter.getAddress() failed", t);
        }
    }

    private static void hookWlan0Mac() {
        try {
            Pine.hook(NetworkInterface.class.getDeclaredMethod("getHardwareAddress"),
                    new MethodHook() {
                        @Override
                        public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                            if (callFrame.hasThrowable()) {
                                return;
                            }
                            Object recv = callFrame.thisObject;
                            if (!(recv instanceof NetworkInterface)) {
                                return;
                            }
                            NetworkInterface nif = (NetworkInterface) recv;
                            if (!"wlan0".equals(nif.getName())) {
                                return; // only the Wi-Fi interface is spoofed
                            }
                            BSpoofManager spoof = BSpoofManager.get();
                            if (spoof == null || !spoof.isSpoofActive()) {
                                return;
                            }
                            byte[] mac = parseMac(spoof.getWifiMac());
                            if (mac != null) {
                                callFrame.setResult(mac);
                            }
                        }
                    });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on NetworkInterface.getHardwareAddress() failed", t);
        }
    }

    private static void hookWebViewUa() {
        final Method target;
        try {
            target = WebSettings.class.getDeclaredMethod("getDefaultUserAgent", Context.class);
        } catch (Throwable t) {
            Slog.e(TAG, "WebSettings.getDefaultUserAgent(Context) not found; skipping", t);
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
                    String ua = spoof.getWebViewUa();
                    if (ua != null && !ua.isEmpty()) {
                        callFrame.setResult(ua);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on WebSettings.getDefaultUserAgent() failed", t);
        }
    }

    /** Parses "02:XX:XX:XX:XX:XX" into 6 bytes; null on malformed input. */
    private static byte[] parseMac(String mac) {
        if (mac == null) {
            return null;
        }
        String[] parts = mac.split(":");
        if (parts.length != 6) {
            return null;
        }
        byte[] out = new byte[6];
        try {
            for (int i = 0; i < 6; i++) {
                out[i] = (byte) Integer.parseInt(parts[i], 16);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return out;
    }
}
