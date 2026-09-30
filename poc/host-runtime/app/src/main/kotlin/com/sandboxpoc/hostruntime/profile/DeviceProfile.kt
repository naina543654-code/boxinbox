package com.sandboxpoc.hostruntime.profile

/**
 * Spoofed device profile for one virtual identity. `extras` is an extensible
 * map for future engine-specific fields (serial, IMEI-shaped values, MAC,
 * telephony props, …). The core fields mirror android.os.Build so an engine
 * adapter can map them 1:1 — see INTEGRATION.md.
 *
 * [DeviceProfile] rows are device-definition TEMPLATES (androidId="template");
 * the per-identity artifact is [SpoofProfile], built from a row by
 * ProfileGenerator and serialized to the exact JSON contract via
 * [SpoofProfile.toJson] / [SpoofProfile.fromJson]. No new dependencies are
 * used: the JSON writer/parser is hand-rolled in this file.
 */
data class DeviceProfile(
    val manufacturer: String,
    val brand: String,
    val model: String,
    val device: String,
    val product: String,
    val board: String,
    val hardware: String,
    val fingerprint: String,
    val buildId: String,
    val buildTags: String,
    val buildType: String,
    val androidVersion: String,
    val apiLevel: Int,
    val securityPatch: String,
    val androidId: String,
    val extras: Map<String, String> = emptyMap(),
) {
    companion object {
        /**
         * androidVersion -> expected apiLevel. Used by ProfileValidator to
         * verify coherence; adapters should keep this in sync with the
         * device table below.
         */
        val API_FOR_ANDROID_VERSION: Map<String, Int> = mapOf(
            "11" to 30,
            "12" to 31,
            "12L" to 32,
            "13" to 33,
            "14" to 34,
            "15" to 35,
            "16" to 36,
        )

        /**
         * Internal table of real device definitions. Fields within each row
         * are mutually consistent (manufacturer/brand/model/device/product/
         * board/hardware/fingerprint/buildId/securityPatch/androidVersion/
         * apiLevel are logically compatible — NOT independently random).
         * ProfileGenerator picks a row and only regenerates the per-identity
         * fields when building the SpoofProfile.
         */
        internal val DEVICE_TABLE: List<DeviceProfile> = listOf(
            // ---------------- Android 14 ----------------
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 8",
                device = "shiba", product = "shiba",
                board = "shiba", hardware = "tensor_g3",
                fingerprint = "google/shiba/shiba:14/UQ1A.240205.002/12038998:user/release-keys",
                buildId = "UQ1A.240205.002", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-02-05",
                androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 8 Pro",
                device = "husky", product = "husky",
                board = "husky", hardware = "tensor_g3",
                fingerprint = "google/husky/husky:14/UQ1A.240205.002/12038998:user/release-keys",
                buildId = "UQ1A.240205.002", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-02-05",
                androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 9",
                device = "tokay", product = "tokay",
                board = "tokay", hardware = "tensor_g4",
                fingerprint = "google/tokay/tokay:14/AP1A.240905.019.A1/12999725:user/release-keys",
                buildId = "AP1A.240905.019.A1", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-09-05",
                androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S921B",
                device = "dm1q", product = "dm1qx",
                board = "exynos2400", hardware = "exynos2400",
                fingerprint = "samsung/dm1qx/dm1q:14/UP1A.231005.007/S921BXXS3AXGF:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-10-05",
                androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S911B",
                device = "dm2q", product = "dm2qx",
                board = "kalama", hardware = "qcom",
                fingerprint = "samsung/dm2qx/dm2q:14/UP1A.231005.007/S911BXXS6CXHA:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-10-05",
                androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "OnePlus", brand = "OnePlus", model = "CPH2581",
                device = "OP5958L1", product = "CPH2581",
                board = "OP5958L1", hardware = "qcom",
                fingerprint = "OnePlus/CPH2581/OP5958L1:14/UKQ1.230924.001/1721980182:user/release-keys",
                buildId = "UKQ1.230924.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-10-05",
                androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "Xiaomi", model = "23127PN0CG",
                device = "houji", product = "houji_global",
                board = "houji", hardware = "mt6896",
                fingerprint = "Xiaomi/houji_global/houji:14/UKQ1.230924.001/V816.0.4.0.UNAMIXM:user/release-keys",
                buildId = "UKQ1.230924.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-10-05",
                androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "Nothing", brand = "Nothing", model = "A065",
                device = "Pong", product = "Pong",
                board = "Pong", hardware = "qcom",
                fingerprint = "Nothing/Pong/Pong:14/UKQ1.230924.001/2407080000:user/release-keys",
                buildId = "UKQ1.230924.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-10-05",
                androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "motorola", brand = "motorola", model = "XT2303-2",
                device = "lyriq", product = "lyriq_retail",
                board = "lyriq", hardware = "mt6893",
                fingerprint = "motorola/lyriq_retail/lyriq:14/U1TDS34.94-20-9-7/0f1a2:user/release-keys",
                buildId = "U1TDS34.94-20-9-7", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-10-05",
                androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "asus", brand = "asus", model = "ASUS_AI2401",
                device = "AI2401", product = "WW_AI2401",
                board = "AI2401", hardware = "pineapple",
                fingerprint = "asus/WW_AI2401/AI2401:14/WW_34.1420.1420.114/0:user/release-keys",
                buildId = "WW_34.1420.1420.114", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-02-05",
                androidId = "template",
            ),
            // ---------------- Android 15 ----------------
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 8",
                device = "shiba", product = "shiba",
                board = "shiba", hardware = "tensor_g3",
                fingerprint = "google/shiba/shiba:15/AP3A.241005.015/12366759:user/release-keys",
                buildId = "AP3A.241005.015", buildTags = "release-keys", buildType = "user",
                androidVersion = "15", apiLevel = 35, securityPatch = "2024-10-05",
                androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 9",
                device = "tokay", product = "tokay",
                board = "tokay", hardware = "tensor_g4",
                fingerprint = "google/tokay/tokay:15/AP3A.241005.015/12366759:user/release-keys",
                buildId = "AP3A.241005.015", buildTags = "release-keys", buildType = "user",
                androidVersion = "15", apiLevel = 35, securityPatch = "2024-10-05",
                androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S921B",
                device = "dm1q", product = "dm1qx",
                board = "exynos2400", hardware = "exynos2400",
                fingerprint = "samsung/dm1qx/dm1q:15/AP3A.240905.015.A2/S921BXXU5BYA1:user/release-keys",
                buildId = "AP3A.240905.015.A2", buildTags = "release-keys", buildType = "user",
                androidVersion = "15", apiLevel = 35, securityPatch = "2024-09-05",
                androidId = "template",
            ),
            // ---------------- Android 16 ----------------
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 9",
                device = "tokay", product = "tokay",
                board = "tokay", hardware = "tensor_g4",
                fingerprint = "google/tokay/tokay:16/BP2A.250605.031.A2/13338153:user/release-keys",
                buildId = "BP2A.250605.031.A2", buildTags = "release-keys", buildType = "user",
                androidVersion = "16", apiLevel = 36, securityPatch = "2025-06-05",
                androidId = "template",
            ),
        )
    }
}

