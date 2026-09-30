package black.com.android.internal.telephony;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIMmsStub {
  public static IMmsStubStatic getWithException() {
    return BlackReflection.create(IMmsStubStatic.class, null, true);
  }

  public static IMmsStubStatic get() {
    return BlackReflection.create(IMmsStubStatic.class, null, false);
  }

  public static IMmsStubContext getWithException(final Object caller) {
    return BlackReflection.create(IMmsStubContext.class, caller, true);
  }

  public static IMmsStubContext get(final Object caller) {
    return BlackReflection.create(IMmsStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IMmsStubContext.class);
  }
}
