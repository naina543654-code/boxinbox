package com.sandboxpoc.host

import android.app.Application
import com.sandboxpoc.host.capability.CapabilityManager
import com.sandboxpoc.host.identity.IdentityManager
import com.sandboxpoc.host.runtime.SandboxRuntime
import com.sandboxpoc.host.runtime.StubRuntime
import com.sandboxpoc.host.storage.StorageManager
import com.sandboxpoc.host.util.EventLog

/**
 * Application subclass. Wires the engine-agnostic modules together.
 *
 * INTEGRATION SEAM: [runtime] is the single swap point. To plug in the real
 * engine, replace `StubRuntime()` with the engine adapter (a SandboxRuntime
 * implementation living in the `runtime` package or a new `engine` package —
 * see INTEGRATION.md). Nothing else changes.
 */
class SandboxApp : Application() {

    lateinit var storage: StorageManager
        private set
    lateinit var log: EventLog
        private set
    lateinit var capabilities: CapabilityManager
        private set
    lateinit var runtime: SandboxRuntime
        private set
    lateinit var identities: IdentityManager
        private set

    override fun onCreate() {
        super.onCreate()
        storage = StorageManager(this)
        log = EventLog(storage)
        capabilities = CapabilityManager()
        // >>> ENGINE INTEGRATION SEAM: swap StubRuntime() for the adapter. <<<
        runtime = StubRuntime()
        identities = IdentityManager(storage, runtime, log)
        log.i("app", "Sandbox PoC started (runtime=${runtime.getStatus()})")
    }
}
