# Privacy Sandbox — Phase 1 Proof-of-Concept

Phase 1 scope: **engine/runtime feasibility only**. This tree contains the
PoC sources and signed APKs. Nothing here is the full V1 product.

## Projects

| Project | Package | APK | What it proves |
|---|---|---|---|
| `probe/` | `com.sandboxpoc.probe` | `apks/probe-v1.apk` | Reports what a guest app actually observes (Build fields, Android ID, sensors, network, paths, GMS, virtualization indicators). Run standalone AND inside the sandbox; compare the two reports. |
| `host/` | `com.sandboxpoc.host` | `apks/host-poc-v1.apk` | Engine-agnostic host: one-active-identity lifecycle, coherent profile generation/validation, APK-only staging, honest `StubRuntime` (operations report `ENGINE_NOT_INTEGRATED`). Proves app-layer behavior, not guest execution. |
| `host-runtime/` | `com.sandboxpoc.hostruntime` | `apks/host-runtime-poc-v1.apk` | Runtime-enabled host: `BlackBoxRuntime` adapter drives the BlackBox engine (virtual user 0). Generate identity → create sandbox → install APK → launch → reset → clean state → new identity. **Profile spoofing is deliberately NOT implemented** — the engine has no Build/Android-ID/sensor virtualization (see `engine-eval/`). |
| `engine/` | — | `engine/Bcore-release.aar` | BlackBox engine (zitanioi/blackbox) built from source: 566 sources, 2,450 classes, `libblackbox.so` + `libpine.so` (arm64/armv7/x86). |
| `engine-eval/` | — | — | Head-to-head fork audit: `CAPABILITY_MATRIX.md`, `BUILD_NOTES.md`, `NATIVE_HOOKS.md`, `RECOMMENDATION.md`. |

## Building

### On a normal machine (Jason's): Gradle

Each project ships a complete Gradle wrapper:

```bash
cd probe/       && ./gradlew assembleDebug   # or assembleRelease
cd host/        && ./gradlew assembleDebug
cd host-runtime/ && ./gradlew assembleDebug
```

`host-runtime` needs the engine AAR in `app/libs/Bcore-release.aar`
(checked in, 4.1 MB). To rebuild it from engine source:
`cd engine && ./build-aar-manual.sh all` — requires the NDK.

Note: `./gradlew` downloads the Gradle 8.10.2 distribution on first run —
that needs normal network access (it cannot run inside this build sandbox,
which is why the manual scripts exist).

### Manual builds (this sandbox only)

Gradle's daemon needs JVM TCP, which this sandbox blocks, so builds here use
aapt2/kotlinc/d8/zipalign/apksigner directly:

```bash
cd probe/        && ./build-manual.sh all
cd host/         && ./build-manual.sh all
cd host-runtime/ && ./build-manual.sh all          # stages: manifest|link|kotlin|dex|apk
cd engine/       && ./build-aar-manual.sh all
```

Outputs land in `apks/`. All three APKs are signed and `apksigner verify`'d.

## Device testing

See `TEST-INSTRUCTIONS.md` for the full flow (install, generate identity,
install probe into the sandbox, launch, compare reports, reset, re-identify)
and the logcat commands for init/install/launch/ANR/crash diagnosis.

## Keystores

PoC self-signed debug keys (NOT release keys):
- `poc-debug.keystore` → `probe-v1.apk`
- `host/poc-host-debug.keystore` → `host-poc-v1.apk`
- `host-runtime/poc-host-runtime-debug.keystore` → `host-runtime-poc-v1.apk`
- Keystore password: `android` (debug PoC convention; do not reuse for production).

## Phase 1 status (2026-09-30)

**Local build/integration readiness: COMPLETE.** All three APKs build,
sign, and pass static verification (package IDs, minSdk 33, targetSdk 35,
native ABIs, merged engine components).

**On-device runtime verdict: PENDING.** Jason's device evidence is required
for: engine init, guest install, guest launch (the old black-screen/ANR
failure signature), guest Internet, clean reset, changed guest-observed
identity. See `TEST-INSTRUCTIONS.md` for what to capture.

Known honest limits of this PoC: the engine provides **no** Build-field,
Android-ID, sensor, or network-identity spoofing (source-verified) — guest
probe reports will expose host values for these until/unless a spoofing
layer is added. MicroG is not integrated. 32-bit `.so` outputs are 4 KB
aligned; only arm64 (the Android 16 target) is 16 KB aligned. These are
documented, not hidden.
