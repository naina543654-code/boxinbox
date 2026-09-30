package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRActivityClient {
  public static ActivityClientStatic getWithException() {
    return BlackReflection.create(ActivityClientStatic.class, null, true);
  }

  public static ActivityClientStatic get() {
    return BlackReflection.create(ActivityClientStatic.class, null, false);
  }

  public static ActivityClientContext getWithException(final Object caller) {
    return BlackReflection.create(ActivityClientContext.class, caller, true);
  }

  public static ActivityClientContext get(final Object caller) {
    return BlackReflection.create(ActivityClientContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ActivityClientContext.class);
  }
}
