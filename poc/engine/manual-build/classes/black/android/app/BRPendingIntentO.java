package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRPendingIntentO {
  public static PendingIntentOStatic getWithException() {
    return BlackReflection.create(PendingIntentOStatic.class, null, true);
  }

  public static PendingIntentOStatic get() {
    return BlackReflection.create(PendingIntentOStatic.class, null, false);
  }

  public static PendingIntentOContext getWithException(final Object caller) {
    return BlackReflection.create(PendingIntentOContext.class, caller, true);
  }

  public static PendingIntentOContext get(final Object caller) {
    return BlackReflection.create(PendingIntentOContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(PendingIntentOContext.class);
  }
}
