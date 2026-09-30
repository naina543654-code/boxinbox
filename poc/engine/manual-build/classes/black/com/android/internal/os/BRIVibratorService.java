package black.com.android.internal.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIVibratorService {
  public static IVibratorServiceStatic getWithException() {
    return BlackReflection.create(IVibratorServiceStatic.class, null, true);
  }

  public static IVibratorServiceStatic get() {
    return BlackReflection.create(IVibratorServiceStatic.class, null, false);
  }

  public static IVibratorServiceContext getWithException(final Object caller) {
    return BlackReflection.create(IVibratorServiceContext.class, caller, true);
  }

  public static IVibratorServiceContext get(final Object caller) {
    return BlackReflection.create(IVibratorServiceContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IVibratorServiceContext.class);
  }
}
