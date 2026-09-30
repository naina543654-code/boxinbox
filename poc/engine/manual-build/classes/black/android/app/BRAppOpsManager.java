package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRAppOpsManager {
  public static AppOpsManagerStatic getWithException() {
    return BlackReflection.create(AppOpsManagerStatic.class, null, true);
  }

  public static AppOpsManagerStatic get() {
    return BlackReflection.create(AppOpsManagerStatic.class, null, false);
  }

  public static AppOpsManagerContext getWithException(final Object caller) {
    return BlackReflection.create(AppOpsManagerContext.class, caller, true);
  }

  public static AppOpsManagerContext get(final Object caller) {
    return BlackReflection.create(AppOpsManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(AppOpsManagerContext.class);
  }
}
