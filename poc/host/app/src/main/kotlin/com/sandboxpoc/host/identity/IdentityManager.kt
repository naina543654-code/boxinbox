package com.sandboxpoc.host.identity

import com.sandboxpoc.host.runtime.RuntimeStatus
import com.sandboxpoc.host.runtime.SandboxRuntime
import com.sandboxpoc.host.storage.StorageManager
import com.sandboxpoc.host.util.EventLog
import java.util.UUID

/**
 * Owns the ONE active identity and its lifecycle. All runtime calls go
 * through [SandboxRuntime]; with the [com.sandboxpoc.host.runtime.StubRuntime]
 * they throw UnsupportedOperationException("engine not integrated"), which is
 * caught, logged, and surfaced — the app-layer flow (generate → reset →
 * delete, with profile diffing) stays fully exercisable.
 */
class IdentityManager(
    private val storage: StorageManager,
    private val runtime: SandboxRuntime,
    private val log: EventLog,
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
        val profile = ProfileGenerator.newProfile()
        val violations = ProfileValidator.validate(profile)
        require(violations.isEmpty()) { "Generated profile invalid: $violations" }

        val identity = Identity(
            id = UUID.randomUUID().toString(),
            createdAt = System.currentTimeMillis(),
            profile = profile,
        )
        storage.saveIdentity(identity)
        log.i("identity", "generated ${identity.id} " +
            "(device=${profile.manufacturer} ${profile.model}, androidId=${profile.androidId})")

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

        var profile = ProfileGenerator.newProfile()
        var guard = 0
        while (profile.androidId == existing.profile.androidId && guard++ < 10) {
            profile = ProfileGenerator.newProfile()
        }
        check(profile.androidId != existing.profile.androidId) {
            "Reset produced an identical androidId — refusing to continue"
        }
        val violations = ProfileValidator.validate(profile)
        require(violations.isEmpty()) { "Reset profile invalid: $violations" }

        val identity = Identity(
            id = UUID.randomUUID().toString(),
            createdAt = System.currentTimeMillis(),
            profile = profile,
        )
        storage.saveIdentity(identity)
        log.i("identity", "regenerated ${identity.id} " +
            "(old androidId=${existing.profile.androidId}, new androidId=${profile.androidId})")

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
        log.i("identity", "deleted ${existing.id}; no active identity")
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
