package black.com.android.internal.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIVibratorServiceStub {
  public static IVibratorServiceStubStatic getWithException() {
    return BlackReflection.create(IVibratorServiceStubStatic.class, null, true);
  }

  public static IVibratorServiceStubStatic get() {
    return BlackReflection.create(IVibratorServiceStubStatic.class, null, false);
  }

  public static IVibratorServiceStubContext getWithException(final Object caller) {
    return BlackReflection.create(IVibratorServiceStubContext.class, caller, true);
  }

  public static IVibratorServiceStubContext get(final Object caller) {
    return BlackReflection.create(IVibratorServiceStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IVibratorServiceStubContext.class);
  }
}
