package com.sandboxpoc.hostruntime.profile

/**
 * Derives the build incremental from a fingerprint of the form
 * `<brand>/<product>/<device>:<version>/<buildId>/<incremental>:<type>/<tags>`.
 * Returns "" when the fingerprint does not have the expected shape.
 */
internal fun deriveIncremental(fingerprint: String): String {
    val segs = fingerprint.split('/')
    if (segs.size < 5) return ""
    return segs[4].substringBefore(':')
}

/**
 * Derives a plausible `ro.build.display.id` (Build.DISPLAY) from the row's
 * brand, build ID and incremental.
 *
 * Verified OEM patterns: Samsung reports `<buildId>.<incremental>`
 * (e.g. `TP1A.220624.014.A346BXXU1AWB9`); Pixel reports the build ID itself;
 * Xiaomi/Redmi/POCO report the MIUI version (= incremental, e.g.
 * `V816.0.15.0.UNRMIXM`); Motorola reports the full version string
 * (= incremental). For other brands (OnePlus/OPPO/realme/Nothing) the exact
 * OEM display.id pattern is not verified per row, so we fall back to the
 * build ID — always coherent (it is a substring of the fingerprint) rather
 * than leaking the host's real display string.
 */
internal fun deriveDisplayId(brand: String, buildId: String, incremental: String): String =
    when (brand.lowercase()) {
        "samsung" -> "$buildId.$incremental"
        "google" -> buildId
        "xiaomi", "redmi", "poco" -> incremental.ifEmpty { buildId }
        "motorola" -> incremental.ifEmpty { buildId }
        else -> buildId
    }

/**
 * Plausible kernel version (`os.version` / `System.getProperty("os.version")`)
 * per API level. Synthetic but shape-real: real Android 13 devices ship
 * 5.10/5.15 kernels, Android 14 devices 5.15/6.1. The exact build hash is not
 * per-device verified — the goal is to stop leaking the host's real kernel
 * (e.g. a 4.19 Lineage kernel on an API-33 identity), not to impersonate one
 * specific device's kernel build.
 */
internal fun kernelForApi(apiLevel: Int): String = when (apiLevel) {
    34 -> "6.1.25-android14-4-00001-g3f2e1d0c9b8a"
    else -> "5.10.107-android13-4-00001-g7a6b5c4d3e2f"
}

/**
 * Plausible `Build.TIME` (ro.build.date.utc, seconds→ms): real factory builds
 * are stamped within days of their security patch level. We stamp 12:00 UTC
 * on the SPL date itself. Returns a fixed fallback for "unknown".
 */
