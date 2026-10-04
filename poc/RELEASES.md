# Release Notes — BoxInBox PoC

Per-version notes for every tagged release. Updated with each version
commit; the newest release is at the top. Commit hashes are the tagged
(or current) `main` commits on `naina543654-code/boxinbox`.

Nothing here is claimed as device-verified unless the note says so —
see `poc/SPOOFING_MATRIX.md` for the verification matrix.

---

## Unreleased (on `main` since `poc-v2.2`)

- Re-audit 2 residual batch (2026-10-04): closes the 7 remaining re-audit
  findings (N6/N8/N9/N10/N11/N12 + Advertising-ID claim correction).
  - MEDIUM: `TelephonyManager.getNai()` Pine-hooked → always null (new
    `ValueKind.NULL_ALWAYS`; SIM-derived NAI is a stable cross-identity
    link with no per-identity profile value).
  - MEDIUM: `IWifiManagerProxy.getPasspointConfigurations` hooked → empty
    list when active (saved Passpoint profiles identify like saved SSIDs;
    AIDL declares a plain `List`, so no slice wrapping needed).
  - MEDIUM: `IPhoneSubInfo` direct-binder residuals closed — added
    `getNaiForSubscriber`, `getGroupIdLevel1ForSubscriber` (GID1),
    `getDeviceSvnUsingSubId`, `getLine1AlphaTag`/`getVoiceMailAlphaTag`
    (+`ForSubscriber` variants), and the bare `getLine1Number` /
    `getMsisdn` / `getVoiceMailNumber` / `getDeviceIdWithFeature` /
    `getSubscriberIdWithFeature` / `getIccSerialNumberWithFeature`
    (all names verified against AOSP android14-release IPhoneSubInfo.aidl).
  - LOW: `getLine1NumberForSubscriber` now serves the per-identity MSISDN
    like `getMsisdnForSubscriber` (was null — an incoherent tell pair).
  - LOW: `BSubscriptionSpoof` returns a mutable `ArrayList` instead of
    `Collections.emptyList()` (a mutating caller would crash with
    `UnsupportedOperationException`).
  - LOW-MEDIUM: `BSpoofManager` re-applies the `android.os.Build` static
    field patches after a profile-generation invalidation (a surviving
    guest process previously kept the OLD identity's Build statics).
  - Matrix correction: Advertising ID demoted Supported → Partial — the
    raw GMS `IAdvertisingIdService` binder path is unhooked (client-library
    path is spoofed); closing it needs bindService interception, deferred.
- Re-audit 2 fix batch (2026-10-04, sources TBD — second full code audit,
  `files/CODE-AUDIT-R2-2026-10-04.md`: 58 OK / 4 partial / 1 broken of
  63 matrix rows; 22/26 batch items verified, 4 flawed; 0 regressions).
  - CRITICAL crash fix: `IWifiManagerProxy.GetConfiguredNetworks` returned a
    raw `ArrayList` where the AIDL declares `ParceledListSlice` → guest
    `ClassCastException` on any configured-networks read. Now builds an empty
    `ParceledListSlice` via reflection (the class is @hide, absent from
    android.jar).
  - CRITICAL: `SubscriptionManager.getPhoneNumber(int)`/`(int,int)` hooked →
    per-identity MSISDN (public-API bypass of the `getLine1Number` hooks).
  - HIGH: `ro.boot.serialno` now serves the per-identity serial (was leaking
    the real hardware serial, zero permission).
  - HIGH: `MediaDrm.getPropertyString("systemId")` hooked → per-identity hex
    (the batch had missed the sibling property).
  - HIGH: `gsm.version.baseband` now answered at the Java `SystemProperties`
    Pine layer too (was only covered on the getprop/exec path).
  - HIGH: `SubscriptionInfo.getCardId()` → per-identity deterministic int and
    `getGroupUuid()` → null (stable SIM links via alternate getters).
  - HIGH: Reset/Delete now clears the daemon's in-memory account map via new
    `IBAccountManagerService.clearAccountsForUser(int)` — deleting
    `accounts.conf` from disk was only half the fix (old accounts stayed
    visible in-session).
  - MEDIUM: `SystemProviderStub.query()` projection fix extended to the API
    30+ `query(Uri, String[], Bundle, CancellationSignal)` signature (the
    projection lives inside `queryArgs`; the old fix was inert on API 30+).
  - MEDIUM: `BSubscriptionSpoof` is now fully fail-closed (spoofed values
    served unconditionally, matching the `getLine1Number` Pine hook).
  None of this batch is device-verified yet — compile + AIDL + dex checked;
  hook firing needs the probe run on-device.

- `ff10944` — Per-identity dynamic radio network type (60% LTE / 25% NR /
  15% HSPA). Confirmed varying across identities on-device (NR=20 on one
  identity vs HSPA=10 on the previous).
- `d24b388` — Telephony fix: Pine-hooks `TelephonyManager.getNetworkOperatorName()`
  / `getNetworkOperator()` directly (neither the ITelephony binder hooks nor
  the SystemProperties hooks intercept those two getters on API 30+).
  Verified on-device with GMS installed (operator T-Mobile/20416/nl all PASS).
- `ab809a0` — Fixed recursive spoof-profile loading that froze the sandbox
  on the logo.
- `70070aa` — Identity-wipe rewrite: Binder proxies only, hard IPC timeouts,
  disk-first guest-package collection, daemon user-0 healing/verification,
  symlink-safe deletion, inconclusive-abort when the daemon is unresponsive.
  Trace: `filesDir/wipe-trace.log`, logcat tag `BoxInBoxWipe`. Also repaired
  two latent manual-build breakages (merge-manifest.py package attr; host
  res/ now compiled) — first successful local APK build. VERIFIED on-device
  2026-10-02: Reset and Delete both work.
- `89edc0b` — Spoof expansion: `Build.DISPLAY` (OEM-coherent derivation),
  `VERSION.INCREMENTAL` (from fingerprint), `VERSION.SECURITY_PATCH`
  (researched per row; skipped when unknown), sensor `getVendor()`/`getName()`
  Pine hooks, and `System.getProperty("os.version")` kernel hook (plausible
  5.10.x/6.1.x per API level). ProbeV2 gained expected-vs-observed rows for
  all of them.
- `df159ed` — Hotfix: `KERNEL_RE` rejected every generated kernel
  (`-4-00001` has two numeric segments); identity generation was broken.
  Regex widened; host APK rebuilt.
- `UNRELEASED-BATCH` — OS-identity leak closure (this commit): extended `Build`
  fields `TIME` (patch-date-derived), `USER`/`HOST` (`android-build`/`abfarm`),
  `BOOTLOADER`/`RADIO` (`unknown`), `SOC_MANUFACTURER`/`SOC_MODEL` (only
  verified hardware mappings; unknown hardware → skipped, never fabricated);
  fixed `VERSION.CODENAME`/`BASE_OS`/`PREVIEW_SDK_INT` release constants;
  per-identity ICCID (19-digit `89…`) + `getSimSerialNumber()` binder hook;
  cell privacy (empty `getAllCellInfo()`, null `getCellLocation()`, empty
  `getNeighboringCellInfo()`); empty `getScanResults()`; Pine hooks for
  `TimeZone.getDefault()`/`Locale.getDefault()` (country-coherent with the GPS
  city), Bluetooth name (spoofed model) / address (per-identity `02:` MAC),
  `NetworkInterface.getHardwareAddress()` on `wlan0` only, and
  `WebSettings.getDefaultUserAgent()` (prebuilt UA with spoofed model/build
  ID). Network values vary per identity (fresh MACs + ICCID each generate).
  ProbeV2 gained rows for every new spoof + cell/Wi-Fi privacy assertions.
  Camera characteristics assessed: Unsupported (no public constructor;
  per-device HAL specs unresearched) — documented gap.
  All three binaries rebuilt locally and verified in-dex. UNVERIFIED on-device.
- `UNRELEASED-STAMP` — Build identification (this commit): git SHA + commit
  date are now stamped into both APKs at build time (`poc/version-stamp.sh`;
  `FORCE_SHA`/`FORCE_DATE` overrides for the release flow, since the stamped
  APKs are committed after the sources). The probe shows a
  `Probe build: <sha> (<date)>` footer plus a `probe_build` row in the report
  header; the host home screen shows a `Build: <sha> (<date)>` footer; both
  APK `versionName`s carry the SHA (`1.0-<sha>`, `1.0-poc-runtime-<sha>`).
  Wired into both the Gradle and manual build paths. Added after repeated
  stale-install confusion on-device made the installed build unverifiable.

## `poc-v2.2` — `15b015b` (2026-10-01)

- `DEVICE_TABLE` rewritten to 84 verified rows (47x API 33 + 37x API 34,
  10 brands). API 35/36 rows removed (no verified data).

## `poc-v2.1` — `91cff2e` (2026-10-01)

- Per-identity locale: SSID/BSSID, operator, and base location are now
  random per identity (previously fixed per build).
- Location pool expanded to 121 world cities / 59 countries. Jio excluded
  per request.

## `poc-v2-stable` — `eff83d6` (2026-10-01)

- Stable checkpoint: Wakie clone launches and runs fully in-sandbox after
  the split-APK `makeApplication` fix (splitNames + splitClassLoaderNames).
- GMS support: install clones the host's real GMS packages (wiped on Reset
  Identity). Verified on-device — Wakie passed the Play Services gate.
