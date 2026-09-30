package black.android.app.admin;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIDevicePolicyManagerStub {
  public static IDevicePolicyManagerStubStatic getWithException() {
    return BlackReflection.create(IDevicePolicyManagerStubStatic.class, null, true);
  }

  public static IDevicePolicyManagerStubStatic get() {
    return BlackReflection.create(IDevicePolicyManagerStubStatic.class, null, false);
  }

  public static IDevicePolicyManagerStubContext getWithException(final Object caller) {
    return BlackReflection.create(IDevicePolicyManagerStubContext.class, caller, true);
  }

  public static IDevicePolicyManagerStubContext get(final Object caller) {
    return BlackReflection.create(IDevicePolicyManagerStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IDevicePolicyManagerStubContext.class);
  }
}
