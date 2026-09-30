package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRLoadedApkHuaWei {
  public static LoadedApkHuaWeiStatic getWithException() {
    return BlackReflection.create(LoadedApkHuaWeiStatic.class, null, true);
  }

  public static LoadedApkHuaWeiStatic get() {
    return BlackReflection.create(LoadedApkHuaWeiStatic.class, null, false);
  }

  public static LoadedApkHuaWeiContext getWithException(final Object caller) {
    return BlackReflection.create(LoadedApkHuaWeiContext.class, caller, true);
  }

  public static LoadedApkHuaWeiContext get(final Object caller) {
    return BlackReflection.create(LoadedApkHuaWeiContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(LoadedApkHuaWeiContext.class);
  }
}
