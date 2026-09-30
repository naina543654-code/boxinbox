package black.android.content.pm;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRPackageParser {
  public static PackageParserStatic getWithException() {
    return BlackReflection.create(PackageParserStatic.class, null, true);
  }

  public static PackageParserStatic get() {
    return BlackReflection.create(PackageParserStatic.class, null, false);
  }

  public static PackageParserContext getWithException(final Object caller) {
    return BlackReflection.create(PackageParserContext.class, caller, true);
  }

  public static PackageParserContext get(final Object caller) {
    return BlackReflection.create(PackageParserContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(PackageParserContext.class);
  }
}
