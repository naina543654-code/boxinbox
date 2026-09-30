package com.sandboxpoc.host.runtime

/**
 * No-op [SandboxRuntime] used until a real engine adapter is integrated.
 *
 * - [getStatus] always returns [RuntimeStatus.ENGINE_NOT_INTEGRATED].
 * - Every operation throws [UnsupportedOperationException] with the message
 *   "engine not integrated". Callers must surface this honestly in the UI
 *   instead of faking success.
 */
class StubRuntime : SandboxRuntime {

    private fun unsupported(): Nothing =
        throw UnsupportedOperationException("engine not integrated")

    override fun create(): Unit = unsupported()
    override fun start(): Unit = unsupported()
    override fun stop(): Unit = unsupported()
    override fun installApplication(apkPath: String): Unit = unsupported()
    override fun uninstallApplication(packageName: String): Unit = unsupported()
    override fun launchApplication(packageName: String): Unit = unsupported()
    override fun stopApplication(packageName: String): Unit = unsupported()
    override fun reset(): Unit = unsupported()
    override fun destroy(): Unit = unsupported()

    override fun getStatus(): RuntimeStatus = RuntimeStatus.ENGINE_NOT_INTEGRATED
}
