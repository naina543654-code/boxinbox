package black.android.location;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRILocationListener {
  public static ILocationListenerStatic getWithException() {
    return BlackReflection.create(ILocationListenerStatic.class, null, true);
  }

  public static ILocationListenerStatic get() {
    return BlackReflection.create(ILocationListenerStatic.class, null, false);
  }

  public static ILocationListenerContext getWithException(final Object caller) {
    return BlackReflection.create(ILocationListenerContext.class, caller, true);
  }

  public static ILocationListenerContext get(final Object caller) {
    return BlackReflection.create(ILocationListenerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ILocationListenerContext.class);
  }
}
