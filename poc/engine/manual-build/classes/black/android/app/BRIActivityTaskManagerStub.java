package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIActivityTaskManagerStub {
  public static IActivityTaskManagerStubStatic getWithException() {
    return BlackReflection.create(IActivityTaskManagerStubStatic.class, null, true);
  }

  public static IActivityTaskManagerStubStatic get() {
    return BlackReflection.create(IActivityTaskManagerStubStatic.class, null, false);
  }

  public static IActivityTaskManagerStubContext getWithException(final Object caller) {
    return BlackReflection.create(IActivityTaskManagerStubContext.class, caller, true);
  }

  public static IActivityTaskManagerStubContext get(final Object caller) {
    return BlackReflection.create(IActivityTaskManagerStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IActivityTaskManagerStubContext.class);
  }
}