internal fun deriveBuildTime(securityPatch: String): Long {
    if (securityPatch == "unknown") return 1_700_406_720_000L // 2023-12-31T12:00Z
    val parts = securityPatch.split('-')
    if (parts.size != 3) return 1_700_406_720_000L
    val y = parts[0].toIntOrNull(); val mo = parts[1].toIntOrNull(); val d = parts[2].toIntOrNull()
    if (y == null || mo == null || d == null) return 1_700_406_720_000L
    val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
    cal.set(y, mo - 1, d, 12, 0, 0)
    cal.set(java.util.Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

/**
 * SoC (ro.soc.manufacturer / ro.soc.model) derived from the device's
 * `hardware` string, with a model override where the platform codename
 * covers more than one SoC (lahaina = both Snapdragon 888 and 778G).
 * Only rows whose hardware maps to a verified SoC are
 * covered — unknown values return ("", "") and the engine skips the patch.
 */
internal fun socFor(hardware: String, model: String): Pair<String, String> {
    // Model wins over platform codename where they disagree: the Galaxy
    // A52s (SM-A528B) and A73 5G (SM-A736B) report board "lahaina" but
    // carry the Snapdragon 778G, not the 888.
    if (model == "SM-A528B" || model == "SM-A736B") {
        return "Qualcomm" to "Snapdragon 778G"
    }
    return when (hardware) {
    "s5e9925" -> "Samsung" to "Exynos 2400"
    "s5e8835" -> "Samsung" to "Exynos 1480"
    "s5e8825", "exynos1380" -> "Samsung" to "Exynos 1380"
    "exynos1280" -> "Samsung" to "Exynos 1280"
    "lahaina" -> "Qualcomm" to "Snapdragon 888"
    "atoll" -> "Qualcomm" to "Snapdragon 720G"
    "mt6877" -> "MediaTek" to "Dimensity 900"
    "raven", "oriole", "bluejay" -> "Google" to "Tensor"
    "panther", "cheetah", "lynx", "felix" -> "Google" to "Tensor G2"
    else -> "" to ""
    }
}

/**
 * Major Chrome version of the host's installed WebView, baked into the
 * per-identity UA so the UA never claims a newer Chrome than the WebView
 * package actually installed. Set at host app start from PackageManager;
 * defaults to the last hardcoded value until then.
 */
internal var webViewChromeMajor: String = "126"

/**
 * Prebuilt WebView default user-agent. Model and build ID come from the
 * spoofed profile, so the UA never contains the host's real model/build.
 * The engine returns this string verbatim for
 * `WebSettings.getDefaultUserAgent`.
 */
internal fun buildWebViewUa(androidVersion: String, model: String, buildId: String): String =
    "Mozilla/5.0 (Linux; Android $androidVersion; $model Build/$buildId) " +
        "AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 " +
        "Chrome/$webViewChromeMajor.0.0.0 Mobile Safari/537.36"

/**
 * IANA timezone for a profile country (primary zone; the per-identity city
 * sits inside it, so TZ, locale and GPS stay coherent).
 */
internal fun timezoneFor(countryIso: String): String = COUNTRY_LOCALE[countryIso]?.first ?: "UTC"

/** BCP-47 locale tag for a profile country. */
internal fun localeFor(countryIso: String): String = COUNTRY_LOCALE[countryIso]?.second ?: "en-US"

private val COUNTRY_LOCALE: Map<String, Pair<String, String>> = mapOf(
    "us" to ("America/New_York" to "en-US"),
    "ca" to ("America/Toronto" to "en-CA"),
    "mx" to ("America/Mexico_City" to "es-MX"),
    "br" to ("America/Sao_Paulo" to "pt-BR"),
    "ar" to ("America/Argentina/Buenos_Aires" to "es-AR"),
    "cl" to ("America/Santiago" to "es-CL"),
    "co" to ("America/Bogota" to "es-CO"),
    "pe" to ("America/Lima" to "es-PE"),
    "gb" to ("Europe/London" to "en-GB"),
    "ie" to ("Europe/Dublin" to "en-IE"),
    "fr" to ("Europe/Paris" to "fr-FR"),
    "de" to ("Europe/Berlin" to "de-DE"),
    "es" to ("Europe/Madrid" to "es-ES"),
    "it" to ("Europe/Rome" to "it-IT"),
    "pt" to ("Europe/Lisbon" to "pt-PT"),
    "nl" to ("Europe/Amsterdam" to "nl-NL"),
    "be" to ("Europe/Brussels" to "nl-BE"),
    "ch" to ("Europe/Zurich" to "de-CH"),
    "at" to ("Europe/Vienna" to "de-AT"),
    "se" to ("Europe/Stockholm" to "sv-SE"),
    "no" to ("Europe/Oslo" to "nb-NO"),
    "dk" to ("Europe/Copenhagen" to "da-DK"),
    "fi" to ("Europe/Helsinki" to "fi-FI"),
    "pl" to ("Europe/Warsaw" to "pl-PL"),
    "cz" to ("Europe/Prague" to "cs-CZ"),
    "hu" to ("Europe/Budapest" to "hu-HU"),
    "ro" to ("Europe/Bucharest" to "ro-RO"),
    "gr" to ("Europe/Athens" to "el-GR"),
    "ua" to ("Europe/Kyiv" to "uk-UA"),
    "tr" to ("Europe/Istanbul" to "tr-TR"),
    "ae" to ("Asia/Dubai" to "ar-AE"),
    "sa" to ("Asia/Riyadh" to "ar-SA"),
    "qa" to ("Asia/Qatar" to "ar-QA"),
    "il" to ("Asia/Jerusalem" to "he-IL"),
    "eg" to ("Africa/Cairo" to "ar-EG"),
    "ma" to ("Africa/Casablanca" to "ar-MA"),
    "ng" to ("Africa/Lagos" to "en-NG"),
    "ke" to ("Africa/Nairobi" to "en-KE"),
    "gh" to ("Africa/Accra" to "en-GH"),
    "za" to ("Africa/Johannesburg" to "en-ZA"),
    "in" to ("Asia/Kolkata" to "en-IN"),
    "pk" to ("Asia/Karachi" to "ur-PK"),
    "bd" to ("Asia/Dhaka" to "bn-BD"),
    "lk" to ("Asia/Colombo" to "si-LK"),
    "np" to ("Asia/Kathmandu" to "ne-NP"),
    "sg" to ("Asia/Singapore" to "en-SG"),
    "my" to ("Asia/Kuala_Lumpur" to "ms-MY"),
    "th" to ("Asia/Bangkok" to "th-TH"),
    "id" to ("Asia/Jakarta" to "id-ID"),
    "ph" to ("Asia/Manila" to "fil-PH"),
    "vn" to ("Asia/Ho_Chi_Minh" to "vi-VN"),
    "kh" to ("Asia/Phnom_Penh" to "km-KH"),
    "jp" to ("Asia/Tokyo" to "ja-JP"),
    "kr" to ("Asia/Seoul" to "ko-KR"),
    "cn" to ("Asia/Shanghai" to "zh-CN"),
    "hk" to ("Asia/Hong_Kong" to "zh-HK"),
    "tw" to ("Asia/Taipei" to "zh-TW"),
    "au" to ("Australia/Sydney" to "en-AU"),
    "nz" to ("Pacific/Auckland" to "en-NZ"),
)

/**
 * Random locally-administered unicast MAC (`02:xx:…`), the same shape as
 * [ProfileGenerator.newBssid] but without the stored-SSID coupling.
 */
internal fun newLocalMac(): String {
    val r = java.security.SecureRandom()
    val b = ByteArray(6); r.nextBytes(b)
    // Fixed 0x02 first byte: locally-administered unicast, matching the
    // validator (LOCAL_MAC_RE) and the documented "02:xx:.." shape. Setting
    // only the local bit (0x02) without clearing the rest produced first
    // bytes like 06/0A/12.., which the validator rejects.
    b[0] = 0x02
    return b.joinToString(":") { "%02X".format(it) }
}

/** Random ICCID: 19 digits starting with 89 (telecom industry issuer). */
internal fun newSimSerial(): String {
    val r = java.security.SecureRandom()
    val sb = StringBuilder("89")
    repeat(17) { sb.append(r.nextInt(10)) }
    return sb.toString()
}

/**
 * Country calling code + typical national mobile-number length, keyed by the
 * profile country ISO. Used to generate a plausible per-identity MSISDN so
 * guests never see the host SIM's real number (a stable cross-identity
 * link for any backend that keys accounts by device-reported number).
 */
private val COUNTRY_CALLING: Map<String, Pair<String, Int>> = mapOf(
    "us" to ("1" to 10), "ca" to ("1" to 10), "mx" to ("52" to 10),
    "br" to ("55" to 11), "ar" to ("54" to 10), "cl" to ("56" to 9),
    "co" to ("57" to 10), "pe" to ("51" to 9), "gb" to ("44" to 10),
    "ie" to ("353" to 9), "fr" to ("33" to 9), "de" to ("49" to 11),
    "es" to ("34" to 9), "it" to ("39" to 10), "pt" to ("351" to 9),
    "nl" to ("31" to 9), "be" to ("32" to 9), "ch" to ("41" to 9),
    "at" to ("43" to 10), "se" to ("46" to 9), "no" to ("47" to 8),
    "dk" to ("45" to 8), "fi" to ("358" to 9), "pl" to ("48" to 9),
    "cz" to ("420" to 9), "hu" to ("36" to 9), "ro" to ("40" to 10),
    "gr" to ("30" to 10), "ua" to ("380" to 9), "tr" to ("90" to 10),
    "ae" to ("971" to 9), "sa" to ("966" to 9), "qa" to ("974" to 8),
    "il" to ("972" to 9), "eg" to ("20" to 10), "ma" to ("212" to 9),
    "ng" to ("234" to 10), "ke" to ("254" to 9), "gh" to ("233" to 9),
    "za" to ("27" to 9), "in" to ("91" to 10), "pk" to ("92" to 10),
    "bd" to ("880" to 10), "lk" to ("94" to 9), "np" to ("977" to 10),
    "sg" to ("65" to 8), "my" to ("60" to 10), "th" to ("66" to 9),
    "id" to ("62" to 11), "ph" to ("63" to 10), "vn" to ("84" to 9),
    "kh" to ("855" to 9), "jp" to ("81" to 10), "kr" to ("82" to 10),
    "cn" to ("86" to 11), "hk" to ("852" to 8), "tw" to ("886" to 9),
    "au" to ("61" to 9), "nz" to ("64" to 9),
)

/** Random per-identity MSISDN in E.164 (`+<cc><national>`), coherent with the profile country. */
internal fun newPhoneNumber(countryIso: String): String {
    val (cc, len) = COUNTRY_CALLING[countryIso] ?: ("1" to 10)
    val r = java.security.SecureRandom()
    val sb = StringBuilder("+").append(cc)
    // First national digit non-zero (like real mobile allocations).
    sb.append(r.nextInt(9) + 1)
    repeat(len - 1) { sb.append(r.nextInt(10)) }
    return sb.toString()
}

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
        /**
         * Internal table of real device definitions, researched 2026-10-01
         * (see device-fingerprints-research.json at the PoC root for sources
         * and verification notes per row). Fields within each row are mutually
         * consistent (manufacturer/brand/model/device/product/board/hardware/
         * fingerprint/buildId/securityPatch/androidVersion/apiLevel are
         * logically compatible — NOT independently random). Rows marked with
         * a board/hw fallback use the device codename where the dump did not
         * record board/hardware; rows with securityPatch "unknown" had no
         * trustworthy patch source (never invented). API 33/34 only, by
         * project decision. ProfileGenerator picks a row and only regenerates
         * the per-identity fields when building the SpoofProfile.
         */
        internal val DEVICE_TABLE: List<DeviceProfile> = listOf(
            // ---------------- Android 14 ----------------
// Google Pixel 6 — gm-stuffs/google_oriole_dump README
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 6",
                device = "oriole", product = "oriole",
                board = "oriole", hardware = "oriole",
                fingerprint = "google/oriole/oriole:14/AP1A.240505.004/11583682:user/release-keys",
                buildId = "AP1A.240505.004", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-05-05",
                androidId = "template",
            ),
// Google Pixel 6 Pro — starlabs.sg Pixel 6 Pro blog (real device 2025-05-20) + forum.antennapod.org crash log ...
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 6 Pro",
                device = "raven", product = "raven",
                board = "raven", hardware = "raven",
                fingerprint = "google/raven/raven:14/UP1A.231005.007/10754064:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-10-05",
                androidId = "template",
            ),
// Google Pixel 6a — Lawnchair#4860 real-device Build dump 2024-09-27 (build.security.level=2024-09-05, hard...
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 6a",
                device = "bluejay", product = "bluejay",
                board = "bluejay", hardware = "bluejay",
                fingerprint = "google/bluejay/bluejay:14/AP2A.240905.003.F1/2024091900:user/release-keys",
                buildId = "AP2A.240905.003.F1", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-09-05",
                androidId = "template",
            ),
// Google Pixel 7 — trustify-ui-tests#7 / tensorflow#66740 real-device logs; hardware=panther via tensorflo...
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 7",
                device = "panther", product = "panther",
                board = "panther", hardware = "panther",
                fingerprint = "google/panther/panther:14/UP1A.231005.007.A1/10762838:user/release-keys",
                buildId = "UP1A.231005.007.A1", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-10-05",
                androidId = "template",
            ),
