# Device Test Instructions — PoC v2 (runtime + spoofing)

Goal: produce on-device evidence for the GO/NO-GO decision AND verify the
spoofing hooks. Test in this order; capture a logcat for each stage
(commands below). The spoofing capability matrix is `SPOOFING_MATRIX.md` —
this file is the hands-on procedure.

## Setup

```bash
adb devices                      # device connected, USB debugging on
adb install -r apks/probe-v2.apk
adb install -r apks/host-runtime-poc-v2.apk
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
3. In the guest probe, tap **Copy Report**. Scroll to the
   **"EXPECTED vs OBSERVED (ProbeV2)"** section at the bottom: every row
   shows EXPECTED (from the staged profile) vs OBSERVED (what the guest
   actually sees) with PASS / FAIL / UNKNOWN / INFO.
   - PASS = the spoof hook held. FAIL = it didn't — report the row.
   - UNKNOWN = unmeasurable (no permission, OS-masked value, standalone
     run) — not a failure, but note the reason shown.
   - INFO = host-provided by design (`Build.VERSION.*`, network
     transport) or observational (packages, movement).
   - Check Internet access works from the guest (the engine passes through
     on the host UID — real routing is intentional).

## 5. Reset — clean state

1. Back in the host, **Manage Identity** → **Reset Identity**.
2. Expected: toast confirming reset with androidId changed (old → new).
3. Re-open **Manage Identity**: the guest-app list must be EMPTY
   (virtual user 0 was deleted and recreated — guest state wiped).
4. Generate → install → launch again, and confirm the ProbeV2 rows now
   PASS against the NEW profile (new Android ID, new telephony IDs).

## 6. Spoofing spot-checks (ProbeV2 deep rows)

Run these after a successful step 4, all inside the guest probe:

1. **Location**: grant the guest probe location permission (Android will
   prompt, or grant via Settings). The ProbeV2 location row should read
   PASS against the profile fix (default: San Francisco + jitter).
   Run the probe standalone (outside the sandbox) for contrast — the
   standalone location row uses the real fix.
2. **Sensors**: the sensor row PASSes only on exact type-set equality.
   If the guest shows extra host-only sensors, the Pine hook didn't land
   on this ART version — report the missing/unexpected type ints verbatim.
3. **Wi-Fi**: connect the device to Wi-Fi first. The guest should report
   the profile SSID/BSSID, not the real network's. If the row shows
   `<unknown ssid>` or `02:00:00:00:00:00`, the OS masked it (UNKNOWN,
   not FAIL) — note it.
4. **Telephony**: rows are informative on most devices (getters may return
   empty without cellular state → UNKNOWN). A crash here is the real
   signal to report.
5. **Packages**: the INFO section lists what the guest can see — confirm
   no host-only apps appear.

## Logcat (one per stage)

```bash
# Engine init + identity creation
adb logcat -c && adb logcat -s "SandboxApp" "BlackBox" "Bcore" "*:E" > init.log

# Guest install
adb logcat -c && adb logcat -s "BlackBox" "PackageManager" "*:E" > install.log

# Guest launch (watch for ANR)
adb logcat -c && adb logcat -s "BlackBox" "ActivityManager" "WindowManager" "*:E" > launch.log

# ProbeV2 EXPECTED vs OBSERVED rows (greppable, one line per field)
adb logcat -c && adb logcat -s "ProbeV2" > probev2.log
# Row format: ProbeV2: FIELD=<name> EXPECTED=<e> OBSERVED=<o> STATE=<PASS|FAIL|UNKNOWN|INFO>

# Spoof-hook diagnostics (engine side)
adb logcat -c && adb logcat -s "BSpoofManager" "BSpoofLocation" "BSpoofSensors" "*:E" > spoof.log

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
4. ProbeV2 rows: paste the full `probev2.log` (or the on-screen section).
   Which rows PASS, which FAIL, which UNKNOWN — and the reason shown for
   each UNKNOWN.
5. Did Reset wipe the guest list?
6. After re-identifying, did the ProbeV2 EXPECTED values change (new
   Android ID / telephony IDs)?

A black screen / ANR at step 4 is a valid NO-GO signal — report it as-is.
FAIL rows in step 4 are equally valuable: they say exactly which hook did
not hold on this Android version.
