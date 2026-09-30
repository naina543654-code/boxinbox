package black.android.net.wifi;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRWifiInfo {
  public static WifiInfoStatic getWithException() {
    return BlackReflection.create(WifiInfoStatic.class, null, true);
  }

  public static WifiInfoStatic get() {
    return BlackReflection.create(WifiInfoStatic.class, null, false);
  }

  public static WifiInfoContext getWithException(final Object caller) {
    return BlackReflection.create(WifiInfoContext.class, caller, true);
  }

  public static WifiInfoContext get(final Object caller) {
    return BlackReflection.create(WifiInfoContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(WifiInfoContext.class);
  }
}
