# Release Notes — BoxInBox PoC

Per-version notes for every tagged release. Updated with each version
commit; the newest release is at the top. Commit hashes are the tagged
(or current) `main` commits on `naina543654-code/boxinbox`.

Nothing here is claimed as device-verified unless the note says so —
see `poc/SPOOFING_MATRIX.md` for the verification matrix.

---

## Unreleased (on `main` since `poc-v2.2`)

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
