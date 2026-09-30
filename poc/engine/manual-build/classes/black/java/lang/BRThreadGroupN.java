package black.java.lang;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRThreadGroupN {
  public static ThreadGroupNStatic getWithException() {
    return BlackReflection.create(ThreadGroupNStatic.class, null, true);
  }

  public static ThreadGroupNStatic get() {
    return BlackReflection.create(ThreadGroupNStatic.class, null, false);
  }

  public static ThreadGroupNContext getWithException(final Object caller) {
    return BlackReflection.create(ThreadGroupNContext.class, caller, true);
  }

  public static ThreadGroupNContext get(final Object caller) {
    return BlackReflection.create(ThreadGroupNContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ThreadGroupNContext.class);
  }
}
