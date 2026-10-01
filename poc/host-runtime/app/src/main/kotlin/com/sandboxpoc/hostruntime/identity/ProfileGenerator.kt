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
 * - location: random city base point per identity + small random jitter,
 *   movement disabled; city, country and operator are picked together so
 *   the spoofed location always matches the spoofed carrier's country
 * - sensors: list consistent with the device class (real Android sensor
 *   type ints; vendor/name strings plausible per manufacturer)
 * - network: random plausible Wi-Fi SSID + random locally-administered
 *   BSSID per identity
 * - telephony: fresh deviceId (16 hex) + subscriberId (15 digits, MCC
 *   prefix matching the operator) per identity; operator picked at random
 *   from real carriers in the locale's country
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

    // Virtual locale pool: each entry keeps city, country and carriers
    // together so a profile never claims e.g. a US carrier while standing
    // in Mumbai. One locale is picked per identity.
    private data class OperatorSpec(val name: String, val numeric: String)
    private data class LocaleSpec(
        val city: String,
        val latitude: Double,
        val longitude: Double,
        val countryIso: String,
        val operators: List<OperatorSpec>,
    )

    private val LOCALES = listOf(
        LocaleSpec("San Francisco", 37.7749, -122.4194, "us", listOf(
            OperatorSpec("T-Mobile", "310260"),
            OperatorSpec("Verizon", "311480"),
            OperatorSpec("AT&T", "310410"),
        )),
        LocaleSpec("New York", 40.7128, -74.0060, "us", listOf(
            OperatorSpec("T-Mobile", "310260"),
            OperatorSpec("Verizon", "311480"),
            OperatorSpec("AT&T", "310410"),
        )),
        LocaleSpec("London", 51.5074, -0.1278, "gb", listOf(
            OperatorSpec("EE", "23430"),
            OperatorSpec("O2", "23410"),
            OperatorSpec("Vodafone", "23415"),
        )),
        LocaleSpec("Berlin", 52.5200, 13.4050, "de", listOf(
            OperatorSpec("Telekom", "26201"),
            OperatorSpec("Vodafone", "26202"),
            OperatorSpec("O2", "26203"),
        )),
        LocaleSpec("Mumbai", 19.0760, 72.8777, "in", listOf(
            OperatorSpec("Airtel", "40410"),
            OperatorSpec("Vi", "40484"),
        )),
        LocaleSpec("Singapore", 1.3521, 103.8198, "sg", listOf(
            OperatorSpec("Singtel", "52501"),
            OperatorSpec("StarHub", "52505"),
        )),
        LocaleSpec("Tokyo", 35.6762, 139.6503, "jp", listOf(
            OperatorSpec("NTT DoCoMo", "44010"),
            OperatorSpec("SoftBank", "44020"),
        )),
        LocaleSpec("Sydney", -33.8688, 151.2093, "au", listOf(
            OperatorSpec("Telstra", "50501"),
            OperatorSpec("Optus", "50502"),
        )),
        LocaleSpec("Toronto", 43.6532, -79.3832, "ca", listOf(
            OperatorSpec("Rogers", "302720"),
            OperatorSpec("Bell", "302610"),
        )),
        LocaleSpec("Paris", 48.8566, 2.3522, "fr", listOf(
            OperatorSpec("Orange", "20801"),
            OperatorSpec("SFR", "20810"),
        )),
    )

    // Plausible generic SSID stems; a random suffix is appended per identity.
    private val SSID_STEMS = listOf(
        "HomeNet", "Home_WiFi", "FiberNet", "CoffeeShop", "Linksys",
        "NETGEAR", "TP-Link", "xfinitywifi", "MySpectrumWiFi", "GuestNet",
        "OfficeWiFi", "Hotel_Guest", "Airport_Free", "CafeConnect",
    )
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
        val locale = LOCALES[random.nextInt(LOCALES.size)]
        val operator = locale.operators[random.nextInt(locale.operators.size)]
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
            location = newLocation(locale),
            sensors = sensorsFor(row),
            network = SpoofProfile.NetworkInfo(
                ssid = newSsid(),
                bssid = newBssid(),
                transport = "WIFI",
            ),
            telephony = SpoofProfile.TelephonyInfo(
                operatorName = operator.name,
                operatorNumeric = operator.numeric,
                countryIso = locale.countryIso,
                deviceId = newAndroidId(),
                subscriberId = newSubscriberId(operator.numeric.take(3)),
            ),
        )
    }

    /** Fresh 16-char lowercase hex ANDROID_ID. */
    fun newAndroidId(): String =
        CharArray(16) { HEX[random.nextInt(16)] }.concatToString()

    /** Fresh 15-digit IMSI-shaped subscriber ID, MCC prefix of the operator. */
    fun newSubscriberId(mcc: String = ""): String {
        val tail = CharArray(15 - mcc.length) { DIGITS[random.nextInt(10)] }.concatToString()
        return mcc + tail
    }

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

    /** Virtual location: locale city base point + per-profile jitter; movement disabled. */
    private fun newLocation(locale: LocaleSpec): SpoofProfile.LocationInfo =
        SpoofProfile.LocationInfo(
            latitude = locale.latitude + (random.nextDouble() - 0.5) * JITTER_DEGREES,
            longitude = locale.longitude + (random.nextDouble() - 0.5) * JITTER_DEGREES,
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

    /** Random plausible SSID per identity (quoted, as Android reports it). */
    private fun newSsid(): String {
        val stem = SSID_STEMS[random.nextInt(SSID_STEMS.size)]
        val suffix = if (random.nextBoolean()) "_5G"
        else "-" + CharArray(4) { "0123456789ABCDEF"[random.nextInt(16)] }.concatToString()
        return "\"$stem$suffix\""
    }

    /** Random locally-administered BSSID per identity (02:xx:xx:xx:xx:xx). */
    private fun newBssid(): String {
        val bytes = ByteArray(5).also { random.nextBytes(it) }
        return "02:" + bytes.joinToString(":") { "%02X".format(it.toInt() and 0xFF) }
    }

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
