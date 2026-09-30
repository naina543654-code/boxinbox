package com.sandboxpoc.host.providers

import com.sandboxpoc.host.capability.CapabilityManager
import kotlin.reflect.KClass

/**
 * Capability wiring for providers: which capability each provider interface
 * is gated on. In the PoC there are no implementations; the wiring exists so
 * the engine adapter knows exactly which capability must become SUPPORTED
 * before a provider may be used. UI/settings screens consult
 * [CapabilityManager] through this map instead of calling providers directly.
 */
object ProviderBindings {

    val bindings: Map<KClass<*>, String> = mapOf(
        DeviceIdentityProvider::class to CapabilityManager.DEVICE_IDENTITY,
        LocationProvider::class to CapabilityManager.LOCATION_SPOOF,
        SensorProvider::class to CapabilityManager.SENSORS_SPOOF,
        NetworkProvider::class to CapabilityManager.NETWORK_ISOLATION,
        PackageVisibilityProvider::class to CapabilityManager.PACKAGE_VISIBILITY,
        GoogleServicesProvider::class to CapabilityManager.GOOGLE_SERVICES,
    )

    /** Google services default for the PoC: no GMS of any kind. */
    val defaultGmsMode: GmsMode = GmsMode.NONE
}
