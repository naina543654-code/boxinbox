# Build Notes — Privacy Sandbox Engine Eval

Date basis: **2026-09-29 UTC**. This file records only commands that were actually run,
their exact outputs/exit codes, exact environment facts, NDK versions, and commit hashes.
No planned or unexecuted commands are included.

## Commits evaluated

| Engine | Commit (HEAD at clone) | Date |
|---|---|---|
| zitanioi/blackbox | `c994edf21fdecbf22196f9e2b0d447af97140f2e` | 2026-09-10 |
| alex5402/newblackbox | `89b59836c66f173756a4ae258cf379a957649820` | 2026-07-19 |

Clones live at (do not push; remotes untouched):
- `~/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/`
- `~/workspace/goals/privacy-sandbox-android-build/poc/engines/alex5402-newblackbox/`

## Build environment (verified 2026-09-29 UTC)

- `ANDROID_HOME` = `ANDROID_SDK_ROOT` = `~/android-sdk`
- `GRADLE_USER_HOME` = `~/workspace/.gradle` — **required on every invocation**; without it
  the wrapper uses `~/.gradle`, finds no seeded distribution, and attempts a fresh download
  (which fails in this sandbox — see below).
- `~/android-sdk/platforms/android-{28,30,33,34,35,36}/android.jar` — all six verified present.
  (Two platform archives had nested top-level dirs and were flattened during setup:
  `platforms/android-30/android-11/*` → `platforms/android-30/`,
  `platforms/android-33/android-33-ext5/*` → `platforms/android-33/`.)
- `~/android-sdk/build-tools/` contains `28.0.2` and `35.0.0`. (`build-tools_r28.0.2-linux.zip`
  extracted with a nested `android-9/` top-level dir; flattened 2026-09-29 with
  `mv android-9/* . && rmdir android-9` — exit 0 — verified `aapt`, `aapt2`, `d8`, `dx` present.)
- `~/android-sdk/platform-tools/adb version` → `Android Debug Bridge version 1.0.41 / Version 37.0.1-15733141`.
- `~/android-sdk/cmdline-tools/latest` present.
- JDKs present: `~/jdk/jdk-11.0.32.1+1` (Temurin 11.0.32.1), `~/jdk/jdk-17.0.20.1+1`
  (Temurin 17.0.20.1), `~/jdk/jdk-21` (Temurin 21.0.12.1+1).
- System CMake 3.28.3 (`/usr/bin/cmake`); `ninja` not installed.
- `~/.gradle/gradle.properties` still sets a global
  `org.gradle.java.home=/home/hatch/workspace/tooling/jdk-17.0.20.1+1`; the commands below
  override it per invocation with `-Dorg.gradle.java.home=…`.
- Temporary egress-proxy `systemProp.http(s).proxy*` entries (with credentials) that were added
  to `~/.gradle/gradle.properties` and `~/workspace/.gradle/gradle.properties` for downloads
  have been **removed** (2026-09-29). Remaining contents: `org.gradle.java.home` in the former,
  `org.gradle.caching=false` in the latter. No proxy credentials are stored anywhere.

## Gradle distributions — download (WORKED)

Ran 2026-09-29, exit 0:

```bash
cd ~/workspace/.sdktmp && curl -fL --retry 3 -o gradle-6.7.1-bin.zip "https://services.gradle.org/distributions/gradle-6.7.1-bin.zip" && curl -fL --retry 3 -o gradle-8.14.5-bin.zip "https://services.gradle.org/distributions/gradle-8.14.5-bin.zip" && ls -la gradle-*-bin.zip
```

Result: `gradle-6.7.1-bin.zip` (102,842,885 bytes), `gradle-8.14.5-bin.zip` (138,068,841 bytes).

## Gradle wrapper seeding — manual (WORKED)

The wrappers' own downloads cannot traverse the sandbox proxy, so each distribution zip was
manually unzipped into the wrapper cache **without modifying `gradle-wrapper.properties`**,
with a `<name>.zip.ok` marker touched alongside:

- `~/workspace/.gradle/wrapper/dists/gradle-6.7.1-bin/bwlcbys1h7rz3272sye1xwiv6/` (unzipped `gradle-6.7.1/`)
- `~/workspace/.gradle/wrapper/dists/gradle-8.14.5-bin/690y85m0j9nfaub7xoiayko8a/` (unzipped `gradle-8.14.5/`;
  the zip itself was also copied into this dir via
  `cp ~/workspace/.sdktmp/gradle-8.14.5-bin.zip ~/workspace/.gradle/wrapper/dists/gradle-8.14.5-bin/690y85m0j9nfaub7xoiayko8a/gradle-8.14.5-bin.zip`)

