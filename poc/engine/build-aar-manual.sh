#!/bin/bash
# Manual AAR build for zitanioi/blackbox Bcore (no Gradle: this sandbox blocks
# the loopback TCP the Gradle daemon needs — see poc/engine-eval/BUILD_NOTES.md).
#
# Engine:  zitanioi/blackbox @ c994edf21fdecbf22196f9e2b0d447af97140f2e
# Output:  poc/engine/Bcore-release.aar
# Stages:  deps | aidl | res | javac | native | aar | all   (default: all)
#
# Replicates what `:Bcore:assembleRelease` would produce:
#   AndroidManifest.xml + classes.jar + R.txt + res/ + jni/<abi>/*.so
set -e
set -o pipefail

STAGE="${1:-all}"

POC=/home/hatch/workspace/goals/privacy-sandbox-android-build/poc
ENGINE=$POC/engines/zitanioi-blackbox
OUT=$POC/engine
WORK=$OUT/manual-build
DEPS=$WORK/deps

SDK=/home/hatch/android-sdk
BT=$SDK/build-tools/35.0.0
A28=$SDK/platforms/android-28/android.jar
A30=$SDK/platforms/android-30/android.jar
NDK=$SDK/ndk/28.2.13676358
JH=/home/hatch/jdk/jdk-17.0.20.1+1
export PATH=$JH/bin:/usr/bin:/bin
export JAVA_HOME=$JH

AAR=$OUT/Bcore-release.aar

mkdir -p "$WORK" "$DEPS" "$OUT"

s_deps() {  # download the external jars the engine actually needs
  mkdir -p "$DEPS"; cd "$DEPS"
  # BlackReflection core (JitPack) — the top.niunaijun.blackreflection.* shim, ~500 imports
  [ -f core-1.1.2.jar ] || curl -fL --retry 3 -o core-1.1.2.jar \
    "https://jitpack.io/com/github/CodingGay/BlackReflection/core/1.1.2/core-1.1.2.jar"
  # BlackReflection annotation processor (GENERATES the black.* impl classes — required)
  [ -f compiler-1.1.2.jar ] || curl -fL --retry 3 -o compiler-1.1.2.jar \
    "https://jitpack.io/com/github/CodingGay/BlackReflection/compiler/1.1.2/compiler-1.1.2.jar"
  # javapoet — required on the annotation-processor path (NoClassDefFoundError otherwise)
  [ -f javapoet-1.13.0.jar ] || curl -fL --retry 3 -o javapoet-1.13.0.jar \
    "https://repo1.maven.org/maven2/com/squareup/javapoet/1.13.0/javapoet-1.13.0.jar"
  # free_reflection — NOTE: zitanioi declares 'me.weishu:free_reflection:3.0.1' but that
  # coordinate resolves NOWHERE (dead Bintray-era group; 404 on Central, absent from
  # Solr). Substituted with the official JitPack artifact, which ships the identical
  # me.weishu.reflection.Reflection.unseal(Context) API (verified via javap).
  [ -f freereflection-3.2.2.aar ] || curl -fL --retry 3 -o freereflection-3.2.2.aar \
    "https://jitpack.io/com/github/tiann/FreeReflection/3.2.2/FreeReflection-3.2.2.aar"
  [ -f freereflection-classes.jar ] || \
    unzip -p freereflection-3.2.2.aar classes.jar > freereflection-classes.jar
  # androidx.annotation (NonNull/Nullable used by libxposed API) + appcompat + androidx.core
  [ -f annotation-1.1.0.jar ] || curl -fL --retry 3 -o annotation-1.1.0.jar \
    "https://dl.google.com/dl/android/maven2/androidx/annotation/annotation/1.1.0/annotation-1.1.0.jar"
  [ -f appcompat-1.2.0.aar ] || curl -fL --retry 3 -o appcompat-1.2.0.aar \
    "https://dl.google.com/dl/android/maven2/androidx/appcompat/appcompat/1.2.0/appcompat-1.2.0.aar"
  [ -f core-1.3.0.aar ] || curl -fL --retry 3 -o core-1.3.0.aar \
    "https://dl.google.com/dl/android/maven2/androidx/core/core/1.3.0/core-1.3.0.aar"
  [ -f androidx-core-classes.jar ] || \
    unzip -p core-1.3.0.aar classes.jar > androidx-core-classes.jar
  ls -la "$DEPS"
  echo "DEPS_OK"
}

