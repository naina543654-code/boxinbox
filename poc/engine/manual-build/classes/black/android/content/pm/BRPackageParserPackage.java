package black.android.content.pm;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRPackageParserPackage {
  public static PackageParserPackageStatic getWithException() {
    return BlackReflection.create(PackageParserPackageStatic.class, null, true);
  }

  public static PackageParserPackageStatic get() {
    return BlackReflection.create(PackageParserPackageStatic.class, null, false);
  }

  public static PackageParserPackageContext getWithException(final Object caller) {
    return BlackReflection.create(PackageParserPackageContext.class, caller, true);
  }

  public static PackageParserPackageContext get(final Object caller) {
    return BlackReflection.create(PackageParserPackageContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(PackageParserPackageContext.class);
  }
}
