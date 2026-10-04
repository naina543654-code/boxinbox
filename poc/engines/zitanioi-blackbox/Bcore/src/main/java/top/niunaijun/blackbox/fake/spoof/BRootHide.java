package top.niunaijun.blackbox.fake.spoof;

import android.os.Debug;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;
import top.niunaijun.blackbox.utils.Slog;

/**
 * Root and debugger hiding for guest apps.
 *
 * <p>Ported from the analyst's Frida dynamic-analysis script
 * ({@code frida_bypass.js}): Wakie-class apps refuse to establish their
 * backend connection when they detect root/debugger artifacts on the
 * device. The sandbox guest sees the host's real filesystem, so without
 * this the guest observes {@code /system/xbin/su}, working {@code su}
 * execs, and debugger state — and loops forever on "restoring the
 * connection" without ever attempting a real socket.
 *
 * <p>Choke points (Pine hooks, process-wide in the guest):
 * <ul>
 *   <li>{@code java.io.File.exists()} — dangerous paths report
 *       {@code false} (su binaries, busybox, Magisk artifacts).</li>
 *   <li>{@code java.lang.Runtime.exec(...)} (all 6 overloads) — dangerous
 *       commands get a dummy {@link Process} with empty output and exit
 *       code 1 ("not found / failed"), the same observable behavior as a
 *       non-rooted device.</li>
 *   <li>{@code android.os.Debug.isDebuggerConnected()} and
 *       {@code waitingForDebugger()} — always {@code false}.</li>
 *   <li>Root-indicating system properties ({@code ro.debuggable},
 *       {@code ro.secure}, {@code ro.build.tags}, {@code ro.build.type})
 *       — handled in {@link BSpoofSystemProps}, the SystemProperties
 *       choke point.</li>
 * </ul>
 *
 * <p>Not covered (deliberately): package-manager queries for root apps
 * already fail — the virtual PackageManager only knows sandbox-installed
 * packages, so {@code getPackageInfo("eu.chainfire.supersu")} throws
 * {@code NameNotFoundException} naturally. SSL-pinning bypass is the
 * analyst's traffic-inspection tooling and is intentionally NOT part of
 * the sandbox: pinning does not break normal TLS, so it cannot be the
 * cause of a connection failure.
 *
 * <p>Fail-open: install failures never throw; every hook additionally
 * gates on {@code BSpoofManager.isSpoofActive()}, so an inactive profile
 * passes everything through untouched.
 */
public final class BRootHide {
    private static final String TAG = "BRootHide";
    private static volatile boolean sInstalled = false;

    /** Exact dangerous paths (from the analyst's bypass script). */
    private static final Set<String> BLOCKED_PATHS = new HashSet<>(Arrays.asList(
            "/system/app/Superuser.apk",
            "/system/xbin/su",
            "/system/bin/su",
            "/system/bin/failsafe/su",
            "/sbin/su",
            "/su/bin/su",
            "/su/bin",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/.ext/su",
            "/system/xbin/mu",
            "/system/xbin/busybox",
            "/system/bin/busybox",
            "/magisk",
            "/sbin/.magisk",
            "/system/app/Magisk.apk"
    ));

    private BRootHide() {
    }

    /** Installs the Pine hooks. Safe to call repeatedly; failures never throw. */
    public static void install() {
        if (sInstalled) {
            return;
        }
        synchronized (BRootHide.class) {
            if (sInstalled) {
                return;
            }
            try {
                hookFileExists();
                hookRuntimeExec();
                hookProcessBuilder();
                hookDebuggerState();
                sInstalled = true;
                Slog.d(TAG, "root/debugger hiding hooks installed");
            } catch (Throwable t) {
                // Fail open: root artifacts remain visible.
                Slog.e(TAG, "root-hide hook install failed; artifacts visible", t);
            }
        }
    }

    // ------------------------------------------------------------------
    // java.io.File.exists()
    // ------------------------------------------------------------------

