package black.android.content.pm;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRPackageUserState {
  public static PackageUserStateStatic getWithException() {
    return BlackReflection.create(PackageUserStateStatic.class, null, true);
  }

  public static PackageUserStateStatic get() {
    return BlackReflection.create(PackageUserStateStatic.class, null, false);
  }

  public static PackageUserStateContext getWithException(final Object caller) {
    return BlackReflection.create(PackageUserStateContext.class, caller, true);
  }

  public static PackageUserStateContext get(final Object caller) {
    return BlackReflection.create(PackageUserStateContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(PackageUserStateContext.class);
  }
}
