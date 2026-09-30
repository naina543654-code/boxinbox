#!/bin/bash
# Manual APK build for the runtime-enabled Sandbox PoC (BlackBox engine).
# No Gradle: this sandbox blocks JVM TCP, which the Gradle daemon requires.
#
# The engine is consumed as the prebuilt Bcore-release.aar (built by
# ../engine/build-aar-manual.sh): its classes.jar is dexed in, its
# res/ is linked, its manifest components are merged (merge-manifest.py,
# mirroring AGP), and its jni/ .so files are packaged.
#
# Stages: manifest | link | kotlin | dex | apk | all   (default: all)
# Output: poc/apks/host-runtime-poc-v2.apk (signed + verified)
# Override the output name with: APK_NAME=foo.apk ./build-manual.sh [stage]
set -e
set -o pipefail

STAGE="${1:-all}"
VER_NAME="1.0-poc-runtime"
APK_NAME="${APK_NAME:-host-runtime-poc-v2.apk}"
APP_ID="com.sandboxpoc.hostruntime"

POC=/home/hatch/workspace/goals/privacy-sandbox-android-build/poc
HR=$POC/host-runtime
ENGINE=$POC/engine
WS=$HR/app/src/main
MB=$HR/manual-build
APKS=$POC/apks
AAR=$ENGINE/Bcore-release.aar

TC=/home/hatch/toolchain
SDK=$TC/android-sdk
BT=$SDK/build-tools/35.0.0
AJAR=$SDK/platforms/android-35/android.jar
JH=$TC/jdk-21.0.7+6/bin
KOTLINC=$TC/kotlinc/bin/kotlinc
STDLIB=$TC/kotlinc/lib/kotlin-stdlib.jar
export PATH=$JH:$PATH

# Engine build artifacts (from ../engine/build-aar-manual.sh)
ENG_MB=$ENGINE/manual-build
ENG_JAR=$ENG_MB/engine-classes.jar
ENG_RTXT=$ENG_MB/R.txt
DEPS=$ENG_MB/deps

mkdir -p "$MB" "$APKS"

s_manifest() {  # merge engine manifest components into the host manifest
  python3 "$HR/merge-manifest.py" \
    "$WS/AndroidManifest.xml" \
    "$POC/engines/zitanioi-blackbox/Bcore/src/main/AndroidManifest.xml" \
    "$MB/AndroidManifest-merged.xml" "$APP_ID"
}

s_link() {  # aapt2: engine res + appcompat res -> base.apk + R.java + R.txt
  rm -rf "$MB/base.apk" "$MB/gen" "$MB/flat"
  mkdir -p "$MB/gen" "$MB/flat"
  # resource inputs (same set used for the AAR build)
  ERES_TMP=$MB/engine-res; rm -rf "$ERES_TMP"; mkdir -p "$ERES_TMP"
  ( cd "$ERES_TMP" && unzip -q -o "$AAR" "res/*" )
  AC_TMP=$MB/appcompat-res; rm -rf "$AC_TMP"; mkdir -p "$AC_TMP"
  ( cd "$AC_TMP" && unzip -q -o "$DEPS/appcompat-1.2.0.aar" "res/*" )
  "$BT/aapt2" compile --dir "$ERES_TMP/res" -o "$MB/engine-res.zip"
  "$BT/aapt2" compile --dir "$AC_TMP/res" -o "$MB/appcompat-res.zip"
  "$BT/aapt2" compile --dir "$ENGINE/manual-build/attr-workaround" -o "$MB/fixattr.zip"
  ( cd "$MB/flat" && unzip -q -o ../engine-res.zip && unzip -q -o ../appcompat-res.zip && unzip -q -o ../fixattr.zip )
  "$BT/aapt2" link -o "$MB/base.apk" \
    -I "$AJAR" \
    --manifest "$MB/AndroidManifest-merged.xml" \
    --min-sdk-version 33 --target-sdk-version 35 \
    --version-code 1 --version-name "$VER_NAME" \
    --java "$MB/gen" \
    --output-text-symbols "$MB/R-merged.txt" \
    $(find "$MB/flat" -name "*.flat")
  # engine R.java (for the prebuilt engine classes' non-final R references)
  mkdir -p "$MB/gen-engine/top/niunaijun/blackbox"
  python3 "$HR/gen-engine-r.py" "$MB/R-merged.txt" "$ENG_RTXT" \
    "$MB/gen-engine/top/niunaijun/blackbox/R.java"
  sed -i 's/public static final class/public static class/g; s/public static final int/public static int/g' \
    "$MB/gen-engine/top/niunaijun/blackbox/R.java"
  echo "LINK_OK"
}

