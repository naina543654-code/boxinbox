package black.android.hardware.display;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRDisplayManagerGlobal {
  public static DisplayManagerGlobalStatic getWithException() {
    return BlackReflection.create(DisplayManagerGlobalStatic.class, null, true);
  }

  public static DisplayManagerGlobalStatic get() {
    return BlackReflection.create(DisplayManagerGlobalStatic.class, null, false);
  }

  public static DisplayManagerGlobalContext getWithException(final Object caller) {
    return BlackReflection.create(DisplayManagerGlobalContext.class, caller, true);
  }

  public static DisplayManagerGlobalContext get(final Object caller) {
    return BlackReflection.create(DisplayManagerGlobalContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(DisplayManagerGlobalContext.class);
  }
}
