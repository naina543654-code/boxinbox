package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIApplicationThreadOreoStub {
  public static IApplicationThreadOreoStubStatic getWithException() {
    return BlackReflection.create(IApplicationThreadOreoStubStatic.class, null, true);
  }

  public static IApplicationThreadOreoStubStatic get() {
    return BlackReflection.create(IApplicationThreadOreoStubStatic.class, null, false);
  }

  public static IApplicationThreadOreoStubContext getWithException(final Object caller) {
    return BlackReflection.create(IApplicationThreadOreoStubContext.class, caller, true);
  }

  public static IApplicationThreadOreoStubContext get(final Object caller) {
    return BlackReflection.create(IApplicationThreadOreoStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IApplicationThreadOreoStubContext.class);
  }
}