// Google Pixel 7 Pro — nippongsi Telegram (Pixel 14 GSI ported from Pixel 7 Pro, patch 2023-10-05)
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 7 Pro",
                device = "cheetah", product = "cheetah",
                board = "cheetah", hardware = "cheetah",
                fingerprint = "google/cheetah/cheetah:14/UP1A.231005.007/10754064:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-10-05",
                androidId = "template",
            ),
// Google Pixel 7a — nippongsi Telegram (Pixel 14 GSI ported from Pixel 7a, patch 2023-10-05); hardware=lynx...
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 7a",
                device = "lynx", product = "lynx",
                board = "lynx", hardware = "lynx",
                fingerprint = "google/lynx/lynx:14/UP1A.231005.007/10754064:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-10-05",
                androidId = "template",
            ),
// Google Pixel Fold — nippongsi Telegram (Pixel 14 GSI ported from Pixel Fold, patch 2023-10-05)
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel Fold",
                device = "felix", product = "felix",
                board = "felix", hardware = "felix",
                fingerprint = "google/felix/felix:14/UP1A.231005.007/10754064:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-10-05",
                androidId = "template",
            ),
// Google Pixel 8a — android_dumps Telegram 2024-09-22; build ID matches guise-verified Google OTA akita AP2...
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 8a",
                device = "akita", product = "akita",
                board = "akita", hardware = "akita",
                fingerprint = "google/akita/akita:14/AP2A.240905.003.E1/12235207:user/release-keys",
                buildId = "AP2A.240905.003.E1", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-09-05",
                androidId = "template",
            ),
// Google Pixel Tablet — android_dumps Telegram (Platform gs201) + webex-android-sdk-example#90 crash log (real ...
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel Tablet",
                device = "tangorpro", product = "tangorpro",
                board = "tangorpro", hardware = "tangorpro",
                fingerprint = "google/tangorpro/tangorpro:14/AP2A.240605.024/11860263:user/release-keys",
                buildId = "AP2A.240605.024", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-06-05",
                androidId = "template",
            ),
// samsung SM-A336B — PDA+patch+OS14 from samfw SM-A336B EUY detail page; build ID seen on real Samsung A14 m...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A336B",
                device = "a33x", product = "a33x",
                board = "s5e8825", hardware = "exynos1280",
                fingerprint = "samsung/a33x/a33x:14/UP1A.231005.007/A336BXXU7DWK6:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-11-01",
                androidId = "template",
            ),
// samsung SM-A346B — PDA+patch+OS14 from samfw/sammobile SM-A346B EUX
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A346B",
                device = "a34x", product = "a34x",
                board = "a34x", hardware = "mt6877",
                fingerprint = "samsung/a34x/a34x:14/UP1A.231005.007/A346BXXU6BXD2:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-04-01",
                androidId = "template",
            ),
// samsung SM-A528B — PDA+patch+OS14/One UI 6.0 from samfw SM-A528B SLK detail page
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A528B",
                device = "a52sxq", product = "a52sxq",
                board = "lahaina", hardware = "lahaina",
                fingerprint = "samsung/a52sxq/a52sxq:14/UP1A.231005.007/A528BXXU5FWK4:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-11-01",
                androidId = "template",
            ),
// samsung SM-A725F — PDA+patch+U(14) from samfw SM-A725F INS detail page; corroborated by sammobile per-buil...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A725F",
                device = "a72q", product = "a72q",
                board = "atoll", hardware = "atoll",
                fingerprint = "samsung/a72q/a72q:14/UP1A.231005.007/A725FXXUAFXL2:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-11-01",
                androidId = "template",
            ),
// samsung SM-A736B — PDA+patch+OS14 'One UI 6.0 Upgrade' from samfw SM-A736B INS detail page (first A14); sa...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A736B",
                device = "a73xq", product = "a73xq",
                board = "lahaina", hardware = "lahaina",
                fingerprint = "samsung/a73xq/a73xq:14/UP1A.231005.007/A736BXXU5DWK2:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-11-01",
                androidId = "template",
            ),
// samsung SM-M546B — PDA+patch from sammobile SM-M546B listing
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-M546B",
                device = "m54x", product = "m54x",
                board = "s5e8835", hardware = "exynos1380",
                fingerprint = "samsung/m54x/m54x:14/UP1A.231005.007/M546BXXS5CXE2:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-05-01",
                androidId = "template",
            ),
// samsung SM-G990B — PDA+patch+OS14 'One UI 6.0 Upgrade (Android 14)' from samfw SM-G990B EUX detail page (f...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-G990B",
                device = "r9q", product = "r9q",
                board = "lahaina", hardware = "lahaina",
                fingerprint = "samsung/r9q/r9q:14/UP1A.231005.007/G990BXXU6FWK3:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-11-01",
                androidId = "template",
            ),
// Nothing A063 — kvaesitso issue #1111 - real-device dump. Display U2.6-240904-1634 (Nothing OS 2.6, Sep...
            DeviceProfile(
                manufacturer = "Nothing", brand = "Nothing", model = "A063",
                device = "Spacewar", product = "Spacewar",
                board = "Spacewar", hardware = "qcom",
                fingerprint = "Nothing/Spacewar/Spacewar:14/UP1A.231005.007/2409041634:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "unknown",
                androidId = "template",
            ),
// motorola XT2201 — MotorolaMobilityLLC/kernel-msm issue #691 ('Kernel Source for U1SHS34.1-177-7-3') - off... [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "motorola", brand = "motorola", model = "XT2201",
                device = "hiphi", product = "hiphi_gu",
                board = "hiphi", hardware = "hiphi",
                fingerprint = "motorola/hiphi_gu/hiphi:14/U1SHS34.1-177-7-3/90a09-afa44:user/release-keys",
                buildId = "U1SHS34.1-177-7-3", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "unknown",
                androidId = "template",
            ),
// OnePlus CPH2613 — t.me/android_dumps channel (device-dump channel, June 2024) - 'Device: crow' [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "OnePlus", brand = "OnePlus", model = "CPH2613",
                device = "OP5D3FL1", product = "CPH2613IN",
                board = "OP5D3FL1", hardware = "OP5D3FL1",
                fingerprint = "OnePlus/CPH2613IN/OP5D3FL1:14/TP1A.220905.001/U.R4T2.185eda8_dde6-16c01:user/release-keys",
                buildId = "TP1A.220905.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "unknown",
                androidId = "template",
            ),
// OnePlus CPH2573 — t.me/android_dumps channel (device-dump channel, July 2024) [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "OnePlus", brand = "OnePlus", model = "CPH2573",
                device = "OP595DL1", product = "CPH2573IN",
                board = "OP595DL1", hardware = "OP595DL1",
                fingerprint = "OnePlus/CPH2573IN/OP595DL1:14/UKQ1.230924.001/U.R4T3.190fa24-1393d-a745b:user/release-keys",
                buildId = "UKQ1.230924.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "unknown",
                androidId = "template",
            ),
// Xiaomi 2312DRA50G — NewPipe issue #13033 (real device); SPL from miuidownloader.com OS1.0.15.0.UNRMIXM chan...
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "Redmi", model = "2312DRA50G",
                device = "garnet", product = "garnet_global",
                board = "garnet", hardware = "qcom",
                fingerprint = "Redmi/garnet_global/garnet:14/UKQ1.231003.002/V816.0.15.0.UNRMIXM:user/release-keys",
                buildId = "UKQ1.231003.002", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-10-01",
                androidId = "template",
            ),
// Xiaomi 2312DRA50G — j-hc/zygisk-detach issue #60 (real device); SPL from miuidownloader.com OS1.0.17.0.UNRM...
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "Redmi", model = "2312DRA50G",
                device = "garnet", product = "garnet_global",
                board = "garnet", hardware = "qcom",
                fingerprint = "Redmi/garnet_global/garnet:14/UKQ1.231003.002/V816.0.17.0.UNRMIXM:user/release-keys",
                buildId = "UKQ1.231003.002", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-11-01",
                androidId = "template",
            ),
