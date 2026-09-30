package black.com.android.internal.telecom;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRITelecomServiceStub {
  public static ITelecomServiceStubStatic getWithException() {
    return BlackReflection.create(ITelecomServiceStubStatic.class, null, true);
  }

  public static ITelecomServiceStubStatic get() {
    return BlackReflection.create(ITelecomServiceStubStatic.class, null, false);
  }

  public static ITelecomServiceStubContext getWithException(final Object caller) {
    return BlackReflection.create(ITelecomServiceStubContext.class, caller, true);
  }

  public static ITelecomServiceStubContext get(final Object caller) {
    return BlackReflection.create(ITelecomServiceStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ITelecomServiceStubContext.class);
  }
}
