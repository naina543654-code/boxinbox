package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRActivityThreadQ {
  public static ActivityThreadQStatic getWithException() {
    return BlackReflection.create(ActivityThreadQStatic.class, null, true);
  }

  public static ActivityThreadQStatic get() {
    return BlackReflection.create(ActivityThreadQStatic.class, null, false);
  }

  public static ActivityThreadQContext getWithException(final Object caller) {
    return BlackReflection.create(ActivityThreadQContext.class, caller, true);
  }

  public static ActivityThreadQContext get(final Object caller) {
    return BlackReflection.create(ActivityThreadQContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ActivityThreadQContext.class);
  }
}
