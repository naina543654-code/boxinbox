package black.android.accounts;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIAccountManager {
  public static IAccountManagerStatic getWithException() {
    return BlackReflection.create(IAccountManagerStatic.class, null, true);
  }

  public static IAccountManagerStatic get() {
    return BlackReflection.create(IAccountManagerStatic.class, null, false);
  }

  public static IAccountManagerContext getWithException(final Object caller) {
    return BlackReflection.create(IAccountManagerContext.class, caller, true);
  }

  public static IAccountManagerContext get(final Object caller) {
    return BlackReflection.create(IAccountManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IAccountManagerContext.class);
  }
}
