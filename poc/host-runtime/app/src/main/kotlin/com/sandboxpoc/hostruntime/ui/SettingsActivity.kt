package com.sandboxpoc.hostruntime.ui

import android.app.Activity
import android.os.Bundle
import com.sandboxpoc.hostruntime.SandboxApp
import com.sandboxpoc.hostruntime.capability.CapabilityManager
import com.sandboxpoc.hostruntime.providers.ProviderBindings
import com.sandboxpoc.hostruntime.runtime.BlackBoxRuntime

class SettingsActivity : Activity() {

    private lateinit var app: SandboxApp
    /** Cached GMS state; null while a background load is in flight. */
    private var gmsInstalled: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as SandboxApp
    }

    override fun onResume() {
        super.onResume()
        gmsInstalled = null
        render()
        loadGms()
    }

    /** Loads the GMS install state off the main thread, then re-renders. */
    private fun loadGms() {
        val bb = app.runtime as? BlackBoxRuntime ?: return
        if (app.identities.activeIdentity() == null) return
        Thread {
            val gms = runCatching { bb.isGmsInstalled() }.getOrNull()
            runOnUiThread {
                gmsInstalled = gms
                render()
            }
        }.start()
    }

    private fun render() {
        val root = Ui.screen(this)
        root.addView(Ui.title(this, "Settings"))

        val sections = listOf(
            "Device Identity" to CapabilityManager.DEVICE_IDENTITY,
            "Location" to CapabilityManager.LOCATION_SPOOF,
            "Sensors" to CapabilityManager.SENSORS_SPOOF,
            "Network" to CapabilityManager.NETWORK_ISOLATION,
            "Package Visibility" to CapabilityManager.PACKAGE_VISIBILITY,
            "Google Services" to CapabilityManager.GOOGLE_SERVICES,
        )
        for ((label, capId) in sections) {
            val cap = app.capabilities.get(capId)
            root.addView(Ui.section(this, label))
            root.addView(Ui.row(this, "Capability: ${cap?.displayName ?: capId}"))
            root.addView(Ui.row(this, "State: ${cap?.state ?: "UNKNOWN"}"))
            if (!cap?.detail.isNullOrEmpty()) root.addView(Ui.row(this, cap!!.detail))
            if (capId == CapabilityManager.GOOGLE_SERVICES) {
                root.addView(Ui.row(this, "GMS mode: ${ProviderBindings.defaultGmsMode} (PoC default)"))
            }
            if (cap?.state?.name == "UNVERIFIED") {
                root.addView(Ui.row(this, "Unverified — the engine adapter has not confirmed " +
                    "this feature. Nothing is silently falling back to host values."))
            }
        }

        root.addView(Ui.section(this, "Advanced"))
        root.addView(Ui.row(this, "Runtime: ${app.identities.runtimeStatus()}"))
        root.addView(Ui.row(this, "Sandbox root:"))
        root.addView(Ui.mono(this, app.storage.sandboxRoot().absolutePath))
        root.addView(Ui.row(this, "Host private data is never copied into the sandbox."))

        root.addView(Ui.section(this, "Google Play Services"))
        val bb = app.runtime as? BlackBoxRuntime
        when {
            bb == null -> root.addView(Ui.row(this, "Not available with this runtime."))
            app.identities.activeIdentity() == null ->
                root.addView(Ui.row(this, "No active identity."))
            gmsInstalled == null -> root.addView(Ui.row(this, "Checking…"))
            gmsInstalled == true ->
                root.addView(Ui.row(this, "Installed in this identity."))
            else -> {
                root.addView(Ui.row(this, "Not installed — guests that require Play " +
                    "Services (e.g. Wakie) will refuse to connect."))
                root.addView(Ui.button(this, "Install Google Play Services") {
                    Ui.bg(this, work = {
                        bb.installGoogleServices()
                    }, onDone = {
                        gmsInstalled = null
                        loadGms()
                    })
                })
            }
        }

        root.addView(Ui.section(this, "Event log (identity/runtime transitions)"))
        val lines = app.log.recent()
        if (lines.isEmpty()) {
            root.addView(Ui.row(this, "(empty)"))
        } else {
            lines.forEach { root.addView(Ui.mono(this, it)) }
        }

        setContentView(Ui.page(root))
    }
}
