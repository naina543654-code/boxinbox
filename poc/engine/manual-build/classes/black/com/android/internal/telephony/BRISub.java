package black.com.android.internal.telephony;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRISub {
  public static ISubStatic getWithException() {
    return BlackReflection.create(ISubStatic.class, null, true);
  }

  public static ISubStatic get() {
    return BlackReflection.create(ISubStatic.class, null, false);
  }

  public static ISubContext getWithException(final Object caller) {
    return BlackReflection.create(ISubContext.class, caller, true);
  }

  public static ISubContext get(final Object caller) {
    return BlackReflection.create(ISubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ISubContext.class);
  }
}