- Random device picker: reset never repeats the same device (`0b740f2`).
- Backup tarball: `~/workspace/goals/privacy-sandbox-android-build/boxinbox-poc-v2-stable-2026-10-01.tar.gz`
  (kept outside the repo).

## `poc-v2` — `4fd8b1c` (2026-09-30)

- PoC v2 spoofing on `main`: Build fields, Android ID, telephony
  IDs/operator/SIM, Wi-Fi SSID/BSSID, virtual location, experimental sensor
  filtering; ProbeV2 EXPECTED-vs-OBSERVED comparison rows.
- Engine: zitanioi/blackbox @ `c994edf` + PoC spoof patches (sources and
  rebuilt AARs committed together).

## `poc-v1` — `48310e4` (2026-09-30)

- Baseline on branch `v1`: engine + host runtime before the v2 spoof hooks.

## 87c33e6 (2026-10-01) — MAC generator hotfix
- Fixed identity generation failing on-device with `Generated profile invalid: [network.wifiMac ...]`.
- Root cause: `newLocalMac()` only set the locally-administered bit, producing first bytes like 06/0A/12.. ~15/16 of the time, but `ProfileValidator.LOCAL_MAC_RE` requires exactly `02:`.
- Generator now forces first byte `0x02`; verified 2000/2000 generated MACs pass the validator.
- Rebuilt host APK stamped `87c33e6 (2026-10-01)`. Probe unchanged.

