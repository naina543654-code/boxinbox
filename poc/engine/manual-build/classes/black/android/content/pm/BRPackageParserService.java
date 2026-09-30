package black.android.content.pm;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRPackageParserService {
  public static PackageParserServiceStatic getWithException() {
    return BlackReflection.create(PackageParserServiceStatic.class, null, true);
  }

  public static PackageParserServiceStatic get() {
    return BlackReflection.create(PackageParserServiceStatic.class, null, false);
  }

  public static PackageParserServiceContext getWithException(final Object caller) {
    return BlackReflection.create(PackageParserServiceContext.class, caller, true);
  }

  public static PackageParserServiceContext get(final Object caller) {
    return BlackReflection.create(PackageParserServiceContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(PackageParserServiceContext.class);
  }
}
