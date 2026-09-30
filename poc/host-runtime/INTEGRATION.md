# Engine Integration — BlackBox (implemented)

This project is the runtime-enabled PoC: the BlackBox engine
(zitanioi/blackbox, built from source as `app/libs/Bcore-release.aar`) is
integrated behind the `SandboxRuntime` seam. No engine imports appear outside
`runtime/BlackBoxRuntime.kt` and `SandboxApp.kt` (init only).

## Adapter: `runtime/BlackBoxRuntime.kt`

One active identity maps to one BlackBox virtual user (`VIRTUAL_USER_ID = 0`).

| SandboxRuntime method | Engine mapping |
|---|---|
| `create()` | `BUserManagerService.createUser(0)` |
| `start()` | ensure virtual user exists (engine starts with the host process) |
| `stop()` | `stopPackage()` for every installed guest package |
| `installApplication(apkPath)` | `BlackBoxCore.installPackageAsUser(File, 0)`; throws if `!result.success` |
| `uninstallApplication(pkg)` | `BlackBoxCore.uninstallPackageAsUser(pkg, 0)` |
| `launchApplication(pkg)` | `BlackBoxCore.launchApk(pkg, 0)`; throws if it returns false |
| `stopApplication(pkg)` | `BlackBoxCore.stopPackage(pkg, 0)` |
| `reset()` | `deleteUser(0)` + `createUser(0)` — guest state wiped |
| `destroy()` | `deleteUser(0)` |
| `getStatus()` | honest local state machine (`NOT_CREATED`/`ACTIVE`/`STOPPED`/`DESTROYED`/`ERROR`) |

`installedGuestPackages()` (not part of the 10-method contract) lists guest
packages for the UI.

**Threading:** every method does Binder IPC into the engine's server process
and refuses to run on the main thread (fail-fast `check`). All UI call sites
use `Ui.bg()` (background thread + toast + re-render). The v2 prototype ANR'd
on main-thread engine calls — this is the fix.

**Engine init** (`SandboxApp`): `doAttachBaseContext(base, config)` in
`attachBaseContext` (passes `base`, not `this`), `doCreate()` in `onCreate`.
Init failures are logged, never silent, and never crash the host.

## Deliberately NOT implemented

Device-profile spoofing. The engine provides no `Build`-field, Android-ID,
or sensor virtualization (verified in the source audit), so the adapter does
not touch the `DeviceProfile`. The probe APK measures what the guest actually
observes; capability states remain `UNVERIFIED`/`UNSUPPORTED` until on-device
evidence says otherwise. Nothing is faked.

## Build

- Gradle (on a normal machine): `./gradlew assembleDebug` / `./gradlew assembleRelease`
- Manual (this sandbox): `./build-manual.sh all`
- The AAR is rebuilt from engine source via `../engine/build-aar-manual.sh all`.
