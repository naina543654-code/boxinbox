package com.sandboxpoc.hostruntime.profile

import java.io.File

/**
 * Persists the active [SpoofProfile] JSON under the host app's private
 * filesDir:
 *
 *   <filesDir>/profiles/active_profile.json   well-known name — the engine reads this
 *   <filesDir>/profiles/profile.json          audit copy
 *
 * Engine-independent: no imports from any engine package (top.niunaijun.*).
 * File I/O must run off the main thread — callers (IdentityManager) are only
 * invoked from background work (see Ui.bg).
 */
object ProfileStore {

    const val DIR_NAME = "profiles"

    /** Well-known file the engine reads for the active profile. */
    const val ACTIVE_PROFILE_NAME = "active_profile.json"

    /** Audit copy of the same JSON. */
    const val PROFILE_COPY_NAME = "profile.json"

    /** Per-identity spoofed build.prop; the engine redirects guest reads of /system/build.prop here. */
    const val BUILD_PROP_NAME = "build.prop"

    /** Per-identity spoofed /proc/version content. */
    const val PROC_VERSION_NAME = "proc_version"

    private fun dir(filesDir: File): File = File(filesDir, DIR_NAME).also { it.mkdirs() }

    /**
     * Writes [profile] to the profile files plus the per-identity procfs
     * spoof files (build.prop, proc_version) the engine redirects guest
     * reads to. Overwrites any previous active profile — V1 scope is one
     * active identity.
     */
    fun saveActive(filesDir: File, profile: SpoofProfile) {
        val d = dir(filesDir)
        val json = profile.toJson()
        File(d, ACTIVE_PROFILE_NAME).writeText(json)
        File(d, PROFILE_COPY_NAME).writeText(json)
        File(d, BUILD_PROP_NAME).writeText(profile.toBuildProp())
        File(d, PROC_VERSION_NAME).writeText(profile.toProcVersion())
    }

    /** Reads back the active profile, or null if absent/unparseable. */
    fun loadActive(filesDir: File): SpoofProfile? {
        val f = File(dir(filesDir), ACTIVE_PROFILE_NAME)
        if (!f.isFile) return null
        return runCatching { SpoofProfile.fromJson(f.readText()) }.getOrNull()
    }

    /** Removes the profile and procfs spoof files. Idempotent — safe to call when absent. */
    fun deleteActive(filesDir: File) {
        val d = dir(filesDir)
        File(d, ACTIVE_PROFILE_NAME).delete()
        File(d, PROFILE_COPY_NAME).delete()
        File(d, BUILD_PROP_NAME).delete()
        File(d, PROC_VERSION_NAME).delete()
    }
}
