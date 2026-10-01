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
 *    "androidVersion":"14","apiLevel":34,"securityPatch":"2024-01-05"},
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
 * <p>Intentionally NOT spoofed: {@code Build.VERSION.*} (SDK_INT/RELEASE drive the real
 * framework in-process; spoofing them destabilizes the guest — treated as Host Provided).
 * {@code Build.getSerial()} reads the {@code ro.serialno} system property natively and is
 * NOT covered by static-field reflection — Partially Supported.
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
    };

    private static final BSpoofManager sInstance = new BSpoofManager();

    private boolean mLoaded;
    private boolean mSpoofActive;

    private final Map<String, String> mBuildFields = new HashMap<>();
    private String mAndroidId;

    private double mLatitude;
    private double mLongitude;
    private float mLocationAccuracy;
    private double mAltitude;
    private boolean mMovementEnabled;
    private float mMovementSpeedMps;
    private float mMovementBearingDeg;
    private int[] mSensorTypes = new int[0];

    private String mSsid;
    private String mBssid;

    private String mOperatorName;
    private String mOperatorNumeric;
    private String mCountryIso;
    private String mTelephonyDeviceId;
    private String mSubscriberId;

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
     * (MANUFACTURER, BRAND, MODEL, DEVICE, PRODUCT, FINGERPRINT, ID, TAGS, TYPE,
     * BOARD, HARDWARE), or null when spoofing is inactive / the field is absent.
     */
    public String getBuildField(String name) {
        ensureLoaded();
        if (!mSpoofActive || name == null) {
            return null;
        }
        return mBuildFields.get(name);
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

    /** SSID as Android reports it, e.g. {@code "\"SandboxNet\""}. */
    public String getSsid() {
        ensureLoaded();
        return mSpoofActive ? mSsid : null;
    }

    public String getBssid() {
        ensureLoaded();
        return mSpoofActive ? mBssid : null;
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

    /**
     * Applies Build-field spoofing for this process via reflection on the
     * android.os.Build static finals. Must be called EARLY in each guest process
     * (HookManager.init(), from BlackBoxCore.doAttachBaseContext) because Build's
     * static fields are initialized once per process at class-init.
     *
     * <p>No-op unless this is a guest (BAppClient) process with an active profile.
     * Build.VERSION.* is intentionally left untouched. Build.getSerial() reads the
     * system property natively and is NOT covered by this method.
     */
    public void applyBuildSpoofing() {
        if (!BlackBoxCore.get().isBlackProcess()) {
            return;
        }
        if (!isSpoofActive()) {
            return;
        }
        for (Map.Entry<String, String> entry : mBuildFields.entrySet()) {
            setBuildStaticField(entry.getKey(), entry.getValue());
        }
        Slog.d(TAG, "applyBuildSpoofing: patched " + mBuildFields.size() + " Build fields");
    }

    private void setBuildStaticField(String fieldName, String value) {
        if (value == null) {
            return;
        }
        try {
            Field field = Build.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            // NOTE (2026-10-01): do NOT use the old "Field.modifiers" trick to strip
            // final — java.lang.reflect.Field has no such declared field on modern
            // Android (NoSuchFieldException), so every patch was silently failing and
            // guests saw the real host Build values. Plain setAccessible + set is
            // sufficient on ART for these non-constant static finals (they are
            // assigned via getString() in <clinit>, not compile-time constants).
            field.set(null, value);
        } catch (Throwable t) {
            Slog.w(TAG, "applyBuildSpoofing: failed to patch Build." + fieldName, t);
        }
    }

    private synchronized void ensureLoaded() {
        if (mLoaded) {
            return;
        }
        mLoaded = true;
        try {
            Context hostContext = BlackBoxCore.getContext();
            if (hostContext == null) {
                Slog.w(TAG, "ensureLoaded: host context not ready yet");
                return;
            }
            File profileFile = new File(hostContext.getFilesDir(), PROFILE_REL_PATH);
            if (!profileFile.isFile()) {
                Slog.d(TAG, "ensureLoaded: no profile at " + profileFile.getAbsolutePath()
                        + " — spoofing inactive, hooks pass through");
                return;
            }
            JSONObject root = new JSONObject(readFully(profileFile));
            parse(root);
            mSpoofActive = true;
            Slog.d(TAG, "ensureLoaded: spoof profile active: "
                    + root.optString("profileId", "<unknown>"));
        } catch (Throwable t) {
            Slog.w(TAG, "ensureLoaded: failed to parse spoof profile — spoofing inactive", t);
            mSpoofActive = false;
        }
    }

    private void parse(JSONObject root) {
        JSONObject device = root.optJSONObject("device");
        if (device != null) {
            for (String[] mapping : BUILD_FIELD_MAP) {
                String value = device.optString(mapping[1], null);
                if (value != null) {
                    mBuildFields.put(mapping[0], value);
                }
            }
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
                    types.add(sensor.optInt("type"));
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
        }

        JSONObject telephony = root.optJSONObject("telephony");
        if (telephony != null) {
            mOperatorName = telephony.optString("operatorName", null);
            mOperatorNumeric = telephony.optString("operatorNumeric", null);
            mCountryIso = telephony.optString("countryIso", null);
            mTelephonyDeviceId = telephony.optString("deviceId", null);
            mSubscriberId = telephony.optString("subscriberId", null);
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
