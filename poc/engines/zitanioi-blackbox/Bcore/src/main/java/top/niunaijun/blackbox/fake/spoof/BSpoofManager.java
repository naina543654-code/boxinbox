package top.niunaijun.blackbox.fake.spoof;

import android.content.Context;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.utils.Slog;

/**
 * Per-identity device-profile spoofing manager (Track B).
 *
 * <p>Loads {@code <host app filesDir>/profiles/active_profile.json} lazily in EACH process
 * (guest stub processes share the host UID, so the host filesDir is readable from every
 * guest process). The host app writes this file; the engine only reads it.
 *
 * <p>Contract: if the profile file is missing or unparseable, {@link #isSpoofActive()}
 * returns false and every hook built on this manager MUST pass through to the real
 * implementation instead of returning (host) values as if they were virtualized.
 *
 * <p>Profile JSON schema (exact field names, written by the host):
 * <pre>
 * {"profileId":"...","generatedAt":123,
 *  "device":{"manufacturer":"Google","brand":"google","model":"Pixel 8 Pro",
 *    "device":"husky","product":"husky","board":"husky","hardware":"husky",
 *    "fingerprint":"google/husky/husky:14/UQ1A.240205.002/12038998:user/release-keys",
 *    "buildId":"UQ1A.240205.002","buildTags":"release-keys","buildType":"user",
 *    "androidVersion":"14","apiLevel":34,"securityPatch":"2024-01-05",
 *    "displayId":"UQ1A.240205.002","buildIncremental":"12038998",
 *    "kernelVersion":"6.1.25-android14-4-00001-g3f2e1d0c9b8a"},
 *  "androidId":"a1b2c3d4e5f60718",
 *  "location":{"latitude":37.42,"longitude":-122.08,"accuracy":12.0,"altitude":10.0,
 *    "speed":0.0,"bearing":0.0,
 *    "movement":{"enabled":false,"speedMps":1.4,"bearingDeg":90.0}},
 *  "sensors":[{"type":1,"name":"...","vendor":"..."}],
 *  "network":{"ssid":"\"SandboxNet\"","bssid":"02:15:3E:4A:5B:6C","transport":"WIFI"},
 *  "telephony":{"operatorName":"T-Mobile","operatorNumeric":"310260","countryIso":"us",
 *    "deviceId":"...","subscriberId":"..."}}
 * </pre>
 *
 * <p>Intentionally NOT spoofed: {@code Build.VERSION.RELEASE} and
 * {@code Build.VERSION.SDK_INT} (they drive the real framework in-process;
 * spoofing them destabilizes the guest — treated as Host Provided).
 * {@code Build.VERSION.SECURITY_PATCH} is spoofed only when the profile
 * carries a researched date (rows with "unknown" pass the host value
 * through). {@code Build.getSerial()} reads the {@code ro.serialno} system
 * property natively and is NOT covered by static-field reflection —
 * Partially Supported.
 */
public class BSpoofManager {
    private static final String TAG = "BSpoofManager";

    /** Relative to the host app filesDir. */
    private static final String PROFILE_REL_PATH = "profiles/active_profile.json";

    /** android.os.Build static field name -> profile device key. */
    private static final String[][] BUILD_FIELD_MAP = {
            {"MANUFACTURER", "manufacturer"},
            {"BRAND", "brand"},
            {"MODEL", "model"},
            {"DEVICE", "device"},
            {"PRODUCT", "product"},
            {"FINGERPRINT", "fingerprint"},
            {"ID", "buildId"},
            {"TAGS", "buildTags"},
            {"TYPE", "buildType"},
            {"BOARD", "board"},
            {"HARDWARE", "hardware"},
            {"DISPLAY", "displayId"},
            {"USER", "buildUser"},
            {"HOST", "buildHost"},
            {"BOOTLOADER", "bootloader"},
            {"RADIO", "radio"},
            // SoC fields are only patched when the profile carries a verified
            // value ("" for unknown hardware -> skipped in parse()).
            {"SOC_MANUFACTURER", "socManufacturer"},
            {"SOC_MODEL", "socModel"},
    };

    /** android.os.Build long static field name -> profile device key. */
    private static final String[][] BUILD_LONG_FIELD_MAP = {
            {"TIME", "buildTime"},
    };

