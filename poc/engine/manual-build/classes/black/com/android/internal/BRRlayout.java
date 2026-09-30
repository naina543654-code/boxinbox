package black.com.android.internal;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRRlayout {
  public static RlayoutStatic getWithException() {
    return BlackReflection.create(RlayoutStatic.class, null, true);
  }

  public static RlayoutStatic get() {
    return BlackReflection.create(RlayoutStatic.class, null, false);
  }

  public static RlayoutContext getWithException(final Object caller) {
    return BlackReflection.create(RlayoutContext.class, caller, true);
  }

  public static RlayoutContext get(final Object caller) {
    return BlackReflection.create(RlayoutContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(RlayoutContext.class);
  }
}
