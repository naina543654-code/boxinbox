package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRWallpaperManagerGlobals {
  public static WallpaperManagerGlobalsStatic getWithException() {
    return BlackReflection.create(WallpaperManagerGlobalsStatic.class, null, true);
  }

  public static WallpaperManagerGlobalsStatic get() {
    return BlackReflection.create(WallpaperManagerGlobalsStatic.class, null, false);
  }

  public static WallpaperManagerGlobalsContext getWithException(final Object caller) {
    return BlackReflection.create(WallpaperManagerGlobalsContext.class, caller, true);
  }

  public static WallpaperManagerGlobalsContext get(final Object caller) {
    return BlackReflection.create(WallpaperManagerGlobalsContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(WallpaperManagerGlobalsContext.class);
  }
}