    /** android.os.Build.VERSION static field name -> profile device key. */
    private static final String[][] VERSION_FIELD_MAP = {
            {"INCREMENTAL", "buildIncremental"},
            {"SECURITY_PATCH", "securityPatch"},
    };

    private static final BSpoofManager sInstance = new BSpoofManager();

    private boolean mLoaded;
    /**
     * Re-entrancy guard for {@link #ensureLoaded()}. The profile bootstrap can
     * trigger hooked APIs on this same thread (e.g. {@code Context.getFilesDir()}
     * internally calls {@code File.exists()}, which the root-hide hook
     * intercepts and routes back through {@link #isSpoofActive()} →
     * {@link #ensureLoaded()}). Without this guard the engine recurses until
     * Android kills the process for a provider-publication timeout (sandbox
     * freezes on the logo). A re-entrant call simply bails out; the outer
     * call completes the load.
     */
    private boolean mLoading;
    private boolean mSpoofActive;

    private final Map<String, String> mBuildFields = new HashMap<>();
    private final Map<String, Long> mBuildLongFields = new HashMap<>();
    private final Map<String, String> mVersionFields = new HashMap<>();
    private String mAndroidId;
    private String mKernelVersion;
    /** Profile Android version string ("13") and API level (33), for ro.build.version.* answers. */
    private String mVersionRelease;
    private int mApiLevel;

    /** Fixed Build.VERSION identity constants for release builds. */
    private static final String VERSION_CODENAME = "REL";
    private static final String VERSION_BASE_OS = "";
    private static final int VERSION_PREVIEW_SDK_INT = 0;

    private double mLatitude;
    private double mLongitude;
    private float mLocationAccuracy;
    private double mAltitude;
    private boolean mMovementEnabled;
    private float mMovementSpeedMps;
    private float mMovementBearingDeg;
    private int[] mSensorTypes = new int[0];
    private final Map<Integer, String> mSensorNames = new HashMap<>();
    private final Map<Integer, String> mSensorVendors = new HashMap<>();

    private String mSsid;
    private String mBssid;
    private String mWifiMac;
    private String mBluetoothMac;

    private String mOperatorName;
    private String mOperatorNumeric;
    private String mCountryIso;
    private String mTelephonyDeviceId;
    private String mSubscriberId;
    private int mNetworkType = 13; // TelephonyManager.NETWORK_TYPE_LTE
    private String mSimSerial;
    /** Per-identity MSISDN (E.164) for getLine1Number — never the real SIM number. */
    private String mPhoneNumber;
    private String mTimezoneId;
    private String mLocaleTag;
    private String mWebViewUa;

    public static BSpoofManager get() {
        return sInstance;
    }

    private BSpoofManager() {
    }

    public boolean isSpoofActive() {
        ensureLoaded();
        return mSpoofActive;
    }

    /**
     * Returns the profile value for an android.os.Build static field name
     * (MANUFACTURER, BRAND, MODEL, DEVICE, PRODUCT, FINGERPRINT, ID, TAGS,
     * TYPE, BOARD, HARDWARE, DISPLAY, USER, HOST, BOOTLOADER, RADIO,
     * SOC_MANUFACTURER, SOC_MODEL), or null when spoofing is inactive /
     * the field is absent.
     */
    public String getBuildField(String name) {
        ensureLoaded();
        if (!mSpoofActive || name == null) {
            return null;
        }
        return mBuildFields.get(name);
    }

    /**
     * Returns the profile value for an android.os.Build.VERSION static field
     * name (INCREMENTAL, SECURITY_PATCH), or null when spoofing is inactive /
     * the field is absent. SECURITY_PATCH is absent for device rows whose
     * researched patch is "unknown" — callers must pass through the host
     * value rather than spoofing a fabricated date.
     */
    public String getVersionField(String name) {
        ensureLoaded();
        if (!mSpoofActive || name == null) {
            return null;
        }
        return mVersionFields.get(name);
    }

    /**
     * Plausible kernel version for {@code System.getProperty("os.version")},
     * or null when spoofing is inactive / absent. Shape-real per API level
     * (5.10.x for 33, 6.1.x for 34); stops the host's real kernel (e.g. a
     * 4.19 Lineage kernel) from leaking through the JVM property.
     */
    public String getKernelVersion() {
        ensureLoaded();
        return mSpoofActive ? mKernelVersion : null;
    }

    public String getAndroidId() {
        ensureLoaded();
        return mSpoofActive ? mAndroidId : null;
    }

