package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIActivityManager {
  public static IActivityManagerStatic getWithException() {
    return BlackReflection.create(IActivityManagerStatic.class, null, true);
  }

  public static IActivityManagerStatic get() {
    return BlackReflection.create(IActivityManagerStatic.class, null, false);
  }

  public static IActivityManagerContext getWithException(final Object caller) {
    return BlackReflection.create(IActivityManagerContext.class, caller, true);
  }

  public static IActivityManagerContext get(final Object caller) {
    return BlackReflection.create(IActivityManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IActivityManagerContext.class);
  }
}