s_kotlin() {  # kotlinc host sources + R.java -> classes
  rm -rf "$MB/classes" "$MB/classes.jar"
  mkdir -p "$MB/classes"
  CP="$AJAR:$STDLIB:$ENG_JAR:$DEPS/core-1.1.2.jar:$DEPS/freereflection-classes.jar:$DEPS/annotation-1.1.0.jar:$DEPS/androidx-core-classes.jar"
  # shellcheck disable=SC2046
  "$KOTLINC" $(find "$WS/kotlin" "$MB/gen" -name "*.kt") "$MB/gen-engine/top/niunaijun/blackbox/R.java" \
    -no-stdlib -no-reflect \
    -cp "$CP" \
    -d "$MB/classes" -jvm-target 17 -nowarn
  "$JH/jar" cf "$MB/classes.jar" -C "$MB/classes" .
  echo "KOTLIN_OK"
}

s_dex() {  # d8 everything -> classes.dex (min-api 33)
  rm -rf "$MB/dex" && mkdir -p "$MB/dex"
  "$BT/d8" --min-api 33 --lib "$AJAR" \
    --output "$MB/dex" \
    "$MB/classes.jar" "$ENG_JAR" \
    "$DEPS/core-1.1.2.jar" "$DEPS/freereflection-classes.jar" \
    "$DEPS/annotation-1.1.0.jar" "$DEPS/androidx-core-classes.jar" \
    "$STDLIB"
  ls -lh "$MB/dex"
  echo "DEX_OK"
}

s_apk() {  # assemble + native libs + zipalign + sign + verify
  rm -rf "$MB/apk-stage" "$MB/unsigned.apk" "$MB/aligned.apk"
  mkdir -p "$MB/apk-stage"
  cd "$MB/apk-stage" && unzip -q -o "$MB/base.apk"
  cp "$MB/dex/classes.dex" .
  # native libs from the AAR (device picks its own ABI)
  for abi in arm64-v8a armeabi-v7a x86; do
    if unzip -l "$AAR" "jni/$abi/*.so" >/dev/null 2>&1; then
      mkdir -p "lib/$abi"
      ( cd "lib/$abi" && unzip -q -o "$AAR" "jni/$abi/*.so" && mv "jni/$abi"/*.so . && rm -rf jni )
    fi
  done
  find lib -name "*.so"
  zip -q -r "$MB/unsigned.apk" .
  cd "$MB"
  "$BT/zipalign" -f -p 4 unsigned.apk aligned.apk
  [ -f "$HR/poc-host-runtime-debug.keystore" ] || \
    "$JH/keytool" -genkeypair -keystore "$HR/poc-host-runtime-debug.keystore" \
      -alias poc -keyalg RSA -keysize 2048 -validity 10950 \
      -dname "CN=Sandbox PoC" -storepass android -keypass android
  "$BT/apksigner" sign \
    --ks "$HR/poc-host-runtime-debug.keystore" \
    --ks-pass pass:android --key-pass pass:android \
    --out "$APKS/$APK_NAME" aligned.apk
  "$BT/apksigner" verify --print-certs "$APKS/$APK_NAME" | head -8
  ls -lh "$APKS/$APK_NAME"
  echo "APK_OK"
}

case "$STAGE" in
  manifest) s_manifest ;; link) s_link ;; kotlin) s_kotlin ;; dex) s_dex ;; apk) s_apk ;;
  all) s_manifest; s_link; s_kotlin; s_dex; s_apk ;;
  *) echo "unknown stage: $STAGE"; exit 1 ;;
esac
