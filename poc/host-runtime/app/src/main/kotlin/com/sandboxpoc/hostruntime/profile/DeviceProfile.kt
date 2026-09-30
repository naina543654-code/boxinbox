package com.sandboxpoc.hostruntime.profile

/**
 * Spoofed device profile for one virtual identity. `extras` is an extensible
 * map for future engine-specific fields (serial, IMEI-shaped values, MAC,
 * telephony props, …). The core fields mirror android.os.Build so an engine
 * adapter can map them 1:1 — see INTEGRATION.md.
 */
data class DeviceProfile(
    val manufacturer: String,
    val brand: String,
    val model: String,
    val device: String,
    val product: String,
    val fingerprint: String,
    val buildId: String,
    val buildTags: String,
    val buildType: String,
    val androidVersion: String,
    val apiLevel: Int,
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
         * fingerprint/buildId/androidVersion/apiLevel are logically
         * compatible — NOT independently random). ProfileGenerator picks a
         * row and only regenerates the per-identity fields (androidId, and
         * optionally a build-number suffix inside extras).
         */
        internal val DEVICE_TABLE: List<DeviceProfile> = listOf(
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 8",
                device = "shiba", product = "shiba",
                fingerprint = "google/shiba/shiba:14/UQ1A.240205.002/12038998:user/release-keys",
                buildId = "UQ1A.240205.002", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 8 Pro",
                device = "husky", product = "husky",
                fingerprint = "google/husky/husky:14/UQ1A.240205.002/12038998:user/release-keys",
                buildId = "UQ1A.240205.002", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "Google", brand = "google", model = "Pixel 9",
                device = "tokay", product = "tokay",
                fingerprint = "google/tokay/tokay:14/AP1A.240905.019.A1/12999725:user/release-keys",
                buildId = "AP1A.240905.019.A1", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S921B",
                device = "dm1q", product = "dm1qx",
                fingerprint = "samsung/dm1qx/dm1q:14/UP1A.231005.007/S921BXXS3AXGF:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "samsung", brand = "samsung", model = "SM-S911B",
                device = "dm2q", product = "dm2qx",
                fingerprint = "samsung/dm2qx/dm2q:14/UP1A.231005.007/S911BXXS6CXHA:user/release-keys",
                buildId = "UP1A.231005.007", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "OnePlus", brand = "OnePlus", model = "CPH2581",
                device = "OP5958L1", product = "CPH2581",
                fingerprint = "OnePlus/CPH2581/OP5958L1:14/UKQ1.230924.001/1721980182:user/release-keys",
                buildId = "UKQ1.230924.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "Xiaomi", brand = "Xiaomi", model = "23127PN0CG",
                device = "houji", product = "houji_global",
                fingerprint = "Xiaomi/houji_global/houji:14/UKQ1.230924.001/V816.0.4.0.UNAMIXM:user/release-keys",
                buildId = "UKQ1.230924.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "Nothing", brand = "Nothing", model = "A065",
                device = "Pong", product = "Pong",
                fingerprint = "Nothing/Pong/Pong:14/UKQ1.230924.001/2407080000:user/release-keys",
                buildId = "UKQ1.230924.001", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "motorola", brand = "motorola", model = "XT2303-2",
                device = "lyriq", product = "lyriq_retail",
                fingerprint = "motorola/lyriq_retail/lyriq:14/U1TDS34.94-20-9-7/0f1a2:user/release-keys",
                buildId = "U1TDS34.94-20-9-7", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, androidId = "template",
            ),
            DeviceProfile(
                manufacturer = "asus", brand = "asus", model = "ASUS_AI2401",
                device = "AI2401", product = "WW_AI2401",
                fingerprint = "asus/WW_AI2401/AI2401:14/WW_34.1420.1420.114/0:user/release-keys",
                buildId = "WW_34.1420.1420.114", buildTags = "release-keys", buildType = "user",
                androidVersion = "14", apiLevel = 34, androidId = "template",
            ),
        )
    }
}
