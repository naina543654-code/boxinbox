package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIActivityManagerContentProviderHolderMIUI {
  public static IActivityManagerContentProviderHolderMIUIStatic getWithException() {
    return BlackReflection.create(IActivityManagerContentProviderHolderMIUIStatic.class, null, true);
  }

  public static IActivityManagerContentProviderHolderMIUIStatic get() {
    return BlackReflection.create(IActivityManagerContentProviderHolderMIUIStatic.class, null, false);
  }

  public static IActivityManagerContentProviderHolderMIUIContext getWithException(
      final Object caller) {
    return BlackReflection.create(IActivityManagerContentProviderHolderMIUIContext.class, caller, true);
  }

  public static IActivityManagerContentProviderHolderMIUIContext get(final Object caller) {
    return BlackReflection.create(IActivityManagerContentProviderHolderMIUIContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IActivityManagerContentProviderHolderMIUIContext.class);
  }
}
