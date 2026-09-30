package black.android.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIPowerManagerStub {
  public static IPowerManagerStubStatic getWithException() {
    return BlackReflection.create(IPowerManagerStubStatic.class, null, true);
  }

  public static IPowerManagerStubStatic get() {
    return BlackReflection.create(IPowerManagerStubStatic.class, null, false);
  }

  public static IPowerManagerStubContext getWithException(final Object caller) {
    return BlackReflection.create(IPowerManagerStubContext.class, caller, true);
  }

  public static IPowerManagerStubContext get(final Object caller) {
    return BlackReflection.create(IPowerManagerStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IPowerManagerStubContext.class);
  }
}
