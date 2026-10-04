package top.niunaijun.blackbox.fake.spoof;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import java.lang.reflect.Field;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;
import top.niunaijun.blackbox.utils.Slog;

/**
 * Track F: hides the host device's real VPN from guest apps.
 *
 * <p>Why this exists: the sandbox gives guests real internet through the
 * host's active network. When that network is a VPN, the framework reports
 * {@code TRANSPORT_VPN} in {@link NetworkCapabilities}, names the interface
 * {@code tun0} in {@link LinkProperties}, and can type the active
 * {@link NetworkInfo} as VPN. Any guest checking "is a VPN active?" (a
 * common environment-integrity probe) immediately sees through the spoofed
 * identity: a phone in the profile's city reporting a personal VPN tunnel
 * contradicts the identity. Sanitizing the returned objects at the
 * {@link ConnectivityManager} public-API choke point removes the tell
 * without touching actual routing — the guest keeps working internet, it
 * just looks like plain Wi-Fi.
 *
 * <p>The objects returned by these getters are per-call binder copies owned
 * by the caller, so rewriting their fields in-place is safe and does not
 * propagate anywhere. The transport bitmask / interface-name fields are
 * non-SDK members; reflective access relies on the engine's hidden-API
 * unseal (see {@code BlackBoxCore}) and every rewrite is fail-open: any
 * failure leaves the original object untouched.
 *
 * <p>Scope note: Pine hooks are process-wide. This installer runs only in
 * guest/server processes (see {@code NetworkSpoofInjector}, registered in
 * {@code HookManager}), and every hook gates on
 * {@code BSpoofManager.isSpoofActive()} — no active profile, no rewrite.
 */
public final class BSpoofNetwork {
    private static final String TAG = "BSpoofNetwork";
    private static volatile boolean sInstalled = false;
    private static boolean sSanitizeWarned = false;

    private BSpoofNetwork() {
    }

    /** Installs the Pine hooks. Safe to call repeatedly; failures never throw. */
    public static void install() {
        if (sInstalled) {
            return;
        }
        synchronized (BSpoofNetwork.class) {
            if (sInstalled) {
                return;
            }
            hookCapabilities();
            hookLinkProperties();
            hookNetworkInfo();
            hookInterfaceEnumeration();
            sInstalled = true;
            Slog.d(TAG, "ConnectivityManager VPN-hiding hooks installed");
        }
    }

