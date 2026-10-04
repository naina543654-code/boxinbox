package top.niunaijun.blackbox.fake.service;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import black.android.net.wifi.BRIWifiManagerStub;
import black.android.net.wifi.BRWifiInfo;
import black.android.net.wifi.BRWifiSsid;
import black.android.os.BRServiceManager;
import top.niunaijun.blackbox.fake.hook.BinderInvocationStub;
import top.niunaijun.blackbox.fake.hook.MethodHook;
import top.niunaijun.blackbox.fake.hook.ProxyMethod;
import top.niunaijun.blackbox.fake.spoof.BSpoofManager;

/**
 * Created by Milk on 4/12/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class IWifiManagerProxy extends BinderInvocationStub {
    public static final String TAG = "IWifiManagerProxy";

    public IWifiManagerProxy() {
        super(BRServiceManager.get().getService(Context.WIFI_SERVICE));
    }

    @Override
    protected Object getWho() {
        return BRIWifiManagerStub.get().asInterface(BRServiceManager.get().getService(Context.WIFI_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    /**
     * Builds an empty {@code android.content.pm.ParceledListSlice} via
     * reflection (the class is @hide, absent from android.jar, but present
     * on every device). Returns null if construction fails, in which case
     * callers fall back to the real implementation rather than crashing
     * the guest.
     *
     * <p>R3 audit 2026-10-05: {@code getPrivilegedConfiguredNetworks}
     * declares {@code ParceledListSlice} too (AOSP IWifiManager.aidl) — the
     * review batch wrongly assumed a plain List and reintroduced the
     * ClassCastException crash class. Both slice-returning hooks share this.
     */
    static Object newParceledListSlice() {
        try {
            Class<?> sliceClass =
                    Class.forName("android.content.pm.ParceledListSlice");
            return sliceClass.getConstructor(java.util.List.class)
                    .newInstance(new ArrayList<>());
        } catch (Throwable t) {
            Log.w(TAG, "ParceledListSlice reflection failed", t);
            return null;
        }
    }

    @ProxyMethod("getScanResults")
    public static class GetScanResults extends MethodHook {
        /**
         * Track B: a spoofed identity must never see the host's real
         * surrounding Wi-Fi networks — nearby BSSIDs are a strong
         * geolocation signal. Return an empty list (no networks in range)
         * when spoofing is active; inactive profile -> pass through.
         */
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (BSpoofManager.get().isSpoofActive()) {                Log.d(TAG, "getScanResults: spoofed -> empty");
                return new ArrayList<>();
            }
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getConfiguredNetworks")
    public static class GetConfiguredNetworks extends MethodHook {
        /**
         * Audit fix 2026-10-04: only scan results and connection info were
         * hooked — the real SAVED networks (SSIDs/BSSIDs, strong geolocation,
         * stable across identities) leaked via getConfiguredNetworks().
         * A fresh identity has no saved networks: empty list when active.
         */
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (BSpoofManager.get().isSpoofActive()) {
                Log.d(TAG, "getConfiguredNetworks: spoofed -> empty");
                // Re-audit 2026-10-04 (CRITICAL): the AIDL declares
                // ParceledListSlice, not List — returning a raw ArrayList
                // throws ClassCastException in the guest. ParceledListSlice
                // is @hide (absent from android.jar), so instantiate it by
                // reflection against the on-device framework class.
                Object slice = newParceledListSlice();
                if (slice != null) {
                    return slice;
                }
                // R4 audit 2026-10-05 (R4-10): if the slice can't be built,
                // fail closed to null (callers tolerate it) — never hand the
                // guest the real saved networks while spoofing is active.
                return null;
            }
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getPasspointConfigurations")
    public static class GetPasspointConfigurations extends MethodHook {
        /**
         * Re-audit 2026-10-04 (N10): saved Passpoint (Hotspot 2.0) profiles
         * identify the user as strongly as saved SSIDs — a fresh identity
         * has none. Empty list when active; inactive profile -> pass through.
         * (Unlike getConfiguredNetworks, this AIDL declares a plain List,
         * so no ParceledListSlice wrapping is needed.)
         */
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (BSpoofManager.get().isSpoofActive()) {
                Log.d(TAG, "getPasspointConfigurations: spoofed -> empty");
                return new ArrayList<>();
            }
            return method.invoke(who, args);
        }
    }

    /**
     * R4 audit 2026-10-05 (R4-6): the regulatory country must agree with the
     * spoofed per-identity locale/country — the real one is a weak
     * inconsistency tell.
     */
    @ProxyMethod("getCountryCode")
    public static class GetCountryCode extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            BSpoofManager spoof = BSpoofManager.get();
            if (spoof.isSpoofActive()) {
                String iso = spoof.getCountryIso();
                if (iso != null) {
                    return iso.toUpperCase(java.util.Locale.US);
                }
                return null;
            }
            return method.invoke(who, args);
        }
    }

    /**
     * R4 audit 2026-10-05 (R4-7): DhcpInfo (IP, gateway, netmask, DNS) is a
     * fingerprint of the host's real LAN — null when active.
     */
    @ProxyMethod("getDhcpInfo")
    public static class GetDhcpInfo extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (BSpoofManager.get().isSpoofActive()) {
                return null;
            }
            return method.invoke(who, args);
        }
    }

    /**
     * R4 audit 2026-10-05 (R4-8): factory MACs are persistent device
     * identifiers. Server-side NETWORK_SETTINGS-gated (guest-unreachable in
     * practice) — defense in depth: empty when active.
     */
    @ProxyMethod("getFactoryMacAddresses")
    public static class GetFactoryMacAddresses extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (BSpoofManager.get().isSpoofActive()) {
                return new String[0];
            }
            return method.invoke(who, args);
        }
    }

    /**
     * R4 audit 2026-10-05 (R4-9): the privileged connected-network config
     * carries the real SSID/BSSID (BSSID link is the concern, given
     * getConnectionInfo is spoofed). NETWORK_SETTINGS-gated in practice —
     * defense in depth: null when active.
     */
    @ProxyMethod("getPrivilegedConnectedNetwork")
    public static class GetPrivilegedConnectedNetwork extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (BSpoofManager.get().isSpoofActive()) {
                return null;
            }
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getPrivilegedConfiguredNetworks")
    public static class GetPrivilegedConfiguredNetworks extends MethodHook {
        /**
         * Review 2026-10-05: the privileged variant of getConfiguredNetworks
         * (API 33+, NETWORK_SETTINGS-gated). R3 audit 2026-10-05: the AIDL
         * declares {@code ParceledListSlice} here too — the first version of
         * this hook returned a raw ArrayList and reintroduced the
         * ClassCastException crash. Reuses the shared slice helper.
         */
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (BSpoofManager.get().isSpoofActive()) {
                Log.d(TAG, "getPrivilegedConfiguredNetworks: spoofed -> empty slice");
                Object slice = IWifiManagerProxy.newParceledListSlice();
                if (slice != null) {
                    return slice;
                }
                // R4 audit 2026-10-05 (R4-10): fail closed to null, never the
                // real privileged networks.
                return null;
            }
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getWifiConfigsForPasspointProfiles")
    public static class GetWifiConfigsForPasspointProfiles extends MethodHook {
        /**
         * R3 audit 2026-10-05: saved WifiConfigurations backing Passpoint
         * profiles — same leak class as the other saved-network hooks.
         *
         * <p>Return-type safety (lesson of R3-1): the exact AIDL return type
         * for this method could not be re-verified against AOSP here, so the
         * hook inspects the interface method's declared return type at
         * runtime and returns the matching empty container — an empty
         * {@code ParceledListSlice} when the AIDL declares the slice (as
         * {@code getConfiguredNetworks} does), a plain empty list otherwise.
         * Either way the guest gets "no saved passpoint configs" with no
         * ClassCastException. Inactive profile -> pass through.
         */
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (BSpoofManager.get().isSpoofActive()) {
                Log.d(TAG, "getWifiConfigsForPasspointProfiles: spoofed -> empty");
                if (method.getReturnType().getName()
                        .equals("android.content.pm.ParceledListSlice")) {
                    Object slice = IWifiManagerProxy.newParceledListSlice();
                    if (slice != null) {
                        return slice;
                    }
                    // R4 audit 2026-10-05 (R4-10): fail closed to null.
                    return null;
                } else {
                    return new ArrayList<>();
                }
            }
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getConnectionInfo")
    public static class GetConnectionInfo extends MethodHook {
        /*
        * It doesn't have public method to set BSSID and SSID fields in WifiInfo class,
        * So the reflection framework invocation appeared.
        * commented by BlackBoxing at 2022/03/08
        *
        * Track B: SSID/BSSID now come from the per-identity spoof profile when active.
        * Inactive profile -> pass through to the real implementation (no spoofing).
        * */
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            WifiInfo wifiInfo = (WifiInfo) method.invoke(who, args);
            BSpoofManager spoof = BSpoofManager.get();
            if (spoof.isSpoofActive() && wifiInfo != null) {
                String bssid = spoof.getBssid();
                if (bssid != null) {
                    BRWifiInfo.get(wifiInfo)._set_mBSSID(bssid);
                    BRWifiInfo.get(wifiInfo)._set_mMacAddress(bssid);
                }
                String ssid = stripSsidQuotes(spoof.getSsid());
                if (ssid != null) {
                    BRWifiInfo.get(wifiInfo)._set_mWifiSsid(BRWifiSsid.get().createFromAsciiEncoded(ssid));
                }
            }
            return wifiInfo;
        }

        /**
         * The profile stores the SSID the way Android reports it (e.g. "\"SandboxNet\"");
         * WifiSsid holds the raw bytes, so strip the surrounding quotes before encoding.
         */
        private static String stripSsidQuotes(String ssid) {
            if (ssid != null && ssid.length() >= 2
                    && ssid.charAt(0) == '"' && ssid.charAt(ssid.length() - 1) == '"') {
                return ssid.substring(1, ssid.length() - 1);
            }
            return ssid;
        }

        public static String intIP2StringIP(int ip) {
            return (ip & 0xFF) + "." +
                    ((ip >> 8) & 0xFF) + "." +
                    ((ip >> 16) & 0xFF) + "." +
                    (ip >> 24 & 0xFF);
        }

        public static int ip2Int(String ipString) {
            // 取 ip 的各段
            String[] ipSlices = ipString.split("\\.");
            int rs = 0;
            for (int i = 0; i < ipSlices.length; i++) {
                // 将 ip 的每一段解析为 int，并根据位置左移 8 位
                int intSlice = Integer.parseInt(ipSlices[i]) << 8 * i;
                // 或运算
                rs = rs | intSlice;
            }
            return rs;
        }
    }
}
