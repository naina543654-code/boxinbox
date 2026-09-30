package black.android.media.session;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRISessionManagerStub {
  public static ISessionManagerStubStatic getWithException() {
    return BlackReflection.create(ISessionManagerStubStatic.class, null, true);
  }

  public static ISessionManagerStubStatic get() {
    return BlackReflection.create(ISessionManagerStubStatic.class, null, false);
  }

  public static ISessionManagerStubContext getWithException(final Object caller) {
    return BlackReflection.create(ISessionManagerStubContext.class, caller, true);
  }

  public static ISessionManagerStubContext get(final Object caller) {
    return BlackReflection.create(ISessionManagerStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ISessionManagerStubContext.class);
  }
}
