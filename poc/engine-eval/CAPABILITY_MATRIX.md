# Engine Capability Matrix — Privacy Sandbox PoC

Evaluated commits:
- **zitanioi/blackbox** @ `c994edf21fdecbf22196f9e2b0d447af97140f2e` (2026-09-10)
- **alex5402/newblackbox** @ `89b59836c66f173756a4ae258cf379a957649820` (2026-07-19)

Legend: **VERIFIED** = implementing code found in source (path given). **PARTIAL** = real code but limited/dead.
**UNVERIFIED** = absent, or README-claimed only. **DEAD CODE** = class exists but never activates at runtime.

## Matrix

| # | Capability | zitanioi/blackbox | alex5402/newblackbox |
|---|-----------|-------------------|----------------------|
| 1 | Build-field spoofing (Build.MANUFACTURER/BRAND/MODEL/DEVICE/PRODUCT/FINGERPRINT) | **UNVERIFIED** — no Build.* interception anywhere in source. `utils/compat/BuildCompat.java` is only SDK-version checks. | **PARTIAL (VERIFIED, limited)** — native Dobby inline hook on libc `__system_property_get` spoofs `ro.product.model/brand/manufacturer/device`, `ro.build.fingerprint`, `ro.serialno`, `ro.hardware`, etc. — but with a **hardcoded Pixel 6 / Android 12 profile** (`Bcore/src/main/cpp/Utils/VirtualSpoof.cpp`, `install_property_get_hook()`, runs via `__attribute__((constructor))` at lib load). Does **not** patch `android.os.Build` static fields (initialized in zygote before the hook), so `Build.MODEL` read directly by a guest still shows the real device. Hook is process-wide (also affects the host process itself). |
| 2 | Android ID (Settings.Secure interception) | **UNVERIFIED** — zero occurrences of `android_id`/`ANDROID_ID` in the entire `Bcore/src`. | **UNVERIFIED (DEAD CODE)** — `fake/service/AndroidIdProxy.java` hooks `getAndroidId/getString/getLong`, but `getWho()` returns `null` so `ClassInvocationStub.injectHook()` returns early and `inject()` is an empty no-op. `ISettingsProviderProxy` only intercepts `feature_flag*` keys + UID-mismatch fallback — no android_id handling. Net effect: guests see the **host's** Android ID. |
| 3 | Location (LocationManager proxy / fake provider) | **VERIFIED** — `fake/service/ILocationManagerProxy.java` hooks `getLastLocation`, `getLastKnownLocation`, `requestLocationUpdates`, `removeUpdates`, `getProviderProperties`, `getBestProvider`, `getAllProviders`, `registerGnssStatusCallback`, `removeGpsStatusListener`. When `BLocationManager.isFakeLocationEnable()`, returns `BLocationManager.get().getLocation(userId, pkg).convert2SystemLocation()` (`fake/frameworks/BLocationManager.java`). | **VERIFIED** — same structure (`fake/service/ILocationManagerProxy.java` + `fake/frameworks/BLocationManager.java`), same method list. |
| 4 | Sensors (SensorManager proxy) | **UNVERIFIED** — no sensor proxy registered in `HookManager`. | **UNVERIFIED (DEAD CODE)** — `fake/service/ISystemSensorManagerProxy.java` exists (`SystemSensorManager` ctor + `parsePackageList` hooks, UID-mismatch fallback) but `getWho()` returns `null` → never injected. |
| 5 | Network info (ConnectivityManager/NetworkInfo/TelephonyManager) | **VERIFIED (mixed)** — `ITelephonyManagerProxy.java`: `getDeviceId`, `getImeiForSlot`, `getMeidForSlot`, `getDeviceIdWithFeature` all return `Md5Utils.md5(hostPackageName)` — spoofed but **constant per host, not per identity**. `getSubscriberId`, `getLine1NumberForDisplay`, `getCellLocation`, `getAllCellInfo` hooked. `IConnectivityManagerProxy.java`: `FixAttributionSource` hook on `getNetworkCapabilities/getLinkProperties/getNetworkInfo/getAllNetworkInfo/getActiveNetworkInfo/requestNetwork/registerNetworkCallback/...` — fixes AttributionSource UID mismatch (Android 14+), but passes through **real** network info (no spoofing). | **VERIFIED (mixed)** — same telephony behavior (`md5(hostPkg)`). Additionally `IPhoneSubInfoProxy` (`getLine1NumberForSubscriber`) and `IWifiManagerProxy` (`getConnectionInfo`) exist. Connectivity: `IConnectivityManagerProxy` present. |
| 6 | Package-visibility filtering (guest PackageManager view) | **VERIFIED** — `BPackageManagerService.getInstalledPackagesAsUser(userId)` returns only virtual packages installed for that virtual user (`core/system/pm/BPackageManagerService.java:661`). Binder layer: `UidMethodProxy`/`IPackageManagerProxy` rewrite calling UID → guests can't see host/system packages. | **VERIFIED** — same `BPackageManagerService`/`IPackageManagerProxy` architecture. |
| 7 | Storage redirection (guest getFilesDir/getExternalFilesDir isolation) | **VERIFIED** — two layers: (a) Java `core/IOCore.java` — `addRedirect(origPath, redirectPath)` trie map, e.g. `/data/data/<guest>/` → `/data/data/<host>/blackbox/...`; `addIORule` pushes rules to native. (b) Native GOT/PLT hooks in `Bcore/src/main/cpp/Hook/NativeIOHook.cpp` (`open/openat/__open_2/__openat_2/fopen/mkdir/access/stat/unlink/rename/opendir/dlopen/android_dlopen_ext`…) plus `UnixFileSystemHook.cpp` JNI hooks (`canonicalize0`, `getBooleanAttributes0`, `list0`, `createDirectory0`, …). | **VERIFIED** — same `core/IOCore.java` + `cpp/IO.cpp` redirect; `UnixFileSystemHook.cpp` JNI hooks present. (Note: zitanioi's broader `NativeIOHook` GOT-rewriting layer has **no direct equivalent** in alex5402 — alex relies on the Java `UnixFileSystemHook` JNI hooks + `IO.cpp`; alex's `FileSystemHook.cpp` `new_open` is **dead code** — `init()` only resolves `open`/`open64` addresses, never installs the hook.) |
| 8 | Signature spoofing (fake signatures in PackageInfo — microG prerequisite) | **UNVERIFIED** — `BPackage.mSignatures` = real APK signing certs (`core/system/pm/BPackage.java:129`); `PackageManagerCompat` maps `GET_SIGNATURES` → real signatures. No `FAKE_PACKAGE_SIGNATURE` mechanism anywhere. | **UNVERIFIED** — same real-signature behavior. |
| 9 | Virtual user model (createUser/deleteUser — identity destroy/reset maps here) | **VERIFIED** — `core/system/user/BUserManagerService.java`: `createUser(int)`, `deleteUser(int)` (cascades to `BPackageManagerService.deleteUser`), per-user data dirs. `BlackBoxCore.createUser/deleteUser` public API. | **VERIFIED** — same `BUserManagerService` (`createUser`/`deleteUser`). |
| 10 | Guest APK install (virtual package manager? APK parsing?) | **VERIFIED** — `BPackageManagerService.installPackageAsUser(file, option, userId)` → `BPackageInstallerService` → `installer/CreatePackageExecutor.java` + `CopyExecutor.java`; APK parsed in-process via `PackageParserCompat.parsePackage()` (`core/system/pm/PackageParserCompat.java`). APKs are **not** installed on the host system; they live under the host's `blackbox/` data dir and are loaded by a custom classloader in the host's process. | **VERIFIED** — same architecture (`BPackageManagerService` → `BPackageInstallerService`). |
| 11 | Guest internet path | **VERIFIED (passthrough)** — guests share the host app's UID/network identity; no traffic interception in Bcore. `ProxyVpnService` (`proxy/ProxyVpnService.java`) exists but is **unwired** (no `initVpnService`/config toggle in zitanioi's `BlackBoxCore`/`ClientConfiguration`). | **VERIFIED (passthrough + optional VPN)** — same passthrough default, plus a **working optional VPN mode**: `ClientConfiguration.isUseVpnNetwork()` → `BlackBoxCore.initVpnService()` starts `proxy/ProxyVpnService` (`BlackBoxCore.java:1683`). Demo app exposes "Use VPN Network" toggle (RELEASE_NOTES.md). |
| 12 | AccountManager isolation | **VERIFIED** — `fake/service/IAccountManagerProxy.java` hooks `getPassword`, `getUserData`, `getAuthenticatorTypes`, `getAccountsForPackage`, `getAccountsByTypeForPackage`, `getAccountByTypeAndFeatures`, `getAccountsByFeatures`, `getAccountsAsUser`, `addAccountExplicitly` — virtual-UID scoped. | **VERIFIED** — same `IAccountManagerProxy` method list. |

