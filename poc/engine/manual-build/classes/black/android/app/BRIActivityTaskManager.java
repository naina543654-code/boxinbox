package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIActivityTaskManager {
  public static IActivityTaskManagerStatic getWithException() {
    return BlackReflection.create(IActivityTaskManagerStatic.class, null, true);
  }

  public static IActivityTaskManagerStatic get() {
    return BlackReflection.create(IActivityTaskManagerStatic.class, null, false);
  }

  public static IActivityTaskManagerContext getWithException(final Object caller) {
    return BlackReflection.create(IActivityTaskManagerContext.class, caller, true);
  }

  public static IActivityTaskManagerContext get(final Object caller) {
    return BlackReflection.create(IActivityTaskManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IActivityTaskManagerContext.class);
  }
}
