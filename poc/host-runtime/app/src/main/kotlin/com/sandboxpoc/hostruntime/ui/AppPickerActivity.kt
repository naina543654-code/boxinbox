package com.sandboxpoc.hostruntime.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import android.widget.Toast
import com.sandboxpoc.hostruntime.SandboxApp
import java.io.File

/**
 * Lists launchable host apps by name, with a search field on top. On
 * select, resolves the APK path (sourceDir/publicSourceDir) plus any split
 * APKs (splitSourceDirs) and installs them into the sandbox. A warning is
 * shown first: only APK artifacts are used — the source app's private data
 * is never copied.
 */
class AppPickerActivity : Activity() {

    private lateinit var app: SandboxApp
    private var allEntries: List<AppEntry> = emptyList()
    private lateinit var resultsBox: LinearLayout

    data class AppEntry(
        val label: String,
        val packageName: String,
        val apkPath: String,
        val splitApkPaths: List<String>,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as SandboxApp
        allEntries = queryLaunchableApps()

        val root = Ui.screen(this)
        root.addView(Ui.title(this, "Clone Application"))
        root.addView(Ui.row(this, "Only the app's APK file is used — never its data."))
        val search = Ui.searchField(this, "Search apps…")
        root.addView(search)
        resultsBox = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        root.addView(resultsBox)
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {
                renderResults(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        renderResults("")
        setContentView(Ui.page(root))
    }

    /** Name-only rows, filtered by the search query (matches name or package). */
    private fun renderResults(filter: String) {
        resultsBox.removeAllViews()
        val q = filter.trim().lowercase()
        val entries = if (q.isEmpty()) allEntries
        else allEntries.filter {
            it.label.lowercase().contains(q) || it.packageName.lowercase().contains(q)
        }
        if (entries.isEmpty()) {
            resultsBox.addView(Ui.row(this, "No apps match."))
        }
        entries.forEach { e ->
            resultsBox.addView(Ui.button(this, e.label) { confirmInstall(e) })
        }
    }

    private fun queryLaunchableApps(): List<AppEntry> {
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val infos = pm.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0))
        return infos.mapNotNull { ri ->
            val ai = ri.activityInfo?.applicationInfo ?: return@mapNotNull null
            if (ai.packageName == packageName) return@mapNotNull null // skip self
            val apkPath = ai.publicSourceDir ?: ai.sourceDir ?: return@mapNotNull null
            val splits = ai.splitSourceDirs?.filter { File(it).isFile }.orEmpty()
            val label = pm.getApplicationLabel(ai)?.toString() ?: ai.packageName
            AppEntry(label, ai.packageName, apkPath, splits)
        }.sortedBy { it.label.lowercase() }
    }

    private fun confirmInstall(entry: AppEntry) {
        AlertDialog.Builder(this)
            .setTitle("Clone \"${entry.label}\"?")
            .setMessage("WARNING: only APK artifacts (base + ${entry.splitApkPaths.size} split(s)) will be copied " +
                "into the sandbox. The app's private data is NEVER copied. " +
                "The guest starts with a clean slate inside the virtual identity.")
            .setPositiveButton("Install into sandbox") { _, _ -> doInstall(entry) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun doInstall(entry: AppEntry) {
        val identity = app.identities.activeIdentity()
        if (identity == null) {
            Toast.makeText(this, "Generate an identity first", Toast.LENGTH_LONG).show()
            return
        }
        val stagedPaths = try {
            // App-layer record: stage ONLY APK artifacts (base + splits).
            // Never app data.
            (listOf(entry.apkPath) + entry.splitApkPaths).map { p ->
                app.storage.stageApk(identity.id, p).also {
                    app.log.i("guest", "staged APK ${entry.packageName} -> ${it.absolutePath}")
                }.absolutePath
            }
        } catch (e: Exception) {
            AlertDialog.Builder(this).setTitle("Staging failed")
                .setMessage(e.message ?: e.toString()).setPositiveButton("OK", null).show()
            return
        }
        Ui.bg(this,
            work = {
                // Install the staged copies (app-private) into the virtual user.
                app.runtime.installApplicationWithSplits(stagedPaths)
                // Remember the display label so Manage Identity can show the
                // application name instead of the package name.
                app.storage.saveGuestLabel(identity.id, entry.packageName, entry.label)
                app.log.i("runtime", "installApplicationWithSplits(${entry.packageName}, ${stagedPaths.size} apks) ok")
                "OK: installed ${entry.label} into sandbox"
            })
    }
}
