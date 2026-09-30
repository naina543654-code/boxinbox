package black.android.content.pm;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRPackageParserLollipop {
  public static PackageParserLollipopStatic getWithException() {
    return BlackReflection.create(PackageParserLollipopStatic.class, null, true);
  }

  public static PackageParserLollipopStatic get() {
    return BlackReflection.create(PackageParserLollipopStatic.class, null, false);
  }

  public static PackageParserLollipopContext getWithException(final Object caller) {
    return BlackReflection.create(PackageParserLollipopContext.class, caller, true);
  }

  public static PackageParserLollipopContext get(final Object caller) {
    return BlackReflection.create(PackageParserLollipopContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(PackageParserLollipopContext.class);
  }
}
