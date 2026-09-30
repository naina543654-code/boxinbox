package black.android.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIDeviceIdentifiersPolicyServiceStub {
  public static IDeviceIdentifiersPolicyServiceStubStatic getWithException() {
    return BlackReflection.create(IDeviceIdentifiersPolicyServiceStubStatic.class, null, true);
  }

  public static IDeviceIdentifiersPolicyServiceStubStatic get() {
    return BlackReflection.create(IDeviceIdentifiersPolicyServiceStubStatic.class, null, false);
  }

  public static IDeviceIdentifiersPolicyServiceStubContext getWithException(final Object caller) {
    return BlackReflection.create(IDeviceIdentifiersPolicyServiceStubContext.class, caller, true);
  }

  public static IDeviceIdentifiersPolicyServiceStubContext get(final Object caller) {
    return BlackReflection.create(IDeviceIdentifiersPolicyServiceStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IDeviceIdentifiersPolicyServiceStubContext.class);
  }
}
