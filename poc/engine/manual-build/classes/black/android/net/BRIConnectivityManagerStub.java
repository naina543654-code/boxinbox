package black.android.net;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIConnectivityManagerStub {
  public static IConnectivityManagerStubStatic getWithException() {
    return BlackReflection.create(IConnectivityManagerStubStatic.class, null, true);
  }

  public static IConnectivityManagerStubStatic get() {
    return BlackReflection.create(IConnectivityManagerStubStatic.class, null, false);
  }

  public static IConnectivityManagerStubContext getWithException(final Object caller) {
    return BlackReflection.create(IConnectivityManagerStubContext.class, caller, true);
  }

  public static IConnectivityManagerStubContext get(final Object caller) {
    return BlackReflection.create(IConnectivityManagerStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IConnectivityManagerStubContext.class);
  }
}