// Xiaomi 2311DRK48G — ICS OpenVPN GitHub log (2025-02-22, real device); SPL from miuidownloader.com OS1.0.17....
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "POCO", model = "2311DRK48G",
                device = "duchamp", product = "duchamp_global",
                board = "duchamp", hardware = "mt6897",
                fingerprint = "POCO/duchamp_global/duchamp:14/UP1A.230905.011/V816.0.17.0.UNLMIXM:user/release-keys",
                buildId = "UP1A.230905.011", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-11-01",
                androidId = "template",
            ),
// realme RMX3800 — NewPipe issue #11800 - real-device bug report (fingerprint verbatim); board=pineapple, ...
            DeviceProfile(
                manufacturer = "realme", brand = "realme", model = "RMX3800",
                device = "RE5C4FL1", product = "RMX3800",
                board = "pineapple", hardware = "qcom",
                fingerprint = "realme/RMX3800/RE5C4FL1:14/UKQ1.231108.001/U.18dc361-1-2:user/release-keys",
                buildId = "UKQ1.231108.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "unknown",
                androidId = "template",
            ),
// samsung SM-S901B — https://forum.gsmhosting.com/vbb/14977095-post3.html
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S901B",
                device = "r0s", product = "r0sxeea",
                board = "universal9925", hardware = "s5e9925",
                fingerprint = "samsung/r0sxeea/r0s:14/UP1A.231005.007/S901BXXS7DXAC:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-02-01",
                androidId = "template",
            ),
// samsung SM-A546B — https://forum.gsmhosting.com/vbb/f684/please-help-samsung-a54-mdm-solved-3293368/
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A546B",
                device = "a54x", product = "a54xnaeea",
                board = "erd8835", hardware = "s5e8835",
                fingerprint = "samsung/a54xnaeea/a54x:14/UP1A.231005.007/A546BXXS6BXC1:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-03-01",
                androidId = "template",
            ),
// samsung SM-S711B — https://forum.gsmhosting.com/vbb/f684/sm-s711b-s23-fe-mdm-lock-remove-3297771/
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S711B",
                device = "r11s", product = "r11sxins",
                board = "universal9925", hardware = "s5e9925",
                fingerprint = "samsung/r11sxins/r11s:14/UP1A.231005.007/S711BXXS2BXBG:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-03-01",
                androidId = "template",
            ),
// samsung SM-S906B — https://www.sammobile.com/samsung/galaxy-s22-plus/firmware/SM-S906B/EUY/download/S906BX...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S906B",
                device = "g0s", product = "g0sxeea",
                board = "universal9925", hardware = "s5e9925",
                fingerprint = "samsung/g0sxeea/g0s:14/UP1A.231005.007/S906BXXU6DWK4:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-11-01",
                androidId = "template",
            ),
// samsung SM-S908B — https://samfw.com/firmware/SM-S908B/MET/S908BXXU6DWK4
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S908B",
                device = "b0s", product = "b0sxeea",
                board = "universal9925", hardware = "s5e9925",
                fingerprint = "samsung/b0sxeea/b0s:14/UP1A.231005.007/S908BXXU6DWK4:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-11-01",
                androidId = "template",
            ),
// samsung SM-S916B — https://www.sammobile.com/samsung/galaxy-s23-plus/firmware/SM-S916B/PLS/download/S916BX...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S916B",
                device = "dm2q", product = "dm2qxxx",
                board = "kalama", hardware = "qcom",
                fingerprint = "samsung/dm2qxxx/dm2q:14/UP1A.231005.007/S916BXXU5CXE3:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-05-01",
                androidId = "template",
            ),
// samsung SM-S918B — https://www.sammobile.com/samsung/galaxy-s23-ultra/firmware/SM-S918B/ACR/download/S918B...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S918B",
                device = "dm3q", product = "dm3qxxx",
                board = "kalama", hardware = "qcom",
                fingerprint = "samsung/dm3qxxx/dm3q:14/UP1A.231005.007/S918BXXU6CXH7:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-08-01",
                androidId = "template",
            ),
// samsung SM-F936B — https://www.sammobile.com/samsung/galaxy-z-fold-4/firmware/SM-F936B/MID/download/F936BX...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-F936B",
                device = "q4q", product = "q4qxeea",
                board = "waipio", hardware = "qcom",
                fingerprint = "samsung/q4qxeea/q4q:14/UP1A.231005.007/F936BXXU4EWL1:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-12-01",
                androidId = "template",
            ),
// samsung SM-F721B — https://www.sammobile.com/samsung/galaxy-z-flip-4/firmware/SM-F721B/INS/download/F721BX...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-F721B",
                device = "b4q", product = "b4qxeea",
                board = "waipio", hardware = "qcom",
                fingerprint = "samsung/b4qxeea/b4q:14/UP1A.231005.007/F721BXXU4EWL1:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-12-01",
                androidId = "template",
            ),
// samsung SM-F946B — https://www.sammobile.com/samsung/galaxy-z-fold-5/firmware/SM-F946B/XSG/download/F946BX...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-F946B",
                device = "q5q", product = "q5qxxx",
                board = "kalama", hardware = "qcom",
                fingerprint = "samsung/q5qxxx/q5q:14/UP1A.231005.007/F946BXXU1BWKF:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-11-01",
                androidId = "template",
            ),
// samsung SM-F731B — https://www.sammobile.com/samsung/galaxy-z-flip-5/firmware/SM-F731B/SFR/download/F731BX...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-F731B",
                device = "b5q", product = "b5qxxx",
                board = "kalama", hardware = "qcom",
                fingerprint = "samsung/b5qxxx/b5q:14/UP1A.231005.007/F731BXXU1BWK9:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-11-01",
                androidId = "template",
            ),
// samsung SM-A536B — https://samfw.com/firmware/SM-A536B/EUX/A536BXXU7DWK6
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A536B",
                device = "a53x", product = "a53xnaeea",
                board = "universal8825", hardware = "s5e8825",
                fingerprint = "samsung/a53xnaeea/a53x:14/UP1A.231005.007/A536BXXU7DWK6:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2023-11-01",
                androidId = "template",
            ),
// Xiaomi Redmi 12 — Real-device crash log (axmolengine/axmol issue #2266) + GitHub dump (RandomPush); SPL f... [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "Redmi", model = "Redmi 12",
                device = "fire", product = "fire_global",
                board = "fire", hardware = "fire",
                fingerprint = "Redmi/fire_global/fire:14/UP1A.231005.007/V816.0.7.0.UMXMIXM:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, securityPatch = "2024-09-01",
                androidId = "template",
            ),
            // ---------------- Android 13 ----------------
// Google Pixel 6 — nippongsi Telegram (Pixel 13.0 GSI ported from Pixel 6)
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 6",
                device = "oriole", product = "oriole",
                board = "oriole", hardware = "oriole",
                fingerprint = "google/oriole/oriole:13/T1B1.220819.006/9012527:user/release-keys",
                buildId = "T1B1.220819.006", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2022-09-05",
                androidId = "template",
            ),
// Google Pixel 6 Pro — nippongsi Telegram (Pixel 13.0 GSI ported from Pixel 6 Pro); hardware=raven via Spotube...
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 6 Pro",
                device = "raven", product = "raven",
                board = "raven", hardware = "raven",
                fingerprint = "google/raven/raven:13/TQ1A.221205.011/9244662:user/release-keys",
                buildId = "TQ1A.221205.011", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2022-12-05",
                androidId = "template",
            ),
// Google Pixel 6a — godot_debug_draw_3d#41 crash log (real device 2024-03-12); hardware=bluejay via Lawncha...
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 6a",
                device = "bluejay", product = "bluejay",
                board = "bluejay", hardware = "bluejay",
                fingerprint = "google/bluejay/bluejay:13/TQ3A.230901.001/10750268:user/release-keys",
                buildId = "TQ3A.230901.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-09-05",
                androidId = "template",
            ),
