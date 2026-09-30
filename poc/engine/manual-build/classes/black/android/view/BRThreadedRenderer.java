package black.android.view;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRThreadedRenderer {
  public static ThreadedRendererStatic getWithException() {
    return BlackReflection.create(ThreadedRendererStatic.class, null, true);
  }

  public static ThreadedRendererStatic get() {
    return BlackReflection.create(ThreadedRendererStatic.class, null, false);
  }

  public static ThreadedRendererContext getWithException(final Object caller) {
    return BlackReflection.create(ThreadedRendererContext.class, caller, true);
  }

  public static ThreadedRendererContext get(final Object caller) {
    return BlackReflection.create(ThreadedRendererContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ThreadedRendererContext.class);
  }
}