/**
 * The per-identity spoofed device profile — the artifact every track shares.
 *
 * JSON contract (field names and nesting are frozen; the engine and the probe
 * parse this exact shape):
 * ```json
 * {
 *   "profileId": "<uuid>", "generatedAt": <epoch millis>,
 *   "device": { "manufacturer","brand","model","device","product","board",
 *     "hardware","fingerprint","buildId","buildTags","buildType",
 *     "androidVersion","apiLevel","securityPatch" },
 *   "androidId": "<16 lowercase hex>",
 *   "location": { "latitude","longitude","accuracy","altitude","speed","bearing",
 *     "movement": {"enabled": false, "speedMps": 1.4, "bearingDeg": 90.0} },
 *   "sensors": [ {"type": <int>, "name": "<string>", "vendor": "<string>"} ],
 *   "network": { "ssid": "\"SandboxNet\"", "bssid": "02:15:3E:4A:5B:6C",
 *     "transport": "WIFI" },
 *   "telephony": { "operatorName": "T-Mobile", "operatorNumeric": "310260",
 *     "countryIso": "us", "deviceId": "<16 hex>", "subscriberId": "<15 digits>" }
 * }
 * ```
 *
 * Engine-independent: no imports from any engine package (top.niunaijun.*).
 * JSON is hand-rolled (no new dependencies); [toJson] and [fromJson] are
 * inverses of each other.
 */
