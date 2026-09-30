package black.android.net.wifi;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRWifiSsid {
  public static WifiSsidStatic getWithException() {
    return BlackReflection.create(WifiSsidStatic.class, null, true);
  }

  public static WifiSsidStatic get() {
    return BlackReflection.create(WifiSsidStatic.class, null, false);
  }

  public static WifiSsidContext getWithException(final Object caller) {
    return BlackReflection.create(WifiSsidContext.class, caller, true);
  }

  public static WifiSsidContext get(final Object caller) {
    return BlackReflection.create(WifiSsidContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(WifiSsidContext.class);
  }
}