    public double getLatitude() {
        ensureLoaded();
        return mLatitude;
    }

    public double getLongitude() {
        ensureLoaded();
        return mLongitude;
    }

    public float getLocationAccuracy() {
        ensureLoaded();
        return mLocationAccuracy;
    }

    public double getAltitude() {
        ensureLoaded();
        return mAltitude;
    }

    public boolean isMovementEnabled() {
        ensureLoaded();
        return mMovementEnabled;
    }

    public float getMovementSpeedMps() {
        ensureLoaded();
        return mMovementSpeedMps;
    }

    public float getMovementBearingDeg() {
        ensureLoaded();
        return mMovementBearingDeg;
    }

    /** Sensor type constants (android.hardware.Sensor.TYPE_*) present on the spoofed device. */
    public int[] getSensorTypes() {
        ensureLoaded();
        return mSensorTypes;
    }

    /**
     * Profile's vendor string for a sensor type (e.g. "STMicroelectronics"),
     * or null when spoofing is inactive / the type has no profile entry.
     * Used by the Sensor.getVendor() Pine hook to hide the host's real
     * sensor hardware (bmi3x0/BOSCH, akm, eminent, qualcomm, ...).
     */
    public String getSensorVendor(int type) {
        ensureLoaded();
        return mSpoofActive ? mSensorVendors.get(type) : null;
    }

    /** Profile's name string for a sensor type, or null when unavailable. */
    public String getSensorName(int type) {
        ensureLoaded();
        return mSpoofActive ? mSensorNames.get(type) : null;
    }

    /** SSID as Android reports it, e.g. {@code "\"SandboxNet\""}. */
    public String getSsid() {
        ensureLoaded();
        return mSpoofActive ? mSsid : null;
    }

    public String getBssid() {
        ensureLoaded();
        return mSpoofActive ? mBssid : null;
    }

    /** Per-identity wlan0 MAC for NetworkInterface.getHardwareAddress("wlan0"). */
    public String getWifiMac() {
        ensureLoaded();
        return mSpoofActive ? mWifiMac : null;
    }

    /** Per-identity MAC for BluetoothAdapter.getAddress(). */
    public String getBluetoothMac() {
        ensureLoaded();
        return mSpoofActive ? mBluetoothMac : null;
    }

    /** IANA timezone ID for TimeZone.getDefault(), coherent with the GPS city. */
    public String getTimezoneId() {
        ensureLoaded();
        return mSpoofActive ? mTimezoneId : null;
    }

    /** BCP-47 locale tag for Locale.getDefault(), coherent with the country. */
    public String getLocaleTag() {
        ensureLoaded();
        return mSpoofActive ? mLocaleTag : null;
    }

    /** Prebuilt WebView default user-agent (spoofed model/build ID baked in). */
    public String getWebViewUa() {
        ensureLoaded();
        return mSpoofActive ? mWebViewUa : null;
    }

    /**
     * Per-identity advertising ID, stable within an identity and different
     * across identities. The cloned GMS would otherwise hand every guest
     * the host's real AAID — a cross-identity link. Derived deterministically
     * from the identity's own Android ID so it survives process restarts.
     */
    public String getAdvertisingId() {
        ensureLoaded();
        if (!mSpoofActive || mAndroidId == null) {
            return null;
        }
        return UUID.nameUUIDFromBytes(("aaid-" + mAndroidId).getBytes()).toString();
    }

