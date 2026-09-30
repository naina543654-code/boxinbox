package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRActivityThreadAppBindData {
  public static ActivityThreadAppBindDataStatic getWithException() {
    return BlackReflection.create(ActivityThreadAppBindDataStatic.class, null, true);
  }

  public static ActivityThreadAppBindDataStatic get() {
    return BlackReflection.create(ActivityThreadAppBindDataStatic.class, null, false);
  }

  public static ActivityThreadAppBindDataContext getWithException(final Object caller) {
    return BlackReflection.create(ActivityThreadAppBindDataContext.class, caller, true);
  }

  public static ActivityThreadAppBindDataContext get(final Object caller) {
    return BlackReflection.create(ActivityThreadAppBindDataContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ActivityThreadAppBindDataContext.class);
  }
}
