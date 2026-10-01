# Spoofing Matrix — PoC v2

**Builds:** `apks/host-runtime-poc-v2.apk` (`com.sandboxpoc.hostruntime`),
`apks/probe-v2.apk` (`com.sandboxpoc.probe`).
**Engine:** zitanioi/blackbox @ `c994edf` + PoC spoof patches (see below).

**Capability states:**
- **Supported** — enforced in code on the guest path; on-device behavior still
  needs Jason's device test (nothing here is claimed from a device run).
- **Partially Supported** — enforced on some paths; a known path leaks the
  host value. Never silent: the leak is documented, not hidden.
- **Experimental** — enforced via a mechanism that is version-fragile or
  fail-open (Pine ART hooks); works when the hook lands, passes through when
  it doesn't.
- **Unsupported** — deliberately not built; the precise blocker is documented.
- **Host Provided** — intentionally left as the host value by design (spoofing
  it would break the runtime).

**Rule (no silent fallback):** when no valid spoof profile is active, every
hook passes through or keeps the engine's legacy behavior — the guest is
never told a host value is virtual.

---

## Profile generation (host side, engine-independent)

| Feature | State | Enforcement path | Probe verification | Device test |
|---|---|---|---|---|
| Coherent device profile (manufacturer↔brand↔model↔fingerprint↔board/hardware/patch) | Supported | `host-runtime/.../profile/DeviceProfile.kt` — 84 verified rows (47× API 33 + 37× API 34, 10 brands); row picked with `apiLevel == Build.VERSION.SDK_INT`, else nearest | ProbeV2 Build rows: PASS = observed exactly equals the row | Confirm the picked row matches the device's API on-device |
| Extended build metadata (TIME/USER/HOST/BOOTLOADER/RADIO) | Supported | `ProfileGenerator.kt` — TIME derived from the security-patch date (12:00 UTC); USER `android-build`, HOST `abfarm`, BOOTLOADER/RADIO `unknown` (what retail devices report) | ProbeV2 `Build.TIME/USER/HOST/BOOTLOADER/RADIO` rows | Must PASS |
| SoC manufacturer/model (ro.soc.*) | Partially Supported | `socFor(hardware)` in `DeviceProfile.kt` — only verified mappings (Exynos 2400/1480/1380/1280, Snapdragon 888/720G, Dimensity 900, Tensor/Tensor G2); unknown hardware → `""` → engine skips the patch, never fabricates | ProbeV2 `Build.SOC_*` rows; UNKNOWN on unmapped hardware | PASS on mapped rows |
| Prebuilt WebView UA (model+buildId spoofed) | Supported | `buildWebViewUa()` in `DeviceProfile.kt`; engine returns it verbatim | ProbeV2 `webview.defaultUa` row (exact match) | Must PASS |
| Fresh ICCID per identity (19-digit, 89 prefix) | Supported | `newSimSerial()` in `DeviceProfile.kt` | ProbeV2 `telephony.simSerial` row (needs READ_PHONE_STATE) | Reset → must differ |
| Timezone + locale per identity | Supported | `timezoneFor()`/`localeFor()` in `DeviceProfile.kt` — IANA zone + BCP-47 tag from the profile country, coherent with the GPS city | ProbeV2 `timezone.default` / `locale.default` rows | Must PASS |
| Per-identity MACs (wlan0 + Bluetooth) | Supported | `newLocalMac()` — locally-administered `02:` unicast, independent per identity | ProbeV2 `net.wlan0.mac` / `bluetooth.address` rows | Reset → must differ |
| Fresh Android ID per identity (16 hex) | Supported | `identity/ProfileGenerator.kt` `newAndroidId()`; persisted with the identity and written to `profiles/active_profile.json` | ProbeV2 ANDROID_ID row | Generate → note ID; Reset → ID must differ |
| Fresh telephony IDs per identity (deviceId 16 hex, subscriberId 15 digits) | Supported | `ProfileGenerator.kt` | ProbeV2 telephony rows | Same reset-differs check |
| Profile persistence + lifecycle | Supported | `profile/ProfileStore.kt` → `<filesDir>/profiles/active_profile.json` (+ `profile.json` copy); save on generate/reset, delete on identity delete (`identity/IdentityManager.kt`) | n/a (host-side) | Inspect via `adb run-as` or host log; engine reads the same file |
| Expected→guest delivery for the probe | Supported | `BlackBoxRuntime.stageExpectedProfile()` writes `expected_profile.json` into the guest's virtual files dir (`BEnvironment.getDataFilesDir(pkg, 0)`); engine redirects guest `getFilesDir()` there | ProbeV2 prints "expected_profile loaded from staged file" when the fallback is used | Launch probe from Manage Identity → comparison rows must not be UNKNOWN |

