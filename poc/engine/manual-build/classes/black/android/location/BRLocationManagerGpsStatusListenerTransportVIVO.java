package black.android.location;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRLocationManagerGpsStatusListenerTransportVIVO {
  public static LocationManagerGpsStatusListenerTransportVIVOStatic getWithException() {
    return BlackReflection.create(LocationManagerGpsStatusListenerTransportVIVOStatic.class, null, true);
  }

  public static LocationManagerGpsStatusListenerTransportVIVOStatic get() {
    return BlackReflection.create(LocationManagerGpsStatusListenerTransportVIVOStatic.class, null, false);
  }

  public static LocationManagerGpsStatusListenerTransportVIVOContext getWithException(
      final Object caller) {
    return BlackReflection.create(LocationManagerGpsStatusListenerTransportVIVOContext.class, caller, true);
  }

  public static LocationManagerGpsStatusListenerTransportVIVOContext get(final Object caller) {
    return BlackReflection.create(LocationManagerGpsStatusListenerTransportVIVOContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(LocationManagerGpsStatusListenerTransportVIVOContext.class);
  }
}
