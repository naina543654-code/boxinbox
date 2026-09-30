package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRActivityThread {
  public static ActivityThreadStatic getWithException() {
    return BlackReflection.create(ActivityThreadStatic.class, null, true);
  }

  public static ActivityThreadStatic get() {
    return BlackReflection.create(ActivityThreadStatic.class, null, false);
  }

  public static ActivityThreadContext getWithException(final Object caller) {
    return BlackReflection.create(ActivityThreadContext.class, caller, true);
  }

  public static ActivityThreadContext get(final Object caller) {
    return BlackReflection.create(ActivityThreadContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ActivityThreadContext.class);
  }
}
