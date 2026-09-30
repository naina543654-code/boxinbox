package black.android.view;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRDisplay {
  public static DisplayStatic getWithException() {
    return BlackReflection.create(DisplayStatic.class, null, true);
  }

  public static DisplayStatic get() {
    return BlackReflection.create(DisplayStatic.class, null, false);
  }

  public static DisplayContext getWithException(final Object caller) {
    return BlackReflection.create(DisplayContext.class, caller, true);
  }

  public static DisplayContext get(final Object caller) {
    return BlackReflection.create(DisplayContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(DisplayContext.class);
  }
}