// Google Pixel 7 — tensorflow#66721 real-device getprop log (ro.hardware=panther, board=panther); patch 20...
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 7",
                device = "panther", product = "panther",
                board = "panther", hardware = "panther",
                fingerprint = "google/panther/panther:13/TQ1A.221205.011/9244662:user/release-keys",
                buildId = "TQ1A.221205.011", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2022-12-05",
                androidId = "template",
            ),
// Google Pixel 7 Pro — dumpithere/google_cheetah_dump README; build ID matches guise-verified Google OTA cheet...
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 7 Pro",
                device = "cheetah", product = "cheetah",
                board = "cheetah", hardware = "cheetah",
                fingerprint = "google/cheetah/cheetah:13/TQ3A.230901.001.C2/10753682:user/release-keys",
                buildId = "TQ3A.230901.001.C2", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-09-05",
                androidId = "template",
            ),
// Google Pixel 7a — android_dumps Telegram; build ID matches guise-verified Google OTA lynx-ota-tq3a.230901...
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 7a",
                device = "lynx", product = "lynx",
                board = "lynx", hardware = "lynx",
                fingerprint = "google/lynx/lynx:13/TQ3A.230901.001.C3/10753802:user/release-keys",
                buildId = "TQ3A.230901.001.C3", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-09-05",
                androidId = "template",
            ),
// Google Pixel Fold — nippongsi Telegram tgoop.com/nippongsi/7997 (Pixel 13.0 GSI ported from Pixel Fold, pat...
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel Fold",
                device = "felix", product = "felix",
                board = "felix", hardware = "felix",
                fingerprint = "google/felix/felix:13/TQ3C.230805.001.A3/10345103:user/release-keys",
                buildId = "TQ3C.230805.001.A3", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-08-05",
                androidId = "template",
            ),
// samsung SM-A336B — PDA+patch+OS13 from samfw SM-A336B TIM detail page; build ID from NSYS report of real S...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A336B",
                device = "a33x", product = "a33x",
                board = "s5e8825", hardware = "exynos1280",
                fingerprint = "samsung/a33x/a33x:13/TP1A.220624.014/A336BXXU7CWH2:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-08-01",
                androidId = "template",
            ),
// samsung SM-A346B — PDA+patch from sammobile SM-A346B multi-region listing (launch firmware); codename a34x...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A346B",
                device = "a34x", product = "a34x",
                board = "a34x", hardware = "mt6877",
                fingerprint = "samsung/a34x/a34x:13/TP1A.220624.014/A346BXXU1AWB9:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-02-01",
                androidId = "template",
            ),
// samsung SM-A528B — PDA+patch from samfw SM-A528B SLK detail page; codename a52sxq twrp.me; board/hardware ...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A528B",
                device = "a52sxq", product = "a52sxq",
                board = "lahaina", hardware = "lahaina",
                fingerprint = "samsung/a52sxq/a52sxq:13/TP1A.220624.014/A528BXXS5EWK1:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-11-01",
                androidId = "template",
            ),
// samsung SM-A725F — PDA+patch+T(13) from samfw SM-A725F INS detail page (last A13); sammobile INS listing; ...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A725F",
                device = "a72q", product = "a72q",
                board = "atoll", hardware = "atoll",
                fingerprint = "samsung/a72q/a72q:13/TP1A.220624.014/A725FXXU6DWH2:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-09-01",
                androidId = "template",
            ),
// samsung SM-A736B — PDA+patch+T(13) from samfw SM-A736B INS detail page (last A13); sammobile INS listing; ...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A736B",
                device = "a73xq", product = "a73xq",
                board = "lahaina", hardware = "lahaina",
                fingerprint = "samsung/a73xq/a73xq:13/TP1A.220624.014/A736BXXU5CWH7:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-08-01",
                androidId = "template",
            ),
// samsung SM-M546B — PDA+patch from samfw SM-M546B XID detail page; codename m54x Wikipedia + device tree; b...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-M546B",
                device = "m54x", product = "m54x",
                board = "s5e8835", hardware = "exynos1380",
                fingerprint = "samsung/m54x/m54x:13/TP1A.220624.014/M546BXXS3AWK2:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-11-01",
                androidId = "template",
            ),
// samsung SM-G990B — PDA+patch+T(13) from samfw SM-G990B EUX detail page (last A13); sammobile EUX/TMS listi...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-G990B",
                device = "r9q", product = "r9q",
                board = "lahaina", hardware = "lahaina",
                fingerprint = "samsung/r9q/r9q:13/TP1A.220624.014/G990BXXS6EWJB:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-11-01",
                androidId = "template",
            ),
// Nothing A063 — Spotube GitHub issue #849 - real-device bug dump. Display build T2.0-231006-1014 (Nothi...
            DeviceProfile(
                manufacturer = "Nothing", brand = "Nothing", model = "A063",
                device = "Spacewar", product = "SpacewarEEA",
                board = "Spacewar", hardware = "qcom",
                fingerprint = "Nothing/SpacewarEEA/Spacewar:13/TKQ1.221220.001/2310061014:user/release-keys",
                buildId = "TKQ1.221220.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-09-01",
                androidId = "template",
            ),
// OnePlus CPH2449 — NoesisGUI forum Unity crash log, device timestamp 2023-06-22 (EU variant). https://www....
            DeviceProfile(
                manufacturer = "OnePlus", brand = "OnePlus", model = "CPH2449",
                device = "OP594DL1", product = "CPH2449EEA",
                board = "kalama", hardware = "qcom",
                fingerprint = "OnePlus/CPH2449EEA/OP594DL1:13/TP1A.220905.001/T.R4T3.ff1adb_83b7-4dfeb:user/release-keys",
                buildId = "TP1A.220905.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "unknown",
                androidId = "template",
            ),
// OnePlus CPH2451 — infinity-for-reddit issue #1396 - real-device crash report (NA variant). Display CPH245...
            DeviceProfile(
                manufacturer = "OnePlus", brand = "OnePlus", model = "CPH2451",
                device = "OP594DL1", product = "CPH2451",
                board = "kalama", hardware = "qcom",
                fingerprint = "OnePlus/CPH2451/OP594DL1:13/TP1A.220905.001/T.R4T3.ded3a6-6ce9-6cea:user/release-keys",
                buildId = "TP1A.220905.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "unknown",
                androidId = "template",
            ),
// OnePlus LE2125 — XDA Play Integrity Fix thread (page-158) - DroidGuard log from real LE2125 device (NA v... [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "OnePlus", brand = "OnePlus", model = "LE2125",
                device = "OnePlus9Pro", product = "OnePlus9Pro",
                board = "OnePlus9Pro", hardware = "OnePlus9Pro",
                fingerprint = "OnePlus/OnePlus9Pro/OnePlus9Pro:13/TP1A.220905.001/R.12ee130-1f9aa-ffaae:user/release-keys",
                buildId = "TP1A.220905.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "unknown",
                androidId = "template",
            ),
// OnePlus CPH2401 — celzero/rethink-app issue #1290 (RethinkDNS ANR report, 2024-03-15) - real-device Build... [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "OnePlus", brand = "OnePlus", model = "CPH2401",
                device = "OP557AL1", product = "CPH2401",
                board = "OP557AL1", hardware = "OP557AL1",
                fingerprint = "OnePlus/CPH2401/OP557AL1:13/TP1A.220905.001/S.14492ff_1:user/release-keys",
                buildId = "TP1A.220905.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "unknown",
                androidId = "template",
            ),
// motorola XT2203 — XDA thread 'Motorola Edge 30 remove Brand' (user cruero, Italy, TIM carrier) - real-dev... [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "motorola", brand = "motorola", model = "XT2203",
                device = "dubai", product = "dubai_ge",
                board = "dubai", hardware = "dubai",
                fingerprint = "motorola/dubai_ge/dubai:13/T1RDS33.116-33-3-2/8c190-e94ca3:user/release-keys",
                buildId = "T1RDS33.116-33-3-2", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "unknown",
                androidId = "template",
            ),
