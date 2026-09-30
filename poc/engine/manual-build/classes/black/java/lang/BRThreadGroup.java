package black.java.lang;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRThreadGroup {
  public static ThreadGroupStatic getWithException() {
    return BlackReflection.create(ThreadGroupStatic.class, null, true);
  }

  public static ThreadGroupStatic get() {
    return BlackReflection.create(ThreadGroupStatic.class, null, false);
  }

  public static ThreadGroupContext getWithException(final Object caller) {
    return BlackReflection.create(ThreadGroupContext.class, caller, true);
  }

  public static ThreadGroupContext get(final Object caller) {
    return BlackReflection.create(ThreadGroupContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ThreadGroupContext.class);
  }
}
