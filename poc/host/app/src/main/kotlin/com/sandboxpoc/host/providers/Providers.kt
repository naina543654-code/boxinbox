package com.sandboxpoc.host.providers

import com.sandboxpoc.host.profile.DeviceProfile

/**
 * Provider interfaces for the virtualization features the engine will back.
 * PoC scope = interfaces + wiring to capabilities only; implementations
 * arrive with the engine adapter (see INTEGRATION.md). No provider may
 * silently return host values — when unimplemented, callers must treat the
 * feature as unavailable and say so.
 */

interface DeviceIdentityProvider {
    /** The spoofed profile currently applied to the virtual runtime, or null. */
    fun currentProfile(): DeviceProfile?
}

interface LocationProvider {
    /** True only if the engine is actively spoofing location for guests. */
    fun isSpoofingEnabled(): Boolean
}

interface SensorProvider {
    /** True only if the engine is actively spoofing sensors for guests. */
    fun isSpoofingEnabled(): Boolean
}

interface NetworkProvider {
    /** True only if guest traffic is isolated/routed by the engine. */
    fun isIsolated(): Boolean
}

interface PackageVisibilityProvider {
    /** Packages the virtual runtime exposes to guests. */
    fun visiblePackages(): List<String>
}

/** Google-services strategy for the virtual runtime. Default: NONE. */
enum class GmsMode {
    NONE,
    MICROG,
    HOST_GMS,
    CUSTOM,
}

interface GoogleServicesProvider {
    fun mode(): GmsMode
}
