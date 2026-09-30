package black.com.android.internal;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRR {
  public static RStatic getWithException() {
    return BlackReflection.create(RStatic.class, null, true);
  }

  public static RStatic get() {
    return BlackReflection.create(RStatic.class, null, false);
  }

  public static RContext getWithException(final Object caller) {
    return BlackReflection.create(RContext.class, caller, true);
  }

  public static RContext get(final Object caller) {
    return BlackReflection.create(RContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(RContext.class);
  }
}