(The `<hash>` dir names are base-36 MD5 of the distribution URL — verified by recomputation.)

## `--version` runs (WORKED — both exit 0)

zitanioi (Gradle 6.7.1 needs JDK 11):

```bash
export GRADLE_USER_HOME=~/workspace/.gradle
cd ~/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox
export JAVA_HOME=~/jdk/jdk-11.0.32.1+1
./gradlew --no-daemon -Dorg.gradle.java.home="$JAVA_HOME" --version
```

Output (exit 0):
```text
------------------------------------------------------------
Gradle 6.7.1
------------------------------------------------------------

Build time:   2020-11-16 17:09:24 UTC
Revision:     2972ff02f3210d2ceed2f1ea880f026acfbab5c0

Kotlin:       1.3.72
Groovy:       2.5.12
Ant:          Apache Ant(TM) version 1.10.8 compiled on May 10 2020
JVM:          11.0.32.1 (Eclipse Adoptium 11.0.32.1+1)
OS:           Linux 7.0.0-39-generic amd64
```

alex5402 (Gradle 8.14.5 needs JDK 17+):

```bash
export GRADLE_USER_HOME=~/workspace/.gradle
cd ~/workspace/goals/privacy-sandbox-android-build/poc/engines/alex5402-newblackbox
export JAVA_HOME=~/jdk/jdk-17.0.20.1+1
./gradlew --no-daemon -Dorg.gradle.java.home="$JAVA_HOME" --version
```

Output (exit 0):
```text
------------------------------------------------------------
Gradle 8.14.5
------------------------------------------------------------

Build time:    2026-05-07 11:03:29 UTC
Revision:      62345becae08b13e793521816d585102fea66398

Kotlin:        2.0.21
Groovy:        3.0.25
Ant:           Apache Ant(TM) version 1.10.15 compiled on August 25 2024
Launcher JVM:  17.0.20.1 (Eclipse Adoptium 17.0.20.1+1)
Daemon JVM:    /home/hatch/jdk/jdk-17.0.20.1+1 (from org.gradle.java.home)
OS:            Linux 7.0.0-39-generic amd64
```

Caution (observed exact failure): running `./gradlew --version` **without**
`GRADLE_USER_HOME=~/workspace/.gradle` makes the wrapper look in `~/.gradle`, miss the seeded
dist, print `Downloading https://services.gradle.org/distributions/gradle-<ver>-bin.zip`, and
fail with `javax.net.ssl.SSLException: Unsupported or unrecognized SSL message` (exit 1) —
the wrapper's own JVM has no proxy configured. This is an environment gotcha, not an engine finding.

## NDK setup

### r28c (28.2.13676358) — installed and VERIFIED

`~/android-sdk/ndk-dl/ndk.zip` (722,261,334 bytes) is a valid zip (`unzip -l` exit 0,
9001 entries, top dir `android-ndk-r28c/`). Installed with (ran 2026-09-29, exit 0):

```bash
mkdir -p ~/android-sdk/ndk/28.2.13676358 && cd ~/android-sdk/ndk && unzip -q -o ../ndk-dl/ndk.zip && mv android-ndk-r28c/* 28.2.13676358/ && rmdir android-ndk-r28c
```

Verified: `~/android-sdk/ndk/28.2.13676358/source.properties` contains
`Pkg.Revision = 28.2.13676358` / `Pkg.ReleaseName = r28c`; `toolchains/llvm/prebuilt/linux-x86_64/bin/clang`
and `llvm-readelf` are present. (zitanioi pins no NDK; point its build at this via `ANDROID_NDK_HOME`.)

### r29-beta3 (29.0.13846066) — reinstalled and VERIFIED (2026-09-29, 22:12 UTC)

The earlier partial/truncated extraction was superseded: the full 748M
`android-ndk-r29-beta3-linux.zip` was re-downloaded from
`https://dl.google.com/android/repository/` and installed fresh with (ran
2026-09-29, exit 0):

