package black.android.media;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRMediaRouter {
  public static MediaRouterStatic getWithException() {
    return BlackReflection.create(MediaRouterStatic.class, null, true);
  }

  public static MediaRouterStatic get() {
    return BlackReflection.create(MediaRouterStatic.class, null, false);
  }

  public static MediaRouterContext getWithException(final Object caller) {
    return BlackReflection.create(MediaRouterContext.class, caller, true);
  }

  public static MediaRouterContext get(final Object caller) {
    return BlackReflection.create(MediaRouterContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(MediaRouterContext.class);
  }
}
