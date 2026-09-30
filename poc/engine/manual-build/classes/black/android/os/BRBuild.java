package black.android.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRBuild {
  public static BuildStatic getWithException() {
    return BlackReflection.create(BuildStatic.class, null, true);
  }

  public static BuildStatic get() {
    return BlackReflection.create(BuildStatic.class, null, false);
  }

  public static BuildContext getWithException(final Object caller) {
    return BlackReflection.create(BuildContext.class, caller, true);
  }

  public static BuildContext get(final Object caller) {
    return BlackReflection.create(BuildContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(BuildContext.class);
  }
}