// motorola XT2301 — t.me/android_dumps channel (device-dump channel, April 2023) - 'Device: rtwo, Version: 13' [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "motorola", brand = "motorola", model = "XT2301",
                device = "rtwo", product = "rtwo",
                board = "rtwo", hardware = "rtwo",
                fingerprint = "motorola/rtwo/rtwo:13/T1TR33.43-20-15/a7538:user/release-keys",
                buildId = "T1TR33.43-20-15", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "unknown",
                androidId = "template",
            ),
// Xiaomi 23049PCD8G — XDA Evolution X crash report (real device log); SPL from miuidownloader.com V14.0.7.0.T...
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "POCO", model = "23049PCD8G",
                device = "marble", product = "marble_global",
                board = "marble", hardware = "qcom",
                fingerprint = "POCO/marble_global/marble:13/SKQ1.221022.001/V14.0.7.0.TMRMIXM:user/release-keys",
                buildId = "SKQ1.221022.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-09-01",
                androidId = "template",
            ),
// Xiaomi 22081212UG — damru DB (akwin1234/damru) fingerprint; SPL from miuidownloader.com V14.0.1.0.TLFMIXM c...
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "Xiaomi", model = "22081212UG",
                device = "diting", product = "diting_global",
                board = "diting", hardware = "qcom",
                fingerprint = "Xiaomi/diting_global/diting:13/TKQ1.220829.002/V14.0.1.0.TLFMIXM:user/release-keys",
                buildId = "TKQ1.220829.002", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-01-01",
                androidId = "template",
            ),
// Xiaomi 22071212AG — damru DB fingerprint; SPL from miuidownloader.com V14.0.4.0.TLQMIXM changelog; model 22...
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "Xiaomi", model = "22071212AG",
                device = "plato", product = "plato_global",
                board = "plato", hardware = "qcom",
                fingerprint = "Xiaomi/plato_global/plato:13/TP1A.220624.014/V14.0.4.0.TLQMIXM:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-03-01",
                androidId = "template",
            ),
// Xiaomi 22021211RG — leddaz-dump-stash/redmi_munch_dump GitHub (real device dump); SPL from miuidownloader.c...
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "POCO", model = "22021211RG",
                device = "munch", product = "munch_global",
                board = "munch", hardware = "qcom",
                fingerprint = "POCO/munch_global/munch:13/RKQ1.211001.001/V14.0.3.0.TLMMIXM:user/release-keys",
                buildId = "RKQ1.211001.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-06-01",
                androidId = "template",
            ),
// OPPO PGEM10 — Kvaesitso issue #1245 - real-device bug report (fingerprint verbatim); board=kalama, ha...
            DeviceProfile(
                manufacturer = "OPPO", brand = "OPPO", model = "PGEM10",
                device = "OP528BL1", product = "PGEM10",
                board = "kalama", hardware = "qcom",
                fingerprint = "OPPO/PGEM10/OP528BL1:13/TP1A.220905.001/T.1414b70_5a66-19f048:user/release-keys",
                buildId = "TP1A.220905.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "unknown",
                androidId = "template",
            ),
// samsung SM-S918B — https://forum.gsmhosting.com/vbb/f684/octoplus-samsung-software-v-4-2-6-out-3206011/ind...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S918B",
                device = "dm3q", product = "dm3qxxx",
                board = "kalama", hardware = "qcom",
                fingerprint = "samsung/dm3qxxx/dm3q:13/TP1A.220624.014/S918BXXU1AWA6:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-01-01",
                androidId = "template",
            ),
// samsung SM-A536B — https://forum.gsmhosting.com/vbb/f684/samsung-a53-5g-sm-a536b-baypass-kg-locked-done-31...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A536B",
                device = "a53x", product = "a53xnaeea",
                board = "universal8825", hardware = "s5e8825",
                fingerprint = "samsung/a53xnaeea/a53x:13/TP1A.220624.014/A536BXXS4BWA2:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-01-01",
                androidId = "template",
            ),
// samsung SM-S901B — https://samfw.com/firmware/SM-S901B/EUX/S901BXXU3CWAI
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S901B",
                device = "r0s", product = "r0sxeea",
                board = "universal9925", hardware = "s5e9925",
                fingerprint = "samsung/r0sxeea/r0s:13/TP1A.220624.014/S901BXXU3CWAI:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-02-01",
                androidId = "template",
            ),
// samsung SM-S906B — https://www.sammobile.com/samsung/galaxy-s22-plus/firmware/SM-S906B/SER/download/S906BX...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S906B",
                device = "g0s", product = "g0sxeea",
                board = "universal9925", hardware = "s5e9925",
                fingerprint = "samsung/g0sxeea/g0s:13/TP1A.220624.014/S906BXXU3CWAI:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-02-01",
                androidId = "template",
            ),
// samsung SM-S908B — https://samfw.com/firmware/SM-S908B/EUX/S908BXXU3CWAI
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S908B",
                device = "b0s", product = "b0sxeea",
                board = "universal9925", hardware = "s5e9925",
                fingerprint = "samsung/b0sxeea/b0s:13/TP1A.220624.014/S908BXXU3CWAI:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-02-01",
                androidId = "template",
            ),
// samsung SM-S911B — https://samfw.com/firmware/SM-S911B/EVR/S911BXXU2AWF1
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S911B",
                device = "dm1q", product = "dm1qxxx",
                board = "kalama", hardware = "qcom",
                fingerprint = "samsung/dm1qxxx/dm1q:13/TP1A.220624.014/S911BXXU2AWF1:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-06-01",
                androidId = "template",
            ),
// samsung SM-S916B — https://www.sammobile.com/samsung/galaxy-s23-plus/firmware/SM-S916B/XFE/download/S916BX...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S916B",
                device = "dm2q", product = "dm2qxxx",
                board = "kalama", hardware = "qcom",
                fingerprint = "samsung/dm2qxxx/dm2q:13/TP1A.220624.014/S916BXXU1AWC8:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-04-01",
                androidId = "template",
            ),
// samsung SM-F936B — https://www.sammobile.com/samsung/galaxy-z-fold-4/firmware/SM-F936B/INS/download/F936BX...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-F936B",
                device = "q4q", product = "q4qxeea",
                board = "waipio", hardware = "qcom",
                fingerprint = "samsung/q4qxeea/q4q:13/TP1A.220624.014/F936BXXU1BVL7:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2022-12-01",
                androidId = "template",
            ),
// samsung SM-F721B — https://samfw.com/firmware/SM-F721B/ZTM/F721BXXU1BVL9
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-F721B",
                device = "b4q", product = "b4qxeea",
                board = "waipio", hardware = "qcom",
                fingerprint = "samsung/b4qxeea/b4q:13/TP1A.220624.014/F721BXXU1BVL9:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2022-12-01",
                androidId = "template",
            ),
// samsung SM-F946B — https://samfw.com/firmware/SM-F946B/TIM/F946BXXU1AWI3
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-F946B",
                device = "q5q", product = "q5qxxx",
                board = "kalama", hardware = "qcom",
                fingerprint = "samsung/q5qxxx/q5q:13/TP1A.220624.014/F946BXXU1AWI3:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-09-01",
                androidId = "template",
            ),
// samsung SM-F731B — https://www.sammobile.com/samsung/galaxy-z-flip-5/firmware/SM-F731B/MOB/download/F731BX...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-F731B",
                device = "b5q", product = "b5qxxx",
                board = "kalama", hardware = "qcom",
                fingerprint = "samsung/b5qxxx/b5q:13/TP1A.220624.014/F731BXXU1AWI3:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-09-01",
                androidId = "template",
            ),
// samsung SM-A546B — https://www.sammobile.com/samsung/galaxy-a54-5g/firmware/SM-A546B/MET/download/A546BXXU...
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-A546B",
                device = "a54x", product = "a54xnaeea",
                board = "erd8835", hardware = "s5e8835",
                fingerprint = "samsung/a54xnaeea/a54x:13/TP1A.220624.014/A546BXXU1AWB7:user/release-keys",
                buildId = "TP1A.220624.014", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-02-01",
                androidId = "template",
            ),
