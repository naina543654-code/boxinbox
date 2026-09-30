package black.android.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRINetworkManagementServiceStub {
  public static INetworkManagementServiceStubStatic getWithException() {
    return BlackReflection.create(INetworkManagementServiceStubStatic.class, null, true);
  }

  public static INetworkManagementServiceStubStatic get() {
    return BlackReflection.create(INetworkManagementServiceStubStatic.class, null, false);
  }

  public static INetworkManagementServiceStubContext getWithException(final Object caller) {
    return BlackReflection.create(INetworkManagementServiceStubContext.class, caller, true);
  }

  public static INetworkManagementServiceStubContext get(final Object caller) {
    return BlackReflection.create(INetworkManagementServiceStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(INetworkManagementServiceStubContext.class);
  }
}
