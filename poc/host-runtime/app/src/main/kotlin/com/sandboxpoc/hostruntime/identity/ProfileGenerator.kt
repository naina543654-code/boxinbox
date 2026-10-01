package com.sandboxpoc.hostruntime.identity

import android.os.Build
import com.sandboxpoc.hostruntime.profile.DeviceProfile
import com.sandboxpoc.hostruntime.profile.SpoofProfile
import java.security.SecureRandom
import java.util.UUID

/**
 * Generates coherent spoofed device profiles. Coherence strategy: pick one
 * row from [DeviceProfile.DEVICE_TABLE] (whose fields are mutually
 * consistent — manufacturer/brand/model/device/product/board/hardware/
 * fingerprint/buildId/securityPatch/androidVersion/apiLevel all belong
 * together) and regenerate only the per-identity fields:
 * - profileId: the identity's UUID (one active identity per V1 scope)
 * - generatedAt: epoch millis at generation time
 * - androidId: fresh 16 lowercase hex chars every time
 * - location: fixed virtual base point + small random jitter per profile,
 *   movement disabled
 * - sensors: list consistent with the device class (real Android sensor
 *   type ints; vendor/name strings plausible per manufacturer)
 * - network: fixed virtual Wi-Fi identity ("SandboxNet")
 * - telephony: fresh deviceId (16 hex) + subscriberId (15 digits) per
 *   identity; operator fields fixed to the contract values
 *
 * Profiles are never built by randomizing device fields independently.
 * Row preference: rows whose apiLevel matches the host device's
 * [Build.VERSION.SDK_INT]; falls back to the nearest apiLevel row.
 *
 * Engine-independent: no imports from any engine package (top.niunaijun.*).
 */
object ProfileGenerator {

    private val random = SecureRandom()
    private val HEX = "0123456789abcdef".toCharArray()
    private val DIGITS = "0123456789".toCharArray()

    // Virtual base location (San Francisco); per-profile jitter is applied.
    private const val BASE_LATITUDE = 37.7749
    private const val BASE_LONGITUDE = -122.4194
    /** Jitter range in degrees (~±550 m) applied per profile. */
    private const val JITTER_DEGREES = 0.01

    /** Real Android sensor type ints used in generated profiles. */
    object SensorTypes {
        const val ACCELEROMETER = 1
        const val MAGNETIC_FIELD = 2
        const val GYROSCOPE = 4
        const val LIGHT = 5
        const val PROXIMITY = 8
        const val ROTATION_VECTOR = 11
    }

    /**
     * Builds a full [SpoofProfile] from a coherent device-table row plus
     * fresh per-identity values.
     *
     * @param avoidFingerprint when resetting, the previous identity's
     * device fingerprint — the generator re-rolls so the new identity is
     * never the same device model twice in a row.
     */
    fun newProfile(
        profileId: String = UUID.randomUUID().toString(),
        avoidFingerprint: String? = null,
    ): SpoofProfile {
        val row = pickRow(avoidFingerprint)
        val device = SpoofProfile.DeviceInfo(
            manufacturer = row.manufacturer,
            brand = row.brand,
            model = row.model,
            device = row.device,
            product = row.product,
            board = row.board,
            hardware = row.hardware,
            fingerprint = row.fingerprint,
            buildId = row.buildId,
            buildTags = row.buildTags,
            buildType = row.buildType,
            androidVersion = row.androidVersion,
            apiLevel = row.apiLevel,
            securityPatch = row.securityPatch,
        )
        return SpoofProfile(
            profileId = profileId,
            generatedAt = System.currentTimeMillis(),
            device = device,
            androidId = newAndroidId(),
            location = newLocation(),
            sensors = sensorsFor(row),
            network = SpoofProfile.NetworkInfo(
                ssid = "\"SandboxNet\"",
                bssid = "02:15:3E:4A:5B:6C",
                transport = "WIFI",
            ),
            telephony = SpoofProfile.TelephonyInfo(
                operatorName = "T-Mobile",
                operatorNumeric = "310260",
                countryIso = "us",
                deviceId = newAndroidId(),
                subscriberId = newSubscriberId(),
            ),
        )
    }

    /** Fresh 16-char lowercase hex ANDROID_ID. */
    fun newAndroidId(): String =
        CharArray(16) { HEX[random.nextInt(16)] }.concatToString()

    /** Fresh 15-digit IMSI-shaped subscriber ID. */
    fun newSubscriberId(): String =
        CharArray(15) { DIGITS[random.nextInt(10)] }.concatToString()

