# Integration Seam — Engine Adapter

The host app is fully engine-agnostic. The virtualization engine (selected by
the parallel engine-evaluation track) plugs in at exactly ONE seam:
`SandboxRuntime`.

## 1. Where the adapter plugs in

| Item | Location |
|---|---|
| Interface to implement | `runtime/SandboxRuntime.kt` — `interface SandboxRuntime` |
| Operations | `create()`, `start()`, `stop()`, `installApplication(apkPath: String)`, `uninstallApplication(packageName: String)`, `launchApplication(packageName: String)`, `stopApplication(packageName: String)`, `reset()`, `destroy()`, `getStatus(): RuntimeStatus` |
| Status enum | `runtime/RuntimeStatus.kt` — return real states (`NOT_CREATED`, `ACTIVE`, `STOPPED`, `DESTROYED`, `ERROR`) instead of `ENGINE_NOT_INTEGRATED` |
| Swap point | `SandboxApp.onCreate()` — replace `runtime = StubRuntime()` with the adapter instance |
| Adapter home | New package `com.sandboxpoc.host.engine` (or inside `runtime/`). **No engine imports may appear in any other package** — `identity`, `profile`, `providers`, `capability`, `storage`, `ui` must keep compiling with zero engine references |

`StubRuntime` throws `UnsupportedOperationException("engine not integrated")`
on every operation and reports `ENGINE_NOT_INTEGRATED`. The adapter must
implement the same contract honestly: `getStatus()` reflects the real engine
state; failures surface as `ERROR` (and exceptions propagate — the app logs
them, it never fakes success).

## 2. What the adapter must translate

### Profile → virtual-device config
`IdentityManager` hands the adapter the active `DeviceProfile` (via
`StorageManager.loadIdentity()` / at `create()` time — extend `SandboxRuntime`
with `create(profile: DeviceProfile)` or pass it to the adapter constructor;
do NOT change other packages' signatures). Map fields to the engine's
virtual-device config:

| DeviceProfile field | Engine target |
|---|---|
| `manufacturer` | `Build.MANUFACTURER` equivalent |
| `brand` | `Build.BRAND` equivalent |
| `model` | `Build.MODEL` equivalent |
| `device` | `Build.DEVICE` equivalent |
| `product` | `Build.PRODUCT` equivalent |
| `fingerprint` | `Build.FINGERPRINT` equivalent |
| `buildId` | `Build.ID` equivalent |
| `buildTags` / `buildType` | `Build.TAGS` / `Build.TYPE` equivalents |
| `androidVersion` / `apiLevel` | `Build.VERSION.RELEASE` / `SDK_INT` equivalents |
| `androidId` | `Settings.Secure.ANDROID_ID` equivalent |
| `extras` map | engine-specific fields (serial, telephony, MAC…) |

The generated profiles are already internally coherent (one consistent device
row + fresh `androidId`); the adapter must apply them atomically, not
re-randomize individual fields.

### installApplication(apkPath) → engine's virtual package install
- `apkPath` points at a staged copy under `filesDir/sandbox/<identityId>/apks/`
  (APK artifact only — `StorageManager.stageApk` guarantees no app data).
- Translate to the engine's virtual install (e.g. BlackBox-style
  `installPackageAsUser`). Keep the host-side staging; the engine owns
  `sandbox/<identityId>/apps/<packageName>/` afterwards.

### launchApplication(packageName) → engine's activity launch
- Translate to the engine's virtual launch path (e.g. BlackBox
  `launchApk` / stub-activity dispatch). This is the call path that ANR'd in
  v2.0–v2.4 — the adapter must be verified on-device for launch, not just
  install.

### destroy()/reset() → engine teardown
- `destroy()`: full engine teardown for the identity (mirrors what the
  app-layer `IdentityManager.deleteIdentity()` does with state files).
- `reset()`: wipe guest state inside the engine without dropping the
  identity/profile.

### Providers → capability flips
Implementations of `providers/*Provider` arrive with the adapter. When a
provider is verified working, the adapter calls
`CapabilityManager.setState(<id>, SUPPORTED, <detail>)` so the UI stops
showing UNVERIFIED. Never mark a capability SUPPORTED speculatively.

## 3. Integrator checklist

- [ ] **Manifest permissions**: add exactly what the engine needs (e.g.
      `QUERY_ALL_PACKAGES` if the app-picker `<queries>` element proves
      insufficient, `FOREGROUND_SERVICE*` if the engine runs a service,
      `REQUEST_INSTALL_PACKAGES` only if the engine's install path requires
      it — justify each in the commit message).
- [ ] **Application subclass hooks**: wire engine init/shutdown into
      `SandboxApp.onCreate()` / `onTerminate()`; do NOT rename the class or
      move the `runtime` swap point.
- [ ] **Stub activities**: if the engine needs stub/proxy activities for
      launch dispatch, declare them in `AndroidManifest.xml` with
      `android:exported="false"` and document why each exists.
- [ ] **16 KB page-size / .so alignment**: verify every native `.so` the
      engine ships is 16 KB-aligned (`zipalign -P 16` / `check`; Android 15+
      enforces this). Host app itself ships no native code.
- [ ] **targetSdk implications**: host targets 35. Confirm the engine's
      required behaviors (background launch, exact alarms, foreground-service
      types) hold at targetSdk 35; document any that force a downgrade and
      why.
- [ ] **Status honesty**: `getStatus()` must return the true engine state;
      remove/retire `ENGINE_NOT_INTEGRATED` from reachable paths once the
      adapter is in.
- [ ] **No silent host fallback**: if a feature (location, sensors, GMS…)
      cannot be virtualized, leave its capability `UNSUPPORTED`/`UNVERIFIED`
      — never return host values through a provider.
- [ ] **On-device verification**: install → launch → ANR-free foreground
      run of a real guest APK; the v2.0–v2.4 failure mode (install OK, black
      screen, "not responding") must be explicitly regression-tested.
- [ ] **Storage invariant**: engine guest state stays under
      `filesDir/sandbox/<identityId>/`; the engine must never read the host
      app's private files outside that tree.

## 4. What stays UNVERIFIED until the adapter lands

All `CapabilityManager` entries (device identity, telephony, location,
sensors, network isolation, package visibility, GMS mode, app
install/launch/uninstall, storage isolation, clipboard isolation) and all
`providers/*` interfaces. The UI renders these states directly.
