# Recommendation — Engine for the Privacy Sandbox PoC

## Verdict

**Use `zitanioi/blackbox` at commit `c994edf21fdecbf22196f9e2b0d447af97140f2e` (2026-09-10, HEAD at evaluation).**

Keep `alex5402/newblackbox` @ `89b59836c66f173756a4ae258cf379a957649820` (2026-07-19) as the fallback, specifically for two donor parts: the native hidden-API exemption (`hidden_api.cpp`) and the optional VPN network mode (`ProxyVpnService` + `ClientConfiguration.isUseVpnNetwork`).

## Why zitanioi

1. **Launch-path fixes vs the v2 failure signature — with a necessary correction.**
   The v2 prototypes (alex5402-based) failed identically with: guest installs → black screen → "Privacy Sandbox is not responding" (ANR). zitanioi's commit `ed61551` (2026-08-30, *"修复 Android 16 上虚拟应用启动失败与 WebView 禁网问题"*) documents fixes for exactly this class of failure:
   - `HCallbackProxy` NPE: `mActivityCallbacks` is lazily initialized and null for lifecycle-only transactions → null-guard added before the for-each.
   - On Android 16, `ActivityThread.getLaunchingActivity()` was removed, so the old swap path silently did nothing; the S+ branch now **additionally swaps the `LaunchActivityItem`'s own `mIntent`/`mInfo`** — commit message: "消除 stub 启动死循环" (eliminate the stub-launch infinite loop; previously MIUI's `rapidActivityLaunch` killed it).

   Mechanism plausibility: `HCallbackProxy.handleMessage` re-queues the launch message at the front of the main-thread queue (`sendMessageAtFrontOfQueue`) whenever `handleLaunchActivity` reports a swap. If the swap never takes effect (removed API / wrong field), the main thread spins on the same message forever → black screen + ANR. That is exactly the v2 symptom — so zitanioi's fix is technically plausible and matches the historical failure mechanism.

   **Correction (verified in source at the evaluated commits):** alex5402 @ `89b5983` is *not* missing the launch swap. Its `handleLaunchActivity` (`Bcore/.../fake/service/HCallbackProxy.java`) already contains:
   - the `mActivityCallbacks` null guard (`if (mActivityCallbacks == null) { … return null; }`, lines ~106–109), and
   - an `if (BuildCompat.isTiramisu())` (API 33+) branch that directly swaps the item's `mIntent`/`mInfo` (`_set_mIntent(stubRecord.mTarget)` / `_set_mInfo(activityInfo)`, lines ~175–178).

   So on Android 13–16 both engines directly swap the `LaunchActivityItem` itself — the launch fix alone does **not** decide the engine choice. The remaining launch-path differences are narrow and version-sliced:
   - alex5402's `else if (BuildCompat.isS())` branch (API 31–32, Android 12/12L) still calls `getLaunchingActivity(token)` **without a null guard** and does *not* additionally swap the item; zitanioi null-guards the record (`if (record != null)`) *and* always swaps the item on every S+ path in a single unified branch.
   - Neither difference is proven to matter on Android 15/16 — **Jason's on-device test remains the decisive evidence**, not the commit messages.

   **What still separates zitanioi (and keeps it the pick):**
   - `checkPermissionForDevice` hook (`IActivityManagerProxy.java:639`): Android 16 routes `checkSelfPermission` through `ActivityManager.checkPermissionForDevice`; without the hook, virtual package names/UIDs miss in the real PMS and `INTERNET` is misjudged DENIED → WebView guests set `blockNetworkLoads=true` (`net::ERR_CACHE_MISS`). **alex5402 has no such hook anywhere** (verified: zero matches).
   - `JniHook` Android 15/16 guards (`ed61551`: null guards across ART method lookup + pending-exception cleanup so `RegisterNatives` failures don't crash) — alex5402's `JniHook.cpp` predates these.
   - Newer Android-16-focused maintenance window (HEAD 2026-09-10 vs 2026-07-19; three Android-16 commits `ed61551`, `a93bea8`, `94263ef` — the last upgrades the Pine ART hook engine for Android 16).
   - Broader native storage-redirection layer (`NativeIOHook` GOT rewriting, 30-entry table) — the isolation primitive the per-identity storage model needs.
2. **Newer and actively fixed for 15/16.** zitanioi HEAD is 2026-09-10, ~7 weeks newer than alex5402, with three Android-16-specific commits (`ed61551`, `a93bea8`, `94263ef` — the last upgrades the Pine ART hook engine to upstream canyie/pine master for Android 16).
3. **Smaller, cleaner hook surface where it counts.** zitanioi's dead code is minimal (unwired `ProxyVpnService`); alex5402 carries four dead-code hook paths (`AndroidIdProxy`, `ISystemSensorManagerProxy`, `AntiDetection.cpp` file blockers, `FileSystemHook.cpp` `new_open`) that look active in a file listing but never install — a trap for anyone extending the spoofing layer.
4. **Broader storage-redirection layer** (`NativeIOHook` GOT rewriting, 30-entry table) — the isolation primitive the per-identity storage model needs.
5. **Xposed-module support** (Pine engine, `BXposedManager`) is already integrated — useful later for guest-side instrumentation, and it passed XposedDetector checks per the README.

## Launch-path analysis (black screen / ANR)

How a guest activity launches (both engines, same architecture):

1. The guest's real intent is wrapped: `ActivityManagerProxy` replaces the target with a
   **stub activity** declared in the host manifest, stashing the real intent plus
   `_B_|_user_id_` / `_B_|_activity_info_` extras on it (`ProxyActivityRecord.saveStub`).
2. AMS delivers a launch transaction to the guest process's `ActivityThread.H`.
   `HCallbackProxy.handleMessage` intercepts it and calls `handleLaunchActivity`,
   which reads the stub intent back out (`ProxyActivityRecord.create(intent)` looks for the
   `_B_|_` extras) and swaps the transaction's intent/activityInfo to the real target.
3. If `handleLaunchActivity` returns true (a swap happened), the message is **re-queued at the
   front of the main-thread looper** (`getH().sendMessageAtFrontOfQueue(Message.obtain(msg))`)
   and consumed. On the second pass the intent no longer carries the stub extras, so
   `create()` yields `activityInfo == null`, `handleLaunchActivity` returns false, and the
   message falls through to the real ActivityThread handler, which instantiates the guest
   activity.

Failure mode → v2 symptom. If the swap silently misses its target object, the stub extras
persist, so every re-queued pass returns true again: the main thread spins on the same
message forever. No frames are drawn (black screen) and the main thread never goes idle
(ANR → "Privacy Sandbox is not responding"). This is exactly what the v2 prototypes showed.

Where the engines differ at the evaluated commits
(`Bcore/src/main/java/top/niunaijun/blackbox/fake/service/HCallbackProxy.java`):

| | zitanioi `c994edf` | alex5402 `89b5983` |
|---|---|---|
| `mActivityCallbacks` null guard (lifecycle-only transactions) | yes (~line 113, with explanatory comment) | yes (~line 106, logs + returns null) |
| API 33+ direct item swap (`_set_mIntent(stubRecord.mTarget)` / `_set_mInfo(activityInfo)` on the `LaunchActivityItem` itself) | yes — inside the unified `isS()` branch (~lines 193–195), with a comment noting `getLaunchingActivity()` is gone | yes — dedicated `if (BuildCompat.isTiramisu())` branch (~lines 175–178) |
| `getLaunchingActivity(token)` record swap (API 31–32 path) | null-guarded (`if (record != null)`, ~line 183) | **no null guard** (~line 180) |
| Item swap on the API 31–32 path | yes (same unified branch) | no (item untouched on that branch) |

Net: on Android 13–16 both engines swap the `LaunchActivityItem` itself, so the
historical "swap missed" loop is addressed in both. zitanioi's `ed61551` additionally
hardens the API 31–32 path and documents the deadlock explicitly ("消除 stub 启动死循环").
Whether the remaining S-path difference matters on Jason's Android 15/16 device is
**untested** — the on-device test decides, not the diff.

Related Android-16 launch-path fixes only in zitanioi: `checkPermissionForDevice` hook
(`IActivityManagerProxy.java:639`, fixes WebView `net::ERR_CACHE_MISS` from misjudged
`INTERNET` denial) and the `JniHook` Android 15/16 pending-exception/null guards
(both in `ed61551`).

## What zitanioi does NOT give you (build it on top)

Per the source audit (CAPABILITY_MATRIX.md), neither engine ships per-identity spoofing for:
- **Build fields** (`Build.MANUFACTURER` etc.) — zitanioi has none; alex5402 has a hardcoded Pixel 6/Android 12 `__system_property_get` hook that also affects the host process. Plan: implement per-identity property spoofing modeled on alex5402's `VirtualSpoof.cpp` but scoped to the guest (hook at the guest's linker namespace / per-UID check) with a configurable device profile, and patch `android.os.Build` static fields via the JNI hook layer at guest bind time.
- **Android ID** — absent in zitanioi; dead code in alex5402. Plan: hook the SettingsProvider path (`ISettingsProviderProxy.getStringForUser`) per virtual user, returning a per-identity random 64-bit hex ID.
- **Sensors** — absent/dead in both. Plan: proxy `SensorManager` / `SystemSensorManager` per identity (spoof or zero-out).
- **Signature spoofing** (microG prerequisite) — both return real APK signatures. Plan: add a `FAKE_PACKAGE_SIGNATURE`-style override in `PackageManagerCompat`/`BPackage` keyed per guest package.
- **Telephony IDs** are constant `md5(hostPkg)` in both — fine as a stopgap, but make them per-identity random.

