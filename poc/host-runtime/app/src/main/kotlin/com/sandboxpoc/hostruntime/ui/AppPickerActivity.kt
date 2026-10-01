package com.sandboxpoc.hostruntime.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import com.sandboxpoc.hostruntime.SandboxApp
import java.io.File

/**
 * Lists launchable host apps (name, icon, packageName, versionName). On
 * select, resolves the APK path (sourceDir/publicSourceDir) plus any split
 * APKs (splitSourceDirs) and installs them into the sandbox. A warning is
 * shown first: only APK artifacts are used — the source app's private data
 * is never copied.
 */
class AppPickerActivity : Activity() {

    private lateinit var app: SandboxApp

    data class AppEntry(
        val label: String,
        val packageName: String,
        val versionName: String?,
        val apkPath: String,
        val splitApkPaths: List<String>,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as SandboxApp
        render()
    }

    private fun render() {
        val root = Ui.screen(this)
        root.addView(Ui.title(this, "Clone Application"))
        root.addView(Ui.row(this, "Pick a host app to clone into the sandbox. " +
            "Only its APK file is used — never its data."))

        val entries = queryLaunchableApps()
        if (entries.isEmpty()) {
            root.addView(Ui.row(this, "No launchable apps visible."))
        }
        entries.forEach { e ->
            root.addView(Ui.button(this, "${e.label}\n${e.packageName}  v${e.versionName ?: "?"}") {
                confirmInstall(e)
            })
        }
        setContentView(Ui.page(root))
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
            val version = runCatching {
                pm.getPackageInfo(ai.packageName, PackageManager.PackageInfoFlags.of(0)).versionName
            }.getOrNull()
            AppEntry(label, ai.packageName, version, apkPath, splits)
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
                app.log.i("runtime", "installApplicationWithSplits(${entry.packageName}, ${stagedPaths.size} apks) ok")
                "OK: installed ${entry.label} into sandbox"
            })
    }
}