```bash
cd ~/workspace/.sdktmp && export S=~/android-sdk B=https://dl.google.com/android/repository
set -e
get() { # url outfile destdir
  echo "== downloading $2"; curl -fL --retry 2 -o "$2" "$B/$1" || { echo "FAILED $2"; exit 1; }
  mkdir -p "$3" && unzip -q -o "$2" -d "$3" && rm -f "$2" && echo "== installed $2 -> $3"
}
get platform-30_r03.zip p30.zip $S/platforms/android-30
get platform-33-ext5_r01.zip p33.zip $S/platforms/android-33
get platform-34-ext12_r01.zip p34.zip $S/platforms/android-34
get platform-36-ext19_r01.zip p36.zip $S/platforms/android-36
get build-tools_r28.0.2-linux.zip bt28.zip $S/build-tools/28.0.2
get android-ndk-r29-beta3-linux.zip ndk29b3.zip $S/ndk/29.0.13846066
echo "SDK PACKAGES DONE"; ls $S/platforms $S/build-tools $S/ndk
```

(The same script also installed platform-30/33/34/36 and build-tools 28.0.2 —
those are recorded below/under "Build environment".)

Verified: `~/android-sdk/ndk/29.0.13846066/source.properties` contains
`Pkg.Revision = 29.0.13846066-beta3` / `Pkg.ReleaseName = r29-beta3`;
`toolchains/llvm/prebuilt/linux-x86_64/bin/clang` and `llvm-readelf` are
present; install tree is 2.4G. **No NDK reinstall is needed on this machine
before building alex5402** — it pins `ndkVersion = "29.0.13846066"` in
`Bcore/build.gradle` and this exact revision is now present and verified.

## Build results

Status 2026-09-29 UTC: **builds are BLOCKED by the sandbox, but narrowly — not "Gradle cannot
run".** Wrapper `--version` operations run in the client JVM and succeed (both engines, exit 0
above). Any **real build task** fails: even with `--no-daemon`, Gradle forks a single-use daemon
JVM that talks to the client over loopback TCP, and this sandbox intercepts loopback, corrupting
the daemon protocol. No AARs were produced, so no 16KB ELF alignment verification was possible.

### zitanioi — `./gradlew --no-daemon :Bcore:assembleRelease` (FAILED before compilation)

Command (with `GRADLE_USER_HOME=~/workspace/.gradle`, `JAVA_HOME=~/jdk/jdk-11.0.32.1+1`):

```bash
./gradlew --no-daemon -Dorg.gradle.java.home="$JAVA_HOME" :Bcore:assembleRelease
```

Output:
```text
To honour the JVM settings for this build a new JVM will be forked. Please consider using the daemon: https://docs.gradle.org/6.7.1/userguide/gradle_daemon.html.

FAILURE: Build failed with an exception.

* What went wrong:
Could not receive a message from the daemon.
```

`--stacktrace` root cause (captured for zitanioi only):
```text
Caused by: org.gradle.internal.remote.internal.MessageIOException: Could not read message from '/127.0.0.1:42749'.
Caused by: java.lang.IllegalArgumentException: Unexpected type tag 109 found.
    at org.gradle.internal.serialize.DefaultSerializerRegistry$TaggedTypeSerializer.read(DefaultSerializerRegistry.java:145)
```

### alex5402 — `./gradlew --no-daemon help` (FAILED before compilation)

```text
To honour the JVM settings for this build a single-use Daemon process will be forked. For more on this, please refer to https://docs.gradle.org/8.14.5/userguide/gradle_daemon.html#sec:disabling_the_daemon in the Gradle documentation.

FAILURE: Build failed with an exception.

* What went wrong:
Could not receive a message from the daemon.
```

Note on evidence: the `Unexpected type tag 109` stack trace was captured **only for zitanioi**.
Alex5402 shows the same top-level `Could not receive a message from the daemon.` failure, but no
stack trace was captured for it, so the tag-109 root cause is established for zitanioi and only
inferred (same failure signature, same sandbox loopback interception) for alex5402.

### Workarounds attempted (all failed with the same daemon-receive error)

- `-Dorg.gradle.java.home=<matching JDK>` — still forks; the fork is triggered by the property
  being set at all, not by a mismatch.
- Removing `org.gradle.java.home` from a build copy (`~/workspace/.buildtmp/zit-build/`, upstream
  untouched, copy deleted afterwards) — still forks. In Gradle 6.7.1/8.14.5, `--no-daemon` still
  forks a single-use daemon JVM; there is no "run in client JVM" mode for real builds.
