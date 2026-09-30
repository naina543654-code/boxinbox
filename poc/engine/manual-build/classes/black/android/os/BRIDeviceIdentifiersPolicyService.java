package black.android.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIDeviceIdentifiersPolicyService {
  public static IDeviceIdentifiersPolicyServiceStatic getWithException() {
    return BlackReflection.create(IDeviceIdentifiersPolicyServiceStatic.class, null, true);
  }

  public static IDeviceIdentifiersPolicyServiceStatic get() {
    return BlackReflection.create(IDeviceIdentifiersPolicyServiceStatic.class, null, false);
  }

  public static IDeviceIdentifiersPolicyServiceContext getWithException(final Object caller) {
    return BlackReflection.create(IDeviceIdentifiersPolicyServiceContext.class, caller, true);
  }

  public static IDeviceIdentifiersPolicyServiceContext get(final Object caller) {
    return BlackReflection.create(IDeviceIdentifiersPolicyServiceContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IDeviceIdentifiersPolicyServiceContext.class);
  }
}
