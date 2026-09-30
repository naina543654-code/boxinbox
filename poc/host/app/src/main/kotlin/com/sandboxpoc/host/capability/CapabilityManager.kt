package com.sandboxpoc.host.capability

/**
 * Registry of every virtualization capability. Every entry defaults to
 * UNVERIFIED; the engine adapter flips states to SUPPORTED (etc.) only after
 * it has actually verified the feature against the engine. The UI renders
 * these states directly — unsupported is explicit, never a silent host
 * fallback.
 */
class CapabilityManager {

    companion object {
        const val DEVICE_IDENTITY = "device.identity"
        const val TELEPHONY_SPOOF = "telephony.spoof"
        const val LOCATION_SPOOF = "location.spoof"
        const val SENSORS_SPOOF = "sensors.spoof"
        const val NETWORK_ISOLATION = "network.isolation"
        const val PACKAGE_VISIBILITY = "package.visibility"
        const val GOOGLE_SERVICES = "gms.mode"
        const val APP_INSTALL = "app.install"
        const val APP_LAUNCH = "app.launch"
        const val APP_UNINSTALL = "app.uninstall"
        const val STORAGE_ISOLATION = "storage.isolation"
        const val CLIPBOARD_ISOLATION = "clipboard.isolation"
    }

    private val capabilities: MutableMap<String, Capability> = linkedMapOf(
        DEVICE_IDENTITY to Capability(DEVICE_IDENTITY, "Device identity spoofing",
            detail = "Build fields + ANDROID_ID presented to guests"),
        TELEPHONY_SPOOF to Capability(TELEPHONY_SPOOF, "Telephony spoofing",
            detail = "SIM / phone-number / operator identity"),
        LOCATION_SPOOF to Capability(LOCATION_SPOOF, "Location spoofing",
            detail = "Virtual GPS / network location for guests"),
        SENSORS_SPOOF to Capability(SENSORS_SPOOF, "Sensor spoofing",
            detail = "Accelerometer / gyro / etc. presented to guests"),
        NETWORK_ISOLATION to Capability(NETWORK_ISOLATION, "Network isolation",
            detail = "Guest traffic routing / isolation policy"),
        PACKAGE_VISIBILITY to Capability(PACKAGE_VISIBILITY, "Package visibility",
            detail = "Which packages guests can see"),
        GOOGLE_SERVICES to Capability(GOOGLE_SERVICES, "Google services mode",
            detail = "NONE / microG / host GMS / custom"),
        APP_INSTALL to Capability(APP_INSTALL, "Guest app install",
            detail = "Install APK into the virtual runtime"),
        APP_LAUNCH to Capability(APP_LAUNCH, "Guest app launch",
            detail = "Launch apps inside the virtual runtime"),
        APP_UNINSTALL to Capability(APP_UNINSTALL, "Guest app uninstall",
            detail = "Remove guest apps from the virtual runtime"),
        STORAGE_ISOLATION to Capability(STORAGE_ISOLATION, "Storage isolation",
            detail = "Guest state separated from host private data"),
        CLIPBOARD_ISOLATION to Capability(CLIPBOARD_ISOLATION, "Clipboard isolation",
            detail = "Clipboard boundary between host and guests"),
    )

    fun all(): List<Capability> = capabilities.values.toList()

    fun get(id: String): Capability? = capabilities[id]

    /**
     * Engine adapter only: mark a capability's real state after verifying it
     * against the engine. Host code must not call this speculatively.
     */
    fun setState(id: String, state: CapabilityState, detail: String = "") {
        val current = capabilities[id] ?: return
        capabilities[id] = current.copy(state = state, detail = detail.ifEmpty { current.detail })
    }
}
