package black.android.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRParcel {
  public static ParcelStatic getWithException() {
    return BlackReflection.create(ParcelStatic.class, null, true);
  }

  public static ParcelStatic get() {
    return BlackReflection.create(ParcelStatic.class, null, false);
  }

  public static ParcelContext getWithException(final Object caller) {
    return BlackReflection.create(ParcelContext.class, caller, true);
  }

  public static ParcelContext get(final Object caller) {
    return BlackReflection.create(ParcelContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ParcelContext.class);
  }
}
