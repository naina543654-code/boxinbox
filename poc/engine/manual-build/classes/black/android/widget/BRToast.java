package black.android.widget;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRToast {
  public static ToastStatic getWithException() {
    return BlackReflection.create(ToastStatic.class, null, true);
  }

  public static ToastStatic get() {
    return BlackReflection.create(ToastStatic.class, null, false);
  }

  public static ToastContext getWithException(final Object caller) {
    return BlackReflection.create(ToastContext.class, caller, true);
  }

  public static ToastContext get(final Object caller) {
    return BlackReflection.create(ToastContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ToastContext.class);
  }
}
