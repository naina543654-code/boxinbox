package black.android.net.wifi;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIWifiManager {
  public static IWifiManagerStatic getWithException() {
    return BlackReflection.create(IWifiManagerStatic.class, null, true);
  }

  public static IWifiManagerStatic get() {
    return BlackReflection.create(IWifiManagerStatic.class, null, false);
  }

  public static IWifiManagerContext getWithException(final Object caller) {
    return BlackReflection.create(IWifiManagerContext.class, caller, true);
  }

  public static IWifiManagerContext get(final Object caller) {
    return BlackReflection.create(IWifiManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IWifiManagerContext.class);
  }
}
