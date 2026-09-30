package black.libcore.io;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRLibcore {
  public static LibcoreStatic getWithException() {
    return BlackReflection.create(LibcoreStatic.class, null, true);
  }

  public static LibcoreStatic get() {
    return BlackReflection.create(LibcoreStatic.class, null, false);
  }

  public static LibcoreContext getWithException(final Object caller) {
    return BlackReflection.create(LibcoreContext.class, caller, true);
  }

  public static LibcoreContext get(final Object caller) {
    return BlackReflection.create(LibcoreContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(LibcoreContext.class);
  }
}
