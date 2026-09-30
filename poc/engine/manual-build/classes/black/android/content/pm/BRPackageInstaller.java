package black.android.content.pm;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRPackageInstaller {
  public static PackageInstallerStatic getWithException() {
    return BlackReflection.create(PackageInstallerStatic.class, null, true);
  }

  public static PackageInstallerStatic get() {
    return BlackReflection.create(PackageInstallerStatic.class, null, false);
  }

  public static PackageInstallerContext getWithException(final Object caller) {
    return BlackReflection.create(PackageInstallerContext.class, caller, true);
  }

  public static PackageInstallerContext get(final Object caller) {
    return BlackReflection.create(PackageInstallerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(PackageInstallerContext.class);
  }
}
