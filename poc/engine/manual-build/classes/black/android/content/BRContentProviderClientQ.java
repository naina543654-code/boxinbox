package black.android.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRContentProviderClientQ {
  public static ContentProviderClientQStatic getWithException() {
    return BlackReflection.create(ContentProviderClientQStatic.class, null, true);
  }

  public static ContentProviderClientQStatic get() {
    return BlackReflection.create(ContentProviderClientQStatic.class, null, false);
  }

  public static ContentProviderClientQContext getWithException(final Object caller) {
    return BlackReflection.create(ContentProviderClientQContext.class, caller, true);
  }

  public static ContentProviderClientQContext get(final Object caller) {
    return BlackReflection.create(ContentProviderClientQContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ContentProviderClientQContext.class);
  }
}
