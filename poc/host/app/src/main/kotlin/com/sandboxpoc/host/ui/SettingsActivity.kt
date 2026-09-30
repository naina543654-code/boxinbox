package com.sandboxpoc.host.ui

import android.app.Activity
import android.os.Bundle
import com.sandboxpoc.host.SandboxApp
import com.sandboxpoc.host.capability.CapabilityManager
import com.sandboxpoc.host.providers.ProviderBindings

class SettingsActivity : Activity() {

    private lateinit var app: SandboxApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as SandboxApp
        render()
    }

    override fun onResume() {
        super.onResume()
        render()
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
