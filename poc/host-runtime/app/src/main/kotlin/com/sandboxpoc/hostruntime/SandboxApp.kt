package com.sandboxpoc.hostruntime

import android.app.Application
import android.content.Context
import android.util.Log
import com.sandboxpoc.hostruntime.capability.CapabilityManager
import com.sandboxpoc.hostruntime.identity.IdentityManager
import com.sandboxpoc.hostruntime.runtime.BlackBoxRuntime
import com.sandboxpoc.hostruntime.runtime.SandboxRuntime
import com.sandboxpoc.hostruntime.storage.StorageManager
import com.sandboxpoc.hostruntime.util.EventLog
import top.niunaijun.blackbox.BlackBoxCore
import top.niunaijun.blackbox.app.configuration.ClientConfiguration

/**
 * Application subclass. Wires the engine-agnostic modules together and
 * initializes the BlackBox engine.
 *
 * Engine init (required by BlackBox):
 * - attachBaseContext -> BlackBoxCore.doAttachBaseContext(base, config)
 *   (passes `base`, not `this` — the v2 prototype ANR'd on this)
 * - onCreate          -> BlackBoxCore.doCreate()
 *
 * The engine itself is still used ONLY through [SandboxRuntime]
 * ([BlackBoxRuntime]); no other package imports engine classes.
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

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        try {
            BlackBoxCore.get().doAttachBaseContext(base, object : ClientConfiguration() {
                override fun getHostPackageName(): String = base.packageName
            })
        } catch (e: Exception) {
            // Engine init failure must never kill the host app silently;
            // it is recorded and surfaced via runtime status.
            Log.e("SandboxApp", "BlackBox attach failed", e)
        }
    }

    override fun onCreate() {
        super.onCreate()
        try {
            BlackBoxCore.get().doCreate()
        } catch (e: Exception) {
            Log.e("SandboxApp", "BlackBox create failed", e)
        }
        storage = StorageManager(this)
        log = EventLog(storage)
        capabilities = CapabilityManager()
        runtime = BlackBoxRuntime()
        identities = IdentityManager(storage, runtime, log, filesDir)
        val engineOk = try {
            BlackBoxCore.get().getUsers()
            true
        } catch (_: Exception) { false }
        log.i("app", "Sandbox PoC (runtime engine) started; engine alive=$engineOk, runtime=${runtime.getStatus()}")
    }
}
