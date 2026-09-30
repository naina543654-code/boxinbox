package black.libcore.io;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BROs {
  public static OsStatic getWithException() {
    return BlackReflection.create(OsStatic.class, null, true);
  }

  public static OsStatic get() {
    return BlackReflection.create(OsStatic.class, null, false);
  }

  public static OsContext getWithException(final Object caller) {
    return BlackReflection.create(OsContext.class, caller, true);
  }

  public static OsContext get(final Object caller) {
    return BlackReflection.create(OsContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(OsContext.class);
  }
}
