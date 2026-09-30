package black.com.android.internal.telephony;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRITelephonyRegistry {
  public static ITelephonyRegistryStatic getWithException() {
    return BlackReflection.create(ITelephonyRegistryStatic.class, null, true);
  }

  public static ITelephonyRegistryStatic get() {
    return BlackReflection.create(ITelephonyRegistryStatic.class, null, false);
  }

  public static ITelephonyRegistryContext getWithException(final Object caller) {
    return BlackReflection.create(ITelephonyRegistryContext.class, caller, true);
  }

  public static ITelephonyRegistryContext get(final Object caller) {
    return BlackReflection.create(ITelephonyRegistryContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ITelephonyRegistryContext.class);
  }
}