## Known risks

1. **targetSdk ≥ 33 vs hidden-API enforcement (biggest structural risk).** Both engines recommend host `targetSdk ≤ 28`; ours must be ≥ 33 (minSdk 33). The engines' reflection shims (`black.*`) and exemption hacks (FreeReflection 3.0.1 in zitanioi) face full blacklist enforcement on API 33–36. **Mitigation:** keep alex5402's native `setHiddenApiExemptions` approach (`hidden_api.cpp`) as a donor — belt and suspenders — and smoke-test guest launch on a real API 35/36 device immediately after integration. If `Reflection.unseal` stops working on 16, this is where it breaks.
2. **ART/JNI hook fragility on 15/16.** `JniHook` patches `ArtMethod` entry points; zitanioi added 15/16 guards, but each new Android release can shift the structs. The Pine trampoline hooks are the most fragile — keep Xposed support optional/disabled until the core launch path is stable.
3. **`NativeIOHook` GOT rewriting** already needed one SIGSEGV fix (`99cc750`); `DT_BIND_NOW`/RELRO-hardened libs can defeat it. Watch for native crashes in guests that `dlopen` aggressively (games).
4. **16KB page alignment** depends on NDK r28+ at build time (zitanioi sets no explicit flags; alex5402 forces `-z,max-page-size=16384`). Verify every release `.so` with `readelf -l` (see BUILD_NOTES.md).
5. **No per-guest network identity by default** — guest traffic uses the host UID. If per-identity egress IP matters, port alex5402's optional VPN mode (`ProxyVpnService`) — it is the only in-source guest-traffic routing mechanism found.

