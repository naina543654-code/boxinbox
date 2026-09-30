package black.android.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIIntentReceiver {
  public static IIntentReceiverStatic getWithException() {
    return BlackReflection.create(IIntentReceiverStatic.class, null, true);
  }

  public static IIntentReceiverStatic get() {
    return BlackReflection.create(IIntentReceiverStatic.class, null, false);
  }

  public static IIntentReceiverContext getWithException(final Object caller) {
    return BlackReflection.create(IIntentReceiverContext.class, caller, true);
  }

  public static IIntentReceiverContext get(final Object caller) {
    return BlackReflection.create(IIntentReceiverContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IIntentReceiverContext.class);
  }
}
