package black.android.location;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRLocationRequestL {
  public static LocationRequestLStatic getWithException() {
    return BlackReflection.create(LocationRequestLStatic.class, null, true);
  }

  public static LocationRequestLStatic get() {
    return BlackReflection.create(LocationRequestLStatic.class, null, false);
  }

  public static LocationRequestLContext getWithException(final Object caller) {
    return BlackReflection.create(LocationRequestLContext.class, caller, true);
  }

  public static LocationRequestLContext get(final Object caller) {
    return BlackReflection.create(LocationRequestLContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(LocationRequestLContext.class);
  }
}
