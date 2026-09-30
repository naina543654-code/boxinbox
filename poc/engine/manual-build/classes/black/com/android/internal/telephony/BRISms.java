package black.com.android.internal.telephony;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRISms {
  public static ISmsStatic getWithException() {
    return BlackReflection.create(ISmsStatic.class, null, true);
  }

  public static ISmsStatic get() {
    return BlackReflection.create(ISmsStatic.class, null, false);
  }

  public static ISmsContext getWithException(final Object caller) {
    return BlackReflection.create(ISmsContext.class, caller, true);
  }

  public static ISmsContext get(final Object caller) {
    return BlackReflection.create(ISmsContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ISmsContext.class);
  }
}