- `GRADLE_OPTS="-Xmx2048m"` to match `org.gradle.jvmargs` — still forks.
- Running `org.gradle.launcher.GradleMain` directly — still forks (fork logic is inside the
  launcher, not just the wrapper `EntryPoint`).

### Prerequisite checklist (state 2026-09-29 UTC)

- [x] `~/android-sdk/platforms/android-{28,30,33,34,35,36}/android.jar`
- [x] `~/android-sdk/build-tools/{28.0.2,35.0.0}` present
- [x] `~/android-sdk/ndk/28.2.13676358/source.properties` — verified r28c install (see above)
- [x] `~/android-sdk/ndk/29.0.13846066/source.properties` — verified r29-beta3 install (see above; `Pkg.ReleaseName = r29-beta3`, clang + llvm-readelf present)
- [x] JDK 11 (`~/jdk/jdk-11.0.32.1+1`), JDK 17 (`~/jdk/jdk-17.0.20.1+1`), JDK 21 (`~/jdk/jdk-21`)
- [x] Gradle 6.7.1 dist seeded; `./gradlew --no-daemon --version` exit 0
- [x] Gradle 8.14.5 dist seeded; `./gradlew --no-daemon --version` exit 0

## 16KB page-alignment check

Method (not performed — no AARs were produced): extract every `.so` from the built AAR and run
the NDK's `llvm-readelf -l` on each; every `LOAD` segment's alignment must be `0x4000`.

Expected AAR paths if builds later succeed on a normal machine:
- `zitanioi-blackbox/Bcore/build/outputs/aar/Bcore-release.aar`
- `alex5402-newblackbox/Bcore/build/outputs/aar/Bcore-release.aar`

## minSdk / targetSdk analysis (host app must be minSdk 33)

- **Both engines recommend targetSdk ≤ 28 for the host app.** alex5402 sets `targetSdkVersion = 28` in the root `build.gradle`; zitanioi's README says "如果条件允许，降级targetSdkVersion到28或以下可以获得更好的兼容性" ("downgrading targetSdkVersion to 28 or below gives better compatibility").
- Why they want ≤ 28: hidden-API enforcement. With `targetSdkVersion ≤ 28`, dark greylist access is still permitted and the engines' hidden-API exemptions (FreeReflection / `setHiddenApiExemptions`) face the weakest enforcement. The engines reach deep into hidden APIs (`ActivityThread.mH`, `IActivityManager` stubs, `PackageParser`, ART internals) via the `black.*` reflection shims.
- **Our host must be minSdk 33 ⇒ targetSdk ≥ 33 (Play requires targetSdk 35+ for updates).** Consequences:
  1. Full hidden-API blacklist enforcement applies. Both engines ship exemption mechanisms (zitanioi: FreeReflection 3.0.1 `Reflection.unseal()`; alex5402: FreeReflection 3.2.2 **plus** a native `setHiddenApiExemptions` call via libart symbol lookup), but these techniques are progressively neutered on Android 12→16 — on API 33–36 there is no guarantee they still work, and failure mode is `NoSuchMethodError`/reflection crashes at guest launch.
  2. The **library's** `targetSdkVersion` (30 / 28) is only manifest metadata — the **host app's** targetSdk is what the platform enforces. But the library's `compileSdkVersion` (28 for zitanioi `:Bcore`!) matters: it was compiled against android-28 APIs, so any API-29+ framework behavior change the engine doesn't handle via reflection is a latent bug on API 33–36 (e.g. the Android 14+ AttributionSource UID check — zitanioi patched around it in `ed61551`, alex5402 in `AttributionSourceUtils`).
  3. Expect to run the PoC host at `targetSdk 33` minimum and verify the exemption path on a real API 35/36 device early — this is the highest build/integration risk after the launch path.

## NDK notes

- 16KB page alignment needs NDK **r28+** (default) — alex5402 additionally forces `-z,max-page-size=16384` in `Android.mk` and pins `ndkVersion 29.0.13846066` (**r29-beta3**, not stable r29 — the exact revision is now
installed and verified on this machine: `~/android-sdk/ndk/29.0.13846066`).
- zitanioi's `CMakeLists.txt` sets no page-size flags and pins no NDK; the verified r28c install above (`ANDROID_NDK_HOME=~/android-sdk/ndk/28.2.13676358`) covers it — with r28+ the default 16KB alignment applies.
