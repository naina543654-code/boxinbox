package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRContextImplICS {
  public static ContextImplICSStatic getWithException() {
    return BlackReflection.create(ContextImplICSStatic.class, null, true);
  }

  public static ContextImplICSStatic get() {
    return BlackReflection.create(ContextImplICSStatic.class, null, false);
  }

  public static ContextImplICSContext getWithException(final Object caller) {
    return BlackReflection.create(ContextImplICSContext.class, caller, true);
  }

  public static ContextImplICSContext get(final Object caller) {
    return BlackReflection.create(ContextImplICSContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ContextImplICSContext.class);
  }
}
