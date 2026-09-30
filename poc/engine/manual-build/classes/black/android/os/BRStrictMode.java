package black.android.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRStrictMode {
  public static StrictModeStatic getWithException() {
    return BlackReflection.create(StrictModeStatic.class, null, true);
  }

  public static StrictModeStatic get() {
    return BlackReflection.create(StrictModeStatic.class, null, false);
  }

  public static StrictModeContext getWithException(final Object caller) {
    return BlackReflection.create(StrictModeContext.class, caller, true);
  }

  public static StrictModeContext get(final Object caller) {
    return BlackReflection.create(StrictModeContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(StrictModeContext.class);
  }
}
