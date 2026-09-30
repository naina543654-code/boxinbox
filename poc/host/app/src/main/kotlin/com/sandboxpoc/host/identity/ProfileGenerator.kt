package com.sandboxpoc.host.identity

import com.sandboxpoc.host.profile.DeviceProfile
import java.security.SecureRandom

/**
 * Generates coherent spoofed device profiles. Coherence strategy: pick one
 * row from [DeviceProfile.DEVICE_TABLE] (whose fields are mutually
 * consistent — manufacturer/brand/model/device/product/fingerprint/buildId/
 * androidVersion/apiLevel all belong together) and regenerate only the
 * per-identity fields:
 * - androidId: fresh 16 lowercase hex chars every time
 * - extras["build_number_suffix"]: fresh random suffix recorded for audit
 *
 * Profiles are never built by randomizing fields independently.
 */
object ProfileGenerator {

    private val random = SecureRandom()
    private val HEX = "0123456789abcdef".toCharArray()

    fun newProfile(): DeviceProfile {
        val table = DeviceProfile.DEVICE_TABLE
        val base = table[random.nextInt(table.size)]
        return base.copy(
            androidId = newAndroidId(),
            extras = base.extras + mapOf("build_number_suffix" to randomSuffix()),
        )
    }

    /** Fresh 16-char lowercase hex ANDROID_ID. */
    fun newAndroidId(): String =
        CharArray(16) { HEX[random.nextInt(16)] }.concatToString()

    private fun randomSuffix(): String =
        CharArray(6) { HEX[random.nextInt(16)] }.concatToString()
}
