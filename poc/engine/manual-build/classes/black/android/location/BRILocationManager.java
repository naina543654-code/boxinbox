package black.android.location;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRILocationManager {
  public static ILocationManagerStatic getWithException() {
    return BlackReflection.create(ILocationManagerStatic.class, null, true);
  }

  public static ILocationManagerStatic get() {
    return BlackReflection.create(ILocationManagerStatic.class, null, false);
  }

  public static ILocationManagerContext getWithException(final Object caller) {
    return BlackReflection.create(ILocationManagerContext.class, caller, true);
  }

  public static ILocationManagerContext get(final Object caller) {
    return BlackReflection.create(ILocationManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ILocationManagerContext.class);
  }
}