s_aidl() {  # android-mirror + Bcore .aidl -> java (AGP does this automatically)
  rm -rf "$WORK/aidl-gen" && mkdir -p "$WORK/aidl-gen"
  MIRROR_AIDL=$ENGINE/android-mirror/src/main/aidl
  BCORE_AIDL=$ENGINE/Bcore/src/main/aidl
  PRELUDE=$OUT/manual-build/aidl-prelude   # stub parcelables for framework types
  OVERLAY=$OUT/manual-build/aidl-overlay   # see note below
  # NOTE (upstream quirks, documented): Bcore/src/main/aidl/.../entity/location/BLocationConfig.aidl
  # declares package top.niunaijun.blackbox.core.system.location but sits in the wrong directory;
  # the aidl tool enforces package/path agreement, so a byte-identical copy is compiled from the
  # correct relative path. IBActivityThread.aidl omits 'import android.content.pm.ProviderInfo;'
  # while using the bare type; the overlay copy adds that one import. Upstream source is NOT modified.
  { find "$MIRROR_AIDL" -name "*.aidl"; find "$BCORE_AIDL" -name "*.aidl" ! -name "BLocationConfig.aidl" ! -name "IBActivityThread.aidl"; \
    find "$OVERLAY" -name "*.aidl"; } > "$WORK/aidl.list"
  wc -l "$WORK/aidl.list"
  fails=0
  while read -r f; do
    "$BT/aidl" --lang=java -I"$MIRROR_AIDL" -I"$BCORE_AIDL" -I"$OVERLAY" -I"$PRELUDE" -o "$WORK/aidl-gen" "$f" \
      || { echo "AIDL_FAIL: $f"; fails=1; }
  done < "$WORK/aidl.list"
  [ "$fails" = 0 ] || return 1
  echo "AIDL_OK: $(find "$WORK/aidl-gen" -name '*.java' | wc -l) java files"
}

s_res() {  # aapt2 compile engine res (+appcompat, needed by LauncherTheme) -> R.java
  rm -rf "$WORK/res-flat" "$WORK/gen-r" "$WORK/R.txt" "$WORK/appcompat" "$WORK/flat"
  mkdir -p "$WORK/res-flat" "$WORK/gen-r" "$WORK/appcompat" "$WORK/flat"
  # appcompat (only external res dependency: styles.xml -> Theme.AppCompat.Light.NoActionBar)
  ( cd "$WORK/appcompat" && unzip -q -o "$DEPS/appcompat-1.2.0.aar" "res/*" )
  # WORKAROUND (documented): appcompat 1.2.0's color/abc_tint_*.xml reference app:alpha
  # but the AAR ships no such attr (verified absent from res/values.xml and R.txt shows
  # it as 0x0). Declared here with framework float semantics so aapt2 can link.
  FIXATTR=$OUT/manual-build/attr-workaround
  "$BT/aapt2" compile --dir "$WORK/appcompat/res" -o "$WORK/appcompat.zip"
  "$BT/aapt2" compile --dir "$ENGINE/Bcore/src/main/res" -o "$WORK/bcore-res.zip"
  "$BT/aapt2" compile --dir "$FIXATTR" -o "$WORK/fixattr.zip"
  ( cd "$WORK/flat" && unzip -q -o ../appcompat.zip && unzip -q -o ../bcore-res.zip && unzip -q -o ../fixattr.zip )
  "$BT/aapt2" link -o "$WORK/res-link.apk" \
    -I "$A28" \
    --manifest "$ENGINE/Bcore/src/main/AndroidManifest.xml" \
    --java "$WORK/gen-r" \
    --output-text-symbols "$WORK/R.txt" \
    --min-sdk-version 21 --target-sdk-version 30 \
    $(find "$WORK/flat" -name "*.flat")
  # NOTE: R.java is intentionally NOT compiled into classes.jar. A standard AGP-built
  # AAR never ships R classes — the consuming app build regenerates them from R.txt +
  # res/. Shipping them caused Gradle's AarToClassTransform to fail with
  # "already contains entry 'top/niunaijun/blackbox/R$anim.class', cannot overwrite".
  # (Verified: no switch(R) or annotation usage of R in the engine sources.)
  echo "RES_OK: $(find "$WORK/gen-r" -name 'R.java')"
}

