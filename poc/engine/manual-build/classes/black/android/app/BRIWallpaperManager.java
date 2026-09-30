package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIWallpaperManager {
  public static IWallpaperManagerStatic getWithException() {
    return BlackReflection.create(IWallpaperManagerStatic.class, null, true);
  }

  public static IWallpaperManagerStatic get() {
    return BlackReflection.create(IWallpaperManagerStatic.class, null, false);
  }

  public static IWallpaperManagerContext getWithException(final Object caller) {
    return BlackReflection.create(IWallpaperManagerContext.class, caller, true);
  }

  public static IWallpaperManagerContext get(final Object caller) {
    return BlackReflection.create(IWallpaperManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IWallpaperManagerContext.class);
  }
}
