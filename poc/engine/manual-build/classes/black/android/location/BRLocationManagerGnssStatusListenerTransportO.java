package black.android.location;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRLocationManagerGnssStatusListenerTransportO {
  public static LocationManagerGnssStatusListenerTransportOStatic getWithException() {
    return BlackReflection.create(LocationManagerGnssStatusListenerTransportOStatic.class, null, true);
  }

  public static LocationManagerGnssStatusListenerTransportOStatic get() {
    return BlackReflection.create(LocationManagerGnssStatusListenerTransportOStatic.class, null, false);
  }

  public static LocationManagerGnssStatusListenerTransportOContext getWithException(
      final Object caller) {
    return BlackReflection.create(LocationManagerGnssStatusListenerTransportOContext.class, caller, true);
  }

  public static LocationManagerGnssStatusListenerTransportOContext get(final Object caller) {
    return BlackReflection.create(LocationManagerGnssStatusListenerTransportOContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(LocationManagerGnssStatusListenerTransportOContext.class);
  }
}
