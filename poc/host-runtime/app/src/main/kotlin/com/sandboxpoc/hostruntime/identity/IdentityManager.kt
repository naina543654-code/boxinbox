package com.sandboxpoc.hostruntime.identity

import com.sandboxpoc.hostruntime.profile.DeviceProfile
import com.sandboxpoc.hostruntime.profile.ProfileStore
import com.sandboxpoc.hostruntime.runtime.RuntimeStatus
import com.sandboxpoc.hostruntime.runtime.SandboxRuntime
import com.sandboxpoc.hostruntime.storage.StorageManager
import com.sandboxpoc.hostruntime.util.EventLog
import java.io.File
import java.util.UUID

/**
 * Owns the ONE active identity and its lifecycle. All runtime calls go
 * through [SandboxRuntime] (implemented by [BlackBoxRuntime]); runtime
 * failures are caught, logged, and surfaced — the app-layer flow
 * (generate → reset → delete, with profile diffing) stays fully exercisable.
 *
 * Profile lifecycle (via [ProfileStore], under <filesDir>/profiles/):
 * - generate → new SpoofProfile persisted (active_profile.json + profile.json)
 * - reset    → new SpoofProfile persisted (overwrites both files)
 * - delete   → both profile files removed
 *
 * File I/O runs off the main thread — all three entry points are invoked
 * from background work (see Ui.bg in the activities).
 */
class IdentityManager(
    private val storage: StorageManager,
    private val runtime: SandboxRuntime,
    private val log: EventLog,
    private val filesDir: File,
) {

    fun activeIdentity(): Identity? = storage.loadIdentity()

    fun runtimeStatus(): RuntimeStatus = runtime.getStatus()

    // ------------------------------------------------------------------
    // Generate: newProfile → validate → persist → runtime.create() → ACTIVE
    // ------------------------------------------------------------------
    fun generateIdentity(): Identity {
        val existing = activeIdentity()
        require(existing == null) {
            "Identity ${existing!!.id} already exists — reset or delete it first"
        }
        // Audit fix 2026-10-04: loadIdentity() returns null on a missing OR
        // unparseable identity.json, so a corrupted record used to let
        // generateIdentity() build a NEW identity over a NEVER-WIPED virtual
        // user — old cloned apps and their data silently inherited. Refuse
        // and wipe first instead.
        if (runtime.hasLeftoverGuestState()) {
            log.w("identity", "leftover guest state with no identity record — wiping before generate")
            attempt("destroy") { runtime.destroy() }
        }
        val identityId = UUID.randomUUID().toString()
        val spoof = ProfileGenerator.newProfile(profileId = identityId)
        val violations = ProfileValidator.validateSpoof(spoof)
        require(violations.isEmpty()) { "Generated profile invalid: $violations" }
        val row = DeviceProfile.DEVICE_TABLE.first { it.fingerprint == spoof.device.fingerprint }

        val identity = Identity(
            id = identityId,
            createdAt = System.currentTimeMillis(),
            profile = row,
            spoofProfile = spoof,
        )
        storage.saveIdentity(identity)
        ProfileStore.saveActive(filesDir, spoof)
        log.i("identity", "generated ${identity.id} " +
            "(device=${row.manufacturer} ${row.model}, androidId=${spoof.androidId}); " +
            "profile persisted to ${ProfileStore.DIR_NAME}/${ProfileStore.ACTIVE_PROFILE_NAME}")

        attempt("create") { runtime.create() }
        log.i("identity", "state -> ACTIVE (runtime=${runtimeStatus()})")
        return identity
    }

    // ------------------------------------------------------------------
    // Reset: runtime.destroy() → delete profile+guest state → NEW profile
    // (must differ: new androidId at minimum) → runtime.create() → clean
    // ------------------------------------------------------------------
    fun resetIdentity(): Identity {
        val existing = activeIdentity()
            ?: throw IllegalStateException("No active identity to reset")

        log.i("identity", "resetting ${existing.id}")
        attempt("destroy") { runtime.destroy() }
        storage.deleteIdentityState(existing.id)
        log.i("identity", "deleted profile + guest state for ${existing.id}")

        val identityId = UUID.randomUUID().toString()
        val avoidFingerprint = existing.spoofProfile.device.fingerprint
        var spoof = ProfileGenerator.newProfile(
            profileId = identityId, avoidFingerprint = avoidFingerprint)
        var guard = 0
        while (spoof.androidId == existing.spoofProfile.androidId && guard++ < 10) {
            spoof = ProfileGenerator.newProfile(
                profileId = identityId, avoidFingerprint = avoidFingerprint)
        }
        check(spoof.androidId != existing.spoofProfile.androidId) {
            "Reset produced an identical androidId — refusing to continue"
        }
        val violations = ProfileValidator.validateSpoof(spoof)
        require(violations.isEmpty()) { "Reset profile invalid: $violations" }
        val row = DeviceProfile.DEVICE_TABLE.first { it.fingerprint == spoof.device.fingerprint }

        val identity = Identity(
            id = identityId,
            createdAt = System.currentTimeMillis(),
            profile = row,
            spoofProfile = spoof,
        )
        storage.saveIdentity(identity)
        ProfileStore.saveActive(filesDir, spoof)
        log.i("identity", "regenerated ${identity.id} " +
            "(old androidId=${existing.spoofProfile.androidId}, new androidId=${spoof.androidId}); " +
            "profile persisted to ${ProfileStore.DIR_NAME}/${ProfileStore.ACTIVE_PROFILE_NAME}")

        attempt("create") { runtime.create() }
        log.i("identity", "state -> ACTIVE (runtime=${runtimeStatus()}, guest state clean)")
        return identity
    }

    // ------------------------------------------------------------------
    // Delete: runtime.destroy() → remove profile + all state → no identity
    // ------------------------------------------------------------------
    fun deleteIdentity() {
        val existing = activeIdentity()
            ?: throw IllegalStateException("No active identity to delete")

        log.i("identity", "deleting ${existing.id}")
        attempt("destroy") { runtime.destroy() }
        storage.deleteIdentityState(existing.id)
        ProfileStore.deleteActive(filesDir)
        log.i("identity", "deleted ${existing.id}; no active identity; profile files removed")
    }

    /**
     * Runs a runtime op; the stub throws UnsupportedOperationException, which
     * is logged honestly (W) instead of failing the app-layer transition.
     * Any other exception is logged as an error and rethrown.
     */
    private fun attempt(op: String, block: () -> Unit) {
        try {
            block()
            log.i("runtime", "$op() ok")
        } catch (e: UnsupportedOperationException) {
            log.w("runtime", "$op(): ${e.message} — continuing with app-layer state only")
        } catch (e: Exception) {
            log.e("runtime", "$op() failed: ${e.message}")
            throw e
        }
    }
}
