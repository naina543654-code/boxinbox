package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRLoadedApkServiceDispatcherInnerConnection {
  public static LoadedApkServiceDispatcherInnerConnectionStatic getWithException() {
    return BlackReflection.create(LoadedApkServiceDispatcherInnerConnectionStatic.class, null, true);
  }

  public static LoadedApkServiceDispatcherInnerConnectionStatic get() {
    return BlackReflection.create(LoadedApkServiceDispatcherInnerConnectionStatic.class, null, false);
  }

  public static LoadedApkServiceDispatcherInnerConnectionContext getWithException(
      final Object caller) {
    return BlackReflection.create(LoadedApkServiceDispatcherInnerConnectionContext.class, caller, true);
  }

  public static LoadedApkServiceDispatcherInnerConnectionContext get(final Object caller) {
    return BlackReflection.create(LoadedApkServiceDispatcherInnerConnectionContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(LoadedApkServiceDispatcherInnerConnectionContext.class);
  }
}