    /**
     * Prefers device rows whose apiLevel equals the host's SDK_INT (so the
     * spoofed device plausibly matches the OS the guest actually runs on);
     * falls back to a random row among those with the nearest apiLevel.
     * The fallback MUST randomize: picking the first nearest row made every
     * identity on an API-33 host a Pixel 8 (first API-34 row in the table).
     */
    private fun pickRow(avoidFingerprint: String? = null): DeviceProfile {
        val table = DeviceProfile.DEVICE_TABLE
        val hostApi = Build.VERSION.SDK_INT
        val exact = table.filter { it.apiLevel == hostApi }
        if (exact.isNotEmpty()) return distinctRandom(exact, avoidFingerprint)
        val nearest = table.minOf { kotlin.math.abs(it.apiLevel - hostApi) }
        val candidates = table.filter { kotlin.math.abs(it.apiLevel - hostApi) == nearest }
        return distinctRandom(candidates, avoidFingerprint)
    }

    /**
     * Random pick that avoids re-rolling the previous identity's device
     * model (so generate/reset never hands back the same phone twice in a
     * row). Falls back to the full pool when every candidate is excluded.
     */
    private fun distinctRandom(
        candidates: List<DeviceProfile>,
        avoidFingerprint: String?,
    ): DeviceProfile {
        val pool = if (avoidFingerprint != null) {
            candidates.filter { it.fingerprint != avoidFingerprint }.ifEmpty { candidates }
        } else {
            candidates
        }
        return pool[random.nextInt(pool.size)]
    }

    /** Virtual location: base point + per-profile jitter; movement disabled. */
    private fun newLocation(): SpoofProfile.LocationInfo =
        SpoofProfile.LocationInfo(
            latitude = BASE_LATITUDE + (random.nextDouble() - 0.5) * JITTER_DEGREES,
            longitude = BASE_LONGITUDE + (random.nextDouble() - 0.5) * JITTER_DEGREES,
            accuracy = 15.0,
            altitude = 25.0,
            speed = 0.0,
            bearing = 0.0,
            movement = SpoofProfile.Movement(
                enabled = false,
                speedMps = 1.4,
                bearingDeg = 90.0,
            ),
        )

    /** Plausible vendor/name strings for the sensor set, per manufacturer. */
    private data class SensorVendorStrings(
        val accelVendor: String,
        val accelName: String,
        val gyroVendor: String,
        val gyroName: String,
        val magVendor: String,
        val magName: String,
    )

    private fun vendorStringsFor(row: DeviceProfile): SensorVendorStrings =
        when (row.manufacturer.lowercase()) {
            "samsung" -> SensorVendorStrings(
                accelVendor = "STMicroelectronics", accelName = "LSM6DSO Accelerometer",
                gyroVendor = "STMicroelectronics", gyroName = "LSM6DSO Gyroscope",
                magVendor = "Asahi Kasei", magName = "AK09918C Magnetometer",
            )
            "google" -> SensorVendorStrings(
                accelVendor = "Google Inc.", accelName = "BMI323 Accelerometer",
                gyroVendor = "Google Inc.", gyroName = "BMI323 Gyroscope",
                magVendor = "Asahi Kasei", magName = "AK09918 Magnetometer",
            )
            else -> SensorVendorStrings(
                accelVendor = "Bosch", accelName = "BMI260 Accelerometer",
                gyroVendor = "Bosch", gyroName = "BMI260 Gyroscope",
                magVendor = "Bosch", magName = "BMM150 Magnetometer",
            )
        }

    /** Sensor list consistent with a flagship-class device. */
    private fun sensorsFor(row: DeviceProfile): List<SpoofProfile.SensorInfo> {
        val v = vendorStringsFor(row)
        return listOf(
            SpoofProfile.SensorInfo(SensorTypes.ACCELEROMETER, v.accelName, v.accelVendor),
            SpoofProfile.SensorInfo(SensorTypes.GYROSCOPE, v.gyroName, v.gyroVendor),
            SpoofProfile.SensorInfo(SensorTypes.MAGNETIC_FIELD, v.magName, v.magVendor),
            SpoofProfile.SensorInfo(SensorTypes.ROTATION_VECTOR, "Rotation Vector", v.accelVendor),
            SpoofProfile.SensorInfo(SensorTypes.PROXIMITY, "Proximity Sensor", v.accelVendor),
            SpoofProfile.SensorInfo(SensorTypes.LIGHT, "Light Sensor", v.accelVendor),
        )
    }
}
