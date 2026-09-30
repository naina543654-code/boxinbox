package com.sandboxpoc.host.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.sandboxpoc.host.SandboxApp

class ManageIdentityActivity : Activity() {

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
        root.addView(Ui.title(this, "Manage Identity"))

        val identity = app.identities.activeIdentity()
        if (identity == null) {
            root.addView(Ui.row(this, "No active identity. Generate one from Home."))
            setContentView(Ui.page(root))
            return
        }
        val p = identity.profile

        root.addView(Ui.section(this, "Identity"))
        root.addView(Ui.mono(this, "id:        ${identity.id}"))
        root.addView(Ui.mono(this, "state:     ${identity.state}"))
        root.addView(Ui.mono(this, "runtime:   ${app.identities.runtimeStatus()}"))

        root.addView(Ui.section(this, "Device profile"))
        root.addView(Ui.mono(this, "manufacturer: ${p.manufacturer}"))
        root.addView(Ui.mono(this, "brand:        ${p.brand}"))
        root.addView(Ui.mono(this, "model:        ${p.model}"))
        root.addView(Ui.mono(this, "device:       ${p.device}"))
        root.addView(Ui.mono(this, "product:      ${p.product}"))
        root.addView(Ui.mono(this, "android:      ${p.androidVersion} (API ${p.apiLevel})"))
        root.addView(Ui.mono(this, "build:        ${p.buildId} ${p.buildType} ${p.buildTags}"))
        root.addView(Ui.mono(this, "fingerprint:"))
        root.addView(Ui.mono(this, "  ${p.fingerprint}"))
        root.addView(Ui.mono(this, "androidId:    ${p.androidId}"))

        root.addView(Ui.section(this, "Capabilities"))
        app.capabilities.all().forEach { cap ->
            root.addView(Ui.row(this, "• ${cap.displayName}: ${cap.state}"))
        }

        root.addView(Ui.section(this, "Guest applications"))
        root.addView(Ui.button(this, "Clone Application…") {
            startActivity(Intent(this, AppPickerActivity::class.java))
        })
        val staged = app.storage.stagedApks(identity.id)
        val guestDirs = app.storage.guestApps(identity.id)
        if (staged.isEmpty() && guestDirs.isEmpty()) {
            root.addView(Ui.row(this, "No guest apps staged."))
        } else {
            staged.forEach { f -> root.addView(Ui.row(this, "staged APK: ${f.name}")) }
            guestDirs.forEach { d -> root.addView(Ui.row(this, "guest state: $d")) }
            if (app.identities.runtimeStatus().name == "ENGINE_NOT_INTEGRATED") {
                root.addView(Ui.row(this, "Nothing is actually installed/launched — " +
                    "engine not integrated. Staged APKs are app-layer records only."))
            }
        }

        root.addView(Ui.divider(this))
        root.addView(Ui.button(this, "Reset Identity") { confirmReset() })
        root.addView(Ui.button(this, "Delete Identity") { confirmDelete() })

        setContentView(Ui.page(root))
    }

    private fun confirmReset() {
        AlertDialog.Builder(this)
            .setTitle("Reset identity?")
            .setMessage("Destroys the runtime, deletes the profile and ALL guest state, " +
                "then generates a brand-new profile (new ANDROID_ID). This cannot be undone.")
            .setPositiveButton("Reset") { _, _ -> doReset() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun doReset() {
        try {
            val old = app.identities.activeIdentity()!!
            val fresh = app.identities.resetIdentity()
            val changed = old.profile.androidId != fresh.profile.androidId
            Toast.makeText(this,
                "Reset done. androidId changed: $changed " +
                    "(${old.profile.androidId} → ${fresh.profile.androidId})",
                Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            AlertDialog.Builder(this).setTitle("Reset failed")
                .setMessage(e.message ?: e.toString()).setPositiveButton("OK", null).show()
        }
        render()
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Delete identity?")
            .setMessage("Destroys the runtime and removes the profile and ALL associated " +
                "state. No active identity will remain.")
            .setPositiveButton("Delete") { _, _ -> doDelete() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun doDelete() {
        try {
            app.identities.deleteIdentity()
            Toast.makeText(this, "Identity deleted", Toast.LENGTH_LONG).show()
            finish()
        } catch (e: Exception) {
            AlertDialog.Builder(this).setTitle("Delete failed")
                .setMessage(e.message ?: e.toString()).setPositiveButton("OK", null).show()
        }
    }
}
