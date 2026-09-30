package black.com.android.internal.net;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRVpnConfig {
  public static VpnConfigStatic getWithException() {
    return BlackReflection.create(VpnConfigStatic.class, null, true);
  }

  public static VpnConfigStatic get() {
    return BlackReflection.create(VpnConfigStatic.class, null, false);
  }

  public static VpnConfigContext getWithException(final Object caller) {
    return BlackReflection.create(VpnConfigContext.class, caller, true);
  }

  public static VpnConfigContext get(final Object caller) {
    return BlackReflection.create(VpnConfigContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(VpnConfigContext.class);
  }
}
