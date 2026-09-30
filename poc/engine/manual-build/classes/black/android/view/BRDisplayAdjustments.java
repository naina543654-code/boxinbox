package black.android.view;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRDisplayAdjustments {
  public static DisplayAdjustmentsStatic getWithException() {
    return BlackReflection.create(DisplayAdjustmentsStatic.class, null, true);
  }

  public static DisplayAdjustmentsStatic get() {
    return BlackReflection.create(DisplayAdjustmentsStatic.class, null, false);
  }

  public static DisplayAdjustmentsContext getWithException(final Object caller) {
    return BlackReflection.create(DisplayAdjustmentsContext.class, caller, true);
  }

  public static DisplayAdjustmentsContext get(final Object caller) {
    return BlackReflection.create(DisplayAdjustmentsContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(DisplayAdjustmentsContext.class);
  }
}
