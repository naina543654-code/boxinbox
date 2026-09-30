package black.android.content.pm;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRLauncherApps {
  public static LauncherAppsStatic getWithException() {
    return BlackReflection.create(LauncherAppsStatic.class, null, true);
  }

  public static LauncherAppsStatic get() {
    return BlackReflection.create(LauncherAppsStatic.class, null, false);
  }

  public static LauncherAppsContext getWithException(final Object caller) {
    return BlackReflection.create(LauncherAppsContext.class, caller, true);
  }

  public static LauncherAppsContext get(final Object caller) {
    return BlackReflection.create(LauncherAppsContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(LauncherAppsContext.class);
  }
}
