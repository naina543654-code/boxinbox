package black.android.content.pm;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRPackageParserPermissionGroup {
  public static PackageParserPermissionGroupStatic getWithException() {
    return BlackReflection.create(PackageParserPermissionGroupStatic.class, null, true);
  }

  public static PackageParserPermissionGroupStatic get() {
    return BlackReflection.create(PackageParserPermissionGroupStatic.class, null, false);
  }

  public static PackageParserPermissionGroupContext getWithException(final Object caller) {
    return BlackReflection.create(PackageParserPermissionGroupContext.class, caller, true);
  }

  public static PackageParserPermissionGroupContext get(final Object caller) {
    return BlackReflection.create(PackageParserPermissionGroupContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(PackageParserPermissionGroupContext.class);
  }
}
