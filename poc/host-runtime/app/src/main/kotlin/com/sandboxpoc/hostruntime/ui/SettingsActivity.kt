package com.sandboxpoc.hostruntime.ui

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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

        // -- Capabilities ----------------------------------------------------
        val capCard = Ui.card(this)
        capCard.addView(Ui.cardTitle(this, "Capabilities"))
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
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                val v = Ui.dp(this@SettingsActivity, 4)
                setPadding(0, v, 0, v)
            }
            row.addView(TextView(this).apply {
                text = label
                textSize = 14f
                setTextColor(Ui.TEXT)
                layoutParams = LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            })
            if (cap != null) row.addView(Ui.capabilityPill(this, cap.state))
            capCard.addView(row)
            if (!cap?.detail.isNullOrEmpty()) {
                capCard.addView(Ui.subtitle(this, cap!!.detail))
            }
            if (capId == CapabilityManager.GOOGLE_SERVICES) {
                capCard.addView(Ui.subtitle(this,
                    "GMS mode: ${ProviderBindings.defaultGmsMode} (PoC default)"))
            }
            if (cap?.state?.name == "UNVERIFIED") {
                capCard.addView(Ui.subtitle(this, "Unverified — the engine adapter has not " +
                    "confirmed this feature. Nothing is silently falling back to host values."))
            }
        }
        root.addView(capCard)

        // -- Google Play Services ---------------------------------------------
        val gmsCard = Ui.card(this)
        val gmsOk = gmsInstalled == true
        gmsCard.addView(Ui.cardHeader(this, "Google Play Services",
            Ui.statePill(this,
                when {
                    gmsInstalled == null -> "Checking…"
                    gmsOk -> "Installed"
                    else -> "Not installed"
                },
                gmsOk)))
        val bb = app.runtime as? BlackBoxRuntime
        when {
            bb == null -> gmsCard.addView(Ui.subtitle(this,
                "Not available with this runtime."))
            app.identities.activeIdentity() == null ->
                gmsCard.addView(Ui.subtitle(this, "No active identity."))
            gmsInstalled == null -> gmsCard.addView(Ui.subtitle(this, "Checking…"))
            gmsOk -> gmsCard.addView(Ui.subtitle(this,
                "Installed in this identity."))
            else -> {
                gmsCard.addView(Ui.subtitle(this, "Not installed — guests that require " +
                    "Play Services (e.g. Wakie) will refuse to connect."))
                gmsCard.addView(Ui.button(this, "Install Google Play Services") {
                    Ui.bg(this, work = {
                        bb.installGoogleServices()
                    }, onDone = {
                        gmsInstalled = null
                        loadGms()
                    })
                })
            }
        }
        root.addView(gmsCard)

        // -- Advanced ------------------------------------------------------------
        val advCard = Ui.card(this)
        advCard.addView(Ui.cardTitle(this, "Advanced"))
        advCard.addView(Ui.kvRow(this, "Runtime", app.identities.runtimeStatus().toString()))
        if (bb != null) {
            advCard.addView(Ui.buttonRow(this,
                Ui.secondaryButton(this, "Stop runtime", 1f) {
                    Ui.bg(this, work = { app.runtime.stop(); "OK: runtime stopped" },
                        onDone = { render() })
                },
                Ui.secondaryButton(this, "Start runtime", 1f) {
                    Ui.bg(this, work = { app.runtime.start(); "OK: runtime started" },
                        onDone = { render() })
                }))
        }
        root.addView(advCard)

        root.addView(Ui.collapsible(this, "Sandbox root") { c ->
            c.addView(Ui.mono(this, app.storage.sandboxRoot().absolutePath))
            c.addView(Ui.subtitle(this, "Host private data is never copied into the sandbox."))
        })

        root.addView(Ui.collapsible(this, "Event log") { c ->
            val lines = app.log.recent()
            if (lines.isEmpty()) {
                c.addView(Ui.subtitle(this, "(empty)"))
            } else {
                lines.forEach { c.addView(Ui.mono(this, it)) }
            }
        })

        setContentView(Ui.page(root))
    }
}
