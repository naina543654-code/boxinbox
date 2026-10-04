package com.sandboxpoc.hostruntime.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.sandboxpoc.hostruntime.R
import com.sandboxpoc.hostruntime.SandboxApp
import com.sandboxpoc.hostruntime.runtime.BlackBoxRuntime

class HomeActivity : Activity() {

    private lateinit var app: SandboxApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as SandboxApp
    }

    override fun onResume() {
        super.onResume()
        render()
    }

    private fun render() {
        val root = Ui.screen(this)
        root.addView(Ui.title(this, "BoxInBox"))
        root.addView(Ui.subtitle(this, "Privacy sandbox · PoC"))

        val identity = app.identities.activeIdentity()
        val status = app.identities.runtimeStatus()

        if (identity == null) {
            val card = Ui.card(this)
            card.addView(Ui.cardTitle(this, "No identity yet"))
            card.addView(Ui.subtitle(this,
                "Generate an identity to create a fresh virtual device."))
            card.addView(Ui.button(this, "Generate Identity") { onGenerate() })
            root.addView(card)
        } else {
            val p = identity.profile
            val sp = identity.spoofProfile
            val card = Ui.card(this)
            card.addView(Ui.cardHeader(this, "Active Identity",
                Ui.statePill(this, identity.state.name,
                    identity.state.name == "ACTIVE")))
            card.addView(Ui.kvRow(this, "Device",
                "${p.manufacturer} ${p.model}"))
            card.addView(Ui.kvRow(this, "Android",
                "${p.androidVersion} (API ${p.apiLevel})"))
            card.addView(Ui.kvRow(this, "Operator",
                "${sp.telephony.operatorName} · ${sp.telephony.countryIso.uppercase()}"))
            val city = sp.locale.timezoneId.substringAfterLast('/', "")
            if (city.isNotEmpty()) {
                card.addView(Ui.kvRow(this, "Location", city))
            }
            card.addView(Ui.kvRow(this, "ANDROID_ID",
                "${sp.androidId.take(8)}…"))
            card.addView(Ui.kvRow(this, "Runtime", status.toString()))
            root.addView(card)

            if (status.name == "ERROR") {
                val err = (app.runtime as? BlackBoxRuntime)?.lastError()
                root.addView(Ui.subtitle(this, "Note: the engine reported an error — see log." +
                    (if (err != null) " Last: $err" else "")))
            }
        }

        root.addView(Ui.section(this, "Actions"))
        if (identity != null) {
            // Generate is hidden while an identity exists — reset or delete
            // it in Manage Identity first.
            root.addView(Ui.subtitle(this,
                "Generate is unavailable while an identity exists — " +
                    "reset or delete it in Manage Identity first."))
        }
        root.addView(Ui.button(this, "Manage Identity") {
            startActivity(Intent(this, ManageIdentityActivity::class.java))
        })
        root.addView(Ui.secondaryButton(this, "Settings") {
            startActivity(Intent(this, SettingsActivity::class.java))
        })

        root.addView(Ui.footnote(this, "Build ${getString(R.string.build_version)}"))

        setContentView(Ui.page(root))
    }

    private fun onGenerate() {
        Ui.bg(this,
            work = {
                val identity = app.identities.generateIdentity()
                "OK: identity ${identity.id.take(8)}… generated (runtime=${app.identities.runtimeStatus()})"
            },
            onDone = { render() })
    }
}
