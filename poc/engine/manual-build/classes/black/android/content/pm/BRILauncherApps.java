package black.android.content.pm;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRILauncherApps {
  public static ILauncherAppsStatic getWithException() {
    return BlackReflection.create(ILauncherAppsStatic.class, null, true);
  }

  public static ILauncherAppsStatic get() {
    return BlackReflection.create(ILauncherAppsStatic.class, null, false);
  }

  public static ILauncherAppsContext getWithException(final Object caller) {
    return BlackReflection.create(ILauncherAppsContext.class, caller, true);
  }

  public static ILauncherAppsContext get(final Object caller) {
    return BlackReflection.create(ILauncherAppsContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ILauncherAppsContext.class);
  }
}
