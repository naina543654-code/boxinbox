package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIActivityManagerContentProviderHolder {
  public static IActivityManagerContentProviderHolderStatic getWithException() {
    return BlackReflection.create(IActivityManagerContentProviderHolderStatic.class, null, true);
  }

  public static IActivityManagerContentProviderHolderStatic get() {
    return BlackReflection.create(IActivityManagerContentProviderHolderStatic.class, null, false);
  }

  public static IActivityManagerContentProviderHolderContext getWithException(final Object caller) {
    return BlackReflection.create(IActivityManagerContentProviderHolderContext.class, caller, true);
  }

  public static IActivityManagerContentProviderHolderContext get(final Object caller) {
    return BlackReflection.create(IActivityManagerContentProviderHolderContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IActivityManagerContentProviderHolderContext.class);
  }
}
