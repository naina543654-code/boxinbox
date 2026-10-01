package com.sandboxpoc.probe;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.InstallSourceInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Process;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * SandboxProbe: measures raw observed device/environment values.
 * Runs both standalone and inside the BlackBox-style sandbox host.
 * NEVER asserts "virtualized: yes/no" - it only reports observations.
 *
 * ProbeV2 addition: when launched with the Intent extra "expected_profile"
 * (or when the host has staged expected_profile.json into the guest's
 * virtual files dir), compares EXPECTED vs OBSERVED per field.
 * (a JSON string of the active virtual device profile, see schema in the
 * section header), each observed field is compared against the expected
 * value and reported as PASS / FAIL / UNKNOWN / INFO, plus a greppable
 * logcat line per field with tag "ProbeV2".
 */
public class MainActivity extends Activity {

    private static final int REQ_PERMS = 1001;
    private static final String[] WANTED_PERMS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };

    /** Intent extra carrying the active virtual device profile as a JSON string. */
    private static final String EXTRA_EXPECTED_PROFILE = "expected_profile";

    private static final String TAG_V2 = "ProbeV2";

    // Best-effort list of well-known app-virtualization / dual-space host
    // package names. Package names verified via public APK metadata pages.
    // PRESENT/NOT PRESENT is reported raw; never used to conclude anything.
    private static final String[] KNOWN_VIRT_PACKAGES = new String[]{
            "com.lbe.parallel.intl",   // Parallel Space (LBE Tech)
            "com.ludashi.dualspace"    // DualSpace
    };

    private TextView reportView;
    private final SpannableStringBuilder report = new SpannableStringBuilder();
    private final ExecutorService bg = Executors.newSingleThreadExecutor();
    private volatile boolean permsAsked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        Button copyBtn = new Button(this);
        copyBtn.setText("Copy report");
        copyBtn.setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.setPrimaryClip(ClipData.newPlainText("SandboxProbe report", report.toString()));
                Toast.makeText(this, "Report copied to clipboard", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Clipboard unavailable", Toast.LENGTH_SHORT).show();
            }
        });

        reportView = new TextView(this);
        reportView.setTextIsSelectable(true);
        reportView.setTypeface(Typeface.MONOSPACE);
        reportView.setTextSize(12);
        reportView.setPadding(16, 16, 16, 16);
        reportView.setText("Collecting…");

        ScrollView sv = new ScrollView(this);
        sv.addView(reportView);

        root.addView(copyBtn);
        root.addView(sv, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));
        setContentView(root);

        startCollection();
    }

    private void startCollection() {
        bg.execute(() -> {
            collectAll();
            runOnUiThread(() -> reportView.setText(report));
            maybeAskPermissions();
        });
    }

    private void maybeAskPermissions() {
        if (permsAsked) return;
        permsAsked = true;
        List<String> missing = new ArrayList<>();
        for (String p : WANTED_PERMS) {
            if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) missing.add(p);
        }
        if (missing.isEmpty()) {
            appendFreshLocation();
        } else {
            requestPermissions(missing.toArray(new String[0]), REQ_PERMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMS) {
            appendFreshLocation();
        }
    }

    // ---------- report helpers ----------

    private synchronized void h(String title) {
        report.append("\n===== ").append(title).append(" =====\n");
    }

    private synchronized void kv(String k, Object v) {
        report.append(k).append(": ").append(v == null ? "null" : String.valueOf(v)).append("\n");
    }

    private synchronized void raw(String line) {
        report.append(line).append("\n");
    }

    private String permState(String perm) {
        return checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED ? "GRANTED" : "DENIED";
    }

    private String stamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US).format(new Date());
    }

    // ---------- collection ----------

    private void collectAll() {
        safe(this::sectionHeader);
        safe(this::sectionApp);
        safe(this::sectionBuild);
        safe(this::sectionDeviceId);
        safe(this::sectionLocation);
        safe(this::sectionSensors);
        safe(this::sectionPackages);
        safe(this::sectionNetwork);
        safe(this::sectionTelephony);
        safe(this::sectionFiles);
        safe(this::sectionGoogle);
        safe(this::sectionVirt);
        safe(this::sectionComparison);
    }

    private void safe(Runnable r) {
        try {
            r.run();
        } catch (Throwable t) {
            h("SECTION ERROR");
            kv("error", t.getClass().getSimpleName() + ": " + t.getMessage());
        }
    }

    private void sectionHeader() {
        h("SANDBOX PROBE REPORT");
        kv("generated_at", stamp());
        kv("probe_package", getPackageName());
    }

    private void sectionApp() {
        h("APP / INSTALL SOURCE");
        String pkg = getPackageName();
        kv("package", pkg);
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(pkg, 0);
            kv("versionName", pi.versionName);
            kv("versionCode", pi.getLongVersionCode());
        } catch (Exception e) {
            kv("version", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
        }
        try {
            InstallSourceInfo isi = getPackageManager().getInstallSourceInfo(pkg);
            kv("installingPackageName", isi.getInstallingPackageName());
            kv("initiatingPackageName", isi.getInitiatingPackageName());
        } catch (Exception e) {
            kv("install_source", "UNAVAILABLE (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")");
        }
    }

    private void sectionBuild() {
        h("BUILD PROPERTIES");
        kv("MANUFACTURER", Build.MANUFACTURER);
        kv("BRAND", Build.BRAND);
        kv("MODEL", Build.MODEL);
        kv("DEVICE", Build.DEVICE);
        kv("PRODUCT", Build.PRODUCT);
        kv("BOARD", Build.BOARD);
        kv("HARDWARE", Build.HARDWARE);
        kv("FINGERPRINT", Build.FINGERPRINT);
        kv("ID", Build.ID);
        kv("TAGS", Build.TAGS);
        kv("TYPE", Build.TYPE);
        kv("DISPLAY", Build.DISPLAY);
        kv("VERSION.RELEASE", Build.VERSION.RELEASE);
        kv("VERSION.SDK_INT", Build.VERSION.SDK_INT);
        kv("VERSION.SECURITY_PATCH", Build.VERSION.SECURITY_PATCH);
        kv("VERSION.INCREMENTAL", Build.VERSION.INCREMENTAL);
        kv("SUPPORTED_ABIS", String.join(",", Build.SUPPORTED_ABIS));
    }

    private void sectionDeviceId() {
        h("DEVICE ID");
        try {
            String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            kv("ANDROID_ID", id == null ? "UNAVAILABLE (null)" : id);
        } catch (Exception e) {
            kv("ANDROID_ID", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
        }
    }

    private void sectionLocation() {
        h("LOCATION");
        kv("fine_location", permState(Manifest.permission.ACCESS_FINE_LOCATION));
        kv("coarse_location", permState(Manifest.permission.ACCESS_COARSE_LOCATION));
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (lm == null) {
            kv("location_manager", "UNAVAILABLE");
            return;
        }
        List<String> providers;
        try {
            providers = lm.getAllProviders();
        } catch (Exception e) {
            kv("providers", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
            return;
        }
        kv("all_providers", providers);
        for (String p : providers) {
            boolean enabled;
            try {
                enabled = lm.isProviderEnabled(p);
            } catch (Exception e) {
                kv("provider." + p + ".enabled", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
                continue;
            }
            kv("provider." + p + ".enabled", enabled);
            try {
                Location l = lm.getLastKnownLocation(p);
                if (l == null) {
                    kv("provider." + p + ".last_known", "null");
                } else {
                    kv("provider." + p + ".last_known",
                            l.getLatitude() + "," + l.getLongitude()
                                    + " acc=" + l.getAccuracy() + "m"
                                    + " t=" + new Date(l.getTime())
                                    + " src=" + l.getProvider());
                }
            } catch (SecurityException se) {
                kv("provider." + p + ".last_known", "PERMISSION_DENIED");
            } catch (Exception e) {
                kv("provider." + p + ".last_known", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
            }
        }
        kv("fresh_location", "see LOCATION (fresh request) section below");
    }

    private void appendFreshLocation() {
        bg.execute(() -> {
            boolean fine = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
            boolean coarse = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
            h("LOCATION (fresh request)");
            kv("fine_granted", fine);
            kv("coarse_granted", coarse);
            if (!fine && !coarse) {
                kv("fresh_location", "PERMISSION_DENIED");
            } else {
                try {
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (lm == null) {
                        kv("fresh_location", "UNAVAILABLE (no LocationManager)");
                    } else {
                        String provider = null;
                        for (String p : new String[]{
                                LocationManager.GPS_PROVIDER,
                                LocationManager.NETWORK_PROVIDER,
                                LocationManager.PASSIVE_PROVIDER}) {
                            try {
                                if (lm.isProviderEnabled(p)) {
                                    provider = p;
                                    break;
                                }
                            } catch (Exception ignored) {
                            }
                        }
                        kv("provider_used", provider);
                        if (provider == null) {
                            kv("fresh_location", "UNAVAILABLE (no enabled provider)");
                        } else {
                            final Location[] out = new Location[1];
                            CountDownLatch latch = new CountDownLatch(1);
                            CancellationSignal cs = new CancellationSignal();
                            lm.getCurrentLocation(provider, cs, Runnable::run,
                                    loc -> {
                                        out[0] = loc;
                                        latch.countDown();
                                    });
                            boolean done = latch.await(12, TimeUnit.SECONDS);
                            if (!done) {
                                cs.cancel();
                                kv("fresh_location", "TIMEOUT (12s, no fix)");
                            } else if (out[0] == null) {
                                kv("fresh_location", "null (no fix)");
                            } else {
                                Location l = out[0];
                                kv("fresh_location", l.getLatitude() + "," + l.getLongitude());
                                kv("fresh_accuracy_m", l.getAccuracy());
                                kv("fresh_time", new Date(l.getTime()));
                                kv("fresh_provider", l.getProvider());
                            }
                        }
                    }
                } catch (SecurityException se) {
                    kv("fresh_location", "PERMISSION_DENIED");
                } catch (Exception e) {
                    kv("fresh_location", "UNAVAILABLE (" + e.getClass().getSimpleName()
                            + ": " + e.getMessage() + ")");
                }
            }
            runOnUiThread(() -> reportView.setText(report));
        });
    }
    private void sectionSensors() {
        h("SENSORS");
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sm == null) {
            kv("sensor_manager", "UNAVAILABLE");
            return;
        }
        List<Sensor> list;
        try {
            list = sm.getSensorList(Sensor.TYPE_ALL);
        } catch (Exception e) {
            kv("sensors", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
            return;
        }
        kv("count", list.size());
        int i = 0;
        for (Sensor s : list) {
            kv("sensor[" + (i++) + "]",
                    s.getName() + " | vendor=" + s.getVendor()
                            + " | version=" + s.getVersion() + " | type=" + s.getType());
        }
    }

    private void sectionPackages() {
        h("PACKAGES");
        PackageManager pm = getPackageManager();
        List<PackageInfo> pkgs;
        try {
            pkgs = pm.getInstalledPackages(0);
        } catch (Exception e) {
            kv("installed_packages", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
            return;
        }
        kv("installed_packages_count", pkgs.size());
        kv("visibility_note",
                "list reflects package-visibility filtering (no QUERY_ALL_PACKAGES); "
                        + "manifest <queries> covers the launcher intent");
        List<String> names = new ArrayList<>();
        for (PackageInfo pi : pkgs) names.add(pi.packageName);
        Collections.sort(names);
        int n = Math.min(30, names.size());
        kv("first_" + n + "_sorted_shown", n);
        for (int i = 0; i < n; i++) raw("  - " + names.get(i));

        Intent launcher = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> acts;
        try {
            acts = pm.queryIntentActivities(launcher, 0);
        } catch (Exception e) {
            kv("launcher_activities", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
            return;
        }
        kv("launcher_activities_count", acts.size());
        List<String> comps = new ArrayList<>();
        for (ResolveInfo ri : acts) {
            if (ri.activityInfo != null) comps.add(ri.activityInfo.packageName + "/" + ri.activityInfo.name);
        }
        Collections.sort(comps);
        int m = Math.min(15, comps.size());
        kv("first_" + m + "_launcher_activities_shown", m);
        for (int i = 0; i < m; i++) raw("  - " + comps.get(i));
    }

    private void sectionNetwork() {
        h("NETWORK");
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            kv("connectivity_manager", "UNAVAILABLE");
            return;
        }
        try {
            Network net = cm.getActiveNetwork();
            kv("active_network", net == null ? "null (offline?)" : net.toString());
            if (net != null) {
                NetworkCapabilities caps = cm.getNetworkCapabilities(net);
                if (caps == null) {
                    kv("network_capabilities", "null");
                } else {
                    List<String> t = new ArrayList<>();
                    if (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) t.add("WIFI");
                    if (caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) t.add("CELLULAR");
                    if (caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) t.add("ETHERNET");
                    if (caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) t.add("VPN");
                    if (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) t.add("WIFI_AWARE");
                    if (caps.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) t.add("LOWPAN");
                    if (caps.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) t.add("BLUETOOTH");
                    kv("transports", t);
                }
                LinkProperties lp = cm.getLinkProperties(net);
                kv("link_interface", lp == null ? "null" : lp.getInterfaceName());
            }
        } catch (Exception e) {
            kv("connectivity", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
        }
    }

    private void sectionTelephony() {
        h("TELEPHONY");
        kv("read_phone_state", permState(Manifest.permission.READ_PHONE_STATE));
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            kv("network_operator_name", "PERMISSION_DENIED");
            kv("data_network_type", "PERMISSION_DENIED");
            return;
        }
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            kv("telephony_manager", "UNAVAILABLE");
            return;
        }
        try {
            kv("network_operator_name", tm.getNetworkOperatorName());
        } catch (SecurityException se) {
            kv("network_operator_name", "PERMISSION_DENIED");
        } catch (Exception e) {
            kv("network_operator_name", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
        }
        try {
            int nt = tm.getDataNetworkType();
            kv("data_network_type", nt + " (" + networkTypeName(nt) + ")");
        } catch (SecurityException se) {
            kv("data_network_type", "PERMISSION_DENIED");
        } catch (Exception e) {
            kv("data_network_type", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
        }
    }

    private String networkTypeName(int t) {
        switch (t) {
            case TelephonyManager.NETWORK_TYPE_GPRS: return "GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE: return "EDGE";
            case TelephonyManager.NETWORK_TYPE_UMTS: return "UMTS";
            case TelephonyManager.NETWORK_TYPE_HSDPA: return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA: return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA: return "HSPA";
            case TelephonyManager.NETWORK_TYPE_LTE: return "LTE";
            case TelephonyManager.NETWORK_TYPE_NR: return "NR";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN: return "UNKNOWN";
            default: return "code=" + t;
        }
    }

    private void sectionFiles() {
        h("FILESYSTEM / ENVIRONMENT");
        kv("filesDir", getFilesDir().getAbsolutePath());
        kv("cacheDir", getCacheDir().getAbsolutePath());
        try {
            java.io.File ef = getExternalFilesDir(null);
            kv("externalFilesDir", ef == null ? "null" : ef.getAbsolutePath());
        } catch (Exception e) {
            kv("externalFilesDir", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
        }
        try {
            kv("dataDir", getDataDir().getAbsolutePath());
        } catch (Exception e) {
            kv("dataDir", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
        }
        kv("uid", Process.myUid());
        kv("os.name", System.getProperty("os.name"));
        kv("os.version", System.getProperty("os.version"));
        kv("os.arch", System.getProperty("os.arch"));
        kv("java.vm.name", System.getProperty("java.vm.name"));
    }

    private void sectionGoogle() {
        h("GOOGLE SERVICES");
        PackageManager pm = getPackageManager();
        try {
            PackageInfo gms = pm.getPackageInfo("com.google.android.gms", 0);
            kv("gms_installed", "PRESENT");
            kv("gms_versionName", gms.versionName);
            kv("gms_versionCode", gms.getLongVersionCode());
        } catch (PackageManager.NameNotFoundException e) {
            kv("gms_installed", "NOT PRESENT");
        } catch (Exception e) {
            kv("gms_installed", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
        }
        try {
            int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
            kv("gms_availability_code", code + " (" + gmsCodeName(code) + ")");
        } catch (Exception e) {
            kv("gms_availability_code", "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
        }
    }

    private String gmsCodeName(int c) {
        switch (c) {
            case ConnectionResult.SUCCESS: return "SUCCESS";
            case ConnectionResult.SERVICE_MISSING: return "SERVICE_MISSING";
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED: return "SERVICE_VERSION_UPDATE_REQUIRED";
            case ConnectionResult.SERVICE_DISABLED: return "SERVICE_DISABLED";
            case ConnectionResult.SERVICE_INVALID: return "SERVICE_INVALID";
            case ConnectionResult.SIGN_IN_REQUIRED: return "SIGN_IN_REQUIRED";
            default: return "code=" + c;
        }
    }

    private void sectionVirt() {
        h("VIRTUALIZATION INDICATORS (raw observations only - NO conclusion drawn)");
        ApplicationInfo ai = getApplicationInfo();
        kv("appInfo.sourceDir", ai.sourceDir);
        kv("appInfo.publicSourceDir", ai.publicSourceDir);
        kv("appInfo.dataDir", ai.dataDir);
        kv("appInfo.nativeLibraryDir", ai.nativeLibraryDir);
        String pkg = getPackageName();
        kv("expected_dataDir_a", "/data/data/" + pkg);
        kv("expected_dataDir_b", "/data/user/0/" + pkg);
        kv("actual_filesDir", getFilesDir().getAbsolutePath());
        kv("note", "compare actual vs expected paths yourself; mismatch alone proves nothing");
        PackageManager pm = getPackageManager();
        for (String k : KNOWN_VIRT_PACKAGES) {
            try {
                pm.getPackageInfo(k, 0);
                kv("known_virt_pkg." + k, "PRESENT");
            } catch (PackageManager.NameNotFoundException e) {
                kv("known_virt_pkg." + k, "NOT PRESENT");
            } catch (Exception e) {
                kv("known_virt_pkg." + k, "UNAVAILABLE (" + e.getClass().getSimpleName() + ")");
            }
        }
    }

    // ============================================================
    // EXPECTED vs OBSERVED comparison (ProbeV2)
    //
    // Comparison semantics:
    //   PASS    - expected value present and exactly equals observed.
    //   FAIL    - expected value present, observed differs.
    //   UNKNOWN - no expected value (standalone run / field absent from the
    //             profile) OR the observed value could not be measured
    //             (permission denied, API unavailable, empty result).
    //   INFO    - informational row: never a pass/fail assertion. Used for
    //             VERSION.RELEASE / SDK_INT (host-provided by design, never
    //             spoofed), package listing, active transport, movement.
    //
    // Tolerances:
    //   location lat/lon : |delta| <= 1e-4 degrees (~11 m)
    //   location accuracy: |delta| <= max(10 m, 50% of expected accuracy)
    //   sensors          : PASS only when the observed type-int set EQUALS
    //                      the expected set exactly; missing and unexpected
    //                      type ints are listed separately.
    //   wifi bssid       : case-insensitive compare (quoting on SSID kept
    //                      exact, Android returns the SSID quoted too).
    //   telephony        : operatorName / countryIso case-insensitive,
    //                      operatorNumeric exact.
    //
    // Every row is also logged to logcat (tag ProbeV2):
    //   ProbeV2: FIELD=<name> EXPECTED=<e> OBSERVED=<o> STATE=<PASS|FAIL|UNKNOWN|INFO>
    // Full (untruncated) values live in logcat; the UI truncates to 24 chars.
    // ============================================================

    private enum Cmp { PASS, FAIL, UNKNOWN, INFO }

    private JSONObject expectedProfile;

    private void sectionComparison() {
        h("EXPECTED vs OBSERVED (ProbeV2)");
        Intent intent = getIntent();
        String json = intent != null ? intent.getStringExtra(EXTRA_EXPECTED_PROFILE) : null;
        if ((json == null || json.isEmpty())) {
            // Fallback: the host stages expected_profile.json into the guest's
            // virtual files dir before launch (engine redirects getFilesDir()).
            java.io.File staged = new java.io.File(getFilesDir(), "expected_profile.json");
            if (staged.isFile()) {
                try {
                    StringBuilder sb = new StringBuilder((int) staged.length());
                    java.io.BufferedReader br = new java.io.BufferedReader(
                            new java.io.FileReader(staged));
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line).append('\n');
                    br.close();
                    json = sb.toString();
                    raw("expected_profile loaded from staged file: " + staged.getAbsolutePath());
                } catch (Exception e) {
                    raw("Could not read staged expected_profile.json: "
                            + e.getClass().getSimpleName() + ": " + e.getMessage());
                    json = null;
                }
            }
        }
        if (json == null || json.isEmpty()) {
            raw("No '" + EXTRA_EXPECTED_PROFILE + "' intent extra and no staged file found: standalone run.");
            raw("Rows below use expected=\"(none)\" -> UNKNOWN for every comparison.");
            expectedProfile = null;
        } else {
            try {
                expectedProfile = new JSONObject(json);
                kv("profile_id", opt(expectedProfile, "profileId"));
                Object gen = expectedProfile.opt("generatedAt");
                kv("generated_at", gen == null || JSONObject.NULL.equals(gen)
                        ? null : new Date(expectedProfile.optLong("generatedAt")));
            } catch (Exception e) {
                raw("Could not parse expected_profile JSON: "
                        + e.getClass().getSimpleName() + ": " + e.getMessage());
                expectedProfile = null;
            }
        }
        raw("");
        raw("FIELD                  | EXPECTED                 | OBSERVED                 | STATE");
        raw("-----------------------+--------------------------+--------------------------+-------");
        JSONObject dev = expectedProfile == null ? null : expectedProfile.optJSONObject("device");
        cmpBuild(dev);
        cmpVersionInfo(dev);
        cmpAndroidId();
        cmpLocation();
        cmpSensors();
        cmpSensorIdentity();
        cmpWifi();
        cmpTelephony();
        cmpCellPrivacy();
        cmpLocale();
        cmpLinkIds();
        cmpWebViewUa();
        cmpPackages();
        cmpTransport();
        cmpMovement();
        raw("");
        raw("Tolerances: lat/lon |delta|<=1e-4 deg; accuracy |delta|<=max(10m,50% of expected);");
        raw("sensors PASS only on exact type-set equality; bssid case-insensitive.");
        raw("UNKNOWN = no expected value or value unmeasurable (permission/API).");
        raw("INFO rows are never spoof assertions (host-provided / manual-review fields).");
        raw("Full values in logcat: adb logcat -s ProbeV2");
    }

    // ---------- comparison row ----------

    private synchronized void cmpRow(String field, String expected, String observed, Cmp state) {
        String e = expected == null ? "(none)" : expected;
        String o = observed == null ? "(unavailable)" : observed;
        String row = pad(field, 22) + " | " + pad(trunc(e, 24), 24) + " | "
                + pad(trunc(o, 24), 24) + " | " + state.name();
        int start = report.length();
        report.append(row).append('\n');
        int tok = start + row.lastIndexOf(state.name());
        report.setSpan(new ForegroundColorSpan(colorFor(state)), tok,
                tok + state.name().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Log.i(TAG_V2, "FIELD=" + oneLine(field) + " EXPECTED=" + oneLine(e)
                + " OBSERVED=" + oneLine(o) + " STATE=" + state.name());
    }

    private int colorFor(Cmp s) {
        switch (s) {
            case PASS: return 0xFF2E7D32;   // green
            case FAIL: return 0xFFC62828;   // red
            case INFO: return 0xFF1565C0;   // blue
            default:   return 0xFF757575;   // gray
        }
    }

    private static String pad(String s, int w) {
        if (s.length() >= w) return s.substring(0, w);
        StringBuilder b = new StringBuilder(s);
        while (b.length() < w) b.append(' ');
        return b.toString();
    }

    private static String trunc(String s, int w) {
        if (s.length() <= w) return s;
        return s.substring(0, w - 1) + "…";
    }

    private static String oneLine(String s) {
        String t = s.replace('\n', ' ').replace('\r', ' ');
        return t.length() <= 160 ? t : t.substring(0, 157) + "...";
    }

    // ---------- JSON helpers ----------

    /** null when the key is missing, JSON null, or an empty string. */
    private static String opt(JSONObject o, String key) {
        if (o == null) return null;
        String v = o.optString(key, null);
        return (v == null || v.isEmpty()) ? null : v;
    }

    private static Double optDouble(JSONObject o, String key) {
        if (o == null || !o.has(key) || o.isNull(key)) return null;
        try {
            return o.getDouble(key);
        } catch (Exception e) {
            return null;
        }
    }

    private static String optNumber(JSONObject o, String key) {
        if (o == null || !o.has(key) || o.isNull(key)) return null;
        return String.valueOf(o.opt(key));
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }

    // ---------- field comparisons ----------

    private void cmpString(String field, String expected, String observed) {
        cmpString(field, expected, observed, false);
    }

    private void cmpString(String field, String expected, String observed, boolean ignoreCase) {
        if (expected == null) {
            cmpRow(field, null, observed, Cmp.UNKNOWN);
            return;
        }
        if (observed == null) {
            cmpRow(field, expected, null, Cmp.UNKNOWN);
            return;
        }
        boolean ok = ignoreCase ? expected.equalsIgnoreCase(observed) : expected.equals(observed);
        cmpRow(field, expected, observed, ok ? Cmp.PASS : Cmp.FAIL);
    }

    private void cmpBuild(JSONObject dev) {
        cmpString("Build.MANUFACTURER", opt(dev, "manufacturer"), Build.MANUFACTURER);
        cmpString("Build.BRAND", opt(dev, "brand"), Build.BRAND);
        cmpString("Build.MODEL", opt(dev, "model"), Build.MODEL);
        cmpString("Build.DEVICE", opt(dev, "device"), Build.DEVICE);
        cmpString("Build.PRODUCT", opt(dev, "product"), Build.PRODUCT);
        cmpString("Build.FINGERPRINT", opt(dev, "fingerprint"), Build.FINGERPRINT);
        cmpString("Build.ID", opt(dev, "buildId"), Build.ID);
        cmpString("Build.TAGS", opt(dev, "buildTags"), Build.TAGS);
        cmpString("Build.TYPE", opt(dev, "buildType"), Build.TYPE);
        cmpString("Build.BOARD", opt(dev, "board"), Build.BOARD);
        cmpString("Build.HARDWARE", opt(dev, "hardware"), Build.HARDWARE);
        cmpString("Build.DISPLAY", opt(dev, "displayId"), Build.DISPLAY);
        cmpString("Build.TIME", optNumber(dev, "buildTime"), String.valueOf(Build.TIME));
        cmpString("Build.USER", opt(dev, "buildUser"), Build.USER);
        cmpString("Build.HOST", opt(dev, "buildHost"), Build.HOST);
        cmpString("Build.BOOTLOADER", opt(dev, "bootloader"), Build.BOOTLOADER);
        cmpString("Build.RADIO", opt(dev, "radio"), Build.RADIO);
        // SoC is only spoofed for rows with a verified hardware->SoC mapping;
        // "" (unknown) -> expected=null -> UNKNOWN, never a fabricated value.
        cmpString("Build.SOC_MANUFACTURER", opt(dev, "socManufacturer"),
                Build.SOC_MANUFACTURER);
        cmpString("Build.SOC_MODEL", opt(dev, "socModel"), Build.SOC_MODEL);
    }

    private void cmpVersionInfo(JSONObject dev) {
        // Host-provided by design, never spoofed: INFO only, never PASS/FAIL.
        cmpRow("Build.VERSION.RELEASE", opt(dev, "androidVersion"),
                Build.VERSION.RELEASE, Cmp.INFO);
        cmpRow("Build.VERSION.SDK_INT", optNumber(dev, "apiLevel"),
                String.valueOf(Build.VERSION.SDK_INT), Cmp.INFO);
        // Spoofed since 2026-10-02: INCREMENTAL (parsed from the fingerprint)
        // and SECURITY_PATCH (researched per device row).
        cmpString("Build.VERSION.INCREMENTAL", opt(dev, "buildIncremental"),
                Build.VERSION.INCREMENTAL);
        String ePatch = opt(dev, "securityPatch");
        if ("unknown".equals(ePatch)) ePatch = null; // not spoofed for unknown rows
        cmpString("Build.VERSION.SECURITY_PATCH", ePatch, Build.VERSION.SECURITY_PATCH);
        // Fixed release-build identity constants (same on every real release
        // build; engine sets them unconditionally when spoofing is active).
        cmpString("Build.VERSION.CODENAME", "REL", Build.VERSION.CODENAME);
        // "" on every real release build; engine sets it unconditionally.
        cmpString("Build.VERSION.BASE_OS", "", Build.VERSION.BASE_OS);
        cmpString("Build.VERSION.PREVIEW_SDK_INT", "0",
                String.valueOf(Build.VERSION.PREVIEW_SDK_INT));
        // JVM property spoof: System.getProperty("os.version") should report
        // the profile's plausible kernel, not the host kernel.
        String oKernel;
        try {
            oKernel = System.getProperty("os.version");
        } catch (Exception e) {
            oKernel = null;
        }
        cmpString("os.version(kernel)", opt(dev, "kernelVersion"), oKernel);
    }

    private void cmpAndroidId() {
        String observed;
        try {
            observed = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            observed = null;
        }
        cmpString("Settings.ANDROID_ID", opt(expectedProfile, "androidId"), observed);
    }

    private Location gpsLastKnown() {
        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            return lm == null ? null : lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException se) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static String fmtLoc(Location l) {
        return l.getLatitude() + "," + l.getLongitude();
    }

    private void cmpLocation() {
        JSONObject loc = expectedProfile == null ? null : expectedProfile.optJSONObject("location");
        Double eLat = optDouble(loc, "latitude");
        Double eLon = optDouble(loc, "longitude");
        Double eAcc = optDouble(loc, "accuracy");
        Location observed = gpsLastKnown();
        if (eLat == null || eLon == null) {
            cmpRow("location.lat,lon", null,
                    observed == null ? null : fmtLoc(observed), Cmp.UNKNOWN);
            return;
        }
        if (observed == null) {
            cmpRow("location.lat,lon", eLat + "," + eLon, null, Cmp.UNKNOWN);
            cmpRow("location.accuracy", eAcc == null ? null : eAcc + "m", null, Cmp.UNKNOWN);
            return;
        }
        boolean latOk = Math.abs(observed.getLatitude() - eLat) <= 1e-4;
        boolean lonOk = Math.abs(observed.getLongitude() - eLon) <= 1e-4;
        cmpRow("location.lat,lon", eLat + "," + eLon, fmtLoc(observed),
                (latOk && lonOk) ? Cmp.PASS : Cmp.FAIL);
        if (eAcc != null) {
            double tol = Math.max(10.0, 0.5 * eAcc);
            boolean accOk = Math.abs(observed.getAccuracy() - eAcc) <= tol;
            cmpRow("location.accuracy", eAcc + "m", observed.getAccuracy() + "m",
                    accOk ? Cmp.PASS : Cmp.FAIL);
        } else {
            cmpRow("location.accuracy", null, observed.getAccuracy() + "m", Cmp.UNKNOWN);
        }
    }

    private static String sortedTypeList(Set<Integer> set) {
        List<Integer> l = new ArrayList<>(set);
        Collections.sort(l);
        List<String> s = new ArrayList<>();
        for (Integer i : l) s.add(String.valueOf(i));
        return "[" + String.join(",", s) + "]";
    }

    private void cmpSensors() {
        Set<Integer> expected = new HashSet<>();
        JSONArray arr = expectedProfile == null ? null : expectedProfile.optJSONArray("sensors");
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject s = arr.optJSONObject(i);
                if (s != null && s.has("type") && !s.isNull("type")) {
                    expected.add(s.optInt("type"));
                }
            }
        }
        Set<Integer> observed = new HashSet<>();
        try {
            SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if (sm != null) {
                for (Sensor s : sm.getSensorList(Sensor.TYPE_ALL)) observed.add(s.getType());
            }
        } catch (Exception ignored) {
        }
        if (arr == null) {
            cmpRow("sensors.type-set", null, sortedTypeList(observed), Cmp.UNKNOWN);
            return;
        }
        Set<Integer> missing = new HashSet<>(expected);
        missing.removeAll(observed);
        Set<Integer> extra = new HashSet<>(observed);
        extra.removeAll(expected);
        Cmp st = (missing.isEmpty() && extra.isEmpty()) ? Cmp.PASS : Cmp.FAIL;
        cmpRow("sensors.type-set", sortedTypeList(expected), sortedTypeList(observed), st);
        if (!missing.isEmpty()) {
            raw("  missing (expected but not observed): " + sortedTypeList(missing));
        }
        if (!extra.isEmpty()) {
            raw("  unexpected (observed but not expected): " + sortedTypeList(extra));
        }
    }

    /**
     * Sensor identity spoof: for each expected sensor entry, the observed
     * Sensor of the same type must report the profile's vendor and name —
     * not the host's real hardware strings. UNKNOWN when there is no
     * expected entry or no observed sensor of that type.
     */
    private void cmpSensorIdentity() {
        JSONArray arr = expectedProfile == null ? null : expectedProfile.optJSONArray("sensors");
        Map<Integer, Sensor> observed = new HashMap<>();
        try {
            SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if (sm != null) {
                for (Sensor s : sm.getSensorList(Sensor.TYPE_ALL)) {
                    if (!observed.containsKey(s.getType())) observed.put(s.getType(), s);
                }
            }
        } catch (Exception ignored) {
        }
        if (arr == null) {
            cmpRow("sensor.vendor", null, null, Cmp.UNKNOWN);
            cmpRow("sensor.name", null, null, Cmp.UNKNOWN);
            return;
        }
        for (int i = 0; i < arr.length(); i++) {
            JSONObject s = arr.optJSONObject(i);
            if (s == null || !s.has("type") || s.isNull("type")) continue;
            int type = s.optInt("type");
            Sensor o = observed.get(type);
            String label = "sensor[" + type + "]";
            cmpString(label + ".vendor", opt(s, "vendor"),
                    o == null ? null : o.getVendor());
            cmpString(label + ".name", opt(s, "name"),
                    o == null ? null : o.getName());
        }
    }

    private void cmpWifi() {
        JSONObject net = expectedProfile == null ? null : expectedProfile.optJSONObject("network");
        String eSsid = opt(net, "ssid");
        String eBssid = opt(net, "bssid");
        String oSsid = null;
        String oBssid = null;
        String err = null;
        try {
            WifiManager wm = (WifiManager) getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            if (wm == null) {
                err = "NO_WIFI_SERVICE";
            } else {
                WifiInfo wi = wm.getConnectionInfo();
                if (wi == null) {
                    err = "NO_WIFI_INFO";
                } else {
                    oSsid = wi.getSSID();
                    oBssid = wi.getBSSID();
                    if (oSsid == null || WifiManager.UNKNOWN_SSID.equals(oSsid)) {
                        oSsid = null;
                        err = "SSID_UNKNOWN (needs location permission?)";
                    }
                    if ("02:00:00:00:00:00".equals(oBssid)) {
                        oBssid = null;
                        if (err == null) err = "BSSID_MASKED_BY_OS";
                    }
                }
            }
        } catch (SecurityException se) {
            err = "PERMISSION_DENIED";
        } catch (Exception e) {
            err = "UNAVAILABLE(" + e.getClass().getSimpleName() + ")";
        }
        if (eSsid == null) {
            cmpRow("wifi.ssid", null, oSsid == null ? err : oSsid, Cmp.UNKNOWN);
        } else if (oSsid == null) {
            cmpRow("wifi.ssid", eSsid, err, Cmp.UNKNOWN);
        } else {
            cmpRow("wifi.ssid", eSsid, oSsid, eSsid.equals(oSsid) ? Cmp.PASS : Cmp.FAIL);
        }
        if (eBssid == null) {
            cmpRow("wifi.bssid", null, oBssid == null ? err : oBssid, Cmp.UNKNOWN);
        } else if (oBssid == null) {
            cmpRow("wifi.bssid", eBssid, err, Cmp.UNKNOWN);
        } else {
            cmpRow("wifi.bssid", eBssid, oBssid,
                    eBssid.equalsIgnoreCase(oBssid) ? Cmp.PASS : Cmp.FAIL);
        }
    }

    private void cmpTelephony() {
        JSONObject tel = expectedProfile == null ? null : expectedProfile.optJSONObject("telephony");
        String eName = opt(tel, "operatorName");
        String eNum = opt(tel, "operatorNumeric");
        String eIso = opt(tel, "countryIso");
        int eNt = tel == null ? -1 : tel.optInt("networkType", -1);
        String oName = null;
        String oNum = null;
        String oIso = null;
        int oNt = -1;
        String oSim = null;
        String err = null;
        try {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                err = "PERMISSION_DENIED";
            } else {
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (tm == null) {
                    err = "NO_TELEPHONY_SERVICE";
                } else {
                    oName = emptyToNull(tm.getNetworkOperatorName());
                    oNum = emptyToNull(tm.getNetworkOperator());
                    oIso = emptyToNull(tm.getNetworkCountryIso());
                    oNt = tm.getDataNetworkType();
                    try {
                        oSim = emptyToNull(tm.getSimSerialNumber());
                    } catch (Exception ignored) {
                        // ICCID unreadable on this build; row below -> UNKNOWN.
                    }
                    if (oName == null && oNum == null && oIso == null) {
                        err = "NO_NETWORK_INFO (empty results)";
                    }
                }
            }
        } catch (SecurityException se) {
            err = "PERMISSION_DENIED";
        } catch (Exception e) {
            err = "UNAVAILABLE(" + e.getClass().getSimpleName() + ")";
        }
        cmpTeleField("telephony.operatorName", eName, oName, err, true);
        cmpTeleField("telephony.operatorNumeric", eNum, oNum, err, false);
        cmpTeleField("telephony.countryIso", eIso, oIso, err, true);
        cmpTeleField("telephony.simSerial", opt(tel, "simSerial"), oSim,
                oSim == null ? "UNREADABLE" : err, false);
        if (err != null) {
            cmpRow("telephony.networkType", eNt < 0 ? null : networkTypeName(eNt), err,
                    Cmp.UNKNOWN);
        } else if (eNt < 0) {
            cmpRow("telephony.networkType", null, networkTypeName(oNt), Cmp.UNKNOWN);
        } else {
            cmpRow("telephony.networkType", networkTypeName(eNt), networkTypeName(oNt),
                    eNt == oNt ? Cmp.PASS : Cmp.FAIL);
        }
    }

    /**
     * Track E: timezone/locale must be coherent with the spoofed GPS city
     * and country — the Pine hooks rewrite TimeZone.getDefault() and
     * Locale.getDefault() for the guest process.
     */
    private void cmpLocale() {
        JSONObject lo = expectedProfile == null ? null
                : expectedProfile.optJSONObject("locale");
        String eTz = opt(lo, "timezoneId");
        String eLocale = opt(lo, "localeTag");
        String oTz;
        try {
            oTz = TimeZone.getDefault().getID();
        } catch (Exception e) {
            oTz = null;
        }
        String oLocale;
        try {
            oLocale = Locale.getDefault().toLanguageTag();
        } catch (Exception e) {
            oLocale = null;
        }
        cmpString("timezone.default", eTz, oTz);
        cmpString("locale.default", eLocale, oLocale);
    }

    /**
     * Track E: link-layer identifiers. wlan0's hardware address and the
     * Bluetooth adapter address must be the per-identity MACs, and the
     * Bluetooth name must be the spoofed model.
     */
    private void cmpLinkIds() {
        JSONObject net = expectedProfile == null ? null
                : expectedProfile.optJSONObject("network");
        JSONObject dev = expectedProfile == null ? null
                : expectedProfile.optJSONObject("device");
        String eWifiMac = opt(net, "wifiMac");
        String eBtMac = opt(net, "bluetoothMac");
        String eBtName = opt(dev, "model");

        String oWifiMac = null;
        String wifiErr = null;
        try {
            NetworkInterface wlan0 = NetworkInterface.getByName("wlan0");
            if (wlan0 == null) {
                wifiErr = "NO_WLAN0";
            } else {
                byte[] mac = wlan0.getHardwareAddress();
                if (mac == null) {
                    wifiErr = "MAC_NULL";
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        if (i > 0) sb.append(':');
                        sb.append(String.format("%02X", mac[i]));
                    }
                    oWifiMac = sb.toString();
                }
            }
        } catch (Exception e) {
            wifiErr = "UNAVAILABLE(" + e.getClass().getSimpleName() + ")";
        }
        if (eWifiMac == null) {
            cmpRow("net.wlan0.mac", null, oWifiMac == null ? wifiErr : oWifiMac,
                    Cmp.UNKNOWN);
        } else if (oWifiMac == null) {
            cmpRow("net.wlan0.mac", eWifiMac, wifiErr, Cmp.UNKNOWN);
        } else {
            cmpRow("net.wlan0.mac", eWifiMac, oWifiMac,
                    eWifiMac.equalsIgnoreCase(oWifiMac) ? Cmp.PASS : Cmp.FAIL);
        }

        String oBtName = null;
        String oBtMac = null;
        String btErr = null;
        try {
            BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
            if (bt == null) {
                btErr = "NO_BT_ADAPTER";
            } else {
                try {
                    oBtName = bt.getName();
                } catch (SecurityException se) {
                    btErr = "PERMISSION_DENIED";
                }
                try {
                    oBtMac = bt.getAddress();
                } catch (SecurityException se) {
                    if (btErr == null) btErr = "PERMISSION_DENIED";
                }
            }
        } catch (Exception e) {
            btErr = "UNAVAILABLE(" + e.getClass().getSimpleName() + ")";
        }
        cmpString("bluetooth.name", eBtName, oBtName == null ? null : oBtName);
        if (eBtMac == null) {
            cmpRow("bluetooth.address", null, oBtMac == null ? btErr : oBtMac,
                    Cmp.UNKNOWN);
        } else if (oBtMac == null) {
            cmpRow("bluetooth.address", eBtMac, btErr, Cmp.UNKNOWN);
        } else {
            cmpRow("bluetooth.address", eBtMac, oBtMac,
                    eBtMac.equalsIgnoreCase(oBtMac) ? Cmp.PASS : Cmp.FAIL);
        }
        if (btErr != null && oBtName == null && oBtMac == null) {
            raw("  bluetooth note: " + btErr + " -> UNKNOWN (not a spoof failure)");
        }
    }

    /**
     * Privacy assertions: with an active spoof profile the guest must see
     * NO real cell towers, NO serving-cell location, and NO real nearby
     * Wi-Fi networks. These rows assert emptiness, not profile equality.
     */
    private void cmpCellPrivacy() {
        Integer cellCount = null;
        String cellErr = null;
        try {
            TelephonyManager tm =
                    (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                cellErr = "NO_TELEPHONY_SERVICE";
            } else {
                try {
                    List<?> cells = tm.getAllCellInfo();
                    cellCount = cells == null ? -1 : cells.size();
                } catch (SecurityException se) {
                    cellErr = "PERMISSION_DENIED";
                }
            }
        } catch (Exception e) {
            cellErr = "UNAVAILABLE(" + e.getClass().getSimpleName() + ")";
        }
        if (cellErr != null) {
            cmpRow("cell.allCellInfo.count", "0 (no real towers)", cellErr, Cmp.UNKNOWN);
        } else {
            cmpRow("cell.allCellInfo.count", "0 (no real towers)",
                    String.valueOf(cellCount),
                    Integer.valueOf(0).equals(cellCount) ? Cmp.PASS : Cmp.FAIL);
        }

        String cellLoc = "n/a";
        String locErr = null;
        try {
            TelephonyManager tm =
                    (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                try {
                    Object cl = tm.getCellLocation();
                    cellLoc = cl == null ? "null" : cl.getClass().getSimpleName();
                } catch (SecurityException se) {
                    locErr = "PERMISSION_DENIED";
                }
            }
        } catch (Exception e) {
            locErr = "UNAVAILABLE(" + e.getClass().getSimpleName() + ")";
        }
        if (locErr != null) {
            cmpRow("cell.getCellLocation", "null (no leak)", locErr, Cmp.UNKNOWN);
        } else {
            cmpRow("cell.getCellLocation", "null (no leak)", cellLoc,
                    "null".equals(cellLoc) ? Cmp.PASS : Cmp.FAIL);
        }

        Integer scanCount = null;
        String scanErr = null;
        try {
            WifiManager wm = (WifiManager) getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            if (wm == null) {
                scanErr = "NO_WIFI_SERVICE";
            } else {
                try {
                    List<?> scans = wm.getScanResults();
                    scanCount = scans == null ? -1 : scans.size();
                } catch (SecurityException se) {
                    scanErr = "PERMISSION_DENIED";
                }
            }
        } catch (Exception e) {
            scanErr = "UNAVAILABLE(" + e.getClass().getSimpleName() + ")";
        }
        if (scanErr != null) {
            cmpRow("wifi.scanResults.count", "0 (no real BSSIDs)", scanErr, Cmp.UNKNOWN);
        } else {
            cmpRow("wifi.scanResults.count", "0 (no real BSSIDs)",
                    String.valueOf(scanCount),
                    Integer.valueOf(0).equals(scanCount) ? Cmp.PASS : Cmp.FAIL);
        }
    }

    /**
     * Track E: the WebView default UA must carry the spoofed model/build ID,
     * never the host's.
     */
    private void cmpWebViewUa() {
        JSONObject dev = expectedProfile == null ? null
                : expectedProfile.optJSONObject("device");
        String eUa = opt(dev, "webViewUa");
        String oUa;
        try {
            oUa = WebSettings.getDefaultUserAgent(this);
        } catch (Exception e) {
            oUa = null;
        }
        if (eUa == null) {
            cmpRow("webview.defaultUa", null, oUa == null ? null : "(present)",
                    Cmp.UNKNOWN);
            return;
        }
        if (oUa == null) {
            cmpRow("webview.defaultUa", "(expected ua)", null, Cmp.UNKNOWN);
            return;
        }
        boolean exact = eUa.equals(oUa);
        cmpRow("webview.defaultUa", "(model+buildId spoofed)",
                exact ? "(exact match)" : oUa, exact ? Cmp.PASS : Cmp.FAIL);
        if (!exact) {
            raw("  expected UA: " + eUa);
            raw("  observed UA: " + oUa);
        }
    }

    private void cmpTeleField(String field, String expected, String observed, String err,
                              boolean ignoreCase) {        if (expected == null) {
            cmpRow(field, null, observed == null ? err : observed, Cmp.UNKNOWN);
            return;
        }
        if (observed == null) {
            cmpRow(field, expected, err, Cmp.UNKNOWN);
            return;
        }
        boolean ok = ignoreCase ? expected.equalsIgnoreCase(observed) : expected.equals(observed);
        cmpRow(field, expected, observed, ok ? Cmp.PASS : Cmp.FAIL);
    }

    private void cmpPackages() {
        int count = -1;
        List<String> names = new ArrayList<>();
        try {
            List<PackageInfo> pkgs = getPackageManager().getInstalledPackages(0);
            count = pkgs.size();
            for (PackageInfo pi : pkgs) names.add(pi.packageName);
            Collections.sort(names);
        } catch (Exception ignored) {
        }
        // No expected value by design: OBSERVED only, for manual guest-isolation review.
        cmpRow("packages.count", null, count < 0 ? null : String.valueOf(count), Cmp.INFO);
        int n = Math.min(20, names.size());
        raw("  first " + n + " installed packages (sorted, OBSERVED only -"
                + " verify guest isolation manually):");
        for (int i = 0; i < n; i++) raw("    - " + names.get(i));
    }

    private void cmpTransport() {
        JSONObject net = expectedProfile == null ? null : expectedProfile.optJSONObject("network");
        String eT = opt(net, "transport");
        List<String> obs = new ArrayList<>();
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            Network n = cm == null ? null : cm.getActiveNetwork();
            NetworkCapabilities caps = (n == null || cm == null) ? null : cm.getNetworkCapabilities(n);
            if (caps != null) {
                if (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) obs.add("WIFI");
                if (caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) obs.add("CELLULAR");
                if (caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) obs.add("ETHERNET");
                if (caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) obs.add("VPN");
            }
        } catch (Exception ignored) {
        }
        // Guest keeps real internet by design: informational only.
        cmpRow("network.transport", eT, obs.isEmpty() ? null : String.join(",", obs), Cmp.INFO);
    }

    private void cmpMovement() {
        JSONObject loc = expectedProfile == null ? null : expectedProfile.optJSONObject("location");
        JSONObject mv = loc == null ? null : loc.optJSONObject("movement");
        if (mv == null) {
            cmpRow("location.movement", null, "no movement block in expected profile",
                    expectedProfile == null ? Cmp.UNKNOWN : Cmp.INFO);
            return;
        }
        boolean enabled = mv.optBoolean("enabled", false);
        if (!enabled) {
            cmpRow("location.movement", "disabled", "disabled (profile)", Cmp.INFO);
            return;
        }
        double speed = mv.optDouble("speedMps", Double.NaN);
        double bearing = mv.optDouble("bearingDeg", Double.NaN);
        Location a = gpsLastKnown();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            cmpRow("location.movement", "enabled", "sampling interrupted", Cmp.UNKNOWN);
            return;
        }
        Location b = gpsLastKnown();
        if (a == null || b == null) {
            cmpRow("location.movement", "enabled", "GPS fix unavailable for sampling", Cmp.UNKNOWN);
            return;
        }
        float dist = a.distanceTo(b);
        raw("  movement sample: fix1=" + fmtLoc(a) + " fix2=" + fmtLoc(b)
                + " moved=" + String.format(Locale.US, "%.1f", dist) + "m over ~10s"
                + " (expected speed=" + speed + "m/s bearing=" + bearing + "deg)");
        // Informational: whether the fix moved, not a spoof assertion.
        cmpRow("location.movement", "enabled(speed=" + speed + "m/s)",
                "moved " + String.format(Locale.US, "%.1f", dist) + "m in ~10s", Cmp.INFO);
    }
}