    /**
     * Spoofed value for an {@code ro.*} system-property key, or null to pass
     * through. Used by the {@code getprop} exec interception in
     * {@link BRootHide}: {@code Runtime.exec("getprop …")} and
     * {@code ProcessBuilder("getprop", …)} bypass the Java
     * {@code SystemProperties} hooks entirely, so the property table has to
     * be answered at the exec layer too.
     */
    public String getSystemPropertySpoof(String key) {
        ensureLoaded();
        if (!mSpoofActive || key == null) {
            return null;
        }
        switch (key) {
            case "ro.product.manufacturer": return mBuildFields.get("MANUFACTURER");
            case "ro.product.brand": return mBuildFields.get("BRAND");
            case "ro.product.model": return mBuildFields.get("MODEL");
            case "ro.product.device": return mBuildFields.get("DEVICE");
            case "ro.product.name": return mBuildFields.get("PRODUCT");
            case "ro.product.board": return mBuildFields.get("BOARD");
            case "ro.build.display.id": return mBuildFields.get("DISPLAY");
            case "ro.build.id": return mBuildFields.get("ID");
            case "ro.build.version.incremental": return mVersionFields.get("INCREMENTAL");
            case "ro.build.version.security_patch": return mVersionFields.get("SECURITY_PATCH");
            case "ro.build.version.release": {
                String v = mBuildFields.get("VERSION_RELEASE");
                return v != null ? v : mVersionRelease;
            }
            case "ro.build.version.sdk": {
                String v = mBuildFields.get("VERSION_SDK");
                return v != null ? v : String.valueOf(mApiLevel);
            }
            case "ro.build.date.utc": {
                Long t = mBuildLongFields.get("TIME");
                return t != null ? String.valueOf(t / 1000L) : null;
            }
            case "ro.build.user": return mBuildFields.get("USER");
            case "ro.build.host": return mBuildFields.get("HOST");
            case "ro.build.tags": return mBuildFields.get("TAGS");
            case "ro.build.type": return mBuildFields.get("TYPE");
            case "ro.build.fingerprint": return mBuildFields.get("FINGERPRINT");
            case "ro.soc.manufacturer": return mBuildFields.get("SOC_MANUFACTURER");
            case "ro.soc.model": return mBuildFields.get("SOC_MODEL");
            case "ro.debuggable": return "0";
            case "ro.secure": return "1";
            default: return null;
        }
    }

    public String getOperatorName() {
        ensureLoaded();
        return mSpoofActive ? mOperatorName : null;
    }

    public String getOperatorNumeric() {
        ensureLoaded();
        return mSpoofActive ? mOperatorNumeric : null;
    }

    public String getCountryIso() {
        ensureLoaded();
        return mSpoofActive ? mCountryIso : null;
    }

    public String getTelephonyDeviceId() {
        ensureLoaded();
        return mSpoofActive ? mTelephonyDeviceId : null;
    }

    public String getSubscriberId() {
        ensureLoaded();
        return mSpoofActive ? mSubscriberId : null;
    }

    /** Per-identity ICCID for TelephonyManager.getSimSerialNumber(). */
    public String getSimSerial() {
        ensureLoaded();
        return mSpoofActive ? mSimSerial : null;
    }

    /**
     * Per-identity MSISDN (E.164) for {@code TelephonyManager.getLine1Number()}.
     * The real SIM number would be a stable cross-identity link for any
     * backend that keys accounts by device-reported number, so it is never
     * passed through. Null when spoofing is inactive / absent.
     */
    public String getPhoneNumber() {
        ensureLoaded();
        return mSpoofActive ? mPhoneNumber : null;
    }

    /**
     * Track B: per-identity radio network type from the spoof profile.
     * Returns -1 when no spoof is active or no valid value was staged.
     */
    public int getNetworkType() {
        ensureLoaded();
        return (mSpoofActive && mNetworkType >= 0) ? mNetworkType : -1;
    }

