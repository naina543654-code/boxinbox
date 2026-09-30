#!/bin/bash
# Manual APK build for the Sandbox PoC host app (no Gradle: this sandbox
# blocks JVM TCP, which the Gradle daemon requires for client<->daemon
# communication — see toolchain/manual_build.sh header).
#
# Stages: link | kotlin | dex | apk | all   (default: all)
# Output: poc/apks/host-poc-v1.apk (signed + verified)
set -e
set -o pipefail

STAGE="${1:-all}"
VER_NAME="1.0-poc"
APK_NAME="host-poc-v1.apk"

HOST=/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/host
WS=$HOST/app/src/main
MB=$HOST/manual-build
APKS=/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/apks
TC=/home/hatch/toolchain
SDK=$TC/android-sdk
BT=$SDK/build-tools/35.0.0
AJAR=$SDK/platforms/android-35/android.jar
JH=$TC/jdk-21.0.7+6/bin
KOTLINC=$TC/kotlinc/bin/kotlinc
STDLIB=$TC/kotlinc/lib/kotlin-stdlib.jar
export PATH=$JH:$PATH

mkdir -p "$MB" "$APKS"

s_link() {  # aapt2 link manifest -> base.apk (+ R.java; no resources in PoC)
  rm -rf "$MB/base.apk" "$MB/gen" "$MB/compiled-res.zip"
  mkdir -p "$MB/gen"
  "$BT/aapt2" link -o "$MB/base.apk" \
    -I "$AJAR" \
    --manifest "$WS/AndroidManifest.xml" \
    --min-sdk-version 33 --target-sdk-version 35 \
    --version-code 1 --version-name "$VER_NAME" \
    --java "$MB/gen"
  echo "LINK_OK"
}

s_kotlin() {  # kotlinc all sources -> classes.jar
  rm -rf "$MB/classes" "$MB/classes.jar"
  mkdir -p "$MB/classes"
  # shellcheck disable=SC2046
  "$KOTLINC" $(find "$WS/kotlin" "$MB/gen" -name "*.kt") \
    -no-stdlib -no-reflect \
    -cp "$AJAR:$STDLIB" \
    -d "$MB/classes" -jvm-target 17 -nowarn
  "$JH/jar" cf "$MB/classes.jar" -C "$MB/classes" .
  echo "KOTLIN_OK"
}

s_dex() {  # d8 -> classes.dex (min-api 33)
  rm -rf "$MB/dex" && mkdir -p "$MB/dex"
  "$BT/d8" --min-api 33 --lib "$AJAR" \
    --output "$MB/dex" "$MB/classes.jar" "$STDLIB"
  ls -lh "$MB/dex"
  echo "DEX_OK"
}

s_apk() {  # assemble + zipalign + sign + verify
  rm -rf "$MB/apk-stage" "$MB/unsigned.apk" "$MB/aligned.apk"
  mkdir -p "$MB/apk-stage"
  cd "$MB/apk-stage" && unzip -q -o "$MB/base.apk"
  cp "$MB/dex/classes.dex" .
  zip -q -r "$MB/unsigned.apk" .
  cd "$MB"
  "$BT/zipalign" -f -p 4 unsigned.apk aligned.apk
  "$BT/apksigner" sign \
    --ks "$HOST/poc-host-debug.keystore" \
    --ks-pass pass:android --key-pass pass:android \
    --out "$APKS/$APK_NAME" aligned.apk
  "$BT/apksigner" verify --print-certs "$APKS/$APK_NAME" | head -8
  ls -lh "$APKS/$APK_NAME"
  echo "APK_OK"
}

case "$STAGE" in
  link) s_link ;; kotlin) s_kotlin ;; dex) s_dex ;; apk) s_apk ;;
  all) s_link; s_kotlin; s_dex; s_apk ;;
  *) echo "unknown stage: $STAGE"; exit 1 ;;
esac
