package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIApplicationThreadOreo {
  public static IApplicationThreadOreoStatic getWithException() {
    return BlackReflection.create(IApplicationThreadOreoStatic.class, null, true);
  }

  public static IApplicationThreadOreoStatic get() {
    return BlackReflection.create(IApplicationThreadOreoStatic.class, null, false);
  }

  public static IApplicationThreadOreoContext getWithException(final Object caller) {
    return BlackReflection.create(IApplicationThreadOreoContext.class, caller, true);
  }

  public static IApplicationThreadOreoContext get(final Object caller) {
    return BlackReflection.create(IApplicationThreadOreoContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IApplicationThreadOreoContext.class);
  }
}