## README claims vs source (spot-checks)

- alex5402 README claims "**Device Spoofing**: Modify device information for virtual apps" — in source this is only the `__system_property_get` hook with a **hardcoded Pixel 6/Android 12** profile (VirtualSpoof.cpp) plus constant `md5(hostPkg)` telephony IDs. "Modify" is true; per-identity configurable spoofing is **not** present.
- alex5402 README claims "works on Android 5.0 to **14.0+**" — the source has API-33+ launch-path fixes (a dedicated `isTiramisu()` branch that directly swaps the `LaunchActivityItem`'s own `mIntent`/`mInfo`, plus the `mActivityCallbacks` null guard — matching zitanioi on API 33–36), but **no Android 15/16 `JniHook` null/pending-exception guards** (see LAUNCH-PATH section in RECOMMENDATION.md).
- alex5402 `RELEASE_NOTES.md` "VPN Network Mode Toggle" — VERIFIED wired in Bcore (`initVpnService`).
- zitanioi README claims Android 5.0–16.0 support + the three Android-16 fix bullets — all **VERIFIED as real commits** (`ed61551`, `a93bea8`, `94263ef`).

## Dead code found (registered or present but never activates)

| Location | Status |
|---|---|
| alex5402 `fake/service/AndroidIdProxy.java` | Registered in `HookManager` but `getWho()=null` → never injected |
| alex5402 `fake/service/ISystemSensorManagerProxy.java` | Same — `getWho()=null` |
| alex5402 `cpp/Utils/AntiDetection.cpp` | `my_access/my_stat/my_open/...` blockers defined; `install_file_hooks()` opens/closes the xdl handle without installing anything |
| alex5402 `cpp/Hook/FileSystemHook.cpp` | `new_open/new_open64` defined; `init()` only resolves addresses, never hooks |
| zitanioi `proxy/ProxyVpnService.java` | Class exists; never started (no wiring in `BlackBoxCore`/`ClientConfiguration`) |

## Bottom line for the spoofing requirements

Neither engine ships per-identity **Build-field**, **Android ID**, **sensor**, or **signature** spoofing out of the box. What exists:
- Per-identity **fake location** (both, needs `setLocation` wiring).
- **Constant** fake telephony IDs (both, `md5(hostPkg)` — same value for every identity on the same host).
- Hardcoded **Pixel 6/Android 12** system-property spoofing (alex5402 only; also hits the host process).
- Real network info passthrough (both); optional guest VPN routing (alex5402 only).

Per-identity spoofing of Build fields / Android ID / sensors / signatures will have to be **built on top** of whichever engine is chosen. zitanioi's cleaner, smaller hook surface and documented Android 16 launch fixes make it the better base for that work.
