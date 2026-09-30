package black.android.location;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRILocationListenerStub {
  public static ILocationListenerStubStatic getWithException() {
    return BlackReflection.create(ILocationListenerStubStatic.class, null, true);
  }

  public static ILocationListenerStubStatic get() {
    return BlackReflection.create(ILocationListenerStubStatic.class, null, false);
  }

  public static ILocationListenerStubContext getWithException(final Object caller) {
    return BlackReflection.create(ILocationListenerStubContext.class, caller, true);
  }

  public static ILocationListenerStubContext get(final Object caller) {
    return BlackReflection.create(ILocationListenerStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ILocationListenerStubContext.class);
  }
}
