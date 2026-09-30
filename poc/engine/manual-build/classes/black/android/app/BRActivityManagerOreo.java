package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRActivityManagerOreo {
  public static ActivityManagerOreoStatic getWithException() {
    return BlackReflection.create(ActivityManagerOreoStatic.class, null, true);
  }

  public static ActivityManagerOreoStatic get() {
    return BlackReflection.create(ActivityManagerOreoStatic.class, null, false);
  }

  public static ActivityManagerOreoContext getWithException(final Object caller) {
    return BlackReflection.create(ActivityManagerOreoContext.class, caller, true);
  }

  public static ActivityManagerOreoContext get(final Object caller) {
    return BlackReflection.create(ActivityManagerOreoContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ActivityManagerOreoContext.class);
  }
}
