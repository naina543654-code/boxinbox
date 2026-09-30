package black.com.android.internal;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRRstyleable {
  public static RstyleableStatic getWithException() {
    return BlackReflection.create(RstyleableStatic.class, null, true);
  }

  public static RstyleableStatic get() {
    return BlackReflection.create(RstyleableStatic.class, null, false);
  }

  public static RstyleableContext getWithException(final Object caller) {
    return BlackReflection.create(RstyleableContext.class, caller, true);
  }

  public static RstyleableContext get(final Object caller) {
    return BlackReflection.create(RstyleableContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(RstyleableContext.class);
  }
}
