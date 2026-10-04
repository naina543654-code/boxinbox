package com.sandboxpoc.hostruntime.runtime

/**
 * Engine-agnostic sandbox runtime. Every virtualization capability of the
 * host app goes through this interface — no engine imports may appear in any
 * other package. [BlackBoxRuntime] implements this interface; see
 * INTEGRATION.md.
 */
interface SandboxRuntime {

    /** Create the virtual runtime environment for the active identity. */
    fun create()

    /** Start a created runtime. */
    fun start()

    /** Stop a running runtime (guest apps suspended, identity kept). */
    fun stop()

    /** Install a guest APK (by file path) into the virtual runtime. */
    fun installApplication(apkPath: String)

    /**
     * Install a guest APK plus its split APKs (base first, then splits) into
     * the virtual runtime. Split APKs cover config/density splits of
     * bundle-distributed apps — without them such apps crash with
     * Resources$NotFoundException. Default: base APK only.
     */
    fun installApplicationWithSplits(apkPaths: List<String>) {
        require(apkPaths.isNotEmpty()) { "no APKs to install" }
        installApplication(apkPaths[0])
    }

    /** Uninstall a guest package from the virtual runtime. */
    fun uninstallApplication(packageName: String)

    /** Launch a guest application inside the virtual runtime. */
    fun launchApplication(packageName: String)

    /** Stop a running guest application inside the virtual runtime. */
    fun stopApplication(packageName: String)

    /** Reset guest state inside an existing runtime (apps wiped, identity kept). */
    fun reset()

    /** Tear down the virtual runtime entirely. */
    fun destroy()

    /**
     * True when the virtual user still holds guest state (installed packages
     * or data dirs) — e.g. after a corrupted identity record. Used by
     * [com.sandboxpoc.hostruntime.identity.IdentityManager] to refuse building
     * a new identity over a never-wiped user.
     */
    fun hasLeftoverGuestState(): Boolean

    /** Current runtime status. Honest — never faked. */
    fun getStatus(): RuntimeStatus
}