    private static void hookCapabilities() {
        try {
            Pine.hook(ConnectivityManager.class.getDeclaredMethod(
                    "getNetworkCapabilities", Network.class), new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable() || !isActive()) {
                        return;
                    }
                    Object result = callFrame.getResult();
                    if (result instanceof NetworkCapabilities) {
                        stripVpnTransport((NetworkCapabilities) result);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on getNetworkCapabilities failed", t);
        }
    }

    private static void hookLinkProperties() {
        try {
            Pine.hook(ConnectivityManager.class.getDeclaredMethod(
                    "getLinkProperties", Network.class), new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable() || !isActive()) {
                        return;
                    }
                    Object result = callFrame.getResult();
                    if (result instanceof LinkProperties) {
                        hideTunnelInterface((LinkProperties) result);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on getLinkProperties failed", t);
        }
    }

    private static void hookNetworkInfo() {
        try {
            Pine.hook(ConnectivityManager.class.getDeclaredMethod(
                    "getActiveNetworkInfo"), new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable() || !isActive()) {
                        return;
                    }
                    Object result = callFrame.getResult();
                    if (result instanceof NetworkInfo) {
                        hideVpnType((NetworkInfo) result);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on getActiveNetworkInfo failed", t);
        }
        try {
            Pine.hook(ConnectivityManager.class.getDeclaredMethod(
                    "getNetworkInfo", int.class), new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable() || !isActive()) {
                        return;
                    }
                    Object result = callFrame.getResult();
                    if (result instanceof NetworkInfo) {
                        hideVpnType((NetworkInfo) result);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on getNetworkInfo failed", t);
        }
        try {
            Pine.hook(ConnectivityManager.class.getDeclaredMethod(
                    "getAllNetworkInfo"), new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    if (callFrame.hasThrowable() || !isActive()) {
                        return;
                    }
                    Object result = callFrame.getResult();
                    if (result instanceof NetworkInfo[]) {
                        for (NetworkInfo info : (NetworkInfo[]) result) {
                            hideVpnType(info);
                        }
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on getAllNetworkInfo failed", t);
        }
    }

    private static boolean isActive() {
        BSpoofManager spoof = BSpoofManager.get();
        return spoof != null && spoof.isSpoofActive();
    }

    /**
     * Filters tunnel interfaces out of
     * {@code NetworkInterface.getNetworkInterfaces()}: interface enumeration
     * is not covered by the {@code ConnectivityManager} object rewrites
     * above, and a {@code tun0} entry here re-exposes the VPN.
     */
    private static void hookInterfaceEnumeration() {
        try {
            Pine.hook(NetworkInterface.class.getDeclaredMethod("getNetworkInterfaces"),
                    new MethodHook() {
                        @Override
                        public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                            if (callFrame.hasThrowable() || !isActive()) {
                                return;
                            }
                            Object result = callFrame.getResult();
                            if (!(result instanceof Enumeration)) {
                                return;
                            }
                            List<NetworkInterface> kept = new ArrayList<>();
                            Enumeration<?> enumeration = (Enumeration<?>) result;
                            while (enumeration.hasMoreElements()) {
                                Object o = enumeration.nextElement();
                                if (!(o instanceof NetworkInterface)) {
                                    continue;
                                }
                                NetworkInterface nif = (NetworkInterface) o;
                                String name;
                                try {
                                    name = nif.getName();
                                } catch (Throwable t) {
                                    continue;
                                }
                                if (name != null && (name.startsWith("tun")
                                        || name.startsWith("ppp")
                                        || name.startsWith("wg")
                                        || name.startsWith("vpn"))) {
                                    continue;
                                }
                                kept.add(nif);
                            }
                            callFrame.setResult(Collections.enumeration(kept));
                        }
                    });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on getNetworkInterfaces failed", t);
        }
    }

    /**
     * Removes TRANSPORT_VPN from the object's transport bitmask, leaving the
     * underlying transports (Wi-Fi) reported. Fail-open on any reflection
     * problem (e.g. a future framework rename of the field).
     */
    private static void stripVpnTransport(NetworkCapabilities caps) {
        try {
            if (!caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                return;
            }
            Field field = NetworkCapabilities.class.getDeclaredField("mTransportTypes");
            field.setAccessible(true);
            long transports = field.getLong(caps);
            field.setLong(caps,
                    transports & ~(1L << NetworkCapabilities.TRANSPORT_VPN));
        } catch (Throwable t) {
            warnOnce("stripVpnTransport", t);
        }
    }

    /** Renames a tunnel interface (tun0/ppp/wg…) to wlan0. Fail-open. */
    private static void hideTunnelInterface(LinkProperties props) {
        try {
            String name = props.getInterfaceName();
            if (name == null || !(name.startsWith("tun") || name.startsWith("ppp")
                    || name.startsWith("wg") || name.startsWith("vpn"))) {
                return;
            }
            Field field = LinkProperties.class.getDeclaredField("mInterfaceName");
            field.setAccessible(true);
            field.set(props, "wlan0");
        } catch (Throwable t) {
            warnOnce("hideTunnelInterface", t);
        }
    }

    /** Retypes a VPN NetworkInfo as Wi-Fi. Fail-open. */
    private static void hideVpnType(NetworkInfo info) {
        if (info == null) {
            return;
        }
        try {
            if (info.getType() != ConnectivityManager.TYPE_VPN) {
                return;
            }
            Field typeField = NetworkInfo.class.getDeclaredField("mNetworkType");
            typeField.setAccessible(true);
            typeField.setInt(info, ConnectivityManager.TYPE_WIFI);
            Field nameField = NetworkInfo.class.getDeclaredField("mTypeName");
            nameField.setAccessible(true);
            nameField.set(info, "WIFI");
        } catch (Throwable t) {
            warnOnce("hideVpnType", t);
        }
    }

    private static void warnOnce(String where, Throwable t) {
        if (!sSanitizeWarned) {
            sSanitizeWarned = true;
            Slog.w(TAG, where + ": sanitize reflection failed (fail-open)", t);
        }
    }
}
