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
            if (BSpoofManager.get().isSpoofActive()) {
                Log.d(TAG, "getScanResults: spoofed -> empty");
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
                return new ArrayList<>();
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