data class SpoofProfile(
    val profileId: String,
    val generatedAt: Long,
    val device: DeviceInfo,
    val androidId: String,
    val location: LocationInfo,
    val sensors: List<SensorInfo>,
    val network: NetworkInfo,
    val telephony: TelephonyInfo,
) {
    data class DeviceInfo(
        val manufacturer: String,
        val brand: String,
        val model: String,
        val device: String,
        val product: String,
        val board: String,
        val hardware: String,
        val fingerprint: String,
        val buildId: String,
        val buildTags: String,
        val buildType: String,
        val androidVersion: String,
        val apiLevel: Int,
        val securityPatch: String,
    )

    data class Movement(
        val enabled: Boolean,
        val speedMps: Double,
        val bearingDeg: Double,
    )

    data class LocationInfo(
        val latitude: Double,
        val longitude: Double,
        val accuracy: Double,
        val altitude: Double,
        val speed: Double,
        val bearing: Double,
        val movement: Movement,
    )

    data class SensorInfo(
        val type: Int,
        val name: String,
        val vendor: String,
    )

    data class NetworkInfo(
        val ssid: String,
        val bssid: String,
        val transport: String,
    )

    data class TelephonyInfo(
        val operatorName: String,
        val operatorNumeric: String,
        val countryIso: String,
        val deviceId: String,
        val subscriberId: String,
    )

    // ------------------------------------------------------------------
    // JSON — hand-rolled (no new dependencies)
    // ------------------------------------------------------------------

    /** Serializes to the exact contract JSON (field order preserved). */
    fun toJson(): String = buildString {
        append('{')
        jname("profileId"); append(':'); jstr(profileId); append(',')
        append("\"generatedAt\":").append(generatedAt).append(',')
        append("\"device\":{")
        jname("manufacturer"); append(':'); jstr(device.manufacturer); append(',')
        jname("brand"); append(':'); jstr(device.brand); append(',')
        jname("model"); append(':'); jstr(device.model); append(',')
        jname("device"); append(':'); jstr(device.device); append(',')
        jname("product"); append(':'); jstr(device.product); append(',')
        jname("board"); append(':'); jstr(device.board); append(',')
        jname("hardware"); append(':'); jstr(device.hardware); append(',')
        jname("fingerprint"); append(':'); jstr(device.fingerprint); append(',')
        jname("buildId"); append(':'); jstr(device.buildId); append(',')
        jname("buildTags"); append(':'); jstr(device.buildTags); append(',')
        jname("buildType"); append(':'); jstr(device.buildType); append(',')
        jname("androidVersion"); append(':'); jstr(device.androidVersion); append(',')
        jname("apiLevel"); append(':'); append(device.apiLevel); append(',')
        jname("securityPatch"); append(':'); jstr(device.securityPatch)
        append("},")
        jname("androidId"); append(':'); jstr(androidId); append(',')
        append("\"location\":{")
        jname("latitude"); append(':'); jnum(location.latitude); append(',')
        jname("longitude"); append(':'); jnum(location.longitude); append(',')
        jname("accuracy"); append(':'); jnum(location.accuracy); append(',')
        jname("altitude"); append(':'); jnum(location.altitude); append(',')
        jname("speed"); append(':'); jnum(location.speed); append(',')
        jname("bearing"); append(':'); jnum(location.bearing); append(',')
        append("\"movement\":{")
        jname("enabled"); append(':'); append(location.movement.enabled); append(',')
        jname("speedMps"); append(':'); jnum(location.movement.speedMps); append(',')
        jname("bearingDeg"); append(':'); jnum(location.movement.bearingDeg)
        append("}},")
        append("\"sensors\":[")
        sensors.forEachIndexed { idx, s ->
            if (idx > 0) append(',')
            append('{')
            jname("type"); append(':'); append(s.type); append(',')
            jname("name"); append(':'); jstr(s.name); append(',')
            jname("vendor"); append(':'); jstr(s.vendor)
            append('}')
        }
        append("],")
        append("\"network\":{")
        jname("ssid"); append(':'); jstr(network.ssid); append(',')
        jname("bssid"); append(':'); jstr(network.bssid); append(',')
        jname("transport"); append(':'); jstr(network.transport)
        append("},")
        append("\"telephony\":{")
        jname("operatorName"); append(':'); jstr(telephony.operatorName); append(',')
        jname("operatorNumeric"); append(':'); jstr(telephony.operatorNumeric); append(',')
        jname("countryIso"); append(':'); jstr(telephony.countryIso); append(',')
        jname("deviceId"); append(':'); jstr(telephony.deviceId); append(',')
        jname("subscriberId"); append(':'); jstr(telephony.subscriberId)
        append('}')
        append('}')
    }

    /** Appends a JSON object key name (always a plain ASCII identifier). */
    private fun StringBuilder.jname(name: String) {
        append('"').append(name).append('"')
    }

    /** Appends a JSON string value with full escaping. */
    private fun StringBuilder.jstr(value: String) {
        append('"')
        for (c in value) {
            when (c) {
                '"' -> append("\\\"")
                '\\' -> append("\\\\")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                '\b' -> append("\\b")
                '' -> append("\\f")
                else ->
                    if (c < ' ') append("\\u%04x".format(c.code))
                    else append(c)
            }
        }
        append('"')
    }

    /** Appends a JSON number from a Double without scientific notation. */
    private fun StringBuilder.jnum(value: Double) {
        append(java.math.BigDecimal.valueOf(value).toPlainString())
    }

    companion object {
        /** Parses contract JSON produced by [toJson]; strict, throws on any mismatch. */
        fun fromJson(json: String): SpoofProfile {
            val root = JsonParser(json).parse() as? Map<*, *>
                ?: throw IllegalArgumentException("top-level profile JSON must be an object")
            val d = getObj(root, "device")
            val l = getObj(root, "location")
            val mv = getObj(l, "movement")
            val n = getObj(root, "network")
            val t = getObj(root, "telephony")
            return SpoofProfile(
                profileId = getStr(root, "profileId"),
                generatedAt = getLong(root, "generatedAt"),
                device = DeviceInfo(
                    manufacturer = getStr(d, "manufacturer"),
                    brand = getStr(d, "brand"),
                    model = getStr(d, "model"),
                    device = getStr(d, "device"),
                    product = getStr(d, "product"),
                    board = getStr(d, "board"),
                    hardware = getStr(d, "hardware"),
                    fingerprint = getStr(d, "fingerprint"),
                    buildId = getStr(d, "buildId"),
                    buildTags = getStr(d, "buildTags"),
                    buildType = getStr(d, "buildType"),
                    androidVersion = getStr(d, "androidVersion"),
                    apiLevel = getInt(d, "apiLevel"),
                    securityPatch = getStr(d, "securityPatch"),
                ),
                androidId = getStr(root, "androidId"),
                location = LocationInfo(
                    latitude = getDouble(l, "latitude"),
                    longitude = getDouble(l, "longitude"),
                    accuracy = getDouble(l, "accuracy"),
                    altitude = getDouble(l, "altitude"),
                    speed = getDouble(l, "speed"),
                    bearing = getDouble(l, "bearing"),
                    movement = Movement(
                        enabled = getBool(mv, "enabled"),
                        speedMps = getDouble(mv, "speedMps"),
                        bearingDeg = getDouble(mv, "bearingDeg"),
                    ),
                ),
                sensors = getArr(root, "sensors").map { entry ->
                    val sm = entry as? Map<*, *>
                        ?: throw IllegalArgumentException("sensor entry is not an object")
                    SensorInfo(
                        type = getInt(sm, "type"),
                        name = getStr(sm, "name"),
                        vendor = getStr(sm, "vendor"),
                    )
                },
                network = NetworkInfo(
                    ssid = getStr(n, "ssid"),
                    bssid = getStr(n, "bssid"),
                    transport = getStr(n, "transport"),
                ),
                telephony = TelephonyInfo(
                    operatorName = getStr(t, "operatorName"),
                    operatorNumeric = getStr(t, "operatorNumeric"),
                    countryIso = getStr(t, "countryIso"),
                    deviceId = getStr(t, "deviceId"),
                    subscriberId = getStr(t, "subscriberId"),
                ),
            )
        }

        private fun getStr(m: Map<*, *>, key: String): String =
            (m[key] as? String)
                ?: throw IllegalArgumentException("'$key' missing or not a string")

        private fun getObj(m: Map<*, *>, key: String): Map<*, *> =
            (m[key] as? Map<*, *>)
                ?: throw IllegalArgumentException("'$key' missing or not an object")

        private fun getArr(m: Map<*, *>, key: String): List<*> =
            (m[key] as? List<*>)
                ?: throw IllegalArgumentException("'$key' missing or not an array")

        private fun getLong(m: Map<*, *>, key: String): Long =
            (m[key] as? Double)?.toLong()
                ?: throw IllegalArgumentException("'$key' missing or not a number")

        private fun getInt(m: Map<*, *>, key: String): Int = getLong(m, key).toInt()

        private fun getDouble(m: Map<*, *>, key: String): Double =
            (m[key] as? Double)
                ?: throw IllegalArgumentException("'$key' missing or not a number")

        private fun getBool(m: Map<*, *>, key: String): Boolean =
            (m[key] as? Boolean)
                ?: throw IllegalArgumentException("'$key' missing or not a boolean")

        /**
         * Minimal JSON parser: objects, arrays, strings (with escapes),
         * numbers (as Double), booleans, null. Sufficient for the fixed
         * contract schema; not a general-purpose parser.
         */
        private class JsonParser(private val s: String) {
            private var i = 0

            fun parse(): Any? {
                val v = value()
                ws()
                check(i == s.length) { "trailing data at index $i" }
                return v
            }

            private fun ws() {
                while (i < s.length && s[i].isWhitespace()) i++
            }

            private fun value(): Any? {
                ws()
                check(i < s.length) { "unexpected end of JSON" }
                return when (s[i]) {
                    '{' -> obj()
                    '[' -> arr()
                    '"' -> str()
                    't' -> lit("true", true)
                    'f' -> lit("false", false)
                    'n' -> lit("null", null)
                    else -> num()
                }
            }

            private fun lit(word: String, v: Any?): Any? {
                check(s.startsWith(word, i)) { "expected '$word' at index $i" }
                i += word.length
                return v
            }

            private fun obj(): Map<String, Any?> {
                i++ // consume '{'
                val m = LinkedHashMap<String, Any?>()
                ws()
                if (i < s.length && s[i] == '}') {
                    i++
                    return m
                }
                while (true) {
                    ws()
                    check(i < s.length && s[i] == '"') { "expected string key at index $i" }
                    val k = str()
                    ws()
                    check(i < s.length && s[i] == ':') { "expected ':' at index $i" }
                    i++
                    m[k] = value()
                    ws()
                    check(i < s.length) { "unexpected end inside object" }
                    when (s[i]) {
                        ',' -> i++
                        '}' -> {
                            i++
                            return m
                        }
                        else -> throw IllegalArgumentException("expected ',' or '}' at index $i")
                    }
                }
            }

            private fun arr(): List<Any?> {
                i++ // consume '['
                val l = ArrayList<Any?>()
                ws()
                if (i < s.length && s[i] == ']') {
                    i++
                    return l
                }
                while (true) {
                    l.add(value())
                    ws()
                    check(i < s.length) { "unexpected end inside array" }
                    when (s[i]) {
                        ',' -> i++
                        ']' -> {
                            i++
                            return l
                        }
                        else -> throw IllegalArgumentException("expected ',' or ']' at index $i")
                    }
                }
            }

            private fun str(): String {
                check(s[i] == '"')
                i++
                val sb = StringBuilder()
                while (true) {
                    check(i < s.length) { "unterminated string" }
                    val c = s[i++]
                    when (c) {
                        '"' -> return sb.toString()
                        '\\' -> {
                            check(i < s.length) { "unterminated escape" }
                            when (val e = s[i++]) {
                                '"', '\\', '/' -> sb.append(e)
                                'b' -> sb.append('\b')
                                'f' -> sb.append('')
                                'n' -> sb.append('\n')
                                'r' -> sb.append('\r')
                                't' -> sb.append('\t')
                                'u' -> {
                                    check(i + 4 <= s.length) { "bad \\u escape at index $i" }
                                    sb.append(s.substring(i, i + 4).toInt(16).toChar())
                                    i += 4
                                }
                                else -> throw IllegalArgumentException("bad escape '\\$e'")
                            }
                        }
                        else -> sb.append(c)
                    }
                }
            }

            private fun num(): Double {
                val start = i
                while (i < s.length && (s[i] in '0'..'9' || s[i] == '-' || s[i] == '+' ||
                        s[i] == '.' || s[i] == 'e' || s[i] == 'E')
                ) {
                    i++
                }
                check(i > start) { "expected value at index $i" }
                return s.substring(start, i).toDouble()
            }
        }
    }
}
