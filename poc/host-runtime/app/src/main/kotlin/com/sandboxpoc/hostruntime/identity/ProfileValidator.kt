package com.sandboxpoc.hostruntime.identity

import com.sandboxpoc.hostruntime.profile.DeviceProfile
import com.sandboxpoc.hostruntime.profile.SpoofProfile

/**
 * Verifies profile coherence. Returns a list of violation descriptions —
 * empty means the profile is coherent.
 *
 * - [validate]: row-level checks for a [DeviceProfile] (fingerprint segments
 *   match brand/product/device/version, apiLevel matches the version map).
 * - [validateSpoof]: full checks for the per-identity [SpoofProfile]
 *   (device coherence via [validate], plus location ranges, sensor types,
 *   network and telephony formats).
 * - [validateDeviceTable]: whole-table checks — API 33/34 only, fingerprint
 *   version segment and buildId coherent with the row, no duplicate
 *   fingerprints.
 *
 * Called on every generate/reset before the profile is persisted; the
 * identity is rejected if violations exist.
 */
object ProfileValidator {

    private val ANDROID_ID_RE = Regex("^[0-9a-f]{16}$")
    private val UUID_RE =
        Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private val SUBSCRIBER_ID_RE = Regex("^[0-9]{15}$")
    private val BSSID_RE = Regex("^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$")
    private val SECURITY_PATCH_RE = Regex("^\\d{4}-\\d{2}-\\d{2}$")
    private val KERNEL_RE = Regex("^\\d+\\.\\d+\\.\\d+(-android\\d+)?(-\\d+)*(-g[0-9a-f]+)?$")

    fun validate(profile: DeviceProfile): List<String> {
        val violations = mutableListOf<String>()

        if (profile.manufacturer.isBlank()) violations += "manufacturer is blank"
        if (profile.brand.isBlank()) violations += "brand is blank"
        if (profile.model.isBlank()) violations += "model is blank"
        if (profile.device.isBlank()) violations += "device is blank"
        if (profile.product.isBlank()) violations += "product is blank"
        if (profile.board.isBlank()) violations += "board is blank"
        if (profile.hardware.isBlank()) violations += "hardware is blank"
        if (profile.buildId.isBlank()) violations += "buildId is blank"
        if (profile.securityPatch != "unknown" && !SECURITY_PATCH_RE.matches(profile.securityPatch)) {
            violations += "securityPatch '${profile.securityPatch}' is not YYYY-MM-DD"
        }

        if (!ANDROID_ID_RE.matches(profile.androidId) && profile.androidId != "template") {
            violations += "androidId '${profile.androidId}' is not 16 lowercase hex chars"
        }

        val expectedApi = DeviceProfile.API_FOR_ANDROID_VERSION[profile.androidVersion]
        when {
            expectedApi == null ->
                violations += "androidVersion '${profile.androidVersion}' has no known API mapping"
            expectedApi != profile.apiLevel ->
                violations += "apiLevel ${profile.apiLevel} does not match androidVersion " +
                    "'${profile.androidVersion}' (expected $expectedApi)"
        }

        // Fingerprint format: <brand>/<product>/<device>:<version>/<buildId>/<incremental>:<type>/<tags>
        val fp = profile.fingerprint
        val fpLower = fp.lowercase()
        val expectedPrefix =
            "${profile.brand.lowercase()}/${profile.product.lowercase()}/${profile.device.lowercase()}:"
        if (!fpLower.startsWith(expectedPrefix)) {
            violations += "fingerprint '$fp' does not start with " +
                "'<brand>/<product>/<device>:' (expected '$expectedPrefix')"
        }
        if (!profile.manufacturer.lowercase().contains(profile.brand.lowercase()) &&
            !profile.brand.lowercase().contains(profile.manufacturer.lowercase())
        ) {
            violations += "manufacturer '${profile.manufacturer}' and brand " +
                "'${profile.brand}' look unrelated"
        }
        if (!fp.contains(profile.buildId)) {
            violations += "fingerprint does not contain buildId '${profile.buildId}'"
        }
        if (!fp.contains(":${profile.androidVersion}/")) {
            violations += "fingerprint does not reference androidVersion '${profile.androidVersion}'"
        }
        if (!fp.endsWith("release-keys") && !fp.endsWith("test-keys") && !fp.endsWith("dev-keys")) {
            violations += "fingerprint has unexpected key tag: '$fp'"
        }

        return violations
    }

