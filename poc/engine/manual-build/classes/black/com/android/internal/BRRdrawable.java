package black.com.android.internal;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRRdrawable {
  public static RdrawableStatic getWithException() {
    return BlackReflection.create(RdrawableStatic.class, null, true);
  }

  public static RdrawableStatic get() {
    return BlackReflection.create(RdrawableStatic.class, null, false);
  }

  public static RdrawableContext getWithException(final Object caller) {
    return BlackReflection.create(RdrawableContext.class, caller, true);
  }

  public static RdrawableContext get(final Object caller) {
    return BlackReflection.create(RdrawableContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(RdrawableContext.class);
  }
}
