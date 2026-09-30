package black.android.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRBundleICS {
  public static BundleICSStatic getWithException() {
    return BlackReflection.create(BundleICSStatic.class, null, true);
  }

  public static BundleICSStatic get() {
    return BlackReflection.create(BundleICSStatic.class, null, false);
  }

  public static BundleICSContext getWithException(final Object caller) {
    return BlackReflection.create(BundleICSContext.class, caller, true);
  }

  public static BundleICSContext get(final Object caller) {
    return BlackReflection.create(BundleICSContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(BundleICSContext.class);
  }
}
