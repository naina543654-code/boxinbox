package black.android.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIntentFilter {
  public static IntentFilterStatic getWithException() {
    return BlackReflection.create(IntentFilterStatic.class, null, true);
  }

  public static IntentFilterStatic get() {
    return BlackReflection.create(IntentFilterStatic.class, null, false);
  }

  public static IntentFilterContext getWithException(final Object caller) {
    return BlackReflection.create(IntentFilterContext.class, caller, true);
  }

  public static IntentFilterContext get(final Object caller) {
    return BlackReflection.create(IntentFilterContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IntentFilterContext.class);
  }
}
