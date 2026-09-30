package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRWallpaperManager {
  public static WallpaperManagerStatic getWithException() {
    return BlackReflection.create(WallpaperManagerStatic.class, null, true);
  }

  public static WallpaperManagerStatic get() {
    return BlackReflection.create(WallpaperManagerStatic.class, null, false);
  }

  public static WallpaperManagerContext getWithException(final Object caller) {
    return BlackReflection.create(WallpaperManagerContext.class, caller, true);
  }

  public static WallpaperManagerContext get(final Object caller) {
    return BlackReflection.create(WallpaperManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(WallpaperManagerContext.class);
  }
}
