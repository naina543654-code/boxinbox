package com.sandboxpoc.hostruntime.runtime

import android.os.Looper
import top.niunaijun.blackbox.BlackBoxCore
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
 * Deliberately NOT claimed: device-profile spoofing (Build fields, Android
 * ID, sensors). The engine does not provide those; the probe APK measures
 * what the guest actually observes. Capability states stay UNVERIFIED/
 * UNSUPPORTED until on-device evidence says otherwise.
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
