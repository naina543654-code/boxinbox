package black.android.app.admin;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIDevicePolicyManager {
  public static IDevicePolicyManagerStatic getWithException() {
    return BlackReflection.create(IDevicePolicyManagerStatic.class, null, true);
  }

  public static IDevicePolicyManagerStatic get() {
    return BlackReflection.create(IDevicePolicyManagerStatic.class, null, false);
  }

  public static IDevicePolicyManagerContext getWithException(final Object caller) {
    return BlackReflection.create(IDevicePolicyManagerContext.class, caller, true);
  }

  public static IDevicePolicyManagerContext get(final Object caller) {
    return BlackReflection.create(IDevicePolicyManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IDevicePolicyManagerContext.class);
  }
}