## Engine spoof hooks (guest processes)

| Feature | State | Enforcement path | Probe verification | Device test |
|---|---|---|---|---|
| `Build` fields (MANUFACTURER, BRAND, MODEL, DEVICE, PRODUCT, FINGERPRINT, ID, TAGS, TYPE, BOARD, HARDWARE) | Supported | `fake/spoof/BSpoofManager.applyBuildSpoofing()` — reflection strips `final` on the statics, called at the top of `HookManager.init()` (earliest per-process point, guest processes only) | ProbeV2 Build rows compare against profile `device.*` | PASS on all 11 rows on-device |
| `Build` extended fields (TIME, USER, HOST, BOOTLOADER, RADIO, SOC_MANUFACTURER, SOC_MODEL) | Supported / Partially Supported (SoC) | Same path — `BUILD_FIELD_MAP` + `BUILD_LONG_FIELD_MAP` (TIME). Empty values are never patched (SoC on unmapped hardware). | ProbeV2 rows; `SOC_*` UNKNOWN on unmapped hardware | Must PASS (TIME/USER/HOST/BOOTLOADER/RADIO) |
| `Build.VERSION.CODENAME` / `BASE_OS` / `PREVIEW_SDK_INT` | Supported | Same path — fixed release-build constants (`REL`, `""`, `0`), not profile fields (identical on every real release build) | ProbeV2 rows | Must PASS |
| `Build.DISPLAY` | Supported | Same path — profile `device.displayId`, derived per OEM: Samsung `<buildId>.<incremental>`, Pixel = buildId, Xiaomi/Redmi/POCO/Motorola = incremental, others = buildId (coherent fallback, never the host's Lineage string) | ProbeV2 `Build.DISPLAY` row | Must PASS; previously leaked `lineage_rhode-userdebug ...` |
| `Build.VERSION.INCREMENTAL` | Supported | Same path — profile `device.buildIncremental`, parsed from the fingerprint's incremental segment (coherent by construction) | ProbeV2 `Build.VERSION.INCREMENTAL` row | Must PASS |
| `Build.VERSION.SECURITY_PATCH` | Partially Supported | Same path — profile `device.securityPatch` (researched per row). Rows with `"unknown"` patch are NOT spoofed (engine skips; never fabricates a date) | ProbeV2 row; UNKNOWN for `unknown` rows | PASS on rows with a researched date |
| `System.getProperty("os.version")` (kernel) | Experimental | `fake/spoof/BSpoofJvmProps` — Pine hooks on `System.getProperty(String[, String])` rewriting only the `os.version` key to the profile's per-API plausible kernel (5.10.x for API 33, 6.1.x for API 34). Recursion-safe: the hook body never calls `getProperty` (profile loads via `applicationInfo.dataDir` field read). Fail-open | ProbeV2 `os.version(kernel)` row | Must PASS; previously leaked host kernel `4.19.304-perf+` |
| Sensor vendor/name (`Sensor.getVendor()`, `getName()`) | Experimental | `fake/spoof/BSpoofSensors.hookSensorIdentity()` — Pine hooks returning the profile's per-manufacturer plausible strings (e.g. STMicroelectronics/LSM6DSO for Samsung) instead of host hardware IDs (bmi3x0/BOSCH, ak0991x/akm, eminent, qualcomm). Same fail-open Pine mechanism as the visibility filter | ProbeV2 `sensor[<type>].vendor` / `.name` rows per expected sensor | Must PASS |
| `Build.VERSION.RELEASE` / `SDK_INT` | Host Provided | Deliberately NOT spoofed — these drive the real in-process framework; spoofing them destabilizes the runtime | ProbeV2 reports them as INFO, never PASS/FAIL | n/a |
| `Build.getSerial()` | Partially Supported | Static-field patch covers the Java field; the native `ro.serialno` read path is untouched | ProbeV2 does not assert serial | Note serial behavior on-device |
| Android ID (`Settings.Secure.ANDROID_ID`) | Supported | `fake/service/context/providers/SystemProviderStub.trySpoofAndroidId()` — intercepts `query()` (NameValueCache path) and `call()` (GET_secure path) for `android_id`, returns a one-row cursor / Bundle with the profile value | ProbeV2 ANDROID_ID row | Must PASS; compare with standalone probe (host value) |
| Telephony IDs (deviceId/IMEI/MEID/subscriberId) | Supported | `fake/service/ITelephonyManagerProxy` — `getDeviceId`, `getImeiForSlot`, `getMeidForSlot`, `getDeviceIdWithFeature` → profile `telephony.deviceId`; `getSubscriberId` → profile `telephony.subscriberId`. Platform note: on API 33+ the IMEI/MEID getters need privileged permission; the proxy returns profile values whenever invoked | ProbeV2 telephony rows (device/subscriber not directly readable by the probe without permission — INFO/UNKNOWN) | Verify no SecurityException crash on launch |
| Telephony operator (name, numeric, countryIso, SIM operator/name/iso) | Supported | `fake/spoof/BSpoofSystemProps` — Pine hooks on `SystemProperties.get(String)` / `get(String,String)` swapping `gsm.operator.alpha` / `gsm.operator.numeric` / `gsm.operator.iso-country` and `gsm.sim.operator.*` variants for profile `telephony.operatorName` / `operatorNumeric` / `countryIso`. Platform note: on API 30+ `TelephonyManager.getNetworkOperatorName/getNetworkOperator/getNetworkCountryIso` read these system properties directly instead of making binder calls, so the `ITelephonyManagerProxy` hooks (`getNetworkOperatorName`, `getSimOperator`, `getSimOperatorName`, `getSimCountryIso`, `getNetworkCountryIso`) never fire for them; the property hook is the effective path. `getDeviceId`/`getImeiForSlot`/`getMeidForSlot`/`getSubscriberId` remain binder-hooked in `ITelephonyManagerProxy` | ProbeV2 telephony rows: name/iso case-insensitive, numeric exact | PASS when cellular info is available; UNKNOWN (not FAIL) when the device reports nothing |
| SIM serial / ICCID | Supported | `fake/service/ITelephonyManagerProxy` — `getSimSerialNumber()` → profile `telephony.simSerial` (19-digit ICCID); public-API getter bypasses the binder path on the framework versions tested (same failure mode as the operator getters in `d24b388`), so this is fail-open | ProbeV2 `telephony.simSerial` row | Must PASS |
| Cell privacy (no real towers) | Supported | `fake/service/ITelephonyManagerProxy` — `getCellLocation()` → null, `getAllCellInfo()` → empty list, `getNeighboringCellInfo()` → empty list whenever spoofing is active | ProbeV2 `cell.allCellInfo.count` / `cell.getCellLocation` rows assert emptiness | Must PASS (0 / null) |
| Wi-Fi scan results (no real BSSIDs) | Supported | `fake/service/IWifiManagerProxy.getScanResults` → empty list whenever spoofing is active | ProbeV2 `wifi.scanResults.count` row asserts 0 | Must PASS |
| `TimeZone.getDefault()` / `Locale.getDefault()` | Experimental | `fake/spoof/BSpoofOsIdentity` — Pine hooks rewriting to the profile's `locale.timezoneId` / `locale.localeTag`; fail-open | ProbeV2 `timezone.default` / `locale.default` rows | Must PASS |
| Bluetooth name / address | Experimental | Same class — Pine hooks: `getName()` → spoofed model, `getAddress()` → per-identity MAC; fail-open | ProbeV2 `bluetooth.name` / `bluetooth.address` rows | Must PASS; UNKNOWN (not FAIL) if BLUETOOTH_CONNECT is denied |
| `NetworkInterface.getHardwareAddress()` (wlan0) | Experimental | Same class — Pine hook, only for interface `wlan0`; other interfaces pass through; fail-open | ProbeV2 `net.wlan0.mac` row | Must PASS |
| `WebSettings.getDefaultUserAgent()` | Experimental | Same class — Pine hook returning the profile's prebuilt UA verbatim; fail-open | ProbeV2 `webview.defaultUa` row (exact match) | Must PASS | `fake/spoof/BSpoofSystemProps` — Pine hooks on `SystemProperties.get(String)` / `get(String,String)` swapping `gsm.operator.alpha` / `gsm.operator.numeric` / `gsm.operator.iso-country` and `gsm.sim.operator.*` variants for profile `telephony.operatorName` / `operatorNumeric` / `countryIso`. Platform note: on API 30+ `TelephonyManager.getNetworkOperatorName/getNetworkOperator/getNetworkCountryIso` read these system properties directly instead of making binder calls, so the `ITelephonyManagerProxy` hooks (`getNetworkOperatorName`, `getSimOperator`, `getSimOperatorName`, `getSimCountryIso`, `getNetworkCountryIso`) never fire for them; the property hook is the effective path. `getDeviceId`/`getImeiForSlot`/`getMeidForSlot`/`getSubscriberId` remain binder-hooked in `ITelephonyManagerProxy` | ProbeV2 telephony rows: name/iso case-insensitive, numeric exact | PASS when cellular info is available; UNKNOWN (not FAIL) when the device reports nothing |
| Inactive-profile telephony fallback | Host Provided / legacy | Device/subscriber-ID getters keep the engine's legacy constant `md5(hostPkg)` fake when no profile is active — genuine passthrough would crash (SecurityException, API 33+) or leak the real IMEI. Operator/country and Wi-Fi DO genuine passthrough when inactive | n/a | n/a |
| Wi-Fi SSID / BSSID (visible identity) | Supported | `fake/service/IWifiManagerProxy.getConnectionInfo` — sets profile SSID/BSSID via the existing `BRWifiInfo`/`BRWifiSsid` reflection pattern; replaces the old hardcoded `BlackBox_Wifi` / `ac:62:5a:82:65:c4` constants | ProbeV2 WiFi rows: SSID exact (profile stores it quoted, as Android reports), BSSID case-insensitive; `<unknown ssid>` / `02:00:00:00:00:00` (OS-masked) → UNKNOWN | PASS when connected; UNKNOWN (not FAIL) when disconnected or location permission is denied |
| Network transport / routing | Host Provided (routing) + Supported (visible VPN hidden) | Routing stays on the host UID with real internet by design. The *visible* VPN tell is now stripped: `fake/spoof/BSpoofNetwork` (via `NetworkSpoofInjector`) Pine-hooks `ConnectivityManager.getNetworkCapabilities` (removes `TRANSPORT_VPN` from the returned copy), `getLinkProperties` (tunnel interface name → `wlan0`), and `getActiveNetworkInfo`/`getNetworkInfo`/`getAllNetworkInfo` (VPN type → WIFI); fail-open, gated on an active profile | ProbeV2 `network.transport` row (INFO): expect `WIFI` only, `link_interface: wlan0` | Confirm guest internet works; transports show no VPN |
| Location (`getLastKnownLocation`, `getLastLocation`) | Supported | `fake/spoof/BSpoofLocation.ensureSeeded()` called at the top of the `getLastLocation` / `getLastKnownLocation` / `requestLocationUpdates` hooks in `fake/service/ILocationManagerProxy`; seeds a `BLocation` from profile lat/long/altitude/accuracy into `BLocationManager` with `OWN_MODE` (per-guest) when `isSpoofActive()`; clears the seed when inactive so calls pass through | ProbeV2 location row: PASS iff \|Δlat\|, \|Δlon\| ≤ 1e-4 (~11 m); accuracy PASS iff \|Δ\| ≤ max(10 m, 50% of expected) | Grant location permission to the guest probe; expect PASS |
| Location movement simulation | Supported | Same class — daemon 2 s tick advances each seeded fix by `speed × dt` along `bearing` (profile `movement`, default disabled, 1.4 m/s @ 90°); propagates to `requestLocationUpdates` listeners through the engine's existing listener loop | ProbeV2 movement check: two samples ~10 s apart, reports moved distance (INFO) | Enable movement in a profile and watch the distance grow |
| Sensors (`getSensorList`, `getDefaultSensor` visibility) | Experimental | `fake/spoof/BSpoofSensors` — Pine ART hooks on `SensorManager.getSensorList(int)` / `getDefaultSensor(int[, boolean])` filter to `BSpoofManager.getSensorTypes()`; fail-open (hook failure / inactive spoof / empty list → passthrough). Pine is process-wide but injectors run in guest processes only | ProbeV2 sensor row: PASS only on exact type-set equality; lists missing/unexpected type ints | PASS if Pine hooks land on the device's ART; if rows show extra host sensors, the hook didn't land — report it |
| Sensor live readings (`registerListener` + synthetic `SensorEvent`s) | Unsupported | Deliberate: `android.hardware.SensorEvent` has no public constructor; faking readings needs hidden-constructor reflection + version-fragile field injection across 6+ `registerListener` overloads. Visibility filtering (above) covers the profile contract | n/a | n/a |
| Camera characteristics (`CameraManager.getCameraCharacteristics()`) | Unsupported | Assessed 2026-10-02: `CameraCharacteristics` has no public constructor, so instances can't be fabricated; per-key spoofing would need a researched per-device camera-HAL spec database we don't have (build research covers fingerprints, not camera modules); blocking keys wholesale would break guest camera use. Documented as a gap, same as synthetic sensor readings | n/a | n/a |
| Package visibility (`getInstalledPackages`, `getInstalledApplications`, …) | Supported | Already virtual-only in the engine (`BPackageManagerService`); audited, no change needed | ProbeV2 packages section is INFO (count + first 20 names) — manual guest-isolation review | Confirm host apps are NOT listed |
| `queryIntentActivities` | Supported | **Gap found and fixed:** was unhooked, so guests hit the real PMS and could enumerate host activities. New `@ProxyMethod("queryIntentActivities")` in `fake/service/IPackageManagerProxy` serves only the virtual package list, no real-PMS fallback | Same INFO section | Same review |
| `resolveIntent` / `resolveService` single-target fallback | Host Provided | Kept deliberately: falls back to the real PMS when the virtual PM returns null (host-passthrough-app design). Single-target resolution, not enumeration | n/a | n/a |

## What this PoC does NOT do (out of scope, unchanged)

- Full 13-module V1 app, multi-identity, UI polish — gated on the runtime
  verdict and Jason's approval.
- `Build.VERSION.RELEASE`/`SDK_INT` spoofing (would destabilize the guest),
  `Build.getSerial()` native path, synthetic sensor readings, camera
  characteristics (no public constructor; per-device HAL specs unresearched),
  VPN network mode — documented above.
- Undetectability claims: the probe measures guest-observed values;
  configuration alone is not proof, and no complete anti-detection is promised.

## File map (source of truth)

- Host: `poc/host-runtime/app/src/main/kotlin/com/sandboxpoc/hostruntime/`
  (`profile/`, `identity/`, `runtime/BlackBoxRuntime.kt`, `ui/ManageIdentityActivity.kt`)
- Engine spoof: `poc/engines/zitanioi-blackbox/Bcore/src/main/java/top/niunaijun/blackbox/fake/spoof/`
  (`BSpoofManager.java`, `BSpoofLocation.java`, `BSpoofSensors.java`, `SensorSpoofInjector.java`)
- Engine hook edits: `fake/hook/HookManager.java`,
  `fake/service/ITelephonyManagerProxy.java`,
  `fake/service/IWifiManagerProxy.java`,
  `fake/service/IPackageManagerProxy.java`,
  `fake/service/ILocationManagerProxy.java`,
  `fake/service/context/providers/SystemProviderStub.java`,
  `entity/location/BLocation.java` (parcel field-order bug fix)
- Probe: `poc/probe/app/src/main/java/com/sandboxpoc/probe/MainActivity.java`
  (ProbeV2 section), `AndroidManifest.xml` (`ACCESS_WIFI_STATE`)