s_javac() {  # compile all engine java (1.8) -> engine-classes.jar
  rm -rf "$WORK/classes" "$WORK/engine-classes.jar" && mkdir -p "$WORK/classes"
  find "$ENGINE/Bcore/src/main/java" \
       "$ENGINE/Bcore/black-fake/src/main/java" \
       "$ENGINE/Bcore/black-hook/src/main/java" \
       "$ENGINE/Bcore/pine-core/src/main/java" \
       "$ENGINE/Bcore/pine-xposed/src/main/java" \
       "$ENGINE/Bcore/pine-xposed/src/main/apacheCommonsLang" \
       "$ENGINE/android-mirror/src/main/java" \
       "$WORK/aidl-gen" "$WORK/gen-r" \
       -name "*.java" > "$WORK/sources.list"
  wc -l "$WORK/sources.list"
  CP="$A30:$DEPS/core-1.1.2.jar:$DEPS/freereflection-classes.jar:$DEPS/annotation-1.1.0.jar:$DEPS/androidx-core-classes.jar"
  PROC="$DEPS/compiler-1.1.2.jar:$DEPS/javapoet-1.13.0.jar:$DEPS/core-1.1.2.jar"
  "$JH/bin/javac" -nowarn -encoding UTF-8 -source 8 -target 8 \
    -cp "$CP" \
    -processorpath "$PROC" \
    -d "$WORK/classes" @"$WORK/sources.list" 2>"$WORK/javac.log" || {
      echo "javac FAILED (no silent fallback — black.* impls require the processor)"; tail -25 "$WORK/javac.log"; return 1;
    }
  # Strip the aapt2-generated R classes: a standard AGP-built AAR never ships them —
  # the consuming app build regenerates top.niunaijun.blackbox.R from R.txt + res/.
  # (LauncherActivity references R at compile time, hence gen-r stays on the sources.)
  # Shipping them broke Gradle's AarToClassTransform:
  #   "already contains entry 'top/niunaijun/blackbox/R$anim.class', cannot overwrite"
  find "$WORK/classes/top/niunaijun/blackbox" -maxdepth 1 -name 'R*.class' -delete
  cd "$WORK/classes" && find . -name "*.class" > "$WORK/classes.list"
  "$JH/bin/jar" cf "$WORK/engine-classes.jar" @"$WORK/classes.list"
  cd - > /dev/null
  echo "JAVAC_OK"
}

