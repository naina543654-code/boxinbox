package black.android.media.session;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRISessionManager {
  public static ISessionManagerStatic getWithException() {
    return BlackReflection.create(ISessionManagerStatic.class, null, true);
  }

  public static ISessionManagerStatic get() {
    return BlackReflection.create(ISessionManagerStatic.class, null, false);
  }

  public static ISessionManagerContext getWithException(final Object caller) {
    return BlackReflection.create(ISessionManagerContext.class, caller, true);
  }

  public static ISessionManagerContext get(final Object caller) {
    return BlackReflection.create(ISessionManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ISessionManagerContext.class);
  }
}