## What to watch on API 33–36

- `ActivityThread` handler/`ClientTransaction`/`LaunchActivityItem` internals (launch path — re-verify each API bump; Android 16 removed `getLaunchingActivity`).
- `ActivityManager.checkPermissionForDevice` (16+) — zitanioi hooks it; ensure it stays hooked.
- AttributionSource UID checks (14+) — both engines paper over these; new system services may add more.
- `PackageParser`/`ApplicationInfo` hidden APIs used by `PackageParserCompat` — compileSdk 28 in zitanioi's `:Bcore` means all newer behavior is reached via reflection; audit on each target bump.
- WebView in guests on 16 (`net::ERR_CACHE_MISS` class of bug) — zitanioi fixed one instance; similar permission-mapping bugs may exist for other permissions.

## Integration pointers (zitanioi)

- Engine AAR: `:Bcore:assembleRelease` → `Bcore/build/outputs/aar/Bcore-release.aar` (builds are blocked in this sandbox — see BUILD_NOTES.md; rerun on a normal machine).
- Host `Application.attachBaseContext`: `BlackBoxCore.get().doAttachBaseContext(base, ClientConfiguration…)`; `onCreate`: `BlackBoxCore.get().doCreate()` (see zitanioi README "Step 1").
- Identity lifecycle: `BlackBoxCore.createUser(userId)` / `deleteUser(userId)`; install: `installPackageAsUser(File, userId)`; launch: `launchApk(packageName, userId)`.
- Per-identity config seam: `BLocationManager.setLocation(userId, pkg, …)` shows the intended pattern (per-user, per-package state in a `B*Manager`). Follow it for the new spoofing managers.
- Donor code from alex5402 if needed: `Bcore/src/main/cpp/Utils/VirtualSpoof.cpp` (property spoof pattern), `Bcore/src/main/cpp/hidden_api.cpp` + `BoxCore.cpp::disableHiddenApi` (native exemption), `proxy/ProxyVpnService.java` + `BlackBoxCore.initVpnService` (guest VPN routing).
