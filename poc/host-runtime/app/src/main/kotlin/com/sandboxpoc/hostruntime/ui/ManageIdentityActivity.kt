package com.sandboxpoc.hostruntime.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
            val card = Ui.card(this)
            card.addView(Ui.cardTitle(this, "No active identity"))
            card.addView(Ui.subtitle(this, "Generate one from Home to begin."))
            root.addView(card)
            setContentView(Ui.page(root))
            return
        }
        val p = identity.profile
        val sp = identity.spoofProfile

        // -- Summary card -------------------------------------------------
        val card = Ui.card(this)
        card.addView(Ui.cardHeader(this, "Identity",
            Ui.statePill(this, identity.state.name, identity.state.name == "ACTIVE")))
        card.addView(Ui.kvRow(this, "ID", "${identity.id.take(8)}…"))
        card.addView(Ui.kvRow(this, "Device", "${p.manufacturer} ${p.model}"))
        card.addView(Ui.kvRow(this, "Android", "${p.androidVersion} (API ${p.apiLevel})"))
        card.addView(Ui.kvRow(this, "Runtime", app.identities.runtimeStatus().toString()))
        root.addView(card)

        // -- Technical details (the full mono dump, collapsed by default) -
        root.addView(Ui.collapsible(this, "Technical details") { c ->
            c.addView(Ui.mono(this, "id:        ${identity.id}"))
            c.addView(Ui.mono(this, "state:     ${identity.state}"))
            c.addView(Ui.mono(this, "runtime:   ${app.identities.runtimeStatus()}"))
            c.addView(Ui.mono(this, "manufacturer: ${p.manufacturer}"))
            c.addView(Ui.mono(this, "brand:        ${p.brand}"))
            c.addView(Ui.mono(this, "model:        ${p.model}"))
            c.addView(Ui.mono(this, "device:       ${p.device}"))
            c.addView(Ui.mono(this, "product:      ${p.product}"))
            c.addView(Ui.mono(this, "android:      ${p.androidVersion} (API ${p.apiLevel})"))
            c.addView(Ui.mono(this, "build:        ${p.buildId} ${p.buildType} ${p.buildTags}"))
            c.addView(Ui.mono(this, "fingerprint:"))
            c.addView(Ui.mono(this, "  ${p.fingerprint}"))
            c.addView(Ui.mono(this, "profileId:    ${sp.profileId}"))
            c.addView(Ui.mono(this, "generatedAt:  ${sp.generatedAt}"))
            c.addView(Ui.mono(this, "androidId:    ${sp.androidId}"))
            c.addView(Ui.mono(this, "location:     ${sp.location.latitude}, ${sp.location.longitude}"))
            c.addView(Ui.mono(this, "              movement=${sp.location.movement.enabled}"))
            c.addView(Ui.mono(this, "telephony:    ${sp.telephony.operatorName} " +
                "${sp.telephony.operatorNumeric}/${sp.telephony.countryIso}"))
            c.addView(Ui.mono(this, "deviceId:     ${sp.telephony.deviceId}"))
            c.addView(Ui.mono(this, "subscriberId: ${sp.telephony.subscriberId}"))
            c.addView(Ui.mono(this, "sensors:      ${sp.sensors.size} " +
                "(types ${sp.sensors.joinToString(",") { it.type.toString() }})"))
            c.addView(Ui.mono(this, "network:      ${sp.network.ssid} ${sp.network.bssid} ${sp.network.transport}"))
        })

        // -- Capabilities ---------------------------------------------------
        val capCard = Ui.card(this)
        capCard.addView(Ui.cardTitle(this, "Capabilities"))
        app.capabilities.all().forEach { cap ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                val v = Ui.dp(this@ManageIdentityActivity, 4)
                setPadding(0, v, 0, v)
            }
            row.addView(TextView(this).apply {
                text = cap.displayName
                textSize = 14f
                setTextColor(Ui.TEXT)
                layoutParams = LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            })
            row.addView(Ui.capabilityPill(this, cap.state))
            capCard.addView(row)
        }
        root.addView(capCard)

        // -- Guest applications ----------------------------------------------
        root.addView(Ui.section(this, "Guest applications"))
        root.addView(Ui.secondaryButton(this, "Clone Application…") {
            startActivity(Intent(this, AppPickerActivity::class.java))
        })
        val bb = app.runtime as? BlackBoxRuntime
        if (bb != null) {
            val guestList = guests
            val labels = app.storage.guestLabels(identity.id)
            when {
                guestList == null ->
                    root.addView(Ui.subtitle(this, "Loading cloned apps…"))
                else -> {
                    // Only the apps the user cloned — the engine's Google Play
                    // Services clones are hidden here (see Settings).
                    val userGuests = guestList.filter { !bb.isGmsPackage(it) }
                    if (userGuests.isEmpty()) {
                        root.addView(Ui.subtitle(this, "No apps cloned in this identity."))
                    } else {
                        userGuests.forEach { pkg ->
                            val gcard = Ui.card(this)
                            gcard.addView(TextView(this).apply {
                                text = labels[pkg] ?: pkg
                                textSize = 15f
                                setTypeface(typeface, android.graphics.Typeface.BOLD)
                                setTextColor(Ui.TEXT)
                            })
                            gcard.addView(Ui.subtitle(this, pkg))
                            gcard.addView(Ui.buttonRow(this,
                                Ui.secondaryButton(this, "Launch", 1f) {
                                    launchGuestWithProfile(pkg)
                                },
                                Ui.dangerButton(this, "Uninstall", 1f) {
                                    Ui.bg(this, work = {
                                        app.runtime.uninstallApplication(pkg)
                                        "OK: uninstalled $pkg"
                                    }, onDone = { guests = null; loadGuests() })
                                }))
                            root.addView(gcard)
                        }
                    }
                }
            }
        } else {
            val infoCard = Ui.card(this)
            infoCard.addView(Ui.cardTitle(this, "Guest state"))
            val staged = app.storage.stagedApks(identity.id)
            val guestDirs = app.storage.guestApps(identity.id)
            if (staged.isEmpty() && guestDirs.isEmpty()) {
                infoCard.addView(Ui.subtitle(this, "No guest apps staged."))
            } else {
                staged.forEach { f -> infoCard.addView(Ui.row(this, "staged APK: ${f.name}")) }
                guestDirs.forEach { d -> infoCard.addView(Ui.row(this, "guest state: $d")) }
            }
            root.addView(infoCard)
        }

        // -- Danger zone ------------------------------------------------------
        val danger = Ui.dangerCard(this)
        danger.addView(Ui.cardTitle(this, "Danger zone", Ui.RED))
        danger.addView(Ui.subtitle(this,
            "Reset or delete destroys the runtime and ALL guest state. This cannot be undone."))
        danger.addView(Ui.buttonRow(this,
            Ui.dangerButton(this, "Reset Identity", 1f) { confirmReset() },
            Ui.dangerButton(this, "Delete Identity", 1f) { confirmDelete() }))
        root.addView(danger)

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
