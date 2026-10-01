package com.sandboxpoc.hostruntime.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.sandboxpoc.hostruntime.SandboxApp
import com.sandboxpoc.hostruntime.runtime.BlackBoxRuntime

class ManageIdentityActivity : Activity() {

    companion object {
        /**
         * Basename (without `.json`) of the staged expected-profile file.
         * Written by [launchGuestWithProfile] into the guest's virtual
         * files dir; read by the probe as a fallback when no Intent extra
         * is present.
         */
        const val EXTRA_EXPECTED_PROFILE = "expected_profile"
    }

    private lateinit var app: SandboxApp
    /** Cached guest package list; null while a background load is in flight. */
    private var guests: List<String>? = null
    /** Cached GMS state; null while a background load is in flight. */
    private var gmsInstalled: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as SandboxApp
    }

    override fun onResume() {
        super.onResume()
        guests = null
        gmsInstalled = null
        render()
        loadGuests()
    }

    /** Loads the guest package list and GMS state off the main thread, then re-renders. */
    private fun loadGuests() {
        val bb = app.runtime as? BlackBoxRuntime ?: return
        Thread {
            val list = runCatching { bb.installedGuestPackages() }.getOrElse { emptyList() }
            val gms = runCatching { bb.isGmsInstalled() }.getOrNull()
            runOnUiThread {
                guests = list
                gmsInstalled = gms
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
        val sp = identity.spoofProfile

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

        root.addView(Ui.section(this, "Spoof profile (profiles/active_profile.json)"))
        root.addView(Ui.mono(this, "profileId:    ${sp.profileId}"))
        root.addView(Ui.mono(this, "generatedAt:  ${sp.generatedAt}"))
        root.addView(Ui.mono(this, "androidId:    ${sp.androidId}"))
        root.addView(Ui.mono(this, "location:     ${sp.location.latitude}, ${sp.location.longitude}"))
        root.addView(Ui.mono(this, "              movement=${sp.location.movement.enabled}"))
        root.addView(Ui.mono(this, "telephony:    ${sp.telephony.operatorName} " +
            "${sp.telephony.operatorNumeric}/${sp.telephony.countryIso}"))
        root.addView(Ui.mono(this, "deviceId:     ${sp.telephony.deviceId}"))
        root.addView(Ui.mono(this, "subscriberId: ${sp.telephony.subscriberId}"))
        root.addView(Ui.mono(this, "sensors:      ${sp.sensors.size} " +
            "(types ${sp.sensors.joinToString(",") { it.type.toString() }})"))
        root.addView(Ui.mono(this, "network:      ${sp.network.ssid} ${sp.network.bssid} ${sp.network.transport}"))

        root.addView(Ui.section(this, "Capabilities"))
        app.capabilities.all().forEach { cap ->
            root.addView(Ui.row(this, "• ${cap.displayName}: ${cap.state}"))
        }

        root.addView(Ui.section(this, "Google Play Services"))
        when (gmsInstalled) {
            null -> root.addView(Ui.row(this, "Checking…"))
            true -> root.addView(Ui.row(this, "Installed in this identity."))
            false -> {
                root.addView(Ui.row(this, "Not installed — guests that require Play " +
                    "Services (e.g. Wakie) will refuse to connect."))
                root.addView(Ui.button(this, "Install Google Play Services") {
                    Ui.bg(this, work = {
                        (app.runtime as? BlackBoxRuntime)?.installGoogleServices()
                            ?: "FAIL: runtime is not BlackBox-backed"
                    }, onDone = {
                        gmsInstalled = null
                        loadGuests()
                    })
                })
            }
        }

        root.addView(Ui.section(this, "Guest applications"))
        root.addView(Ui.button(this, "Clone Application…") {
            startActivity(Intent(this, AppPickerActivity::class.java))
        })
        val bb = app.runtime as? BlackBoxRuntime
        if (bb != null) {
            val guestList = guests
            val labels = app.storage.guestLabels(identity.id)
            if (guestList == null) {
                root.addView(Ui.row(this, "Loading guest apps…"))
            } else if (guestList.isEmpty()) {
                root.addView(Ui.row(this, "No guest apps installed in the virtual user."))
            } else {
                guestList.forEach { pkg ->
                    root.addView(Ui.row(this, labels[pkg] ?: pkg))
                    root.addView(Ui.button(this, "Launch") {
                        launchGuestWithProfile(pkg)
                    })
                    root.addView(Ui.button(this, "Uninstall") {
                        Ui.bg(this, work = {
                            app.runtime.uninstallApplication(pkg)
                            "OK: uninstalled $pkg"
                        }, onDone = { guests = null; loadGuests() })
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

    /**
     * Guest-launch helper used for the probe. Stages the active profile
     * JSON as `expected_profile.json` inside the guest's virtual files
     * dir (via [BlackBoxRuntime.stageExpectedProfile]), then launches.
     *
     * An Intent extra cannot be delivered: BlackBoxCore.launchApk(packageName,
     * userId) accepts no Intent — so the probe reads the staged file as
     * its fallback source for expected values. The file is written on the
     * background thread immediately before launch, so it is always fresh.
     */
    private fun launchGuestWithProfile(packageName: String) {
        Ui.bg(this, work = {
            val identity = app.identities.activeIdentity()
                ?: return@bg "FAIL: no active identity"
            val profileJson = identity.spoofProfile.toJson()
            val bb = app.runtime as? BlackBoxRuntime
                ?: return@bg "FAIL: runtime is not BlackBox-backed"
            bb.stageExpectedProfile(packageName, profileJson)
            app.runtime.launchApplication(packageName)
            app.log.i("probe", "launched $packageName; expected profile staged " +
                "(${profileJson.length} chars)")
            "OK: launched $packageName (expected profile staged: ${profileJson.length} chars)"
        })
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
                val changed = old.spoofProfile.androidId != fresh.spoofProfile.androidId
                "OK: reset done; androidId changed=$changed " +
                    "(${old.spoofProfile.androidId} → ${fresh.spoofProfile.androidId})"
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
