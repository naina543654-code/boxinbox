package black.android.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRDropBoxManager {
  public static DropBoxManagerStatic getWithException() {
    return BlackReflection.create(DropBoxManagerStatic.class, null, true);
  }

  public static DropBoxManagerStatic get() {
    return BlackReflection.create(DropBoxManagerStatic.class, null, false);
  }

  public static DropBoxManagerContext getWithException(final Object caller) {
    return BlackReflection.create(DropBoxManagerContext.class, caller, true);
  }

  public static DropBoxManagerContext get(final Object caller) {
    return BlackReflection.create(DropBoxManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(DropBoxManagerContext.class);
  }
}
