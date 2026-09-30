# Native Hook Surface — Privacy Sandbox Engine Eval

Both engines hook at **three layers**. Every entry below is a version-fragility risk:
ART internals shift between Android releases, and inline/GOT hooks break on hardened linkers.

## Layer 0 — How the hooks are installed

| | zitanioi/blackbox (`c994edf`) | alex5402/newblackbox (`89b5983`) |
|---|---|---|
| Core mechanism | `JniHook` — patches `ArtMethod` entry points (`data+entry_point_from_jni_` etc.) via JNI, i.e. **ART method-entry redirection** for framework JNI methods. Version branches for O/P/Q+ in `JniHook.cpp`. | Same `JniHook` ART method-entry mechanism (no O/P/Q guards updated for 15/16; 194 diff lines vs zitanioi). |
| Native lib | `libblackbox.so` (CMake, `Bcore/src/main/cpp/CMakeLists.txt`), loaded via `System.loadLibrary("blackbox")` in `NativeCore` static init. | `libblackbox.so` (ndk-build, `Bcore/src/main/cpp/Android.mk` + `Application.mk`), same load path. Links **prebuilt** `Dobby/arm64-v8a/libdobby.a` + `armeabi-v7a/libdobby.a` (static) and builds `xdl` from source. |
| Hidden-API exemption | Java: `me.weishu:free_reflection:3.0.1` → `Reflection.unseal(context)` (`BlackBoxCore.java:125`). | Native: `hidden_api.cpp` `disable_hidden_api()` — resolves `VMRuntime.setHiddenApiExemptions` symbol in `libart.so` via `elf_util` and invokes it; called from `NativeCore.disableHiddenApi()` ← `BlackBoxCore.java:1647`. |

## Layer 1 — ART/JNI method hooks (via JniHook::HookJniFun)

### zitanioi (`Bcore/src/main/cpp/Hook/`)

| File | Hooked JNI method | Purpose |
|---|---|---|
| `RuntimeHook.cpp` | `java/lang/Runtime.nativeLoad(String, ClassLoader)` ×2 (two overloads) | Intercept `System.loadLibrary` in guests — redirect .so loads into the virtual lib dir; Unity-compat patch hook point |
| `BinderHook.cpp` | `android/os/Binder.getCallingUid()I` | Return **virtual UID** to guests (identity spoofing at binder layer) |
| `UnixFileSystemHook.cpp` | `java/io/UnixFileSystem.canonicalize0(String)`, `canonicalize0(String,Z)`, `getBooleanAttributes0`, `getLastModifiedTime0`, `setPermission0`, `createFileExclusively0`, `list0`, `createDirectory0`, `setLastModifiedTime0`, `setReadOnly0`, `getSpace0` | Path redirection for all `java.io.File` ops → guest storage isolation |
| `VMClassLoaderHook.cpp` | `java/lang/ClassLoader.findLoadedClass(ClassLoader,String)` | Classloader namespace control for guest code |
| `NativeIOHook.cpp` | **PLT/GOT rewriting** (not JniHook): 30-entry table — `open`, `openat`, `__open_2`, `__openat_2`, `fopen`, `mkdir`, `mkdirat`, `access`, `faccessat`, `faccessat2`, `fstatat`, `fstatat64`, `unlink`, `unlinkat`, `rename`, `renameat`, `rmdir`, `remove`, `chmod`, `truncate`, `opendir`, `stat`, `lstat`, `stat64`, `fstat`, `fstat64`, `open64`, `openat64`, `dlopen`, `android_dlopen_ext` in libc — rewrites GOT entries of every loaded lib under /data (incl. late-loaded ones); `__loader_dlopen`/`__loader_android_dlopen_ext` resolved via libdl ELF scan (BIND_NOW) | Native-level path redirection + dlopen interception for guest .so |
| `UnityCompatPatch.cpp` | (not a hook — file patch) patches `libunity.so` relocation addends so Unity's `/data/data` prefix self-check can't match | Unity 2021.3/2022.3 china_unity games exiting at splash inside the container |
| `LibXposedNative.cpp` + `pine-core`/`pine-xposed` | **Pine ART inline-hook engine**: `trampoline_installer.h` injects jump trampolines into compiled ART methods (mprotect + code patch); `art_method.cpp`, `jit.cpp`, `thread.cpp` manipulate ART internals | Xposed module support inside guests (LSPosed API 102 native modules); per-app libc-hook toggle |

### alex5402 (`Bcore/src/main/cpp/Hook/`, `Utils/`)

