package black.android.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIntentSender {
  public static IntentSenderStatic getWithException() {
    return BlackReflection.create(IntentSenderStatic.class, null, true);
  }

  public static IntentSenderStatic get() {
    return BlackReflection.create(IntentSenderStatic.class, null, false);
  }

  public static IntentSenderContext getWithException(final Object caller) {
    return BlackReflection.create(IntentSenderContext.class, caller, true);
  }

  public static IntentSenderContext get(final Object caller) {
    return BlackReflection.create(IntentSenderContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IntentSenderContext.class);
  }
}
