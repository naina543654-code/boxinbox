package black.android.net;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIVpnManager {
  public static IVpnManagerStatic getWithException() {
    return BlackReflection.create(IVpnManagerStatic.class, null, true);
  }

  public static IVpnManagerStatic get() {
    return BlackReflection.create(IVpnManagerStatic.class, null, false);
  }

  public static IVpnManagerContext getWithException(final Object caller) {
    return BlackReflection.create(IVpnManagerContext.class, caller, true);
  }

  public static IVpnManagerContext get(final Object caller) {
    return BlackReflection.create(IVpnManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IVpnManagerContext.class);
  }
}
