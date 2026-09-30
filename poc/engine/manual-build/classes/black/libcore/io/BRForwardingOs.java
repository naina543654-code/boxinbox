package black.libcore.io;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRForwardingOs {
  public static ForwardingOsStatic getWithException() {
    return BlackReflection.create(ForwardingOsStatic.class, null, true);
  }

  public static ForwardingOsStatic get() {
    return BlackReflection.create(ForwardingOsStatic.class, null, false);
  }

  public static ForwardingOsContext getWithException(final Object caller) {
    return BlackReflection.create(ForwardingOsContext.class, caller, true);
  }

  public static ForwardingOsContext get(final Object caller) {
    return BlackReflection.create(ForwardingOsContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ForwardingOsContext.class);
  }
}
