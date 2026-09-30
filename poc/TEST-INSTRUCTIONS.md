# Device Test Instructions — Phase 1 PoC

Goal: produce on-device evidence for the GO/NO-GO decision. Test in this
order; capture a logcat for each stage (commands below).

## Setup

```bash
adb devices                      # device connected, USB debugging on
adb install -r apks/probe-v1.apk
adb install -r apks/host-runtime-poc-v1.apk
```

Two apps appear: **"Probe"** and **"Sandbox PoC (Runtime)"**.

## 1. Baseline — standalone probe (host values)

1. Open **Probe**, tap **Copy Report**, paste it somewhere safe. This is the
   HOST baseline (raw observed values: Build fields, Android ID, sensors,
   network, paths, GMS, virtualization indicators).

## 2. Generate identity — runtime host

1. Open **Sandbox PoC (Runtime)** → **Generate Identity**.
   - Expected: a toast "OK: identity <id>; androidId <…>", profile rows shown.
   - Behind the scenes: a fresh coherent device profile was generated AND the
     engine created virtual user 0 (`create`).
   - If the engine fails: `ERROR` + toast with the failure; nothing is faked.

## 3. Install probe into the sandbox

1. **Manage Identity** → **Clone App** → pick **Probe** from the list.
2. Expected: "OK: installed … into sandbox".
3. In **Manage Identity**, the guest-app list should now show
   `com.sandboxpoc.probe`.

## 4. Launch the guest

1. In **Manage Identity**, tap **Launch** next to `com.sandboxpoc.probe`.
2. **This is the decisive test.** The v2 prototypes failed here with a black
   screen followed by "Privacy Sandbox is not responding" (ANR). Watch for:
   - Guest probe opens normally → evidence the launch path works on this device.
   - Black screen / ANR → the engine's LaunchActivityItem swap still fails on
     this Android version; capture the ANR trace (below).
3. In the guest probe, tap **Copy Report**. Compare against the host baseline:
   - `Build.*` fields, Android ID, sensors, paths, GMS: the engine has **no**
     spoofing (source-verified), so these will match the host. That is an
     honest `UNVERIFIED`/`UNSUPPORTED`, not a bug.
   - `Settings.Secure.ANDROID_ID` specifically: note whether it differs (the
     engine isolates some settings per virtual user — on-device evidence decides).
   - Check Internet access works from the guest (the engine passes through on
     the host UID).

## 5. Reset — clean state

1. Back in the host, **Manage Identity** → **Reset Identity**.
2. Expected: toast confirming reset with androidId changed (old → new).
3. Re-open **Manage Identity**: the guest-app list must be EMPTY
   (virtual user 0 was deleted and recreated — guest state wiped).
4. Generate → install → launch again, and confirm the guest-observed
   Android ID/profile differs from step 4's report.

## Logcat (one per stage)

```bash
# Engine init + identity creation
adb logcat -c && adb logcat -s "SandboxApp" "BlackBox" "Bcore" "*:E" > init.log

# Guest install
adb logcat -c && adb logcat -s "BlackBox" "PackageManager" "*:E" > install.log

# Guest launch (watch for ANR)
adb logcat -c && adb logcat -s "BlackBox" "ActivityManager" "WindowManager" "*:E" > launch.log

# ANR trace (if the app stops responding)
adb pull /data/anr/traces.txt anr-traces.txt

# Crashes
adb logcat -b crash > crash.log
```

## What to report back

For each step: **what you tapped, what you saw (exact text), and the logcat
file**. Specifically:
1. Did "Generate Identity" succeed? (toast text)
2. Did the probe install into the sandbox? (guest list shows it?)
3. Did the guest probe LAUNCH — and what did its screen show?
4. Guest vs host report diffs (Android ID, Build fields, sensors, Internet).
5. Did Reset wipe the guest list?
6. After re-identifying, did the guest-observed values change?

A black screen / ANR at step 4 is a valid NO-GO signal — report it as-is.
