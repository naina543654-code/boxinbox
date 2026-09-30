package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRActivityThreadCreateServiceData {
  public static ActivityThreadCreateServiceDataStatic getWithException() {
    return BlackReflection.create(ActivityThreadCreateServiceDataStatic.class, null, true);
  }

  public static ActivityThreadCreateServiceDataStatic get() {
    return BlackReflection.create(ActivityThreadCreateServiceDataStatic.class, null, false);
  }

  public static ActivityThreadCreateServiceDataContext getWithException(final Object caller) {
    return BlackReflection.create(ActivityThreadCreateServiceDataContext.class, caller, true);
  }

  public static ActivityThreadCreateServiceDataContext get(final Object caller) {
    return BlackReflection.create(ActivityThreadCreateServiceDataContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ActivityThreadCreateServiceDataContext.class);
  }
}
