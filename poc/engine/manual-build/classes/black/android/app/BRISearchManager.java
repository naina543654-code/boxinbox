package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRISearchManager {
  public static ISearchManagerStatic getWithException() {
    return BlackReflection.create(ISearchManagerStatic.class, null, true);
  }

  public static ISearchManagerStatic get() {
    return BlackReflection.create(ISearchManagerStatic.class, null, false);
  }

  public static ISearchManagerContext getWithException(final Object caller) {
    return BlackReflection.create(ISearchManagerContext.class, caller, true);
  }

  public static ISearchManagerContext get(final Object caller) {
    return BlackReflection.create(ISearchManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ISearchManagerContext.class);
  }
}
