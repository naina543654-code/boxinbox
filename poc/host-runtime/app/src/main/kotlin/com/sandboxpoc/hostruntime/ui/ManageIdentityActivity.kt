package com.sandboxpoc.hostruntime.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.sandboxpoc.hostruntime.SandboxApp
import com.sandboxpoc.hostruntime.runtime.BlackBoxRuntime

class ManageIdentityActivity : Activity() {

    private lateinit var app: SandboxApp
    /** Cached guest package list; null while a background load is in flight. */
    private var guests: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as SandboxApp
    }

    override fun onResume() {
        super.onResume()
        guests = null
        render()
        loadGuests()
    }

    /** Loads the guest package list off the main thread, then re-renders. */
    private fun loadGuests() {
        val bb = app.runtime as? BlackBoxRuntime ?: return
        Thread {
            val list = runCatching { bb.installedGuestPackages() }.getOrElse { emptyList() }
            runOnUiThread {
                guests = list
                render()
            }
        }.start()
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
        val bb = app.runtime as? BlackBoxRuntime
        if (bb != null) {
            val guestList = guests
            if (guestList == null) {
                root.addView(Ui.row(this, "Loading guest apps…"))
            } else if (guestList.isEmpty()) {
                root.addView(Ui.row(this, "No guest apps installed in the virtual user."))
            } else {
                guestList.forEach { pkg ->
                    root.addView(Ui.row(this, "guest: $pkg"))
                    root.addView(Ui.button(this, "Launch $pkg") {
                        Ui.bg(this, work = {
                            app.runtime.launchApplication(pkg)
                            "OK: launched $pkg"
                        })
                    })
                    root.addView(Ui.button(this, "Stop $pkg") {
                        Ui.bg(this, work = {
                            app.runtime.stopApplication(pkg)
                            "OK: stopped $pkg"
                        })
                    })
                    root.addView(Ui.button(this, "Uninstall $pkg") {
                        Ui.bg(this, work = {
                            app.runtime.uninstallApplication(pkg)
                            "OK: uninstalled $pkg"
                        }, onDone = { render() })
                    })
                }
            }
            root.addView(Ui.divider(this))
            root.addView(Ui.button(this, "Stop runtime") {
                Ui.bg(this, work = { app.runtime.stop(); "OK: runtime stopped" }, onDone = { render() })
            })
            root.addView(Ui.button(this, "Start runtime") {
                Ui.bg(this, work = { app.runtime.start(); "OK: runtime started" }, onDone = { render() })
            })
        } else {
            val staged = app.storage.stagedApks(identity.id)
            val guestDirs = app.storage.guestApps(identity.id)
            if (staged.isEmpty() && guestDirs.isEmpty()) {
                root.addView(Ui.row(this, "No guest apps staged."))
            } else {
                staged.forEach { f -> root.addView(Ui.row(this, "staged APK: ${f.name}")) }
                guestDirs.forEach { d -> root.addView(Ui.row(this, "guest state: $d")) }
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
        val old = app.identities.activeIdentity()!!
        Ui.bg(this,
            work = {
                val fresh = app.identities.resetIdentity()
                val changed = old.profile.androidId != fresh.profile.androidId
                "OK: reset done; androidId changed=$changed " +
                    "(${old.profile.androidId} → ${fresh.profile.androidId})"
            },
            onDone = { render() })
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
        Ui.bg(this,
            work = {
                app.identities.deleteIdentity()
                "OK: identity deleted"
            },
            onDone = { finish() })
    }
}
