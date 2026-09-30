package black.android.app.role;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIRoleManagerStub {
  public static IRoleManagerStubStatic getWithException() {
    return BlackReflection.create(IRoleManagerStubStatic.class, null, true);
  }

  public static IRoleManagerStubStatic get() {
    return BlackReflection.create(IRoleManagerStubStatic.class, null, false);
  }

  public static IRoleManagerStubContext getWithException(final Object caller) {
    return BlackReflection.create(IRoleManagerStubContext.class, caller, true);
  }

  public static IRoleManagerStubContext get(final Object caller) {
    return BlackReflection.create(IRoleManagerStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IRoleManagerStubContext.class);
  }
}