// Xiaomi 2201117TG — GitHub dump (parixxshit/redmi_spes_dump) + real-device crash report (GrayJay issue #228) [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "Redmi", model = "2201117TG",
                device = "spes", product = "spes_global",
                board = "bengal", hardware = "spes",
                fingerprint = "Redmi/spes_global/spes:13/TKQ1.221114.001/V14.0.3.0.TGCMIXM:user/release-keys",
                buildId = "TKQ1.221114.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2023-07-01",
                androidId = "template",
            ),
// Xiaomi 22101320G — GitHub dump (gm-stuffs/redmi_redwood_dump) [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "Redmi", model = "22101320G",
                device = "redwood", product = "redwood",
                board = "redwood", hardware = "redwood",
                fingerprint = "Redmi/redwood/redwood:13/RKQ1.211001.001/V816.0.4.0.UMSMIXM:user/release-keys",
                buildId = "RKQ1.211001.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2024-05-01",
                androidId = "template",
            ),
// Xiaomi 23124RA7EO — GitHub dump (gm-stuffs/redmi_sapphire_dump); cross-checked via telegram channel post + ... [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "Redmi", model = "23124RA7EO",
                device = "sapphire", product = "sapphire_global",
                board = "bengal", hardware = "sapphire",
                fingerprint = "Redmi/sapphire_global/sapphire:13/TKQ1.221114.001/V816.0.3.0.UNGMIXM:user/release-keys",
                buildId = "TKQ1.221114.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2024-05-01",
                androidId = "template",
            ),
// Xiaomi Redmi Note 13 Pro 5G — GitHub dump (gm-stuffs/redmi_garnet_dump) [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "Xiaomi", model = "Redmi Note 13 Pro 5G",
                device = "garnet", product = "garnet",
                board = "parrot", hardware = "garnet",
                fingerprint = "Xiaomi/garnet/garnet:13/SKQ1.230401.001/V14.0.9.0.TNRMIXM:user/release-keys",
                buildId = "SKQ1.230401.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "2024-03-01",
                androidId = "template",
            ),
// Xiaomi 22111317PG — Telegram android_dumps channel (2 corroborating posts: ID V14.0.4.0.TMPIDXM + Global V1... [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "POCO", model = "22111317PG",
                device = "moonstone", product = "moonstone_p_global",
                board = "moonstone", hardware = "moonstone",
                fingerprint = "POCO/moonstone_p_global/moonstone:13/TKQ1.221114.001/V14.0.5.0.TMPMIXM:user/release-keys",
                buildId = "TKQ1.221114.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "unknown",
                androidId = "template",
            ),
// Xiaomi 23021RAAEG — Telegram android_dumps channel (MIUI 14 post) [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "Redmi", model = "23021RAAEG",
                device = "tapas", product = "tapas_global",
                board = "bengal", hardware = "tapas",
                fingerprint = "Redmi/tapas_global/tapas:13/TKQ1.221114.001/V14.0.6.0.TMTMIXM:user/release-keys",
                buildId = "TKQ1.221114.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "unknown",
                androidId = "template",
            ),
// realme RMX3741 — Telegram android_dumps channel [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "realme", brand = "realme", model = "RMX3741",
                device = "RE58B6L1", product = "RMX3741",
                board = "RE58B6L1", hardware = "RE58B6L1",
                fingerprint = "realme/RMX3741/RE58B6L1:13/SP1A.210812.016/R4T2.T.1321d8c-9fcc:user/release-keys",
                buildId = "SP1A.210812.016", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "unknown",
                androidId = "template",
            ),
// realme RMX3771 — Telegram android_dumps channel [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "realme", brand = "realme", model = "RMX3771",
                device = "RE58B8L1", product = "RMX3771",
                board = "RE58B8L1", hardware = "RE58B8L1",
                fingerprint = "realme/RMX3771/RE58B8L1:13/SP1A.210812.016/R4T2.T.1321d8c_15e5d:user/release-keys",
                buildId = "SP1A.210812.016", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "unknown",
                androidId = "template",
            ),
