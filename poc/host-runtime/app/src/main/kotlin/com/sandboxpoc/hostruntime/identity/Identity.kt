package com.sandboxpoc.hostruntime.identity

import com.sandboxpoc.hostruntime.profile.DeviceProfile

/** Lifecycle state of the single virtual identity. */
enum class IdentityState {
    /** Identity generated; runtime creation was attempted (may be stub). */
    ACTIVE,
}

/**
 * The ONE active virtual identity. The app enforces a single active identity
 * at a time: generating while one exists requires explicit reset/delete first.
 */
data class Identity(
    val id: String,
    val createdAt: Long,
    val profile: DeviceProfile,
    val state: IdentityState = IdentityState.ACTIVE,
)
