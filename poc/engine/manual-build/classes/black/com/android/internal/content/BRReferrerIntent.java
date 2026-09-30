package black.com.android.internal.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRReferrerIntent {
  public static ReferrerIntentStatic getWithException() {
    return BlackReflection.create(ReferrerIntentStatic.class, null, true);
  }

  public static ReferrerIntentStatic get() {
    return BlackReflection.create(ReferrerIntentStatic.class, null, false);
  }

  public static ReferrerIntentContext getWithException(final Object caller) {
    return BlackReflection.create(ReferrerIntentContext.class, caller, true);
  }

  public static ReferrerIntentContext get(final Object caller) {
    return BlackReflection.create(ReferrerIntentContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ReferrerIntentContext.class);
  }
}
