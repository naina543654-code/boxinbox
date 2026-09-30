package black.android.location;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRLocationManagerListenerTransport {
  public static LocationManagerListenerTransportStatic getWithException() {
    return BlackReflection.create(LocationManagerListenerTransportStatic.class, null, true);
  }

  public static LocationManagerListenerTransportStatic get() {
    return BlackReflection.create(LocationManagerListenerTransportStatic.class, null, false);
  }

  public static LocationManagerListenerTransportContext getWithException(final Object caller) {
    return BlackReflection.create(LocationManagerListenerTransportContext.class, caller, true);
  }

  public static LocationManagerListenerTransportContext get(final Object caller) {
    return BlackReflection.create(LocationManagerListenerTransportContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(LocationManagerListenerTransportContext.class);
  }
}
