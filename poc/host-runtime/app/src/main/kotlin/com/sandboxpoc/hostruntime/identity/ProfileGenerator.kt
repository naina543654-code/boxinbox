package com.sandboxpoc.hostruntime.identity

import android.os.Build
import android.telephony.TelephonyManager
import com.sandboxpoc.hostruntime.profile.DeviceProfile
import com.sandboxpoc.hostruntime.profile.SpoofProfile
import com.sandboxpoc.hostruntime.profile.deriveDisplayId
import com.sandboxpoc.hostruntime.profile.deriveIncremental
import com.sandboxpoc.hostruntime.profile.kernelForApi
import com.sandboxpoc.hostruntime.profile.deriveBuildTime
import com.sandboxpoc.hostruntime.profile.socFor
import com.sandboxpoc.hostruntime.profile.buildWebViewUa
import com.sandboxpoc.hostruntime.profile.timezoneFor
import com.sandboxpoc.hostruntime.profile.localeFor
import com.sandboxpoc.hostruntime.profile.newLocalMac
import com.sandboxpoc.hostruntime.profile.newPhoneNumber
import com.sandboxpoc.hostruntime.profile.newSimSerial
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

    // Carrier pool per country (real MCC/MNC assignments). Jio (405872) is
    // deliberately excluded: it is the host owner's real carrier, and
    // spoofing it would correlate sandbox identities with the real device.
    private data class OperatorSpec(val name: String, val numeric: String)

    private val CARRIERS: Map<String, List<OperatorSpec>> = mapOf(
        "us" to listOf(OperatorSpec("T-Mobile", "310260"), OperatorSpec("Verizon", "311480"), OperatorSpec("AT&T", "310410")),
        "gb" to listOf(OperatorSpec("EE", "23430"), OperatorSpec("O2", "23410"), OperatorSpec("Vodafone", "23415")),
        "de" to listOf(OperatorSpec("Telekom", "26201"), OperatorSpec("Vodafone", "26202"), OperatorSpec("O2", "26203")),
        "fr" to listOf(OperatorSpec("Orange", "20801"), OperatorSpec("SFR", "20810"), OperatorSpec("Bouygues", "20820")),
        "it" to listOf(OperatorSpec("TIM", "22201"), OperatorSpec("Vodafone", "22210"), OperatorSpec("WindTre", "22288")),
        "es" to listOf(OperatorSpec("Movistar", "21407"), OperatorSpec("Vodafone", "21401"), OperatorSpec("Orange", "21403")),
        "nl" to listOf(OperatorSpec("KPN", "20408"), OperatorSpec("Vodafone", "20404"), OperatorSpec("T-Mobile", "20416")),
        "be" to listOf(OperatorSpec("Proximus", "20601"), OperatorSpec("Orange", "20610"), OperatorSpec("Telenet", "20605")),
        "ch" to listOf(OperatorSpec("Swisscom", "22801"), OperatorSpec("Sunrise", "22802"), OperatorSpec("Salt", "22803")),
        "at" to listOf(OperatorSpec("A1", "23201"), OperatorSpec("Magenta", "23203"), OperatorSpec("Drei", "23205")),
        "se" to listOf(OperatorSpec("Telia", "24001"), OperatorSpec("Tele2", "24007"), OperatorSpec("Telenor", "24008")),
        "no" to listOf(OperatorSpec("Telenor", "24201"), OperatorSpec("Telia", "24202")),
        "dk" to listOf(OperatorSpec("TDC", "23801"), OperatorSpec("Telenor", "23802"), OperatorSpec("Telia", "23820")),
        "fi" to listOf(OperatorSpec("Elisa", "24405"), OperatorSpec("DNA", "24412"), OperatorSpec("Telia", "24491")),
        "pl" to listOf(OperatorSpec("Orange", "26003"), OperatorSpec("Play", "26006"), OperatorSpec("Plus", "26001")),
        "ie" to listOf(OperatorSpec("Vodafone", "27201"), OperatorSpec("Three", "27205"), OperatorSpec("Eir", "27203")),
        "pt" to listOf(OperatorSpec("MEO", "26806"), OperatorSpec("NOS", "26803"), OperatorSpec("Vodafone", "26801")),
        "gr" to listOf(OperatorSpec("Cosmote", "20201"), OperatorSpec("Vodafone", "20205"), OperatorSpec("Wind", "20210")),
        "cz" to listOf(OperatorSpec("O2", "23002"), OperatorSpec("T-Mobile", "23001"), OperatorSpec("Vodafone", "23003")),
        "hu" to listOf(OperatorSpec("Telekom", "21630"), OperatorSpec("Yettel", "21601"), OperatorSpec("Vodafone", "21670")),
        "ro" to listOf(OperatorSpec("Orange", "22610"), OperatorSpec("Vodafone", "22601")),
        "ua" to listOf(OperatorSpec("Kyivstar", "25503"), OperatorSpec("Vodafone", "25501"), OperatorSpec("lifecell", "25506")),
        "tr" to listOf(OperatorSpec("Turkcell", "28601"), OperatorSpec("Vodafone", "28602"), OperatorSpec("Turk Telekom", "28603")),
        "in" to listOf(OperatorSpec("Airtel", "40410"), OperatorSpec("Vi", "40484")),
        "pk" to listOf(OperatorSpec("Jazz", "41001"), OperatorSpec("Telenor", "41006"), OperatorSpec("Zong", "41004")),
        "bd" to listOf(OperatorSpec("Grameenphone", "47001"), OperatorSpec("Robi", "47002"), OperatorSpec("Banglalink", "47003")),
        "lk" to listOf(OperatorSpec("Dialog", "41302"), OperatorSpec("Mobitel", "41301")),
        "np" to listOf(OperatorSpec("Ncell", "42902"), OperatorSpec("Nepal Telecom", "42901")),
        "sg" to listOf(OperatorSpec("Singtel", "52501"), OperatorSpec("StarHub", "52505"), OperatorSpec("M1", "52503")),
        "my" to listOf(OperatorSpec("Maxis", "50212"), OperatorSpec("Celcom", "50219"), OperatorSpec("Digi", "50216")),
        "th" to listOf(OperatorSpec("AIS", "52001"), OperatorSpec("TrueMove", "52000"), OperatorSpec("DTAC", "52018")),
        "id" to listOf(OperatorSpec("Telkomsel", "51010"), OperatorSpec("Indosat", "51001"), OperatorSpec("XL", "51011")),
        "ph" to listOf(OperatorSpec("Globe", "51502"), OperatorSpec("Smart", "51503"), OperatorSpec("DITO", "51566")),
        "vn" to listOf(OperatorSpec("Viettel", "45204"), OperatorSpec("Vinaphone", "45201"), OperatorSpec("Mobifone", "45202")),
        "kh" to listOf(OperatorSpec("Smart", "45606"), OperatorSpec("Cellcard", "45602")),
        "jp" to listOf(OperatorSpec("NTT DoCoMo", "44010"), OperatorSpec("SoftBank", "44020"), OperatorSpec("au", "44051")),
        "kr" to listOf(OperatorSpec("SK Telecom", "45005"), OperatorSpec("KT", "45008"), OperatorSpec("LG U+", "45006")),
        "cn" to listOf(OperatorSpec("China Mobile", "46000"), OperatorSpec("China Unicom", "46001"), OperatorSpec("China Telecom", "46011")),
        "hk" to listOf(OperatorSpec("CSL", "45400"), OperatorSpec("SmarTone", "45406"), OperatorSpec("3HK", "45403")),
        "tw" to listOf(OperatorSpec("Chunghwa", "46692"), OperatorSpec("Taiwan Mobile", "46697"), OperatorSpec("FarEasTone", "46601")),
        "au" to listOf(OperatorSpec("Telstra", "50501"), OperatorSpec("Optus", "50502"), OperatorSpec("Vodafone", "50503")),
        "nz" to listOf(OperatorSpec("Spark", "53005"), OperatorSpec("One NZ", "53001"), OperatorSpec("2degrees", "53024")),
        "ca" to listOf(OperatorSpec("Rogers", "302720"), OperatorSpec("Bell", "302610"), OperatorSpec("TELUS", "302660")),
        "mx" to listOf(OperatorSpec("Telcel", "334020"), OperatorSpec("AT&T", "334090"), OperatorSpec("Movistar", "334030")),
        "br" to listOf(OperatorSpec("Vivo", "72406"), OperatorSpec("Claro", "72405"), OperatorSpec("TIM", "72402")),
        "ar" to listOf(OperatorSpec("Movistar", "722010"), OperatorSpec("Personal", "72234"), OperatorSpec("Claro", "722310")),
        "cl" to listOf(OperatorSpec("Movistar", "73002"), OperatorSpec("Entel", "73001"), OperatorSpec("Claro", "73003")),
        "co" to listOf(OperatorSpec("Claro", "732101"), OperatorSpec("Movistar", "732102")),
        "pe" to listOf(OperatorSpec("Movistar", "71606"), OperatorSpec("Claro", "71610"), OperatorSpec("Entel", "71607")),
        "ae" to listOf(OperatorSpec("Etisalat", "42402"), OperatorSpec("du", "42403")),
        "sa" to listOf(OperatorSpec("STC", "42001"), OperatorSpec("Mobily", "42003"), OperatorSpec("Zain", "42004")),
        "qa" to listOf(OperatorSpec("Ooredoo", "42701"), OperatorSpec("Vodafone", "42702")),
        "il" to listOf(OperatorSpec("Cellcom", "42502"), OperatorSpec("Pelephone", "42503"), OperatorSpec("Partner", "42501")),
        "eg" to listOf(OperatorSpec("Vodafone", "60202"), OperatorSpec("Orange", "60201"), OperatorSpec("Etisalat", "60203")),
        "ma" to listOf(OperatorSpec("Maroc Telecom", "60401"), OperatorSpec("Orange", "60402"), OperatorSpec("Inwi", "60403")),
        "ng" to listOf(OperatorSpec("MTN", "62130"), OperatorSpec("Airtel", "62120"), OperatorSpec("Glo", "62150")),
        "ke" to listOf(OperatorSpec("Safaricom", "63902"), OperatorSpec("Airtel", "63903")),
        "za" to listOf(OperatorSpec("Vodacom", "65501"), OperatorSpec("MTN", "65510"), OperatorSpec("Cell C", "65507")),
        "gh" to listOf(OperatorSpec("MTN", "62001"), OperatorSpec("Vodafone", "62002"), OperatorSpec("AirtelTigo", "62006")),
    )

    // Major world cities; one is picked per identity together with a carrier
    // from its country, so location always matches the carrier's country.
    private data class CitySpec(
        val city: String,
        val latitude: Double,
        val longitude: Double,
        val countryIso: String,
    )

    private val CITIES = listOf(
        CitySpec("New York", 40.7128, -74.0060, "us"),
        CitySpec("San Francisco", 37.7749, -122.4194, "us"),
        CitySpec("Los Angeles", 34.0522, -118.2437, "us"),
        CitySpec("Chicago", 41.8781, -87.6298, "us"),
        CitySpec("Houston", 29.7604, -95.3698, "us"),
        CitySpec("Miami", 25.7617, -80.1918, "us"),
        CitySpec("Seattle", 47.6062, -122.3321, "us"),
        CitySpec("Boston", 42.3601, -71.0589, "us"),
        CitySpec("Denver", 39.7392, -104.9903, "us"),
        CitySpec("Atlanta", 33.7490, -84.3880, "us"),
        CitySpec("London", 51.5074, -0.1278, "gb"),
        CitySpec("Manchester", 53.4808, -2.2426, "gb"),
        CitySpec("Birmingham", 52.4862, -1.8904, "gb"),
        CitySpec("Edinburgh", 55.9533, -3.1883, "gb"),
        CitySpec("Berlin", 52.5200, 13.4050, "de"),
        CitySpec("Munich", 48.1351, 11.5820, "de"),
        CitySpec("Hamburg", 53.5511, 9.9937, "de"),
        CitySpec("Frankfurt", 50.1109, 8.6821, "de"),
        CitySpec("Paris", 48.8566, 2.3522, "fr"),
        CitySpec("Lyon", 45.7640, 4.8357, "fr"),
        CitySpec("Marseille", 43.2965, 5.3698, "fr"),
        CitySpec("Rome", 41.9028, 12.4964, "it"),
        CitySpec("Milan", 45.4642, 9.1900, "it"),
        CitySpec("Naples", 40.8518, 14.2681, "it"),
        CitySpec("Madrid", 40.4168, -3.7038, "es"),
        CitySpec("Barcelona", 41.3874, 2.1686, "es"),
        CitySpec("Valencia", 39.4699, -0.3763, "es"),
        CitySpec("Amsterdam", 52.3676, 4.9041, "nl"),
        CitySpec("Rotterdam", 51.9244, 4.4777, "nl"),
        CitySpec("Brussels", 50.8503, 4.3517, "be"),
        CitySpec("Antwerp", 51.2194, 4.4025, "be"),
        CitySpec("Zurich", 47.3769, 8.5417, "ch"),
        CitySpec("Geneva", 46.2044, 6.1432, "ch"),
        CitySpec("Vienna", 48.2082, 16.3738, "at"),
        CitySpec("Salzburg", 47.8095, 13.0550, "at"),
        CitySpec("Stockholm", 59.3293, 18.0686, "se"),
        CitySpec("Gothenburg", 57.7089, 11.9746, "se"),
        CitySpec("Oslo", 59.9139, 10.7522, "no"),
        CitySpec("Copenhagen", 55.6761, 12.5683, "dk"),
        CitySpec("Helsinki", 60.1699, 24.9384, "fi"),
        CitySpec("Warsaw", 52.2297, 21.0122, "pl"),
        CitySpec("Krakow", 50.0647, 19.9450, "pl"),
        CitySpec("Dublin", 53.3498, -6.2603, "ie"),
        CitySpec("Cork", 51.8985, -8.4756, "ie"),
        CitySpec("Lisbon", 38.7223, -9.1393, "pt"),
        CitySpec("Porto", 41.1579, -8.6291, "pt"),
        CitySpec("Athens", 37.9838, 23.7275, "gr"),
        CitySpec("Thessaloniki", 40.6401, 22.9444, "gr"),
        CitySpec("Prague", 50.0755, 14.4378, "cz"),
        CitySpec("Budapest", 47.4979, 19.0402, "hu"),
        CitySpec("Bucharest", 44.4268, 26.1025, "ro"),
        CitySpec("Kyiv", 50.4501, 30.5234, "ua"),
        CitySpec("Lviv", 49.8397, 24.0297, "ua"),
        CitySpec("Istanbul", 41.0082, 28.9784, "tr"),
        CitySpec("Ankara", 39.9334, 32.8597, "tr"),
        CitySpec("Mumbai", 19.0760, 72.8777, "in"),
        CitySpec("Delhi", 28.6139, 77.2090, "in"),
        CitySpec("Bangalore", 12.9716, 77.5946, "in"),
        CitySpec("Hyderabad", 17.3850, 78.4867, "in"),
        CitySpec("Chennai", 13.0827, 80.2707, "in"),
        CitySpec("Kolkata", 22.5726, 88.3639, "in"),
        CitySpec("Karachi", 24.8607, 67.0011, "pk"),
        CitySpec("Lahore", 31.5204, 74.3587, "pk"),
        CitySpec("Dhaka", 23.8103, 90.4125, "bd"),
        CitySpec("Colombo", 6.9271, 79.8612, "lk"),
        CitySpec("Kathmandu", 27.7172, 85.3240, "np"),
        CitySpec("Singapore", 1.3521, 103.8198, "sg"),
        CitySpec("Kuala Lumpur", 3.1390, 101.6869, "my"),
        CitySpec("Penang", 5.4141, 100.3288, "my"),
        CitySpec("Bangkok", 13.7563, 100.5018, "th"),
        CitySpec("Chiang Mai", 18.7883, 98.9853, "th"),
        CitySpec("Jakarta", -6.2088, 106.8456, "id"),
        CitySpec("Surabaya", -7.2575, 112.7521, "id"),
        CitySpec("Manila", 14.5995, 120.9842, "ph"),
        CitySpec("Cebu", 10.3157, 123.8854, "ph"),
        CitySpec("Ho Chi Minh City", 10.8231, 106.6297, "vn"),
        CitySpec("Hanoi", 21.0278, 105.8342, "vn"),
        CitySpec("Phnom Penh", 11.5564, 104.9282, "kh"),
        CitySpec("Tokyo", 35.6762, 139.6503, "jp"),
        CitySpec("Osaka", 34.6937, 135.5023, "jp"),
        CitySpec("Kyoto", 35.0116, 135.7681, "jp"),
        CitySpec("Fukuoka", 33.5904, 130.4017, "jp"),
        CitySpec("Seoul", 37.5665, 126.9780, "kr"),
        CitySpec("Busan", 35.1796, 129.0756, "kr"),
        CitySpec("Beijing", 39.9042, 116.4074, "cn"),
        CitySpec("Shanghai", 31.2304, 121.4737, "cn"),
        CitySpec("Guangzhou", 23.1291, 113.2644, "cn"),
        CitySpec("Shenzhen", 22.5431, 114.0579, "cn"),
        CitySpec("Hong Kong", 22.3193, 114.1694, "hk"),
        CitySpec("Taipei", 25.0330, 121.5654, "tw"),
        CitySpec("Sydney", -33.8688, 151.2093, "au"),
        CitySpec("Melbourne", -37.8136, 144.9631, "au"),
        CitySpec("Brisbane", -27.4698, 153.0251, "au"),
        CitySpec("Auckland", -36.8485, 174.7633, "nz"),
        CitySpec("Toronto", 43.6532, -79.3832, "ca"),
        CitySpec("Vancouver", 49.2827, -123.1207, "ca"),
        CitySpec("Montreal", 45.5017, -73.5673, "ca"),
        CitySpec("Calgary", 51.0447, -114.0719, "ca"),
        CitySpec("Mexico City", 19.4326, -99.1332, "mx"),
        CitySpec("Guadalajara", 20.6597, -103.3496, "mx"),
        CitySpec("Monterrey", 25.6866, -100.3161, "mx"),
        CitySpec("Sao Paulo", -23.5558, -46.6396, "br"),
        CitySpec("Rio de Janeiro", -22.9068, -43.1729, "br"),
        CitySpec("Brasilia", -15.7801, -47.9292, "br"),
        CitySpec("Buenos Aires", -34.6037, -58.3816, "ar"),
        CitySpec("Santiago", -33.4489, -70.6693, "cl"),
        CitySpec("Bogota", 4.7110, -74.0721, "co"),
        CitySpec("Lima", -12.0464, -77.0428, "pe"),
        CitySpec("Dubai", 25.2048, 55.2708, "ae"),
        CitySpec("Abu Dhabi", 24.4539, 54.3773, "ae"),
        CitySpec("Riyadh", 24.7136, 46.6753, "sa"),
        CitySpec("Jeddah", 21.4858, 39.1925, "sa"),
        CitySpec("Doha", 25.2854, 51.5310, "qa"),
        CitySpec("Tel Aviv", 32.0853, 34.7818, "il"),
        CitySpec("Cairo", 30.0444, 31.2357, "eg"),
        CitySpec("Casablanca", 33.5731, -7.5898, "ma"),
        CitySpec("Lagos", 6.5244, 3.3792, "ng"),
        CitySpec("Nairobi", -1.2921, 36.8219, "ke"),
        CitySpec("Johannesburg", -26.2041, 28.0473, "za"),
        CitySpec("Cape Town", -33.9249, 18.4241, "za"),
        CitySpec("Accra", 5.6037, -0.1870, "gh"),
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
        val city = CITIES[random.nextInt(CITIES.size)]
        val operators = CARRIERS[city.countryIso] ?: CARRIERS.getValue("us")
        val operator = operators[random.nextInt(operators.size)]
        val incremental = deriveIncremental(row.fingerprint)
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
            displayId = deriveDisplayId(row.brand, row.buildId, incremental),
            buildIncremental = incremental,
            kernelVersion = kernelForApi(row.apiLevel),
            buildTime = deriveBuildTime(row.securityPatch),
            buildUser = "android-build",
            buildHost = "abfarm",
            bootloader = "unknown",
            radio = "unknown",
            socManufacturer = socFor(row.hardware, row.model).first,
            socModel = socFor(row.hardware, row.model).second,
            webViewUa = buildWebViewUa(row.androidVersion, row.model, row.buildId),
        )
        return SpoofProfile(
            profileId = profileId,
            generatedAt = System.currentTimeMillis(),
            device = device,
            androidId = newAndroidId(),
            location = newLocation(city),
            sensors = sensorsFor(row),
            network = SpoofProfile.NetworkInfo(
                ssid = newSsid(),
                bssid = newBssid(),
                transport = "WIFI",
                wifiMac = newLocalMac(),
                bluetoothMac = newLocalMac(),
            ),
            telephony = SpoofProfile.TelephonyInfo(
                operatorName = operator.name,
                operatorNumeric = operator.numeric,
                countryIso = city.countryIso,
                deviceId = newAndroidId(),
                subscriberId = newSubscriberId(operator.numeric.take(3)),
                networkType = newNetworkType(),
                simSerial = newSimSerial(),
                phoneNumber = newPhoneNumber(city.countryIso),
            ),
            locale = SpoofProfile.LocaleInfo(
                timezoneId = timezoneFor(city.countryIso),
                localeTag = localeFor(city.countryIso),
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
     * Per-identity radio network type, weighted toward what real phones
     * report: mostly LTE, sometimes NR (5G), occasionally HSPA.
     */
    fun newNetworkType(): Int {
        return when (random.nextInt(100)) {
            in 0 until 60 -> TelephonyManager.NETWORK_TYPE_LTE
            in 60 until 85 -> TelephonyManager.NETWORK_TYPE_NR
            else -> TelephonyManager.NETWORK_TYPE_HSPA
        }
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

    /** Virtual location: city base point + per-profile jitter; movement disabled. */
    private fun newLocation(city: CitySpec): SpoofProfile.LocationInfo =
        SpoofProfile.LocationInfo(
            latitude = city.latitude + (random.nextDouble() - 0.5) * JITTER_DEGREES,
            longitude = city.longitude + (random.nextDouble() - 0.5) * JITTER_DEGREES,
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
    private fun newBssid(): String = newLocalMac()

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
                magVendor = "AKM", magName = "AK09918C Magnetometer",
            )
            "google" -> SensorVendorStrings(
                accelVendor = "Google Inc.", accelName = "BMI323 Accelerometer",
                gyroVendor = "Google Inc.", gyroName = "BMI323 Gyroscope",
                magVendor = "AKM", magName = "AK09918 Magnetometer",
            )
            else -> SensorVendorStrings(
                accelVendor = "BOSCH", accelName = "BMI260 Accelerometer",
                gyroVendor = "BOSCH", gyroName = "BMI260 Gyroscope",
                magVendor = "BOSCH", magName = "BMM150 Magnetometer",
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
