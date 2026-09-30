package com.sandboxpoc.hostruntime.identity

import com.sandboxpoc.hostruntime.profile.DeviceProfile
import com.sandboxpoc.hostruntime.profile.SpoofProfile

/** Lifecycle state of the single virtual identity. */
enum class IdentityState {
    /** Identity generated; runtime creation was attempted (may be stub). */
    ACTIVE,
}

/**
 * The ONE active virtual identity. The app enforces a single active identity
 * at a time: generating while one exists requires explicit reset/delete first.
 *
 * - [profile]: the coherent device-table row the identity was built from
 *   (display/audit; DeviceProfile template).
 * - [spoofProfile]: the per-identity spoofed profile — the artifact shared
 *   with the engine and the probe (SpoofProfile JSON contract).
 */
data class Identity(
    val id: String,
    val createdAt: Long,
    val profile: DeviceProfile,
    val spoofProfile: SpoofProfile,
    val state: IdentityState = IdentityState.ACTIVE,
)
