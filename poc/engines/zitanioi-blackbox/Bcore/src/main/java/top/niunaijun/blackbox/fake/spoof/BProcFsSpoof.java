package top.niunaijun.blackbox.fake.spoof;

import android.os.Process;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import top.niunaijun.blackbox.core.IOCore;
import top.niunaijun.blackbox.utils.Slog;

/**
 * Hides host-identity and virtualization tells that live in procfs and in
 * {@code /system/build.prop} — all of which bypass every Java-layer hook
 * ({@code BSpoofSystemProps} only sees {@code SystemProperties} Java calls,
 * {@code BSpoofManager} only patches in-process {@code Build} fields).
 *
 * <p>What a guest can read with zero permissions:
 * <ul>
 *   <li>{@code /proc/self/maps} — contains {@code libblackbox.so},
 *       {@code libpine.so} and the host package's library paths. Classic
 *       hook-framework scan ("pine" sits on modern detector lists next to
 *       xposed/frida/substrate).</li>
 *   <li>{@code /system/build.prop} — the host's full fingerprint
 *       ({@code ro.product.model=moto g52}, build tags, …).</li>
 *   <li>{@code /proc/version} — the host kernel ({@code 4.19.304-perf+}),
 *       contradicting the spoofed kernel from {@code BSpoofJvmProps}.</li>
 * </ul>
 *
 * <p>Mechanism: the engine's IO redirect table
 * ({@link IOCore#addRedirect}, honored by both the Java
 * {@code UnixFileSystemHook} and the native libc GOT hook) swaps these
 * paths for per-identity generated files:
 * <ul>
 *   <li>maps → a filtered copy of the process's own mappings with
 *       engine/host tells removed (generated per process at install time,
 *       after all engine libraries are loaded);</li>
 *   <li>build.prop / proc/version → per-profile files the host writes
 *       next to {@code active_profile.json} at identity creation.</li>
 * </ul>
 *
 * <p>Fail-open: any failure leaves the real files visible. The maps copy is
 * a snapshot — libraries loaded later by the guest won't appear in it, but
 * detectors scan for known-bad entries, not for completeness, so a snapshot
 * taken after engine init is sufficient.
 */
public final class BProcFsSpoof {
    private static final String TAG = "BProcFsSpoof";
    private static volatile boolean sInstalled = false;

    private BProcFsSpoof() {
    }

    /**
     * Installs the procfs/build.prop redirects for this guest process.
     *
     * @param filesDir the host app's files dir
     *                 ({@code <dataDir>/files}); per-profile spoof files live
     *                 in {@code <filesDir>/profiles/}.
     */
    public static void install(String filesDir) {
        if (sInstalled) {
            return;
        }
        synchronized (BProcFsSpoof.class) {
            if (sInstalled) {
                return;
            }
            try {
                installMapsFilter(filesDir);
                installStaticRedirects(filesDir);
                sInstalled = true;
                Slog.d(TAG, "procfs/build.prop redirects installed");
            } catch (Throwable t) {
                Slog.e(TAG, "procfs spoof install failed; host files visible", t);
            }
        }
    }

    private static void installMapsFilter(String filesDir) {
        int pid;
        try {
            pid = Process.myPid();
        } catch (Throwable t) {
            Slog.w(TAG, "myPid failed; skipping maps filter");
            return;
        }
        // Read the REAL maps before installing any redirect for it.
        String raw;
        try {
            raw = readFile(new File("/proc/self/maps"));
        } catch (Throwable t) {
            Slog.w(TAG, "cannot read /proc/self/maps; skipping filter", t);
            return;
        }
        StringBuilder filtered = new StringBuilder(raw.length());
        for (String line : raw.split("\n")) {
            String low = line.toLowerCase();
            if (low.contains("blackbox") || low.contains("libpine")
                    || low.contains("sandboxpoc") || low.contains("hostruntime")) {
                continue;
            }
            filtered.append(line).append('\n');
        }
        File out = new File(filesDir, "proc_maps_" + pid + ".txt");
        try {
            writeFile(out, filtered.toString());
        } catch (Throwable t) {
            Slog.w(TAG, "cannot write filtered maps; skipping", t);
            return;
        }
        try {
            IOCore.get().addRedirect("/proc/self/maps", out.getAbsolutePath());
            IOCore.get().addRedirect("/proc/" + pid + "/maps", out.getAbsolutePath());
        } catch (Throwable t) {
            Slog.w(TAG, "maps redirect failed", t);
        }
    }

    private static void installStaticRedirects(String filesDir) {
        File profiles = new File(filesDir, "profiles");
        File buildProp = new File(profiles, "build.prop");
        File procVersion = new File(profiles, "proc_version");
        File wlan0Address = new File(profiles, "wlan0_address");
        try {
            if (buildProp.isFile()) {
                IOCore.get().addRedirect("/system/build.prop", buildProp.getAbsolutePath());
            }
            if (procVersion.isFile()) {
                IOCore.get().addRedirect("/proc/version", procVersion.getAbsolutePath());
            }
            // Audit fix 2026-10-04: /sys/class/net/wlan0/address exposed the
            // real Wi-Fi MAC (only 4 static redirects existed). The host
            // writes the per-identity MAC here at identity creation.
            if (wlan0Address.isFile()) {
                IOCore.get().addRedirect("/sys/class/net/wlan0/address",
                        wlan0Address.getAbsolutePath());
            }
        } catch (Throwable t) {
            Slog.w(TAG, "static procfs redirects failed", t);
        }
    }

    private static String readFile(File file) throws Exception {
        InputStream in = new FileInputStream(file);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int read;
            while ((read = in.read(buf)) != -1) {
                out.write(buf, 0, read);
            }
            return new String(out.toByteArray(), Charset.forName("UTF-8"));
        } finally {
            try {
                in.close();
            } catch (Throwable ignored) {
            }
        }
    }

    private static void writeFile(File file, String content) throws Exception {
        OutputStream out = new FileOutputStream(file);
        try {
            out.write(content.getBytes(Charset.forName("UTF-8")));
        } finally {
            try {
                out.close();
            } catch (Throwable ignored) {
            }
        }
    }
}
