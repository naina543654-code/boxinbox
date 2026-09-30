package com.sandboxpoc.host.runtime

/**
 * Runtime status. ENGINE_NOT_INTEGRATED is the honest PoC state: the host
 * app is fully built, but no virtualization engine is plugged in yet.
 * The app never pretends the sandbox is live.
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

    /** No engine is integrated — operations are unavailable. */
    ENGINE_NOT_INTEGRATED,

    /** The engine reported an error. */
    ERROR,
}