| File | Hooked target | Purpose | Status |
|---|---|---|---|
| `RuntimeHook.cpp` | `Runtime.nativeLoad` ×2 | Same .so-load interception | active |
| `BinderHook.cpp` | `Binder.getCallingUid()I` | Virtual UID | active |
| `UnixFileSystemHook.cpp` | `canonicalize0`×2, `getLastModifiedTime0`, `setPermission0`, `createFileExclusively0`, `list0`, `createDirectory0`, `setLastModifiedTime0`, `setReadOnly0`, `getSpace0` (9; lacks `getBooleanAttributes0`) | Path redirection | active |
| `VMClassLoaderHook.cpp` | `ClassLoader.findLoadedClass` | Classloader control | active |
| `DexFileHook.cpp` | `dalvik/system/DexFile.openDexFileNative` (**only when API ≥ 34**, `__ANDROID_API_U__`) | Dex open interception on Android 14+ | active |
| `Utils/VirtualSpoof.cpp` | **Dobby inline hook** on libc `__system_property_get` (resolved via `xdl_dsym`); `__attribute__((constructor))` auto-installs at `dlopen` | Spoof `ro.product.*`, `ro.build.fingerprint`, `ro.serialno`, … with hardcoded Pixel 6/Android 12 values | active, **process-wide** |
| `Utils/AntiDetection.cpp` | `access/stat/lstat/fopen/open/readlink/opendir` blockers for su/Magisk/emulator/Xposed/virtual-app paths | Anti-detection | **DEAD** — `install_file_hooks()` never installs |
| `Hook/FileSystemHook.cpp` | `open/open64` (`resource-cache`, `@idmap`, `.frro`, `systemui` filter) | Resource-cache path filtering | **DEAD** — `init()` only resolves symbols |
| `hidden_api.cpp` | libart `setHiddenApiExemptions` symbol | Hidden-API exemption | active (called via JNI) |
| `Utils/elf_util.cpp`, `xdl/*` | ELF parsing / symbol lookup helpers (not hooks themselves) | Support for the above | — |

**Notable asymmetry:** zitanioi has the broad `NativeIOHook` GOT-rewriting layer and the Pine Xposed engine; alex5402 has the Dobby `__system_property_get` spoof, `DexFileHook` (API 34+), and the native hidden-API exemption — but two of alex5402's file-hook files are dead code.

## Layer 2 — Java binder-proxy hooks (for completeness)

Both engines proxy ~40 system services via `ClassInvocationStub` (dynamic `Proxy` over the binder interface) plus `AppInstrumentation` (ActivityThread `mInstrumentation` swap) and `HCallbackProxy` (ActivityThread `H` handler callback). Full injector lists are in `fake/hook/HookManager.java` in each repo. The binder proxies are version-sensitive wherever they touch AIDL signatures or hidden fields (see `black/android/...` reflection shims in `android-mirror` (zitanioi) / `Bcore/src/main/java/black|android` (alex5402)).

## 16KB page-alignment

- **alex5402**: explicitly requests it — `Android.mk`: `LOCAL_LDFLAGS += -Wl,--gc-sections,--strip-all,-z,max-page-size=16384`; `Application.mk`: `APP_SUPPORT_FLEXIBLE_PAGE_SIZES := true`; pinned `ndkVersion = "29.0.13846066"` (r29 ≥ r28 ⇒ 16KB-aligned by default anyway). The **prebuilt `libdobby.a`** is linked statically — its object alignment is whatever the dobby build used; needs `readelf` verification on the final `.so` (see BUILD_NOTES.md).
- **zitanioi**: `CMakeLists.txt` sets no page-size flags; relies on the NDK default. NDK **r28+** defaults to 16KB alignment, so building with r28 satisfies the requirement — but this must be verified with `readelf -l` on the produced `libblackbox.so` (see BUILD_NOTES.md), since the coordinator's NDK choice decides it.

## Fragility ranking (highest risk first)

1. `JniHook` ArtMethod entry-point patching — ART struct layouts change per release; zitanioi added Android 15/16 guards (`ed61551`), alex5402 did not.
2. zitanioi `NativeIOHook` GOT rewriting — breaks on RELRO-hardened / `DT_BIND_NOW` libs; had a SIGSEGV fix already (`99cc750`).
3. Pine trampoline inline hooks (zitanioi) — compiled-code patching, most fragile; but isolated to Xposed-module path.
4. alex5402 Dobby inline hook on `__system_property_get` — inline hooks in libc; plus it's process-global (host included).
5. `hidden_api`/`free_reflection` exemptions — exemption techniques degrade on newer Android (15/16 restrict meta-reflection); both engines depend on them while targeting ≥33.
6. Binder-proxy AIDL signature drift — mitigated by the `black.*` reflection shims, but each new API level needs checking (e.g. `checkPermissionForDevice` on 16 — only zitanioi has it).