## 2026-10-02 (post-92c85f9) — stale-AAR trap + footer staleness (build fixes)
- On-device evidence: fresh identity generates fine (MAC fix works), but every 89edc0b/97497ff spoof row FAILs and Delete/Reset broke again on Jason's self-built host. Root cause: his Gradle build consumed an untracked hand-copied `app/libs/Bcore-release.aar` — `git pull` never refreshes it, so his host APK (sources at HEAD) ran a pre-89edc0b engine. The tracked `poc/engine/Bcore-release.aar` contains all new hook classes (verified in classes.jar: BSpoofOsIdentity, BSpoofSensors, BSpoofJvmProps, ...), md5 05d106998c1f140b44ceac778d37223e.
- host-runtime Gradle now depends on `../../engine/Bcore-release.aar` directly — the app/libs copy step (and its staleness trap) is gone.
- `generateVersionRes` (probe + host) now declares buildSha/buildDate as task inputs. Without inputs, Gradle's up-to-date check skipped the task after the first build: versionName (configuration-time) refreshed while the footer froze at the old SHA.
- Source-only changes (Gradle build files); shipped APKs in poc/apks/ are unaffected.
- Correction to the note above: the stale app/libs AAR was tracked (last touched at d24b388), not untracked — pulls delivered it; it was simply never regenerated alongside engine/. md5 c2039905f31b1760701ea0b31b752be9 (Jason's machine matched this exactly) vs engine 05d106998c1f140b44ceac778d37223e. The app/libs copy is now removed from the repo so nothing can consume it again.

## 2026-10-02 — leak patch batch (VPN visibility + SoC data + rebuilt artifacts)
- New Track F (`BSpoofNetwork` + `NetworkSpoofInjector`, registered in `HookManager`): hides the host's real VPN from guests at the ConnectivityManager API — strips TRANSPORT_VPN from returned NetworkCapabilities, renames tunnel interfaces (tun0…) to wlan0 in LinkProperties, retypes VPN NetworkInfo as WIFI. Fail-open, gated on an active profile; routing untouched (guest keeps real internet). ProbeV2 transport row should now read WIFI only / wlan0.
- SoC data fix: `socFor` is now model-aware — Galaxy A52s (SM-A528B) and A73 5G (SM-A736B) report board "lahaina" but carry the Snapdragon 778G, not the 888 the codename map produced. ProbeV2 on SM-A528B showed the mismatch.
- Diagnosis behind this build: the 02:52 ProbeV2 report (fresh probe c49d14d, identity c6d0480c) still showed the whole 89edc0b/97497ff hook batch inert (DISPLAY/TIME/USER/HOST/BOOTLOADER/SoC/kernel/sensors/timezone/locale/UA). Source review found the hook code and the host↔engine profile JSON contract sound — the failure signature matches a host APK still embedding a pre-89edc0b engine exactly. The repo APKs below are rebuilt from current sources so the installed engine is known-good by construction; if the batch still fails with these, the hooks themselves get debugged next (logcat tags BSpoofOsIdentity / BSpoofJvmProps / BSpoofSensors).
- Untested on-device, as usual: needs Jason's install + probe run.
- Artifacts rebuilt from 6c7d7c7 and committed: engine AAR md5 8762a5fa569e1e72b3cd2d9d08b61ae6 (BSpoofNetwork/BSpoofOsIdentity verified in classes.jar); host APK versionName 1.0-poc-runtime-6c7d7c7; probe 1.0-6c7d7c7 (stamps verified in resources.arsc).

## 2026-10-04 — detection-hardening batch (tell survey fixes)
- Source-verified audit of guest-observable virtualization tells (see SPOOFING_MATRIX.md); patched the cheap, high-value ones:
- `BProcFsSpoof` (new): per-process filtered `/proc/self/maps` (drops blackbox/libpine/host-package lines) + redirects for `/system/build.prop` and `/proc/version` to per-identity files the host now writes at identity creation (`ProfileStore`: `build.prop`, `proc_version`; also deleted on wipe).
- `BRootHide`: new `ProcessBuilder.start()` hook (it bypassed the `Runtime.exec` filter); `getprop` interception — single-key queries answered from the profile (`BSpoofManager.getSystemPropertySpoof`), bare `getprop` dumps the spoofed build.prop; unknown keys pass through.
- `BSpoofSystemProps`: typed overloads `getInt`/`getLong`/`getBoolean` hooked with the same key table.
- `SystemProviderStub`: `adb_enabled` / `development_settings_enabled` report `0` when a profile is active.
- `BSpoofNetwork`: `NetworkInterface.getNetworkInterfaces()` now filters tunnel interfaces.
- `IAlarmManagerProxy`: `getTimeZone()` returns the profile timezone (coheres with `TimeZone.getDefault()`).
- `BSpoofAaid` (new, via `AaidSpoofInjector`): per-identity advertising ID — the cloned GMS would otherwise hand every identity the host's real AAID (stable cross-identity link).
- Host: `SandboxApp` bakes the installed WebView's real Chrome major into each identity's UA (fixes Chrome/126 claim vs installed 121 skew); `ClientConfiguration.isHideXposed()` now true.
- Known residuals (documented, not patched): Play Integrity verdicts (unforgeable client-side), shared-UID provability, dataDir paths containing the host package (needs path virtualization — risky), Pine frames in stack traces, sensor/camera/GPU live hardware, `/proc/net/route` + DNS, `Resources.getConfiguration()` locale vs `Locale.getDefault()`.
- Untested on-device: needs Jason's install + probe run.
- Artifacts rebuilt from 4052ec2 and committed: engine AAR md5 171fbe4702eec8142503b097da41ee4e (BProcFsSpoof/BSpoofAaid/AaidSpoofInjector verified in classes.jar and host dex); host APK versionName 1.0-poc-runtime-4052ec2; probe 1.0-4052ec2 (stamps verified in resources.arsc).

## 2026-10-04 — per-identity phone number (MSISDN leak fix)
- Root cause for "Wakie detects the previous account on a fresh identity": `TelephonyManager.getLine1Number()` had NO hook — neither the `ITelephonyManagerProxy` AIDL hook nor a Pine API hook covered it — so every guest read the host SIM's REAL phone number, identical across all identities. Any backend keying accounts by device-reported number links every new account to the previous one.
- Fix: per-identity E.164 MSISDN (`telephony.phoneNumber`), generated at identity creation coherent with the profile country (calling-code table for all 59 pool countries), validated (`^\+\d{7,15}$`), plumbed through the profile JSON contract, and served at BOTH choke points (AIDL proxy + Pine `TelephonyManager.getLine1Number()` hook). The real SIM number is never passed through when a profile is active.
- ProbeV2 gains a `telephony.line1Number` comparison row (needs READ_PHONE_STATE).
- Untested on-device: needs Jason's install + probe run with phone permission granted.
- Artifacts rebuilt from 61844fb and committed: engine AAR md5 7efee965ea8cca3f04e7470d97cb7484 (GetLine1Number proxy + Pine hooks verified in host dex); host APK versionName 1.0-poc-runtime-61844fb; probe 1.0-61844fb (stamps verified in resources.arsc; probe gains the telephony.line1Number row).

## 2026-10-04 — audit-fix batch (CODE-AUDIT-2026-10-04.md)
Full six-track code audit (51 matrix rows: 44 OK / 2 partial / 5 broken). Fixed:
- CRITICAL: SubscriptionManager/SubscriptionInfo had no ISub proxy — real MSISDN+ICCID leaked around the getLine1Number fix. New BSubscriptionSpoof Pine hooks (empty list / per-identity number+ICCID, fail-closed).
- CRITICAL: IPhoneSubInfo passthrough — hooked getDeviceId/getImeiForSubscriber/getDeviceIdForPhone/getSubscriberIdForSubscriber/getIccSerialNumberForSubscriber/getMsisdnForSubscriber/getVoiceMailNumberForSubscriber.
- CRITICAL: Widevine deviceUniqueId unhooked — new BSpoofMediaDrm serves deterministic per-identity SHA-256("widevine-"+androidId).
- CRITICAL: ro.serialno leaked — new per-identity device.serial (16 hex) served via Build.SERIAL patch, SystemProperties table, getprop table, build.prop, and profile-gated getSerialForPackage.
- CRITICAL: getConfiguredNetworks leaked real saved SSIDs/BSSIDs — now empty when active.
- BROKEN: dead @ProxyMethod names removed (getSubscriberId/getSimSerialNumber/getLine1Number — nonexistent on ITelephony API 33/34, verified vs AOSP); Pine public-API hooks are the live enforcement (added for getSubscriberId/getSimSerialNumber/getImei/getMeid/getDeviceId/getVoiceMailNumber).
- BROKEN: SystemProviderStub query() honored a hardcoded {"name","value"} cursor — guests saw the literal key string; now honors the caller's projection. Dev-mode rows use the requested key name.
- HIGH: accounts.conf now wiped on Reset/Delete (virtual accounts no longer survive).
- HIGH: stale in-memory profile — host bumps profiles/generation on save/delete; engine re-stats throttled (2s) and invalidates.
- IdentityManager.generateIdentity() now wipes leftover guest state if the identity record is missing/corrupt (was: silent build-over).
- ProfileStore: atomic writes (temp+rename), per-identity wlan0_address file, generation bump on delete.
- SpoofProfile.fromJson: strict for identity-critical fields (wifiMac/bluetoothMac/simSerial/phoneNumber/serial) — no more per-parse fabrication.
- BSpoofManager: fail-closed activation (empty {} no longer counts as spoofed); apiLevel absent no longer fabricates "0"; gsm.version.baseband answered from profile radio.
- BProcFsSpoof: /sys/class/net/wlan0/address redirected to per-identity file.
- BluetoothAdapter.getBondedDevices() → empty set when active.
- ClassInvocationStub: hook exceptions now fall back to the real method (was: crash into guest).
- BSpoofOsIdentity: idempotency guard.
- ProbeV2: 14 new rows (subscriberId, voiceMail, deviceId, build.serial, sysprop.ro.serialno, submgr.activeList, wifi.configuredNets, mediadrm.deviceUid, proc.version, sys.build.prop, sys.wlan0.address, bt.bondedDevices, settings.adb_enabled) + BLUETOOTH_CONNECT permission.
- UI: full visual redesign (cards, pills, collapsible technical details, danger zone) — same features, decluttered.
- Untested on-device: entire batch needs Jason's install + probe run.
- Artifacts rebuilt from 0f3fd99 and committed: engine AAR md5 b1c692c47adea72cc7496cc1c6d25e61 (BSubscriptionSpoof, BSpoofMediaDrm, IPhoneSubInfo hooks, GetConfiguredNetworks verified in host dex); host APK versionName 1.0-poc-runtime-0f3fd99 (new UI); probe 1.0-0f3fd99 (14 new audit rows + BLUETOOTH_CONNECT).
