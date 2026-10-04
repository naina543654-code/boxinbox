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

    /**
     * Per-identity MAC for the /sys/class/net/wlan0/address redirect;
     * content is the profile's wifiMac followed by a newline (sysfs shape).
     */
    const val WLAN0_ADDRESS_NAME = "wlan0_address"

    /**
     * Profile generation counter, bumped on every save/delete. The engine
     * keeps the parsed profile in a process-static singleton; a guest process
     * that survives Reset/Delete polls this file and drops its cache when
     * the generation changes, so it can never keep serving a deleted
     * identity's spoofs.
     */
    const val GENERATION_NAME = "generation"

    private fun dir(filesDir: File): File = File(filesDir, DIR_NAME).also { it.mkdirs() }

    /** Atomic write: temp file + rename, so a guest never reads a half-written profile. */
    private fun writeAtomic(dir: File, name: String, content: String) {
        val tmp = File(dir, "$name.tmp")
        tmp.writeText(content)
        val dest = File(dir, name)
        if (!tmp.renameTo(dest)) {
            // renameTo can fail across filesystems; fall back to a direct
            // write (same dir, so this should not happen in practice).
            dest.writeText(content)
            tmp.delete()
        }
    }

    /**
     * Writes [profile] to the profile files plus the per-identity procfs
     * spoof files (build.prop, proc_version, wlan0_address) the engine
     * redirects guest reads to. Overwrites any previous active profile — V1
     * scope is one active identity. All writes are atomic; the generation
     * counter is bumped last so engine invalidation sees a complete set.
     */
    fun saveActive(filesDir: File, profile: SpoofProfile) {
        val d = dir(filesDir)
        val json = profile.toJson()
        writeAtomic(d, ACTIVE_PROFILE_NAME, json)
        writeAtomic(d, PROFILE_COPY_NAME, json)
        writeAtomic(d, BUILD_PROP_NAME, profile.toBuildProp())
        writeAtomic(d, PROC_VERSION_NAME, profile.toProcVersion())
        writeAtomic(d, WLAN0_ADDRESS_NAME, profile.network.wifiMac + "\n")
        writeAtomic(d, GENERATION_NAME, System.currentTimeMillis().toString())
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
        File(d, WLAN0_ADDRESS_NAME).delete()
        // Bump the generation (rather than just deleting it) so a surviving
        // guest process observes the change and drops its cached profile.
        writeAtomic(d, GENERATION_NAME, System.currentTimeMillis().toString())
    }
}
