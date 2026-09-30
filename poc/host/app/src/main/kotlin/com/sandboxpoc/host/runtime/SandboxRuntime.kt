package com.sandboxpoc.host.runtime

/**
 * Engine-agnostic sandbox runtime. Every virtualization capability of the
 * host app goes through this interface — no engine imports may appear in any
 * other package. The real engine adapter (see INTEGRATION.md) implements
 * this interface later; until then [StubRuntime] stands in.
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

    /** Current runtime status. Honest — never faked. */
    fun getStatus(): RuntimeStatus
}