    private static void hookFileExists() {
        final Method target;
        try {
            target = File.class.getDeclaredMethod("exists");
        } catch (Throwable t) {
            Slog.e(TAG, "File.exists not found; skipping", t);
            return;
        }
        try {
            Pine.hook(target, new MethodHook() {
                @Override
                public void beforeCall(Pine.CallFrame callFrame) {
                    if (!BSpoofManager.get().isSpoofActive()) {
                        return;
                    }
                    Object recv = callFrame.thisObject;
                    if (!(recv instanceof File)) {
                        return;
                    }
                    // getAbsolutePath() is not hooked; no recursion risk.
                    if (isDangerousPath(((File) recv).getAbsolutePath())) {
                        callFrame.setResult(false);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on File.exists failed", t);
        }
    }

    /** True if the path is a known root artifact or named like one. */
    private static boolean isDangerousPath(String path) {
        if (path == null) {
            return false;
        }
        if (BLOCKED_PATHS.contains(path)) {
            return true;
        }
        String name = path;
        int slash = path.lastIndexOf('/');
        if (slash >= 0) {
            name = path.substring(slash + 1);
        }
        return name.equals("su")
                || name.equals("busybox")
                || name.contains("magisk")
                || name.contains("supersu");
    }

    // ------------------------------------------------------------------
    // java.lang.Runtime.exec(...) — all overloads
    // ------------------------------------------------------------------

    private static void hookRuntimeExec() {
        Class<?>[][] sigs = {
                {String.class},
                {String[].class},
                {String[].class, String[].class},
                {String.class, String[].class},
                {String[].class, String[].class, File.class},
                {String.class, String[].class, File.class},
        };
        for (Class<?>[] sig : sigs) {
            final Method target;
            try {
                target = Runtime.class.getDeclaredMethod("exec", sig);
            } catch (Throwable t) {
                Slog.e(TAG, "Runtime.exec/" + sig.length + " not found; skipping", t);
                continue;
            }
            try {
                Pine.hook(target, new MethodHook() {
                    @Override
                    public void beforeCall(Pine.CallFrame callFrame) {
                        if (!BSpoofManager.get().isSpoofActive()) {
                            return;
                        }
                        Object[] args = callFrame.args;
                        if (args == null || args.length == 0) {
                            return;
                        }
                        boolean dangerous = false;
                        if (args[0] instanceof String) {
                            dangerous = isDangerousExec((String) args[0]);
                        } else if (args[0] instanceof String[]) {
                            for (String part : (String[]) args[0]) {
                                if (isDangerousExecToken(part)) {
                                    dangerous = true;
                                    break;
                                }
                            }
                        }
                        if (dangerous) {
                            // Same observable behavior as a non-rooted
                            // device: empty output, exit code 1.
                            callFrame.setResult(dummyProcess());
                        } else {
                            // getprop bypasses the SystemProperties Java
                            // hooks; answer identity keys at the exec layer.
                            Process spoofed = maybeSpoofGetprop(toArgv(args[0]));
                            if (spoofed != null) {
                                callFrame.setResult(spoofed);
                            }
                        }
                    }
                });
            } catch (Throwable t) {
                Slog.e(TAG, "Pine hook on Runtime.exec failed", t);
            }
        }
    }

    /** Tokenizes a shell command line; true if any token is a root binary. */
    private static boolean isDangerousExec(String cmd) {
        if (cmd == null) {
            return false;
        }
        for (String token : cmd.split("[\\s;|&]+")) {
            if (isDangerousExecToken(token)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isDangerousExecToken(String token) {
        if (token == null) {
            return false;
        }
        String t = token.replace("\"", "").replace("'", "");
        return t.equals("su")
                || t.endsWith("/su")
                || t.equals("busybox")
                || t.endsWith("/busybox")
                || t.contains("magisk")
                || t.contains("supersu");
    }

    /**
     * A do-nothing Process: empty stdout/stderr, exit code 1. Anonymous
     * subclassing is legal (Process is abstract, not final).
     */
    private static Process dummyProcess() {
        return new Process() {
            @Override
            public OutputStream getOutputStream() {
                return new ByteArrayOutputStream();
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(new byte[0]);
            }

            @Override
            public InputStream getErrorStream() {
                return new ByteArrayInputStream(new byte[0]);
            }

            @Override
            public int waitFor() {
                return 1;
            }

            @Override
            public int exitValue() {
                return 1;
            }

            @Override
            public void destroy() {
            }
        };
    }

    /** Tokenizes an exec command argument into argv. */
    private static String[] toArgv(Object cmd) {
        if (cmd instanceof String[]) {
            return (String[]) cmd;
        }
        if (cmd instanceof String) {
            return ((String) cmd).split("[\\s;|&]+");
        }
        return null;
    }

    // ------------------------------------------------------------------
    // java.lang.ProcessBuilder.start() — bypasses Runtime.exec entirely
    // ------------------------------------------------------------------

    /**
     * Hooks {@code ProcessBuilder.start()}: it calls {@code ProcessImpl}
     * directly and never goes through the hooked {@code Runtime.exec}
     * overloads, so without this a guest can run {@code su} or
     * {@code getprop} unfiltered via {@code new ProcessBuilder(...)}.
     */
    private static void hookProcessBuilder() {
        final Method target;
        try {
            target = ProcessBuilder.class.getDeclaredMethod("start");
        } catch (Throwable t) {
            Slog.e(TAG, "ProcessBuilder.start not found; skipping", t);
            return;
        }
        try {
            Pine.hook(target, new MethodHook() {
                @Override
                public void beforeCall(Pine.CallFrame callFrame) {
                    if (!BSpoofManager.get().isSpoofActive()) {
                        return;
                    }
                    Object recv = callFrame.thisObject;
                    if (!(recv instanceof ProcessBuilder)) {
                        return;
                    }
                    java.util.List<String> command;
                    try {
                        command = ((ProcessBuilder) recv).command();
                    } catch (Throwable t) {
                        return;
                    }
                    if (command == null || command.isEmpty()) {
                        return;
                    }
                    String[] argv = command.toArray(new String[0]);
                    for (String token : argv) {
                        if (isDangerousExecToken(token)) {
                            callFrame.setResult(dummyProcess());
                            return;
                        }
                    }
                    Process spoofed = maybeSpoofGetprop(argv);
                    if (spoofed != null) {
                        callFrame.setResult(spoofed);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on ProcessBuilder.start failed", t);
        }
    }

    // ------------------------------------------------------------------
    // getprop interception — answers identity properties at the exec layer
    // ------------------------------------------------------------------

    /**
     * Intercepts {@code getprop} invocations. {@code Runtime.exec("getprop
     * …")} and {@code ProcessBuilder("getprop", …)} bypass the Java
     * {@code SystemProperties} hooks entirely, so without this the guest
     * reads the host's real {@code ro.build.display.id},
     * {@code ro.product.model}, etc. straight from the property service.
     *
     * @param argv tokenized command line
     * @return a {@link Process} serving the spoofed answer, or null to let
     *         the real command run (unknown key / no active profile).
     */
    static Process maybeSpoofGetprop(String[] argv) {
        if (argv == null || argv.length == 0) {
            return null;
        }
        String bin = argv[0];
        if (bin == null || !(bin.equals("getprop") || bin.endsWith("/getprop"))) {
            return null;
        }
        BSpoofManager spoof = BSpoofManager.get();
        if (!spoof.isSpoofActive()) {
            return null;
        }
        try {
            if (argv.length == 1) {
                // Bare `getprop`: dump the spoofed build.prop in getprop
                // output format. The file read itself goes through the
                // /system/build.prop redirect installed by BProcFsSpoof.
                String dump = readSpoofedBuildProp();
                if (dump != null) {
                    return textProcess(formatGetpropDump(dump));
                }
                return null;
            }
            String key = argv[1];
            String value = spoof.getSystemPropertySpoof(key);
            if (value != null) {
                return textProcess(value + "\n");
            }
            // Unknown key: `getprop <key> <default>` prints the default.
            if (argv.length >= 3) {
                return textProcess(argv[2] + "\n");
            }
        } catch (Throwable t) {
            Slog.w(TAG, "getprop spoof failed; passing through", t);
        }
        return null;
    }

    private static String readSpoofedBuildProp() {
        java.io.File prop = new java.io.File("/system/build.prop");
        if (!prop.isFile()) {
            return null;
        }
        try {
            java.io.InputStream in = new java.io.FileInputStream(prop);
            try {
                java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
                byte[] buf = new byte[8192];
                int read;
                while ((read = in.read(buf)) != -1) {
                    out.write(buf, 0, read);
                }
                return new String(out.toByteArray(), "UTF-8");
            } finally {
                try {
                    in.close();
                } catch (Throwable ignored) {
                }
            }
        } catch (Throwable t) {
            return null;
        }
    }

    /** Converts {@code key=value} lines to getprop's {@code [key]: [value]} format. */
    private static String formatGetpropDump(String buildProp) {
        StringBuilder out = new StringBuilder(buildProp.length() + 256);
        for (String line : buildProp.split("\n")) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            int eq = line.indexOf('=');
            if (eq <= 0) {
                continue;
            }
            out.append('[').append(line.substring(0, eq).trim()).append("]: [")
                    .append(line.substring(eq + 1).trim()).append("]\n");
        }
        return out.toString();
    }

    /** A Process serving fixed text on stdout, exit code 0. */
    private static Process textProcess(final String text) {
        final byte[] bytes;
        try {
            bytes = text.getBytes("UTF-8");
        } catch (Throwable t) {
            return dummyProcess();
        }
        return new Process() {
            @Override
            public OutputStream getOutputStream() {
                return new ByteArrayOutputStream();
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(bytes);
            }

            @Override
            public InputStream getErrorStream() {
                return new ByteArrayInputStream(new byte[0]);
            }

            @Override
            public int waitFor() {
                return 0;
            }

            @Override
            public int exitValue() {
                return 0;
            }

            @Override
            public void destroy() {
            }
        };
    }

    // ------------------------------------------------------------------
    // android.os.Debug — debugger state
    // ------------------------------------------------------------------

    private static void hookDebuggerState() {
        hookStaticBoolean(Debug.class, "isDebuggerConnected");
        hookStaticBoolean(Debug.class, "waitingForDebugger");
    }

    private static void hookStaticBoolean(Class<?> clazz, String name) {
        final Method target;
        try {
            target = clazz.getDeclaredMethod(name);
        } catch (Throwable t) {
            Slog.e(TAG, name + " not found; skipping", t);
            return;
        }
        try {
            Pine.hook(target, new MethodHook() {
                @Override
                public void beforeCall(Pine.CallFrame callFrame) {
                    if (BSpoofManager.get().isSpoofActive()) {
                        callFrame.setResult(false);
                    }
                }
            });
        } catch (Throwable t) {
            Slog.e(TAG, "Pine hook on " + name + " failed", t);
        }
    }
}
