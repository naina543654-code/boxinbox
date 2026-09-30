package black.android.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRContentProviderNative {
  public static ContentProviderNativeStatic getWithException() {
    return BlackReflection.create(ContentProviderNativeStatic.class, null, true);
  }

  public static ContentProviderNativeStatic get() {
    return BlackReflection.create(ContentProviderNativeStatic.class, null, false);
  }

  public static ContentProviderNativeContext getWithException(final Object caller) {
    return BlackReflection.create(ContentProviderNativeContext.class, caller, true);
  }

  public static ContentProviderNativeContext get(final Object caller) {
    return BlackReflection.create(ContentProviderNativeContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ContentProviderNativeContext.class);
  }
}
