#!/bin/bash
# Manual (no-Gradle) build of SandboxProbe.
# This is the exact procedure used to produce poc/apks/probe-v1.apk on a
# machine where the Gradle daemon cannot run (loopback TCP is intercepted).
# On a normal machine prefer: ./gradlew assembleRelease (see README.md).
#
# Requirements:
#   ANDROID_HOME set to an SDK with platforms/android-35 and build-tools/35.0.0
#   JAVA_HOME pointing at JDK 17
#   Play-services AARs (downloaded once):
#     com.google.android.gms:play-services-base:18.5.0
#     com.google.android.gms:play-services-basement:18.4.0
#     com.google.android.gms:play-services-tasks:18.2.0
#   Keystore: ../poc-debug.keystore (passwords in ../keystore-info.txt)
#
# Usage: ./build-manual.sh [output-apk]
set -euo pipefail

: "${ANDROID_HOME:?ANDROID_HOME must be set}"
: "${JAVA_HOME:?JAVA_HOME must be set}"

BT="$ANDROID_HOME/build-tools/35.0.0"
PLATFORM="$ANDROID_HOME/platforms/android-35/android.jar"
PROBE="$(cd "$(dirname "$0")" && pwd)"
OUT="${1:-$PROBE/../apks/probe-v1.apk}"
W="$(mktemp -d)"
trap 'rm -rf "$W"' EXIT

# --- 0. play-services classes (extract once, reuse) ---
GMS="$W/gms"
mkdir -p "$GMS"
dl() { # dl <artifact-path> <version> <file>
  local dest="$GMS/$1-$2.aar"
  [ -f "$dest" ] || curl -sSL -o "$dest" \
    "https://dl.google.com/dl/android/maven2/com/google/android/gms/$1/$2/$1-$2.aar"
}
dl play-services-base 18.5.0
dl play-services-basement 18.4.0
dl play-services-tasks 18.2.0
CP="$PLATFORM"
for a in play-services-base-18.5.0 play-services-basement-18.4.0 play-services-tasks-18.2.0; do
  mkdir -p "$GMS/$a" && unzip -q -o "$GMS/$a.aar" -d "$GMS/$a" classes.jar
  CP="$CP:$GMS/$a/classes.jar"
done

# --- 1. aapt2 link (manifest -> base APK) ---
# aapt2 requires the package attribute; AGP injects it from `namespace`,
# so we inject it into a throwaway copy (source manifest stays Gradle-clean).
sed 's|<manifest xmlns:android="http://schemas.android.com/apk/res/android">|<manifest xmlns:android="http://schemas.android.com/apk/res/android"\n    package="com.sandboxpoc.probe">|' \
  "$PROBE/app/src/main/AndroidManifest.xml" > "$W/AndroidManifest.xml"
"$BT/aapt2" link -o "$W/base.apk" -I "$PLATFORM" \
  --manifest "$W/AndroidManifest.xml" \
  --min-sdk-version 33 --target-sdk-version 35 \
  --version-code 1 --version-name 1.0

# --- 2. javac ---
mkdir -p "$W/classes"
"$JAVA_HOME/bin/javac" -encoding UTF-8 -source 17 -target 17 -nowarn \
  -classpath "$CP" -d "$W/classes" \
  $(find "$PROBE/app/src/main/java" -name "*.java")

# --- 3. d8 (dex) ---
mkdir -p "$W/dex"
"$BT/d8" --lib "$PLATFORM" --min-api 33 --output "$W/dex" \
  $(find "$W/classes" -name "*.class") \
  "$GMS/play-services-base-18.5.0/classes.jar" \
  "$GMS/play-services-basement-18.4.0/classes.jar" \
  "$GMS/play-services-tasks-18.2.0/classes.jar"
(cd "$W/dex" && "$JAVA_HOME/bin/jar" uf "$W/base.apk" *.dex)

# --- 4. zipalign ---
"$BT/zipalign" -f 4 "$W/base.apk" "$W/aligned.apk"

# --- 5. sign with the shared PoC key ---
KS="$PROBE/../poc-debug.keystore"
PASSFILE="$W/ksp"
{ grep '^storepass=' "$PROBE/../keystore-info.txt" | cut -d= -f2
  grep '^keypass='   "$PROBE/../keystore-info.txt" | cut -d= -f2; } > "$PASSFILE"
chmod 600 "$PASSFILE"
ALIAS=$(grep '^alias=' "$PROBE/../keystore-info.txt" | cut -d= -f2)
mkdir -p "$(dirname "$OUT")"
"$BT/apksigner" sign --ks "$KS" --ks-pass "file:$PASSFILE" \
  --ks-key-alias "$ALIAS" --out "$OUT" "$W/aligned.apk"

echo "OK: $OUT"
"$BT/apksigner" verify --print-certs "$OUT" | head -4
