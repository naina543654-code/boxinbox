package black.android.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIPowerManager {
  public static IPowerManagerStatic getWithException() {
    return BlackReflection.create(IPowerManagerStatic.class, null, true);
  }

  public static IPowerManagerStatic get() {
    return BlackReflection.create(IPowerManagerStatic.class, null, false);
  }

  public static IPowerManagerContext getWithException(final Object caller) {
    return BlackReflection.create(IPowerManagerContext.class, caller, true);
  }

  public static IPowerManagerContext get(final Object caller) {
    return BlackReflection.create(IPowerManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IPowerManagerContext.class);
  }
}