// realme RMX3740 — Telegram android_dumps channel [board/hw fallback to device codename]
            DeviceProfile(
                manufacturer = "realme", brand = "realme", model = "RMX3740",
                device = "RE5865", product = "RMX3740",
                board = "RE5865", hardware = "RE5865",
                fingerprint = "realme/RMX3740/RE5865:13/SP1A.210812.016/T.1311add_1:user/release-keys",
                buildId = "SP1A.210812.016", buildTags = "release-keys", buildType = "user",
                androidVersion = "13", apiLevel = 33, securityPatch = "unknown",
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
 *     "countryIso": "us", "deviceId": "<16 hex>", "subscriberId": "<15 digits>",
 *     "networkType": 13 }
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
    val locale: LocaleInfo,
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
        /** Build.DISPLAY — derived per-OEM pattern, see [deriveDisplayId]. */
        val displayId: String,
        /** Build.VERSION.INCREMENTAL — parsed from [fingerprint]. */
        val buildIncremental: String,
        /** Plausible kernel for `System.getProperty("os.version")`. */
        val kernelVersion: String,
        /** Build.TIME — derived from the security-patch date (builds ship around their SPL). */
        val buildTime: Long,
        /** Build.USER — constant "android-build", as on real factory builds. */
        val buildUser: String,
        /** Build.HOST — constant "abfarm", as on real factory builds. */
        val buildHost: String,
        /** Build.BOOTLOADER — "unknown" (what most retail devices report here). */
        val bootloader: String,
        /** Build.RADIO — "unknown" (ditto). */
        val radio: String,
        /** Build.SOC_MANUFACTURER — derived from the hardware string; "" = unknown, not patched. */
        val socManufacturer: String,
        /** Build.SOC_MODEL — derived from the hardware string; "" = unknown, not patched. */
        val socModel: String,
        /** Prebuilt WebView default user-agent (model + build ID already spoofed). */
        val webViewUa: String,
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
        /** Per-identity wlan0 MAC (locally administered) for NetworkInterface.getHardwareAddress. */
        val wifiMac: String,
        /** Per-identity Bluetooth MAC for BluetoothAdapter.getAddress. */
        val bluetoothMac: String,
    )

    data class TelephonyInfo(
        val operatorName: String,
        val operatorNumeric: String,
        val countryIso: String,
        val deviceId: String,
        val subscriberId: String,
        val networkType: Int,
        /** Per-identity ICCID (19-20 digits, 89 prefix) for getSimSerialNumber. */
        val simSerial: String,
        /** Per-identity MSISDN (E.164) for getLine1Number — never the real SIM number. */
        val phoneNumber: String,
    )

    data class LocaleInfo(
        /** IANA zone derived from the profile city, e.g. "Africa/Lagos". */
        val timezoneId: String,
        /** BCP-47 tag derived from the profile country, e.g. "en-NG". */
        val localeTag: String,
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
        jname("securityPatch"); append(':'); jstr(device.securityPatch); append(',')
        jname("displayId"); append(':'); jstr(device.displayId); append(',')
        jname("buildIncremental"); append(':'); jstr(device.buildIncremental); append(',')
        jname("kernelVersion"); append(':'); jstr(device.kernelVersion); append(',')
        jname("buildTime"); append(':'); append(device.buildTime.toString()); append(',')
        jname("buildUser"); append(':'); jstr(device.buildUser); append(',')
        jname("buildHost"); append(':'); jstr(device.buildHost); append(',')
        jname("bootloader"); append(':'); jstr(device.bootloader); append(',')
        jname("radio"); append(':'); jstr(device.radio); append(',')
        jname("socManufacturer"); append(':'); jstr(device.socManufacturer); append(',')
        jname("socModel"); append(':'); jstr(device.socModel); append(',')
        jname("webViewUa"); append(':'); jstr(device.webViewUa)
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
        jname("transport"); append(':'); jstr(network.transport); append(',')
        jname("wifiMac"); append(':'); jstr(network.wifiMac); append(',')
        jname("bluetoothMac"); append(':'); jstr(network.bluetoothMac)
        append("},")
        append("\"telephony\":{")
        jname("operatorName"); append(':'); jstr(telephony.operatorName); append(',')
        jname("operatorNumeric"); append(':'); jstr(telephony.operatorNumeric); append(',')
        jname("countryIso"); append(':'); jstr(telephony.countryIso); append(',')
        jname("deviceId"); append(':'); jstr(telephony.deviceId); append(',')
        jname("subscriberId"); append(':'); jstr(telephony.subscriberId); append(',')
        jname("networkType"); append(':'); append(telephony.networkType.toString()); append(',')
        jname("simSerial"); append(':'); jstr(telephony.simSerial); append(',')
        jname("phoneNumber"); append(':'); jstr(telephony.phoneNumber)
        append("},")
        append("\"locale\":{")
        jname("timezoneId"); append(':'); jstr(locale.timezoneId); append(',')
        jname("localeTag"); append(':'); jstr(locale.localeTag)
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

    /**
     * Per-identity `/system/build.prop` content. The engine redirects guest
     * reads of `/system/build.prop` (and `getprop` with no key) to this
     * file, so file-level readers see the spoofed identity instead of the
     * host's real fingerprint. Only non-empty values are emitted.
     */
    fun toBuildProp(): String = buildString {
        append("# per-identity spoofed build.prop — generated, do not edit\n")
        fun prop(key: String, value: String) {
            if (value.isNotEmpty()) append(key).append('=').append(value).append('\n')
        }
        val d = device
        prop("ro.product.manufacturer", d.manufacturer)
        prop("ro.product.brand", d.brand)
        prop("ro.product.model", d.model)
        prop("ro.product.device", d.device)
        prop("ro.product.name", d.product)
        prop("ro.product.board", d.board)
        prop("ro.build.id", d.buildId)
        prop("ro.build.display.id", d.displayId)
        prop("ro.build.version.incremental", d.buildIncremental)
        prop("ro.build.version.sdk", d.apiLevel.toString())
        prop("ro.build.version.release", d.androidVersion)
        prop("ro.build.version.security_patch", d.securityPatch)
        if (d.buildTime > 0) prop("ro.build.date.utc", (d.buildTime / 1000).toString())
        prop("ro.build.type", d.buildType)
        prop("ro.build.user", d.buildUser)
        prop("ro.build.host", d.buildHost)
        prop("ro.build.tags", d.buildTags)
        prop("ro.build.fingerprint", d.fingerprint)
        prop("ro.soc.manufacturer", d.socManufacturer)
        prop("ro.soc.model", d.socModel)
        prop("ro.debuggable", "0")
        prop("ro.secure", "1")
        // Real, non-identifying hardware facts (same on any arm64 phone).
        prop("ro.product.cpu.abi", "arm64-v8a")
        prop("ro.product.cpu.abilist", "arm64-v8a,armeabi-v7a,armeabi")
    }

    /**
     * Per-identity `/proc/version` content. Format mirrors real kernels:
     * `Linux version <ver> (<builder>) #1 SMP PREEMPT <date>`.
     */
    fun toProcVersion(): String {
        val kernel = device.kernelVersion.ifEmpty { "5.10.107-android13-4-00001-g000000000000" }
        val date = if (device.buildTime > 0) {
            java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.US)
                .apply { timeZone = java.util.TimeZone.getTimeZone("UTC") }
                .format(java.util.Date(device.buildTime))
        } else {
            "Thu Nov 02 12:00:00 UTC 2023"
        }
        return "Linux version $kernel (android-build@abfarm) #1 SMP PREEMPT $date\n"
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
                device = run {
                    val fingerprint = getStr(d, "fingerprint")
                    val buildId = getStr(d, "buildId")
                    val brand = getStr(d, "brand")
                    val apiLevel = getInt(d, "apiLevel")
                    val incremental = (d["buildIncremental"] as? String)
                        ?.takeIf { it.isNotEmpty() }
                        ?: deriveIncremental(fingerprint)
                    DeviceInfo(
                    manufacturer = getStr(d, "manufacturer"),
                    brand = brand,
                    model = getStr(d, "model"),
                    device = getStr(d, "device"),
                    product = getStr(d, "product"),
                    board = getStr(d, "board"),
                    hardware = getStr(d, "hardware"),
                    fingerprint = fingerprint,
                    buildId = buildId,
                    buildTags = getStr(d, "buildTags"),
                    buildType = getStr(d, "buildType"),
                    androidVersion = getStr(d, "androidVersion"),
                    apiLevel = apiLevel,
                    securityPatch = getStr(d, "securityPatch"),
                    // Tolerant: profiles persisted before 2026-10-02 lack
                    // these fields — derive the same values ProfileGenerator
                    // would have written.
                    displayId = (d["displayId"] as? String)
                        ?.takeIf { it.isNotEmpty() }
                        ?: deriveDisplayId(brand, buildId, incremental),
                    buildIncremental = incremental,
                    kernelVersion = (d["kernelVersion"] as? String)
                        ?.takeIf { it.isNotEmpty() }
                        ?: kernelForApi(apiLevel),
                    buildTime = (d["buildTime"] as? Number)?.toLong()
                        ?: deriveBuildTime(getStr(d, "securityPatch")),
                    buildUser = (d["buildUser"] as? String)
                        ?.takeIf { it.isNotEmpty() } ?: "android-build",
                    buildHost = (d["buildHost"] as? String)
                        ?.takeIf { it.isNotEmpty() } ?: "abfarm",
                    bootloader = (d["bootloader"] as? String)
                        ?.takeIf { it.isNotEmpty() } ?: "unknown",
                    radio = (d["radio"] as? String)
                        ?.takeIf { it.isNotEmpty() } ?: "unknown",
                    socManufacturer = (d["socManufacturer"] as? String) ?: "",
                    socModel = (d["socModel"] as? String) ?: "",
                    webViewUa = (d["webViewUa"] as? String)
                        ?.takeIf { it.isNotEmpty() }
                        ?: buildWebViewUa(
                            getStr(d, "androidVersion"),
                            getStr(d, "model"),
                            buildId,
                        ),
                    )
                },
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
                    // Tolerant: profiles persisted before 2026-10-02 lack
                    // these; derive fresh random values so old identities
                    // still get coherent spoofing.
                    wifiMac = (n["wifiMac"] as? String)
                        ?.takeIf { it.isNotEmpty() } ?: newLocalMac(),
                    bluetoothMac = (n["bluetoothMac"] as? String)
                        ?.takeIf { it.isNotEmpty() } ?: newLocalMac(),
                ),
                telephony = TelephonyInfo(
                    operatorName = getStr(t, "operatorName"),
                    operatorNumeric = getStr(t, "operatorNumeric"),
                    countryIso = getStr(t, "countryIso"),
                    deviceId = getStr(t, "deviceId"),
                    // Tolerant: profiles persisted before networkType existed
                    // (e.g. pre-2026-10-01 identities) default to LTE.
                    networkType = (t["networkType"] as? Number)?.toInt() ?: 13,
                    subscriberId = getStr(t, "subscriberId"),
                    simSerial = (t["simSerial"] as? String)
                        ?.takeIf { it.isNotEmpty() } ?: newSimSerial(),
                    // Tolerant: profiles persisted before phoneNumber existed
                    // get a fresh per-identity number rather than failing.
                    phoneNumber = (t["phoneNumber"] as? String)
                        ?.takeIf { it.isNotEmpty() }
                        ?: newPhoneNumber(getStr(t, "countryIso")),
                ),
                locale = run {
                    val lo = root["locale"] as? Map<*, *>
                    val iso = getStr(t, "countryIso")
                    LocaleInfo(
                        timezoneId = (lo?.get("timezoneId") as? String)
                            ?.takeIf { it.isNotEmpty() }
                            ?: timezoneFor(iso),
                        localeTag = (lo?.get("localeTag") as? String)
                            ?.takeIf { it.isNotEmpty() }
                            ?: localeFor(iso),
                    )
                },
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
