# Sandbox PoC — Host App

Minimal proof-of-concept host app for the no-root Android privacy-sandbox
project ("like clone.app"). Package `com.sandboxpoc.hostruntime`, app name
"Sandbox PoC".

**Engine status:** no virtualization engine is integrated yet. The app runs
against `StubRuntime`, which reports `ENGINE_NOT_INTEGRATED` honestly —
identity/profile/capability flows are fully usable; runtime operations
(install/launch/…) explain that no engine is plugged in instead of faking
success. The integration seam for the engine adapter is documented in
[INTEGRATION.md](INTEGRATION.md).

## Requirements

- **JDK 17** (Gradle 8.x + Android Gradle Plugin 8.5.x require it)
- **Android SDK** with:
  - `platforms/android-35` (compileSdk/targetSdk 35)
  - `build-tools/35.0.0`
  - (`platforms/android-33` satisfies the minSdk 33 floor; only android-35
    is strictly needed to compile)
- Set `ANDROID_HOME` (or `ANDROID_SDK_ROOT`) to the SDK path, e.g.:

```sh
export ANDROID_HOME=$HOME/android-sdk
export ANDROID_SDK_ROOT=$HOME/android-sdk
```

No `local.properties` is shipped — the environment variable is enough.

## Build

```sh
cd poc/host
./gradlew assembleDebug      # debug APK
./gradlew assembleRelease    # release APK (currently signed with the debug key — see below)
```

The Gradle wrapper (`gradlew`, `gradle/wrapper/`) pins Gradle 8.10.2 and
downloads it automatically on first run. No system Gradle needed.

APKs land in `app/build/outputs/apk/debug/` and `app/build/outputs/apk/release/`.

## Signing

- Debug builds use the standard Gradle debug key (`~/.android/debug.keystore`,
  auto-created).
- The `release` build type is currently wired to the debug signing config so
  the PoC is installable without extra setup. Before anything release-facing,
  add a real signing config in `app/build.gradle.kts`.
- `poc-host-debug.keystore` (in this directory, passwords in
  [KEYSTORE.md](KEYSTORE.md)) is the key the reference PoC APK was signed
  with. It is a debug-only key.

## Project layout

```
app/src/main/
  AndroidManifest.xml
  kotlin/com/sandboxpoc/host/
    SandboxApp.kt            # Application subclass — THE engine swap point
    runtime/                 # SandboxRuntime interface + StubRuntime + RuntimeStatus
    identity/                # IdentityManager, Identity, ProfileGenerator, ProfileValidator
    profile/                 # DeviceProfile (+ coherent device table)
    providers/               # Provider interfaces + GmsMode + ProviderBindings
    capability/              # CapabilityManager + Capability
    storage/                 # StorageManager (guest state under filesDir/sandbox/)
    util/                    # EventLog
    ui/                      # Home / Manage Identity / Settings / App picker
```

Key invariants (enforced in code, not just documented):

- **ONE active identity max** — generating while one exists requires
  explicit reset/delete first.
- Profiles are **coherent by construction**: picked from a table of real
  device definitions (manufacturer/brand/model/device/product/fingerprint/
  build/version/API mutually consistent); only per-identity fields
  (`androidId`, …) are freshly random. `ProfileValidator` rejects anything
  incoherent.
- **Host private data is never copied into the sandbox** — guest install
  stages only the APK artifact.
- **Never silently fall back to host values** — unprovided capabilities stay
  `UNVERIFIED`/`UNSUPPORTED` and the UI says so.

## Next step: the engine adapter

See [INTEGRATION.md](INTEGRATION.md) — it specifies exactly where the adapter
plugs in (`SandboxRuntime`, swapped in `SandboxApp.onCreate()`), what it must
translate (profile → virtual-device config, `installApplication`,
`launchApplication`, …), and the integrator checklist (manifest permissions,
stub activities, 16 KB `.so` alignment, targetSdk implications).

## Notes

- `build-manual.sh` is a maintainer script that builds the APK without Gradle
  (aapt2 + kotlinc + d8 + apksigner) for environments where the Gradle daemon
  cannot run. Normal builds should use `./gradlew` as above.
- A prebuilt signed PoC APK is at `../apks/host-poc-v1.apk`.
