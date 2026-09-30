package com.sandboxpoc.probe;

import android.Manifest;
import android.app.Activity;
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
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Process;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * SandboxProbe: measures raw observed device/environment values.
 * Runs both standalone and inside the BlackBox-style sandbox host.
 * NEVER asserts "virtualized: yes/no" - it only reports observations.
 */
public class MainActivity extends Activity {

    private static final int REQ_PERMS = 1001;
    private static final String[] WANTED_PERMS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };

    // Best-effort list of well-known app-virtualization / dual-space host
    // package names. Package names verified via public APK metadata pages.
    // PRESENT/NOT PRESENT is reported raw; never used to conclude anything.
    private static final String[] KNOWN_VIRT_PACKAGES = new String[]{
            "com.lbe.parallel.intl",   // Parallel Space (LBE Tech)
            "com.ludashi.dualspace"    // DualSpace
    };

    private TextView reportView;
    private final StringBuilder report = new StringBuilder();
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
            runOnUiThread(() -> reportView.setText(report.toString()));
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
            runOnUiThread(() -> reportView.setText(report.toString()));
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
}
