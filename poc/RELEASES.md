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
  res/ now compiled) — first successful local APK build.
- `89edc0b` — Spoof expansion: `Build.DISPLAY` (OEM-coherent derivation),
  `VERSION.INCREMENTAL` (from fingerprint), `VERSION.SECURITY_PATCH`
  (researched per row; skipped when unknown), sensor `getVendor()`/`getName()`
  Pine hooks, and `System.getProperty("os.version")` kernel hook (plausible
  5.10.x/6.1.x per API level). ProbeV2 gained expected-vs-observed rows for
  all of them.
- `df159ed` — Hotfix: `KERNEL_RE` rejected every generated kernel
  (`-4-00001` has two numeric segments); identity generation was broken.
  Regex widened; host APK rebuilt.

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
