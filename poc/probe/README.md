# SandboxProbe

Diagnostic probe APK for the Privacy Sandbox (no-root, BlackBox-style app virtualization) project.

- **Package:** `com.sandboxpoc.probe`
- **App name:** SandboxProbe
- **versionCode:** 1 / **versionName:** 1.0
- **minSdk:** 33, **targetSdk:** 35, **compileSdk:** 35
- **Language:** Java (no Kotlin, no NDK, no AndroidX)

The probe runs **both as a normal standalone install and inside the sandbox host**.
It reports raw observed values only — it never concludes "virtualized: yes/no".
Anything it cannot access is reported honestly as
`PERMISSION_DENIED`, `UNAVAILABLE (<reason>)`, `NOT PRESENT`, or `TIMEOUT`.

## What it measures (one section per header, `key: value` lines)

| Section | Measures |
|---|---|
| SANDBOX PROBE REPORT | generation timestamp, probe package |
| APP / INSTALL SOURCE | package, versionName, versionCode, `installingPackageName`, `initiatingPackageName` (via `getInstallSourceInfo`) |
| BUILD PROPERTIES | `Build.MANUFACTURER/BRAND/MODEL/DEVICE/PRODUCT/BOARD/HARDWARE/FINGERPRINT/ID/TAGS/TYPE/DISPLAY`, `VERSION.RELEASE/SDK_INT/SECURITY_PATCH/INCREMENTAL`, `SUPPORTED_ABIS` |
| DEVICE ID | `Settings.Secure.ANDROID_ID` |
| LOCATION | runtime permission state; `LocationManager` all providers, per-provider enabled state and last-known location (lat,lon,accuracy,time); fresh-location result lives in the follow-up section |
| LOCATION (fresh request) | one `getCurrentLocation()` fix on the best enabled provider (GPS→network→passive), 12 s timeout → `TIMEOUT (12s, no fix)` |
| SENSORS | `SensorManager.getSensorList(TYPE_ALL)` → name, vendor, version, type per sensor |
| PACKAGES | `getInstalledPackages(0)` count + first 30 package names sorted (visibility-filtered — no `QUERY_ALL_PACKAGES`); `queryIntentActivities(MAIN/LAUNCHER)` count + first 15 components (manifest `<queries>` block makes this meaningful on API 33+) |
| NETWORK | active network, `NetworkCapabilities` transports (WIFI/CELLULAR/ETHERNET/VPN/…), `LinkProperties` interface name |
| TELEPHONY | runtime permission state; `TelephonyManager.getNetworkOperatorName()`, `getDataNetworkType()` |
| FILESYSTEM / ENVIRONMENT | `filesDir`, `cacheDir`, `externalFilesDir`, `dataDir` absolute paths; `Process.myUid()`; `os.name/version/arch`, `java.vm.name` |
| GOOGLE SERVICES | `com.google.android.gms` PRESENT/NOT PRESENT (+ versionName/versionCode); `GoogleApiAvailability.isGooglePlayServicesAvailable()` code mapped to name |
| VIRTUALIZATION INDICATORS | **raw observations only**: `ApplicationInfo.sourceDir/publicSourceDir/dataDir/nativeLibraryDir`, actual `filesDir` vs expected `/data/data/<pkg>` and `/data/user/0/<pkg>` paths (you compare), PRESENT/NOT PRESENT for best-effort known app-virtualization host packages (`com.lbe.parallel.intl` = Parallel Space, `com.ludashi.dualspace` = DualSpace) |

The UI is a scrollable monospace report plus a **Copy report** button (copies the full text to the clipboard).

## Permissions

Declared (all runtime-optional, requested gracefully at startup):

- `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` → location sections report `PERMISSION_DENIED` when denied; everything else still runs
- `READ_PHONE_STATE` → telephony values report `PERMISSION_DENIED` when denied

**The app produces a full report even with every runtime permission denied.** It never crashes on denial.

## Building

### Requirements

- **JDK 17** (`JAVA_HOME` set)
- **Android SDK**: `ANDROID_HOME` (and `ANDROID_SDK_ROOT`) pointing at an SDK containing
  `platforms/android-35` and `build-tools/35.0.0` (platforms 33–34 optional)
- Network access to `google()` / `mavenCentral()` for AGP 8.5.2 and
  `play-services-base:18.5.0` (used only for the `GoogleApiAvailability` check)

### With the Gradle wrapper (preferred)

```sh
cd poc/probe
./gradlew assembleDebug      # app/build/outputs/apk/debug/app-debug.apk (auto-signed)
./gradlew assembleRelease    # app/build/outputs/apk/release/app-release-unsigned.apk
```

The wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper/`) pins Gradle 8.10.2;
no local Gradle install needed.

### Without Gradle (manual toolchain)

`build-manual.sh` reproduces the exact pipeline used for the shipped APK —
`aapt2 link` → `javac` → `d8` → `zipalign` → `apksigner` — using only the SDK
build-tools plus the three play-services AARs (fetched automatically):

```sh
cd poc/probe
export JAVA_HOME=<jdk-17> ANDROID_HOME=~/android-sdk ANDROID_SDK_ROOT=~/android-sdk
./build-manual.sh ../apks/probe-v1.apk
```

## Signing

Release APKs are signed with `apksigner` using the **shared PoC key**:

- Keystore: `poc/poc-debug.keystore`
- Alias: `poc-debug` (store/key passwords in `poc/keystore-info.txt`)
- `probe-v1.apk` signer cert SHA-256: `a35713df39010dd09a92fdec77f137bcc35d3b62d7e68fe58f354c715c6f2a2e`
  (DN: `CN=SandboxPoC, OU=Test, O=Local, C=US`), v3 signature scheme verified.

Verify any build yourself:

```sh
$ANDROID_HOME/build-tools/35.0.0/apksigner verify --print-certs poc/apks/probe-v1.apk
$ANDROID_HOME/build-tools/35.0.0/aapt dump badging poc/apks/probe-v1.apk | head
```

## How to read the report

1. Install `poc/apks/probe-v1.apk` on the stock phone → open SandboxProbe → grant or deny the permission prompts → tap **Copy report**.
2. Install/launch the same APK **inside the sandbox** → repeat.
3. Diff the two reports. Interesting deltas: `ANDROID_ID`, `Build.*` fields, `dataDir`/`filesDir` paths, `installingPackageName`, visible-package counts, GMS availability code, sensor list. The probe asserts nothing — you interpret.

## Project layout

```
poc/probe/
├── settings.gradle / build.gradle / gradle.properties
├── gradlew / gradlew.bat / gradle/wrapper/   # self-contained Gradle 8.10.2 wrapper
├── build-manual.sh                            # no-Gradle build (aapt2/javac/d8/apksigner)
├── README.md
└── app/
    ├── build.gradle
    └── src/main/
        ├── AndroidManifest.xml
        └── java/com/sandboxpoc/probe/MainActivity.java
```

Standalone-safe: zero dependencies at install/run time beyond the Android
framework (play-services client classes are bundled into the APK's dex).