    /**
     * Applies Build-field spoofing for this process via reflection on the
     * android.os.Build static finals. Must be called EARLY in each guest process
     * (HookManager.init(), from BlackBoxCore.doAttachBaseContext) because Build's
     * static fields are initialized once per process at class-init.
     *
     * <p>No-op unless this is a guest (BAppClient) process with an active profile.
     * Build.VERSION.RELEASE / SDK_INT are intentionally left untouched (they
     * drive the real framework); INCREMENTAL and SECURITY_PATCH are patched
     * when the profile carries them. Build.getSerial() reads the system
     * property natively and is NOT covered by this method.
     */
    public void applyBuildSpoofing() {
        if (!BlackBoxCore.get().isBlackProcess()) {
            return;
        }
        if (!isSpoofActive()) {
            return;
        }
        for (Map.Entry<String, String> entry : mBuildFields.entrySet()) {
            setBuildStaticField(Build.class, entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, Long> entry : mBuildLongFields.entrySet()) {
            setBuildStaticLongField(Build.class, entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : mVersionFields.entrySet()) {
            setBuildStaticField(Build.VERSION.class, entry.getKey(), entry.getValue());
        }
        // Fixed release-build identity constants. CODENAME/BASE_OS/
        // PREVIEW_SDK_INT are the same on every real release build, so they
        // are constants rather than profile fields.
        setBuildStaticField(Build.VERSION.class, "CODENAME", VERSION_CODENAME);
        setBuildStaticField(Build.VERSION.class, "BASE_OS", VERSION_BASE_OS);
        setBuildStaticIntField(Build.VERSION.class, "PREVIEW_SDK_INT", VERSION_PREVIEW_SDK_INT);
        Slog.d(TAG, "applyBuildSpoofing: patched " + mBuildFields.size()
                + " Build fields + " + mBuildLongFields.size() + " long Build fields + "
                + mVersionFields.size() + " Build.VERSION fields");
    }

    private void setBuildStaticField(Class<?> clazz, String fieldName, String value) {
        if (value == null) {
            return;
        }
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            // NOTE (2026-10-01): do NOT use the old "Field.modifiers" trick to strip
            // final — java.lang.reflect.Field has no such declared field on modern
            // Android (NoSuchFieldException), so every patch was silently failing and
            // guests saw the real host Build values. Plain setAccessible + set is
            // sufficient on ART for these non-constant static finals (they are
            // assigned via getString() in <clinit>, not compile-time constants).
            field.set(null, value);
        } catch (Throwable t) {
            Slog.w(TAG, "applyBuildSpoofing: failed to patch " + clazz.getSimpleName()
                    + "." + fieldName, t);
        }
    }

    private void setBuildStaticLongField(Class<?> clazz, String fieldName, long value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setLong(null, value);
        } catch (Throwable t) {
            Slog.w(TAG, "applyBuildSpoofing: failed to patch " + clazz.getSimpleName()
                    + "." + fieldName, t);
        }
    }

