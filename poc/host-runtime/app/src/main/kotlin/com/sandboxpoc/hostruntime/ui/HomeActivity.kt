package com.sandboxpoc.hostruntime.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
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
        root.addView(Ui.title(this, "Sandbox PoC"))
        val identity = app.identities.activeIdentity()
        val status = app.identities.runtimeStatus()

        root.addView(Ui.section(this, "Status"))
        root.addView(Ui.row(this, "Runtime: $status"))
        if (identity == null) {
            root.addView(Ui.row(this, "Identity: none — generate one to begin"))
        } else {
            val p = identity.profile
            root.addView(Ui.row(this, "Identity: ${identity.id.take(8)}… (${identity.state})"))
            root.addView(Ui.row(this, "Device: ${p.manufacturer} ${p.model}"))
            root.addView(Ui.row(this, "Android ${p.androidVersion} (API ${p.apiLevel})"))
            root.addView(Ui.row(this, "ANDROID_ID: ${identity.spoofProfile.androidId}"))
        }
        if (status.name == "ERROR") {
            val err = (app.runtime as? BlackBoxRuntime)?.lastError()
            root.addView(Ui.row(this, "Note: the engine reported an error — see log." +
                (if (err != null) " Last: $err" else "")))
        }

        root.addView(Ui.section(this, "Actions"))
        val genBtn = Ui.button(this, "Generate Identity") { onGenerate() }
        genBtn.isEnabled = identity == null
        root.addView(genBtn)
        if (identity != null) {
            root.addView(Ui.row(this, "Generate is disabled while an identity exists — " +
                "reset or delete it in Manage Identity first."))
        }
        root.addView(Ui.button(this, "Manage Identity") {
            startActivity(Intent(this, ManageIdentityActivity::class.java))
        })
        root.addView(Ui.button(this, "Settings") {
            startActivity(Intent(this, SettingsActivity::class.java))
        })

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
