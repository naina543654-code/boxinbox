package com.sandboxpoc.hostruntime.runtime

import android.os.Looper
import top.niunaijun.blackbox.BlackBoxCore
import top.niunaijun.blackbox.core.env.BEnvironment
import top.niunaijun.blackbox.core.system.user.BUserManagerService
import java.io.File

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
    }

    @Volatile private var state: RuntimeStatus = RuntimeStatus.NOT_CREATED
    @Volatile private var lastError: String? = null

    /** Last engine error, for diagnostics. Null when the last op succeeded. */
    fun lastError(): String? = lastError

    /** Package names installed in the virtual user (UI helper; not part of the 10-method contract). */
    fun installedGuestPackages(): List<String> {
        checkBackground()
        return core().getInstalledPackages(0, VIRTUAL_USER_ID).map { it.packageName }.sorted()
    }

    /** Whether the engine's GMS packages live in the virtual user (UI helper). */
    fun isGmsInstalled(): Boolean {
        checkBackground()
        return core().isInstallGms(VIRTUAL_USER_ID)
    }

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
        for (pkg in core().getInstalledPackages(0, VIRTUAL_USER_ID)) {
            try { core().stopPackage(pkg.packageName, VIRTUAL_USER_ID) } catch (_: Exception) { }
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

    override fun uninstallApplication(packageName: String) = attempt("uninstall") {
        core().uninstallPackageAsUser(packageName, VIRTUAL_USER_ID)
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
        // Clean guest state = drop the whole virtual user and recreate it.
        runCatching { users().deleteUser(VIRTUAL_USER_ID) }
        users().createUser(VIRTUAL_USER_ID)
        state = RuntimeStatus.ACTIVE
    }

    override fun destroy() = attempt("destroy") {
        runCatching { users().deleteUser(VIRTUAL_USER_ID) }
        state = RuntimeStatus.DESTROYED
    }

    override fun getStatus(): RuntimeStatus = state
}