    private void setBuildStaticIntField(Class<?> clazz, String fieldName, int value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setInt(null, value);
        } catch (Throwable t) {
            Slog.w(TAG, "applyBuildSpoofing: failed to patch " + clazz.getSimpleName()
                    + "." + fieldName, t);
        }
    }

    private synchronized void ensureLoaded() {
        if (mLoaded || mLoading) {
            return;
        }
        // NOTE: mLoaded is only set after a successful parse. A missing
        // context, missing file, or parse failure leaves mLoaded=false so the
        // next hook call retries — a transient early-init state must never
        // permanently disable spoofing for the life of the process.
        mLoading = true;
        try {
            Context hostContext = BlackBoxCore.getContext();
            if (hostContext == null) {
                Slog.w(TAG, "ensureLoaded: host context not ready yet — will retry");
                return;
            }
            // Resolve the profile path WITHOUT calling Context.getFilesDir() /
            // getDataDir(): on some framework builds those internally call
            // File.exists(), which our own root-hide hook intercepts and routes
            // back here (see mLoading). applicationInfo.dataDir is a plain
            // field read — no method dispatch, no hook re-entry.
            String dataDir = hostContext.getApplicationInfo().dataDir;
            if (dataDir == null) {
                Slog.w(TAG, "ensureLoaded: applicationInfo.dataDir null — will retry");
                return;
            }
            File profileFile = new File(dataDir + "/files", PROFILE_REL_PATH);
            if (!profileFile.isFile()) {
                Slog.d(TAG, "ensureLoaded: no profile at " + profileFile.getAbsolutePath()
                        + " — spoofing inactive, hooks pass through (will retry)");
                return;
            }
            JSONObject root = new JSONObject(readFully(profileFile));
            parse(root);
            mLoaded = true;
            mSpoofActive = true;
            Slog.d(TAG, "ensureLoaded: spoof profile active: "
                    + root.optString("profileId", "<unknown>"));
            // Procfs/build.prop tells bypass every Java hook; hide them via
            // the IO redirect table now that the profile is active.
            try {
                BProcFsSpoof.install(dataDir + "/files");
            } catch (Throwable t) {
                Slog.w(TAG, "ensureLoaded: BProcFsSpoof failed (fail-open)", t);
            }
        } catch (Throwable t) {
            Slog.w(TAG, "ensureLoaded: failed to parse spoof profile — spoofing inactive (will retry)", t);
            mSpoofActive = false;
        } finally {
            mLoading = false;
        }
    }

    private void parse(JSONObject root) {
        JSONObject device = root.optJSONObject("device");
        if (device != null) {
            for (String[] mapping : BUILD_FIELD_MAP) {
                String value = device.optString(mapping[1], null);
                // Empty values are never patched (e.g. SoC fields for device
                // rows whose hardware has no verified SoC mapping).
                if (value != null && !value.isEmpty()) {
                    mBuildFields.put(mapping[0], value);
                }
            }
            for (String[] mapping : BUILD_LONG_FIELD_MAP) {
                long value = device.optLong(mapping[1], 0L);
                if (value > 0L) {
                    mBuildLongFields.put(mapping[0], value);
                }
            }
            for (String[] mapping : VERSION_FIELD_MAP) {
                String value = device.optString(mapping[1], null);
                // "unknown" patch rows carry no researched date — never
                // spoof a fabricated security patch; pass the host value
                // through instead.
                if (value == null || value.isEmpty() || value.equals("unknown")) {
                    continue;
                }
                mVersionFields.put(mapping[0], value);
            }
            String kernel = device.optString("kernelVersion", null);
            if (kernel != null && !kernel.isEmpty()) {
                mKernelVersion = kernel;
            }
            String ua = device.optString("webViewUa", null);
            if (ua != null && !ua.isEmpty()) {
                mWebViewUa = ua;
            }
            mVersionRelease = device.optString("androidVersion", null);
            mApiLevel = device.optInt("apiLevel", 0);
        }

        mAndroidId = root.optString("androidId", null);

        JSONObject location = root.optJSONObject("location");
        if (location != null) {
            mLatitude = location.optDouble("latitude", 0d);
            mLongitude = location.optDouble("longitude", 0d);
            mLocationAccuracy = (float) location.optDouble("accuracy", 0d);
            mAltitude = location.optDouble("altitude", 0d);
            JSONObject movement = location.optJSONObject("movement");
            if (movement != null) {
                mMovementEnabled = movement.optBoolean("enabled", false);
                mMovementSpeedMps = (float) movement.optDouble("speedMps", 0d);
                mMovementBearingDeg = (float) movement.optDouble("bearingDeg", 0d);
            }
        }

        JSONArray sensors = root.optJSONArray("sensors");
        if (sensors != null) {
            List<Integer> types = new ArrayList<>();
            for (int i = 0; i < sensors.length(); i++) {
                JSONObject sensor = sensors.optJSONObject(i);
                if (sensor != null && sensor.has("type")) {
                    int type = sensor.optInt("type");
                    types.add(type);
                    String name = sensor.optString("name", null);
                    String vendor = sensor.optString("vendor", null);
                    if (name != null && !name.isEmpty()) {
                        mSensorNames.put(type, name);
                    }
                    if (vendor != null && !vendor.isEmpty()) {
                        mSensorVendors.put(type, vendor);
                    }
                }
            }
            mSensorTypes = new int[types.size()];
            for (int i = 0; i < types.size(); i++) {
                mSensorTypes[i] = types.get(i);
            }
        }

        JSONObject network = root.optJSONObject("network");
        if (network != null) {
            mSsid = network.optString("ssid", null);
            mBssid = network.optString("bssid", null);
            mWifiMac = network.optString("wifiMac", null);
            mBluetoothMac = network.optString("bluetoothMac", null);
        }

        JSONObject telephony = root.optJSONObject("telephony");
        if (telephony != null) {
            mOperatorName = telephony.optString("operatorName", null);
            mOperatorNumeric = telephony.optString("operatorNumeric", null);
            mCountryIso = telephony.optString("countryIso", null);
            mTelephonyDeviceId = telephony.optString("deviceId", null);
            mSubscriberId = telephony.optString("subscriberId", null);
            mNetworkType = telephony.optInt("networkType", 13);
            mSimSerial = telephony.optString("simSerial", null);
            mPhoneNumber = telephony.optString("phoneNumber", null);
        }

        JSONObject locale = root.optJSONObject("locale");
        if (locale != null) {
            mTimezoneId = locale.optString("timezoneId", null);
            mLocaleTag = locale.optString("localeTag", null);
        }
    }

    private static String readFully(File file) throws Exception {
        InputStream in = new FileInputStream(file);
        try {
            byte[] buf = new byte[(int) file.length()];
            int off = 0;
            int read;
            while (off < buf.length
                    && (read = in.read(buf, off, buf.length - off)) != -1) {
                off += read;
            }
            return new String(buf, 0, off, "UTF-8");
        } finally {
            try {
                in.close();
            } catch (Throwable ignored) {
            }
        }
    }
}
