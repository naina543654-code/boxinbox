package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRContextImpl {
  public static ContextImplStatic getWithException() {
    return BlackReflection.create(ContextImplStatic.class, null, true);
  }

  public static ContextImplStatic get() {
    return BlackReflection.create(ContextImplStatic.class, null, false);
  }

  public static ContextImplContext getWithException(final Object caller) {
    return BlackReflection.create(ContextImplContext.class, caller, true);
  }

  public static ContextImplContext get(final Object caller) {
    return BlackReflection.create(ContextImplContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ContextImplContext.class);
  }
}
