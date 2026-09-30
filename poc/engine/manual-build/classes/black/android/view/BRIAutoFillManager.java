package black.android.view;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIAutoFillManager {
  public static IAutoFillManagerStatic getWithException() {
    return BlackReflection.create(IAutoFillManagerStatic.class, null, true);
  }

  public static IAutoFillManagerStatic get() {
    return BlackReflection.create(IAutoFillManagerStatic.class, null, false);
  }

  public static IAutoFillManagerContext getWithException(final Object caller) {
    return BlackReflection.create(IAutoFillManagerContext.class, caller, true);
  }

  public static IAutoFillManagerContext get(final Object caller) {
    return BlackReflection.create(IAutoFillManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IAutoFillManagerContext.class);
  }
}
