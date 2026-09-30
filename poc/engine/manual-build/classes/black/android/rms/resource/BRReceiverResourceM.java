package black.android.rms.resource;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRReceiverResourceM {
  public static ReceiverResourceMStatic getWithException() {
    return BlackReflection.create(ReceiverResourceMStatic.class, null, true);
  }

  public static ReceiverResourceMStatic get() {
    return BlackReflection.create(ReceiverResourceMStatic.class, null, false);
  }

  public static ReceiverResourceMContext getWithException(final Object caller) {
    return BlackReflection.create(ReceiverResourceMContext.class, caller, true);
  }

  public static ReceiverResourceMContext get(final Object caller) {
    return BlackReflection.create(ReceiverResourceMContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ReceiverResourceMContext.class);
  }
}
