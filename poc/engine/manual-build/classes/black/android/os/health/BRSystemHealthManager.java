package black.android.os.health;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRSystemHealthManager {
  public static SystemHealthManagerStatic getWithException() {
    return BlackReflection.create(SystemHealthManagerStatic.class, null, true);
  }

  public static SystemHealthManagerStatic get() {
    return BlackReflection.create(SystemHealthManagerStatic.class, null, false);
  }

  public static SystemHealthManagerContext getWithException(final Object caller) {
    return BlackReflection.create(SystemHealthManagerContext.class, caller, true);
  }

  public static SystemHealthManagerContext get(final Object caller) {
    return BlackReflection.create(SystemHealthManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(SystemHealthManagerContext.class);
  }
}
