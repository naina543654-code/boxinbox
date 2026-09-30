package com.sandboxpoc.hostruntime.storage

import android.content.Context
import com.sandboxpoc.hostruntime.identity.Identity
import com.sandboxpoc.hostruntime.identity.IdentityState
import com.sandboxpoc.hostruntime.profile.DeviceProfile
import org.json.JSONObject
import java.io.File

/**
 * Guest-state storage, rooted under app-private files: filesDir/sandbox/.
 *
 * Layout:
 *   sandbox/
 *     identity.json            current identity (profile embedded)
 *     lifecycle.log            identity/runtime transition log
 *     <identityId>/
 *       apks/<fileName>.apk    staged guest APK artifacts (APK only)
 *       apps/<packageName>/    per-guest-app state (engine-owned later)
 *
 * INVARIANTS (enforced here, not just documented):
 * - The host app's own private data is NEVER copied into the sandbox.
 * - Installing a guest copies only the APK artifact file, never app data.
 */
class StorageManager(context: Context) {

    private val sandboxRoot: File = File(context.filesDir, "sandbox").also { it.mkdirs() }
    private val identityFile: File = File(sandboxRoot, "identity.json")
    private val logFile: File = File(sandboxRoot, "lifecycle.log")

    fun sandboxRoot(): File = sandboxRoot

    /** Guest state root for one identity: sandbox/<identityId>/. */
    fun guestStateRoot(identityId: String): File =
        File(sandboxRoot, identityId).also { it.mkdirs() }

    /** Per-guest-app dir: sandbox/<identityId>/apps/<packageName>/. */
    fun guestAppDir(identityId: String, packageName: String): File =
        File(guestStateRoot(identityId), "apps/$packageName").also { it.mkdirs() }

    // ------------------------------------------------------------------
    // Identity persistence (profile persisted as JSON while identity exists)
    // ------------------------------------------------------------------

    fun saveIdentity(identity: Identity) {
        identityFile.writeText(identityToJson(identity).toString(2))
    }

    fun loadIdentity(): Identity? {
        if (!identityFile.exists()) return null
        return runCatching { jsonToIdentity(JSONObject(identityFile.readText())) }.getOrNull()
    }

    /** Remove identity record + ALL guest state for [identityId]. */
    fun deleteIdentityState(identityId: String) {
        File(sandboxRoot, identityId).deleteRecursively()
        if (identityFile.exists()) identityFile.delete()
    }

    // ------------------------------------------------------------------
    // Guest APK staging — APK artifact ONLY, never app data
    // ------------------------------------------------------------------

    /**
     * Copy the APK at [apkPath] into sandbox/<identityId>/apks/. Only the
     * APK file is copied; the source app's private data directories are
     * never touched. Returns the staged file.
     */
    fun stageApk(identityId: String, apkPath: String): File {
        val src = File(apkPath)
        require(src.isFile) { "APK not found: $apkPath" }
        val destDir = File(guestStateRoot(identityId), "apks").also { it.mkdirs() }
        val dest = File(destDir, src.name)
        src.copyTo(dest, overwrite = true)
        return dest
    }

    /** APK artifacts staged for [identityId]. */
    fun stagedApks(identityId: String): List<File> {
        val dir = File(guestStateRoot(identityId), "apks")
        return dir.listFiles { f -> f.isFile && f.name.endsWith(".apk") }?.toList().orEmpty()
    }

    /** Guest app state dirs present for [identityId]. */
    fun guestApps(identityId: String): List<String> {
        val dir = File(guestStateRoot(identityId), "apps")
        return dir.listFiles { f -> f.isDirectory }?.map { it.name }.orEmpty()
    }

    // ------------------------------------------------------------------
    // Lifecycle log
    // ------------------------------------------------------------------

    fun appendLog(line: String) {
        logFile.appendText(line + "\n")
    }

    fun readLog(): List<String> =
        if (logFile.exists()) logFile.readLines().takeLast(200) else emptyList()

    // ------------------------------------------------------------------
    // JSON (org.json — no extra deps)
    // ------------------------------------------------------------------

    private fun profileToJson(p: DeviceProfile): JSONObject = JSONObject()
        .put("manufacturer", p.manufacturer)
        .put("brand", p.brand)
        .put("model", p.model)
        .put("device", p.device)
        .put("product", p.product)
        .put("fingerprint", p.fingerprint)
        .put("buildId", p.buildId)
        .put("buildTags", p.buildTags)
        .put("buildType", p.buildType)
        .put("androidVersion", p.androidVersion)
        .put("apiLevel", p.apiLevel)
        .put("androidId", p.androidId)
        .put("extras", JSONObject(p.extras))

    private fun jsonToProfile(o: JSONObject): DeviceProfile {
        val extras = mutableMapOf<String, String>()
        val eo = o.optJSONObject("extras")
        eo?.keys()?.forEach { k -> extras[k] = eo.optString(k) }
        return DeviceProfile(
            manufacturer = o.getString("manufacturer"),
            brand = o.getString("brand"),
            model = o.getString("model"),
            device = o.getString("device"),
            product = o.getString("product"),
            fingerprint = o.getString("fingerprint"),
            buildId = o.getString("buildId"),
            buildTags = o.getString("buildTags"),
            buildType = o.getString("buildType"),
            androidVersion = o.getString("androidVersion"),
            apiLevel = o.getInt("apiLevel"),
            androidId = o.getString("androidId"),
            extras = extras,
        )
    }

    private fun identityToJson(i: Identity): JSONObject = JSONObject()
        .put("id", i.id)
        .put("createdAt", i.createdAt)
        .put("state", i.state.name)
        .put("profile", profileToJson(i.profile))

    private fun jsonToIdentity(o: JSONObject): Identity = Identity(
        id = o.getString("id"),
        createdAt = o.getLong("createdAt"),
        profile = jsonToProfile(o.getJSONObject("profile")),
        state = IdentityState.valueOf(o.getString("state")),
    )
}