s_native() {  # cmake + make per ABI with NDK r28c (16KB pages by default)
  TC=$NDK/build/cmake/android.toolchain.cmake
  build_one() { # $1=srcDir $2=target $3=abi $4=stl $5=extraLinkFlags
    local src="$1" target="$2" abi="$3" stl="$4" extra="${5:-}"
    local bdir="$WORK/native-$target-$abi"
    rm -rf "$bdir" && mkdir -p "$bdir"
    # WORKAROUND (documented): pine's scoped_local_ref.h uses std::nullptr_t without including
    # <cstddef>; older NDKs provided it transitively via jni.h, NDK r28c does not. Forced include
    # keeps upstream sources untouched.
    # WORKAROUND (documented): pine's hand-written thumb2.S does PC-relative ldr against .global
    # data slots; NDK r28c's lld rejects the resulting TEXTRELs in shared libs. -Bsymbolic binds
    # those references locally (they are patched in-place by thumb2.cpp anyway). arm64 unaffected.
    local linkflags=""
    [ "$target" = "pine" ] && [ "$abi" = "armeabi-v7a" ] && linkflags="-Wl,-Bsymbolic"
    cmake -S "$src" -B "$bdir" -G "Unix Makefiles" \
      -DCMAKE_TOOLCHAIN_FILE="$TC" \
      -DANDROID_ABI="$abi" -DANDROID_PLATFORM=android-21 \
      -DANDROID_STL="$stl" -DCMAKE_BUILD_TYPE=Release \
      -DCMAKE_CXX_FLAGS="-include cstddef" \
      ${linkflags:+-DCMAKE_SHARED_LINKER_FLAGS="$linkflags"} > "$bdir/cmake.log" 2>&1
    cmake --build "$bdir" --target "$target" -j"$(nproc)" > "$bdir/build.log" 2>&1
    find "$bdir" -name "lib${target}.so" | head -2
  }
  BCORE_CPP=$ENGINE/Bcore/src/main/cpp
  PINE_CPP=$ENGINE/Bcore/pine-core/src/main/cpp
  for abi in arm64-v8a armeabi-v7a x86; do
    build_one "$BCORE_CPP" blackbox "$abi" c++_shared
  done
  for abi in arm64-v8a armeabi-v7a; do
    build_one "$PINE_CPP" pine "$abi" c++_static
  done
  echo "NATIVE_OK"
  # 16KB page-alignment check on every .so
  for so in $(find "$WORK" -path "*native-*" -name "*.so"); do
    align=$($NDK/toolchains/llvm/prebuilt/linux-x86_64/bin/llvm-readelf -l "$so" \
      | grep -A1 "^  LOAD" | grep -oP "0x4000|0x1000" | sort -u | tr '\n' ' ')
    echo "$(basename $(dirname $so))/$(basename $so): LOAD align [$align]"
  done
}

s_aar() {  # assemble the AAR zip
  rm -rf "$WORK/aar" "$AAR" && mkdir -p "$WORK/aar/jni"
  cp "$ENGINE/Bcore/src/main/AndroidManifest.xml" "$WORK/aar/"
  cp "$WORK/engine-classes.jar" "$WORK/aar/classes.jar"
  cp "$WORK/R.txt" "$WORK/aar/R.txt"
  cp -r "$ENGINE/Bcore/src/main/res" "$WORK/aar/res"
  for abi in arm64-v8a armeabi-v7a x86; do
    mkdir -p "$WORK/aar/jni/$abi"
    so=$(find "$WORK/native-blackbox-$abi" -name "libblackbox.so" | head -1)
    [ -n "$so" ] && cp "$so" "$WORK/aar/jni/$abi/"
    if [ "$abi" != "x86" ]; then
      pine=$(find "$WORK/native-pine-$abi" -name "libpine.so" | head -1)
      [ -n "$pine" ] && cp "$pine" "$WORK/aar/jni/$abi/"
    fi
    # libblackbox.so links against the SHARED libc++ (c++_shared STL). A normal
    # AGP/CMake build auto-packages it; the manual build must do it explicitly,
    # otherwise guest launch dies with UnsatisfiedLinkError: libc++_shared.so not found.
    case "$abi" in
      arm64-v8a)   triple=aarch64-linux-android ;;
      armeabi-v7a) triple=arm-linux-androideabi ;;
      x86)         triple=i686-linux-android ;;
    esac
    cp "$NDK/toolchains/llvm/prebuilt/linux-x86_64/sysroot/usr/lib/$triple/libc++_shared.so" \
       "$WORK/aar/jni/$abi/"
  done
  ( cd "$WORK/aar" && "$JH/bin/jar" cf "$AAR" . )
  echo "AAR_OK: $AAR"
  unzip -l "$AAR" | head -15
  du -h "$AAR"
}

case "$STAGE" in
  deps) s_deps;; aidl) s_aidl;; res) s_res;; javac) s_javac;;
  native) s_native;; aar) s_aar;;
  all) s_deps; s_aidl; s_res; s_javac; s_native; s_aar;;
  *) echo "unknown stage $STAGE"; exit 1;;
esac
