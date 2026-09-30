package black.android.rms;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRHwSysResImplP {
  public static HwSysResImplPStatic getWithException() {
    return BlackReflection.create(HwSysResImplPStatic.class, null, true);
  }

  public static HwSysResImplPStatic get() {
    return BlackReflection.create(HwSysResImplPStatic.class, null, false);
  }

  public static HwSysResImplPContext getWithException(final Object caller) {
    return BlackReflection.create(HwSysResImplPContext.class, caller, true);
  }

  public static HwSysResImplPContext get(final Object caller) {
    return BlackReflection.create(HwSysResImplPContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(HwSysResImplPContext.class);
  }
}