    /** Full coherence check for the per-identity [SpoofProfile]. */
    fun validateSpoof(sp: SpoofProfile): List<String> {
        val violations = mutableListOf<String>()

        if (!UUID_RE.matches(sp.profileId)) {
            violations += "profileId '${sp.profileId}' is not a UUID"
        }
        if (sp.generatedAt <= 0) {
            violations += "generatedAt ${sp.generatedAt} is not a positive epoch-millis value"
        }

        // Device coherence: project onto DeviceProfile and reuse the
        // row-level checks (fingerprint segments, version/apiLevel mapping).
        violations += validate(
            DeviceProfile(
                manufacturer = sp.device.manufacturer,
                brand = sp.device.brand,
                model = sp.device.model,
                device = sp.device.device,
                product = sp.device.product,
                board = sp.device.board,
                hardware = sp.device.hardware,
                fingerprint = sp.device.fingerprint,
                buildId = sp.device.buildId,
                buildTags = sp.device.buildTags,
                buildType = sp.device.buildType,
                androidVersion = sp.device.androidVersion,
                apiLevel = sp.device.apiLevel,
                securityPatch = sp.device.securityPatch,
                androidId = sp.androidId,
            ),
        )

        // New spoof surface (2026-10-02): display / incremental / kernel.
        val dev = sp.device
        if (dev.displayId.isBlank()) violations += "device.displayId is blank"
        if (dev.buildIncremental.isBlank()) {
            violations += "device.buildIncremental is blank"
        } else if (!dev.fingerprint.contains("/${dev.buildIncremental}:")) {
            violations += "device.buildIncremental '${dev.buildIncremental}' is not the " +
                "fingerprint's incremental segment"
        }
        if (!KERNEL_RE.matches(dev.kernelVersion)) {
            violations += "device.kernelVersion '${dev.kernelVersion}' is not a plausible kernel version"
        }

        // Location sanity.
        val loc = sp.location
        if (loc.latitude !in -90.0..90.0) {
            violations += "location.latitude ${loc.latitude} out of range [-90, 90]"
        }
        if (loc.longitude !in -180.0..180.0) {
            violations += "location.longitude ${loc.longitude} out of range [-180, 180]"
        }
        if (loc.accuracy < 0) violations += "location.accuracy ${loc.accuracy} is negative"
        if (loc.speed < 0) violations += "location.speed ${loc.speed} is negative"
        if (loc.bearing !in 0.0..360.0) {
            violations += "location.bearing ${loc.bearing} out of range [0, 360]"
        }
        if (loc.movement.speedMps < 0) {
            violations += "location.movement.speedMps ${loc.movement.speedMps} is negative"
        }
        if (loc.movement.bearingDeg !in 0.0..360.0) {
            violations += "location.movement.bearingDeg ${loc.movement.bearingDeg} out of range [0, 360]"
        }

        // Sensors.
        if (sp.sensors.isEmpty()) violations += "sensors list is empty"
        sp.sensors.forEachIndexed { idx, s ->
            if (s.type !in 1..40) {
                violations += "sensors[$idx].type ${s.type} is not a plausible Android sensor type"
            }
            if (s.name.isBlank()) violations += "sensors[$idx].name is blank"
            if (s.vendor.isBlank()) violations += "sensors[$idx].vendor is blank"
        }

        // Network.
        if (sp.network.ssid.isBlank()) violations += "network.ssid is blank"
        if (!BSSID_RE.matches(sp.network.bssid)) {
            violations += "network.bssid '${sp.network.bssid}' is not a MAC address"
        }
        if (sp.network.transport.isBlank()) violations += "network.transport is blank"

        // Telephony.
        if (!ANDROID_ID_RE.matches(sp.telephony.deviceId)) {
            violations += "telephony.deviceId '${sp.telephony.deviceId}' is not 16 lowercase hex chars"
        }
        if (!SUBSCRIBER_ID_RE.matches(sp.telephony.subscriberId)) {
            violations += "telephony.subscriberId '${sp.telephony.subscriberId}' is not 15 digits"
        }
        if (sp.telephony.operatorName.isBlank()) violations += "telephony.operatorName is blank"
        if (!sp.telephony.operatorNumeric.all { it.isDigit() }) {
            violations += "telephony.operatorNumeric '${sp.telephony.operatorNumeric}' is not all digits"
        }
        if (sp.telephony.countryIso.isBlank()) violations += "telephony.countryIso is blank"
        if (sp.telephony.networkType !in setOf(13, 20, 10)) {
            violations += "telephony.networkType '${sp.telephony.networkType}' is not one of LTE(13)/NR(20)/HSPA(10)"
        }

        return violations
    }

    /**
     * Whole-table checks for [DeviceProfile.DEVICE_TABLE]: every row passes
     * [validate], apiLevel is 33 or 34 only, the fingerprint's version
     * segment matches the row's androidVersion, the buildId sits inside the
     * fingerprint, and no two rows share a fingerprint.
     */
    fun validateDeviceTable(): List<String> {
        val violations = mutableListOf<String>()
        val seen = mutableSetOf<String>()
        for (row in DeviceProfile.DEVICE_TABLE) {
            violations += validate(row).map { "${row.model}: $it" }
            if (row.apiLevel != 33 && row.apiLevel != 34) {
                violations += "${row.model}: apiLevel ${row.apiLevel} not in (33, 34)"
            }
            val versionSeg = row.fingerprint.substringAfter(":").substringBefore("/")
            if (versionSeg != row.androidVersion) {
                violations += "${row.model}: fingerprint :$versionSeg/ != androidVersion ${row.androidVersion}"
            }
            if (!row.fingerprint.contains("/${row.buildId}/")) {
                violations += "${row.model}: buildId '${row.buildId}' not inside fingerprint"
            }
            if (!seen.add(row.fingerprint)) {
                violations += "${row.model}: duplicate fingerprint"
            }
        }
        return violations
    }
}
