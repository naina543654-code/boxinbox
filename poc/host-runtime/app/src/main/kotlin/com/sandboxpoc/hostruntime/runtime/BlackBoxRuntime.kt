package com.sandboxpoc.hostruntime.runtime

import android.os.Looper
import android.util.Log
import top.niunaijun.blackbox.BlackBoxCore
import top.niunaijun.blackbox.core.GmsCore
import top.niunaijun.blackbox.core.env.BEnvironment
import top.niunaijun.blackbox.core.system.user.BUserManagerService
import top.niunaijun.blackbox.fake.frameworks.BPackageManager
import top.niunaijun.blackbox.fake.frameworks.BUserManager
import java.io.File
import java.nio.file.Files
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * [SandboxRuntime] backed by the BlackBox application-virtualization engine
 * (zitanioi/blackbox, built from source as Bcore-release.aar).
 *
 * This is the ONLY place engine imports may appear — everything else talks
 * to [SandboxRuntime].
 *
 * Mapping (one active identity -> one BlackBox virtual user):
 * - create()              -> create virtual user [VIRTUAL_USER_ID]
 * - start()               -> ensure the virtual user exists (engine itself
 *                            starts with the host process via SandboxApp)
 * - stop()                -> stop every guest package (identity/state kept)
 * - installApplication()  -> BlackBox installPackageAsUser; fails loudly if
 *                            the engine reports !success
 * - uninstallApplication()-> BlackBox uninstallPackageAsUser
 * - launchApplication()   -> BlackBox launchApk; fails loudly if it returns false
 * - stopApplication()     -> BlackBox stopPackage
 * - reset()               -> delete + recreate the virtual user (guest state
 *                            wiped, virtual user id kept)
 * - destroy()             -> delete the virtual user
 * - getStatus()           -> honest local state machine
 *
 * Deliberately NOT claimed without on-device evidence: that the spoof
 * hooks (Build fields, Android ID, telephony, Wi-Fi identity, location,
 * sensors — see fake/spoof/ in the engine) actually take effect on a
 * given Android version. The probe APK measures what the guest actually
 * observes; capability states stay UNVERIFIED until logcat evidence
 * says otherwise.
 *
 * Threading: every method performs Binder IPC into the engine's server
 * process and MUST NOT run on the main thread — enforced fail-fast
 * (the v2 prototype ANR'd precisely because of main-thread engine calls).
 */
class BlackBoxRuntime : SandboxRuntime {

    companion object {
        /** Virtual user backing the single active identity (V1). */
        const val VIRTUAL_USER_ID = 0
        /** Logcat tag for wipe phase markers (delete/reset diagnostics). */
        private const val TAG_WIPE = "BoxInBoxWipe"
        /** Worker pool for timeout-guarded engine IPC (see [ipc]). */
        private val IpcExec = Executors.newCachedThreadPool()
    }

    @Volatile private var state: RuntimeStatus = RuntimeStatus.NOT_CREATED
    @Volatile private var lastError: String? = null

    /**
     * Thrown by [ipc] when the daemon does not answer within the timeout.
     * Distinct from other failures so callers can fail fast on a wedged
     * daemon instead of burning one timeout per package.
     */
    class IpcTimeoutException(message: String) : IllegalStateException(message)

    /**
     * Run a Binder IPC against the engine daemon with a hard timeout.
     *
     * Why: on 2026-10-01 Delete/Reset hung forever inside
     * core().getInstalledPackages() — the :black daemon stopped answering
     * Binder transactions (it ANR'd on "executing service DaemonService")
     * and the wipe thread blocked with no toast, no crash, no trace past
     * "phase=collect". A wedged daemon must FAIL LOUDLY (naming the stuck
     * call) instead of hanging the wipe with zero feedback.
     *
     * Note: cancelling the future interrupts the worker, but an in-flight
     * Binder transaction is not interruptible — a truly wedged daemon
     * leaks one stuck worker thread per timed-out call. That is the price
     * of not hanging the wipe thread; the user force-stops/relaunches
     * anyway when the daemon is in that state.
     */
    private fun <T> ipc(name: String, timeoutSec: Long, block: () -> T): T {
        val fut = IpcExec.submit<T> { block() }
        try {
            return fut.get(timeoutSec, TimeUnit.SECONDS)
        } catch (e: TimeoutException) {
            fut.cancel(true)
            throw IpcTimeoutException(
                "wipe aborted: engine call '$name' timed out after ${timeoutSec}s " +
                    "— daemon not responding (last trace lines in filesDir/wipe-trace.log)")
        } catch (e: java.util.concurrent.ExecutionException) {
            val cause = e.cause
            throw IllegalStateException(
                "wipe aborted: engine call '$name' failed: ${cause?.message}", cause)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            throw IllegalStateException("wipe aborted: engine call '$name' interrupted")
        }
    }

    /** Last engine error, for diagnostics. Null when the last op succeeded. */
    fun lastError(): String? = lastError

    /** Package names installed in the virtual user (UI helper; not part of the 10-method contract). */
    fun installedGuestPackages(): List<String> {
        checkBackground()
        // Timeout-guarded: a wedged daemon must surface as an exception
        // (the Manage Identity caller falls back to an empty list) rather
        // than hanging the listing thread forever.
        return ipc("getInstalledPackages", 20) {
            core().getInstalledPackages(0, VIRTUAL_USER_ID).map { it.packageName }.sorted()
        }
    }

    /** Whether the engine's GMS packages live in the virtual user (UI helper). */
    fun isGmsInstalled(): Boolean {
        checkBackground()
        return core().isInstallGms(VIRTUAL_USER_ID)
    }

    /**
     * Whether this package is one of the engine's Google Play Services
     * clones rather than a user-cloned app. Pure static check — no IPC.
     * UI helper so Manage Identity shows only the apps the user cloned.
     */
    fun isGmsPackage(packageName: String): Boolean =
        GmsCore.isGoogleAppOrService(packageName)

    /**
     * Clones the host's real Google Play Services packages
     * (com.google.android.gms, com.google.android.gsf, …) into the virtual
     * user via the engine's GmsCore. Guests that gate on
     * GoogleApiAvailability (e.g. Wakie) refuse to connect without this.
     * Heavy (hundreds of MB), so it is explicit — never automatic.
     * Returns a human-readable result for the UI.
     */
    fun installGoogleServices(): String {
        checkBackground()
        return try {
            val result = core().installGms(VIRTUAL_USER_ID)
            lastError = null
            if (result.success) "OK: Google Play Services installed in this identity"
            else "FAIL: GMS install refused: ${result.msg ?: "no details"}"
        } catch (e: Exception) {
            lastError = "installGms: ${e.message}"
            "FAIL: ${e.message}"
        }
    }

    /**
     * Stage the active spoof profile as `expected_profile.json` inside the
     * guest's virtual files dir, before the guest is launched.
     *
     * Why a file and not an Intent extra: BlackBoxCore.launchApk(packageName,
     * userId) accepts no Intent, so extras cannot be delivered at launch.
     * The engine redirects the guest's getFilesDir() to exactly this
     * directory (IOCore maps /data/user/<n>/<pkg> to the virtual data dir),
     * so the guest probe can read its expected values with
     * `new File(getFilesDir(), "expected_profile.json")` — no knowledge of
     * the host's package name needed. Same UID, direct java.io write.
     */
    fun stageExpectedProfile(packageName: String, profileJson: String) {
        checkBackground()
        val dir = BEnvironment.getDataFilesDir(packageName, VIRTUAL_USER_ID)
        check(dir.mkdirs() || dir.isDirectory) { "cannot create guest files dir: $dir" }
        File(dir, "expected_profile.json").writeText(profileJson)
    }

    private fun checkBackground() {
        check(Looper.myLooper() != Looper.getMainLooper()) {
            "BlackBoxRuntime must not be called on the main thread (Binder IPC blocks)"
        }
    }

    private fun <T> attempt(op: String, block: () -> T): T {
        checkBackground()
        try {
            val r = block()
            lastError = null
            return r
        } catch (e: Exception) {
            state = RuntimeStatus.ERROR
            lastError = "$op: ${e.message}"
            throw e
        }
    }

    private fun users() = BUserManagerService.get()
    private fun core() = BlackBoxCore.get()

    override fun create() = attempt("create") {
        users().createUser(VIRTUAL_USER_ID)
        state = RuntimeStatus.ACTIVE
    }

    override fun start() = attempt("start") {
        // The engine lives with the host process (see SandboxApp); starting
        // the runtime means the virtual user exists and is usable.
        users().createUser(VIRTUAL_USER_ID)
        state = RuntimeStatus.ACTIVE
    }

    override fun stop() = attempt("stop") {
        for (pkg in ipc("getInstalledPackages", 20) { core().getInstalledPackages(0, VIRTUAL_USER_ID) }) {
            try { ipc("stopPackage", 10) { core().stopPackage(pkg.packageName, VIRTUAL_USER_ID) } } catch (_: Exception) { }
        }
        state = RuntimeStatus.STOPPED
    }

    override fun installApplication(apkPath: String) = attempt("install") {
        val apk = File(apkPath)
        require(apk.isFile) { "APK not found: $apkPath" }
        val result = core().installPackageAsUser(apk, VIRTUAL_USER_ID)
        check(result.success) { "engine install failed: ${result.msg}" }
    }

    override fun installApplicationWithSplits(apkPaths: List<String>) = attempt("install") {
        require(apkPaths.isNotEmpty()) { "no APKs to install" }
        val base = File(apkPaths[0])
        require(base.isFile) { "APK not found: ${apkPaths[0]}" }
        val result = core().installPackageAsUser(base, VIRTUAL_USER_ID)
        check(result.success) { "engine install failed: ${result.msg}" }
        // Split APKs (config/density splits of bundle-distributed apps) are
        // copied next to base.apk preserving their original split_<name>.apk
        // filenames, so the engine can derive the real split names for
        // ApplicationInfo.splitNames. The framework's LoadedApk then loads
        // split resources with AssetManager. Stale splits from a previous
        // version are removed first.
        val codeDir = BEnvironment.getBaseApkDir(result.packageName).parentFile
            ?: throw IllegalStateException("no code dir for ${result.packageName}")
        codeDir.listFiles { f -> f.isFile && f.name.startsWith("split_") && f.name.endsWith(".apk") }
            ?.forEach { it.delete() }
        apkPaths.drop(1).forEach { sp ->
            val src = File(sp)
            require(src.isFile) { "split APK not found: $sp" }
            src.copyTo(File(codeDir, src.name), overwrite = true)
        }
    }

    override fun uninstallApplication(packageName: String) {
        attempt("uninstall") {
            // Before/after logging: if the "calling" line appears in logcat
            // without the "returned" line, the engine call is stuck (not failed).
            Log.i(TAG_WIPE, "uninstall: calling uninstallPackageAsUser($packageName, $VIRTUAL_USER_ID)")
            core().uninstallPackageAsUser(packageName, VIRTUAL_USER_ID)
            Log.i(TAG_WIPE, "uninstall: uninstallPackageAsUser($packageName) returned")
        }
    }

    override fun launchApplication(packageName: String) = attempt("launch") {
        val ok = core().launchApk(packageName, VIRTUAL_USER_ID)
        check(ok) { "engine refused to launch $packageName (no launch intent)" }
        state = RuntimeStatus.ACTIVE
    }

    override fun stopApplication(packageName: String) = attempt("stopApp") {
        core().stopPackage(packageName, VIRTUAL_USER_ID)
    }

    override fun reset() = attempt("reset") {
        // Clean guest state = fully wipe the virtual user, then recreate it.
        wipeVirtualUser()
        users().createUser(VIRTUAL_USER_ID)
        state = RuntimeStatus.ACTIVE
    }

    override fun destroy() = attempt("destroy") {
        wipeVirtualUser()
        state = RuntimeStatus.DESTROYED
    }

    /**
     * Audit fix 2026-10-04: detects a never-wiped virtual user (e.g. the
     * identity record was corrupted/deleted out-of-band). Disk-based, no IPC —
     * safe to call any time.
     */
    override fun hasLeftoverGuestState(): Boolean {
        return runCatching {
            val appRoot = BEnvironment.getAppRootDir()
            val pkgs = appRoot.listFiles { f -> f.isDirectory }
            if (!pkgs.isNullOrEmpty()) return true
            val userDir = BEnvironment.getUserDir(VIRTUAL_USER_ID)
            if (userDir.isDirectory && userDir.listFiles()?.isNotEmpty() == true) return true
            false
        }.getOrDefault(false)
    }

    /**
     * Fully remove the virtual user and everything in it.
     *
     * Bug history: this used to be `runCatching { deleteUser(..) }` — any
     * failure was silently swallowed while the app reported "identity
     * deleted", and the next identity reused the never-deleted virtual
     * user, keeping old cloned apps and their data. Two further traps:
     * the engine's own deleteUser iterates packages in one unguarded loop
     * (a single bad package aborts the whole wipe), and
     * getInstalledPackages hides GMS clones, so "uninstall everything
     * listed" misses them.
     *
     * This wipe therefore trusts nothing: it first heals the engine's
     * user record through the Binder proxy (a previous failed wipe can
     * remove user 0 from the daemon's user table, which turns every later
     * uninstallPackageAsUser into a silent no-op); then every package gets
     * an isolated per-package uninstall while user 0 still exists; GMS goes
     * through the engine's own uninstaller; the engine's deleteUser is
     * SKIPPED — BUserManagerService.deleteUser takes mUserLock+mUsers while
     * calling into BPackageManagerService.deleteUser (lock-order inversion,
     * deadlock risk), its internal package loop removes entries from the
     * ArrayMap it iterates (silently skipping packages), and it is
     * redundant anyway (steps below remove every package and every data
     * dir; the bare user record carries no package state); every on-disk
     * location is force-cleared with an iterative deleter (no recursion —
     * deep GMS trees); and the result is VERIFIED against a live daemon
     * plus on-disk truth. Anything surviving is reported by name instead
     * of silently kept. Each phase logs to logcat (tag BoxInBoxWipe) and
     * to filesDir/wipe-trace.log so a slow or stuck wipe shows exactly
     * where it is.
     */
    /**
     * Destroys ALL guest state for the virtual user, verified step by step.
     *
     * Process-model lesson (2026-10-01, the hard one): this code runs in the
     * HOST process, where the engine's service singletons
     * (BPackageManagerService.get(), BUserManagerService.get()) are
     * UNINITIALIZED PHANTOMS. SystemCallProvider — the component that runs
     * BlackBoxSystem.startup() -> systemReady() and scans real state off
     * disk — is declared android:process=":black", so it only ever runs in
     * the daemon. The host-process copies never scan: their package/user
     * maps are EMPTY. An earlier wipe revision "verified" against those
     * empty maps, so every check trivially passed, and
     * BUserManagerService.get().createUser() even WROTE the user config
     * file from the host via AtomicFile, racing the daemon's own copy.
     * => This wipe NEVER touches B*Service.get() in this process.
     *
     * Hard lessons encoded here:
     * - The engine's client-side proxies (BPackageManager, BUserManager)
     *   SILENTLY SWALLOW RemoteException (uninstall = no-op,
     *   getInstalledPackages = empty list, isInstalled = false), and the
     *   service's uninstallPackageAsUser SILENTLY RETURNS when the package
     *   is missing / not installed for the user / XPOSED. So every proxy
     *   call runs inside ipc() with a HARD TIMEOUT — on 2026-10-01 the
     *   daemon wedged (ANR "executing service DaemonService") and
     *   getInstalledPackages blocked the wipe thread forever with no toast
     *   and no trace past "phase=collect". A timed-out call now aborts
     *   loudly naming the stuck call.
     * - Swallowed failures make "empty list" ambiguous (dead daemon vs
     *   genuinely clean), so final verification first PINGS the daemon's
     *   Binder: only a daemon that answers is trusted when its package
     *   list comes back empty. An unresponsive daemon fails the wipe as
     *   INCONCLUSIVE instead of false-passing as clean.
     * - On-disk state (BEnvironment paths) is host-local, needs no IPC,
     *   and cannot be faked by a dead proxy: package collection starts
     *   from a disk scan, and verification requires every collected app
     *   dir to be gone from disk.
     * - A file trace (filesDir/wipe-trace.log) records every phase with a
     *   start AND end line per source, so a hung wipe thread still leaves
     *   evidence of exactly which call it died in.
     */
    private fun wipeVirtualUser() {
        val traceFile = File(BlackBoxCore.getContext().filesDir, "wipe-trace.log")
        fun trace(msg: String) {
            val line = "${System.currentTimeMillis()} $msg"
            Log.i(TAG_WIPE, msg)
            runCatching {
                traceFile.appendText(line + "\n")
            }
        }

        // NOTE: B*Service.get() singletons are NEVER touched in this process —
        // they are uninitialized phantoms here (see KDoc above). All engine
        // contact goes through the Binder proxies with hard timeouts.
        val userMgr = BUserManager.get()

        // 0. Heal the DAEMON's user record FIRST, then VERIFY it exists. A
        //    previous failed wipe may have removed user 0 from the daemon's
        //    user table, which turns every later uninstallPackageAsUser
        //    into a SILENT NO-OP (service returns early for unknown user).
        //    Proxy createUser returns null when the IPC itself failed; the
        //    getUsers check below is the real gate either way.
        trace("phase=ensure-user: proxy createUser($VIRTUAL_USER_ID)")
        val created = ipc("createUser", 15) { userMgr.createUser(VIRTUAL_USER_ID) }
        trace("phase=ensure-user: createUser returned ${if (created != null) "user record" else "null (IPC failed or user absent)"}")
        val daemonUsers = ipc("getUsers", 15) { userMgr.getUsers().map { it.id } }
        check(daemonUsers.contains(VIRTUAL_USER_ID)) {
            "wipe aborted: virtual user $VIRTUAL_USER_ID missing from daemon after " +
                "createUser (daemon users=$daemonUsers) — every uninstall would silently no-op"
        }
        trace("phase=ensure-user: user record verified present in daemon")

        // 1. Collect package names. Disk FIRST (host-local, no IPC, cannot
        //    hang), then the daemon's list as a supplement (a registered-
        //    but-dir-less package still deserves an uninstall attempt).
        //    Each source gets start/end trace lines so a stall is
        //    attributable to the exact call.
        trace("phase=collect: disk scan start")
        val pkgs = mutableSetOf<String>()
        runCatching {
            BEnvironment.getAppRootDir()
                .listFiles { f -> f.isDirectory }
                ?.forEach { pkgs += it.name }
        }.onFailure { trace("phase=collect: disk scan threw: ${it.message}") }
        trace("phase=collect: disk scan end, pkgs=${pkgs.size}")
        trace("phase=collect: daemon list start")
        runCatching {
            ipc("getInstalledPackages", 20) {
                core().getInstalledPackages(0, VIRTUAL_USER_ID).map { it.packageName }
            }
        }.onSuccess { pkgs += it }
            .onFailure {
                trace("phase=collect: daemon list FAILED (${it.message}); " +
                    "continuing with disk set only")
            }
        trace("phase=collect: end, pkgs=${pkgs.size} [${pkgs.sorted().joinToString(",")}]")

        // 2. Stop running guests so open files don't block deletion.
        //    Best effort per package, but a TIMEOUT means the daemon is
        //    wedged: fail fast instead of burning one timeout per package
        //    (the sweep's retry re-kills before its second pass anyway).
        trace("phase=stop pkgs=${pkgs.size}")
        for (pkg in pkgs) {
            try {
                ipc("stopPackage", 10) { core().stopPackage(pkg, VIRTUAL_USER_ID) }
            } catch (e: IpcTimeoutException) {
                throw e
            } catch (e: Exception) {
                trace("stop $pkg FAILED: ${e.message}")
            }
        }
        trace("phase=stop: end")

        // 2b. Re-audit 2026-10-04 (HIGH): clear the daemon's in-memory
        //     account map for the virtual user BEFORE the uninstall sweep.
        //     Deleting accounts.conf from disk (phase 6) alone leaves the
        //     in-memory BUserAccounts visible in-session, so a re-cloned GMS
        //     could rediscover the previous identity's accounts.
        trace("phase=clear-accounts: start")
        runCatching {
            ipc("clearAccountsForUser", 20) {
                top.niunaijun.blackbox.fake.frameworks.BAccountManager.get()
                    .clearAccountsForUser(VIRTUAL_USER_ID)
            }
        }.onSuccess { trace("phase=clear-accounts: end (daemon account map cleared)") }
            .onFailure {
                trace("phase=clear-accounts: FAILED (${it.message}); " +
                    "continuing — disk wipe still removes accounts.conf")
            }

        // 3. Isolated per-package uninstall while user 0 exists. A failed
        //    IPC aborts loudly (the daemon is not cooperating; further
        //    calls would just burn timeouts). Silent no-ops (service
        //    returns early for missing/XPOSED packages) are caught by the
        //    end-of-wipe verification, which names survivors.
        trace("phase=uninstall-all count=${pkgs.size}")
        for (pkg in pkgs) {
            trace("uninstalling $pkg")
            ipc("uninstallPackageAsUser", 25) {
                core().uninstallPackageAsUser(pkg, VIRTUAL_USER_ID)
            }
            trace("uninstall $pkg: daemon call returned")
        }
        trace("phase=uninstall-all: end")

        // 4. GMS via the engine's own uninstaller, verified through the
        //    proxy (a timed-out/failed check is INCONCLUSIVE -> abort, not
        //    a pass: the proxy swallows RemoteException as "not installed").
        trace("phase=uninstall-gms: start")
        ipc("uninstallGms", 60) { core().uninstallGms(VIRTUAL_USER_ID) }
        trace("phase=uninstall-gms: uninstaller returned")
        val gmsGone = ipc("isInstalledGoogleService", 15) {
            !GmsCore.isInstalledGoogleService(VIRTUAL_USER_ID)
        }
        check(gmsGone) { "wipe aborted: GMS still installed after uninstallGms" }
        trace("phase=uninstall-gms: verified gone")

        // 5. NOTE: we deliberately do NOT call the engine's deleteUser here.
        //    BUserManagerService.deleteUser holds mUserLock+mUsers while
        //    calling into BPackageManagerService.deleteUser (lock-order
        //    inversion -> potential deadlock), and its internal package loop
        //    removes entries from the ArrayMap it iterates (silently skipping
        //    packages). It is also redundant: steps 3/4 removed every
        //    package, step 6 clears every data dir. The user record itself
        //    (id + status) carries no package state, so there is nothing
        //    left for deleteUser to clean.
        trace("phase=engine-delete-user: SKIPPED (redundant and unsafe; see note)")

        // 6. Scorched earth: force-remove every on-disk location that can
        //    hold user-0 state. Survivors get one retry after a re-kill;
        //    anything still standing fails loudly.
        val dirs = mutableListOf(
            BEnvironment.getUserDir(VIRTUAL_USER_ID),
            File(BEnvironment.getVirtualRoot(), "data/user_de/$VIRTUAL_USER_ID"),
            BEnvironment.getExternalUserDir(VIRTUAL_USER_ID),
            BEnvironment.getHotfixDir(VIRTUAL_USER_ID),
            // Audit fix 2026-10-04: virtual AccountManager accounts
            // (blackbox/system/accounts.conf) survived delete/reset — a
            // re-cloned GMS re-discovered the previous identity's Google
            // account without sign-in. The largest hole against the
            // fresh-env-per-identity rule.
            BEnvironment.getAccountsConf(),
        )
        pkgs.forEach { dirs += BEnvironment.getAppDir(it) }
        val targets = dirs.distinct().filter { it.exists() }
        trace("phase=sweep-dirs count=${targets.size}")
        val dirSurvivors = mutableListOf<String>()
        for (d in targets) {
            if (!deleteTree(d)) dirSurvivors += d.absolutePath
        }
        if (dirSurvivors.isNotEmpty()) {
            trace("phase=sweep-retry survivors=${dirSurvivors.size}")
            for (pkg in pkgs) {
                runCatching { ipc("stopPackage", 10) { core().stopPackage(pkg, VIRTUAL_USER_ID) } }
            }
            val retry = dirSurvivors.toList()
            dirSurvivors.clear()
            for (path in retry) {
                if (!deleteTree(File(path))) dirSurvivors += path
            }
        }
        check(dirSurvivors.isEmpty()) {
            "wipe aborted: dirs survived deletion: ${dirSurvivors.joinToString(",")}"
        }
        trace("phase=sweep-dirs: all clear")

        // 7. Final verification. Trust chain, in order:
        //    (a) the daemon must ANSWER a Binder ping — the proxies swallow
        //        RemoteException as empty/false, so an unresponsive daemon
        //        would otherwise false-pass every check below as "clean".
        //        Inconclusive here aborts instead of passing.
        //    (b) the daemon's package list for user 0 must be empty;
        //    (c) GMS must be gone;
        //    (d) every wiped dir must be gone from disk (host-local truth,
        //        no IPC involved to fake it).
        //    The user record is NOT re-created: deleteUser was skipped, so
        //    user 0 (verified in phase 0) still exists in the daemon.
        trace("phase=verify: daemon ping start")
        val daemonAlive = ipc("daemon-ping", 10) {
            val svc = BPackageManager.get().service
            svc != null && svc.asBinder().pingBinder()
        }
        check(daemonAlive) {
            "wipe aborted: engine daemon not responding to ping — " +
                "verification inconclusive, refusing to report clean"
        }
        trace("phase=verify: daemon alive; package list start")
        val stillRegistered = ipc("verify-list", 20) {
            core().getInstalledPackages(0, VIRTUAL_USER_ID).map { it.packageName }.sorted()
        }
        trace("phase=verify: list end, stillRegistered=${stillRegistered.size}")
        val gmsLeft = ipc("verify-gms", 15) { GmsCore.isInstalledGoogleService(VIRTUAL_USER_ID) }
        val dirsLeft = dirs.filter { it.exists() }.map { it.absolutePath }
        trace("phase=verify: gmsLeft=$gmsLeft dirsLeft=${dirsLeft.size}")
        check(stillRegistered.isEmpty() && !gmsLeft && dirsLeft.isEmpty()) {
            buildString {
                append("identity wipe incomplete")
                if (stillRegistered.isNotEmpty()) {
                    append("; still registered: ${stillRegistered.joinToString(",")}")
                }
                if (gmsLeft) append("; GMS still registered")
                if (dirsLeft.isNotEmpty()) {
                    append("; dirs survived: ${dirsLeft.joinToString(",")}")
                }
            }.toString()
        }
        trace("wipe complete")
    }

    /**
     * Iterative post-order directory delete. [File.deleteRecursively] is
     * recursive and can die with a StackOverflowError on deep trees (GMS
     * data); this never recurses, never follows symlinks, and reports
     * failure instead of throwing.
     */
    private fun deleteTree(root: File): Boolean {
        return try {
            val stack = ArrayDeque<File>()
            val order = ArrayDeque<File>()
            stack.add(root)
            while (stack.isNotEmpty()) {
                val f = stack.removeLast()
                order.add(f)
                if (f.isDirectory && !Files.isSymbolicLink(f.toPath())) {
                    f.listFiles()?.forEach { stack.add(it) }
                }
            }
            var ok = true
            while (order.isNotEmpty()) {
                val f = order.removeLast()
                if (f.exists() && !f.delete()) ok = false
            }
            ok && !root.exists()
        } catch (t: Throwable) {
            false
        }
    }

    override fun getStatus(): RuntimeStatus = state
}
