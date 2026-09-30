package black.android.util;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRSingleton {
  public static SingletonStatic getWithException() {
    return BlackReflection.create(SingletonStatic.class, null, true);
  }

  public static SingletonStatic get() {
    return BlackReflection.create(SingletonStatic.class, null, false);
  }

  public static SingletonContext getWithException(final Object caller) {
    return BlackReflection.create(SingletonContext.class, caller, true);
  }

  public static SingletonContext get(final Object caller) {
    return BlackReflection.create(SingletonContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(SingletonContext.class);
  }
}
