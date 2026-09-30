package black.com.android.internal.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRUserManagerStub {
  public static UserManagerStubStatic getWithException() {
    return BlackReflection.create(UserManagerStubStatic.class, null, true);
  }

  public static UserManagerStubStatic get() {
    return BlackReflection.create(UserManagerStubStatic.class, null, false);
  }

  public static UserManagerStubContext getWithException(final Object caller) {
    return BlackReflection.create(UserManagerStubContext.class, caller, true);
  }

  public static UserManagerStubContext get(final Object caller) {
    return BlackReflection.create(UserManagerStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(UserManagerStubContext.class);
  }
}
