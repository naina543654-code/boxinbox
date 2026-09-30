package black.android.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRHandler {
  public static HandlerStatic getWithException() {
    return BlackReflection.create(HandlerStatic.class, null, true);
  }

  public static HandlerStatic get() {
    return BlackReflection.create(HandlerStatic.class, null, false);
  }

  public static HandlerContext getWithException(final Object caller) {
    return BlackReflection.create(HandlerContext.class, caller, true);
  }

  public static HandlerContext get(final Object caller) {
    return BlackReflection.create(HandlerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(HandlerContext.class);
  }
}
