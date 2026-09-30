package black.com.android.internal.policy;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRPhoneWindow {
  public static PhoneWindowStatic getWithException() {
    return BlackReflection.create(PhoneWindowStatic.class, null, true);
  }

  public static PhoneWindowStatic get() {
    return BlackReflection.create(PhoneWindowStatic.class, null, false);
  }

  public static PhoneWindowContext getWithException(final Object caller) {
    return BlackReflection.create(PhoneWindowContext.class, caller, true);
  }

  public static PhoneWindowContext get(final Object caller) {
    return BlackReflection.create(PhoneWindowContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(PhoneWindowContext.class);
  }
}
