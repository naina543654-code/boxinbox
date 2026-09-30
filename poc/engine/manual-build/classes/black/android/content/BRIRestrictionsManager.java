package black.android.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIRestrictionsManager {
  public static IRestrictionsManagerStatic getWithException() {
    return BlackReflection.create(IRestrictionsManagerStatic.class, null, true);
  }

  public static IRestrictionsManagerStatic get() {
    return BlackReflection.create(IRestrictionsManagerStatic.class, null, false);
  }

  public static IRestrictionsManagerContext getWithException(final Object caller) {
    return BlackReflection.create(IRestrictionsManagerContext.class, caller, true);
  }

  public static IRestrictionsManagerContext get(final Object caller) {
    return BlackReflection.create(IRestrictionsManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IRestrictionsManagerContext.class);
  }
}
