package com.sandboxpoc.host.capability

/** Capability support state. The app must NEVER silently fall back to host
 * values — a capability that is not provided by the engine is marked
 * explicitly (UNVERIFIED / UNSUPPORTED / EXPERIMENTAL), never hidden. */
enum class CapabilityState {
    SUPPORTED,
    PARTIALLY_SUPPORTED,
    EXPERIMENTAL,
    UNSUPPORTED,
    /** Provided by the host OS, not virtualized — shown explicitly, never silent. */
    HOST_PROVIDED,
    /** Engine integration pending — support unknown. Default for all. */
    UNVERIFIED,
}

data class Capability(
    val id: String,
    val displayName: String,
    val state: CapabilityState = CapabilityState.UNVERIFIED,
    val detail: String = "",
)
