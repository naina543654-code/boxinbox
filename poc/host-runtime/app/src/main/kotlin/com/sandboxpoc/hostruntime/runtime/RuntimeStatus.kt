package com.sandboxpoc.hostruntime.runtime

/**
 * Runtime status. The BlackBox engine is integrated; states reflect the
 * real virtual-user lifecycle. The app never pretends the sandbox is live
 * when it isn't (ERROR surfaces instead).
 */
enum class RuntimeStatus {
    /** No runtime has been created for the current identity. */
    NOT_CREATED,

    /** The virtual runtime is created and active. */
    ACTIVE,

    /** The runtime is stopped (identity and guest state preserved). */
    STOPPED,

    /** The runtime was destroyed. */
    DESTROYED,

    /** No engine is integrated — operations are unavailable. (Unused in this variant.) */
    ENGINE_NOT_INTEGRATED,

    /** The engine reported an error. */
    ERROR,
}
