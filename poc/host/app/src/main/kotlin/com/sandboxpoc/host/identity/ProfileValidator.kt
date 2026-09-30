package com.sandboxpoc.host.identity

import com.sandboxpoc.host.profile.DeviceProfile

/**
 * Verifies profile coherence. Returns a list of violation descriptions —
 * empty means the profile is coherent. Called on every generate/reset before
 * the profile is persisted; the identity is rejected if violations exist.
 */
object ProfileValidator {

    private val ANDROID_ID_RE = Regex("^[0-9a-f]{16}$")

    fun validate(profile: DeviceProfile): List<String> {
        val violations = mutableListOf<String>()

        if (profile.manufacturer.isBlank()) violations += "manufacturer is blank"
        if (profile.brand.isBlank()) violations += "brand is blank"
        if (profile.model.isBlank()) violations += "model is blank"
        if (profile.device.isBlank()) violations += "device is blank"
        if (profile.product.isBlank()) violations += "product is blank"
        if (profile.buildId.isBlank()) violations += "buildId is blank"

        if (!ANDROID_ID_RE.matches(profile.androidId)) {
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

        // Fingerprint format: <brand>/<product>/<device>:<version>/<buildId>/…
        val fp = profile.fingerprint
        if (!fp.lowercase().contains(profile.brand.lowercase())) {
            violations += "fingerprint does not contain brand token '${profile.brand}'"
        }
        if (!fp.lowercase().contains(profile.device.lowercase())) {
            violations += "fingerprint does not contain device token '${profile.device}'"
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
}
