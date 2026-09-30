package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRActivity {
  public static ActivityStatic getWithException() {
    return BlackReflection.create(ActivityStatic.class, null, true);
  }

  public static ActivityStatic get() {
    return BlackReflection.create(ActivityStatic.class, null, false);
  }

  public static ActivityContext getWithException(final Object caller) {
    return BlackReflection.create(ActivityContext.class, caller, true);
  }

  public static ActivityContext get(final Object caller) {
    return BlackReflection.create(ActivityContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ActivityContext.class);
  }
}
