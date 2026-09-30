package black.android.media;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRMediaRouterStaticKitkat {
  public static MediaRouterStaticKitkatStatic getWithException() {
    return BlackReflection.create(MediaRouterStaticKitkatStatic.class, null, true);
  }

  public static MediaRouterStaticKitkatStatic get() {
    return BlackReflection.create(MediaRouterStaticKitkatStatic.class, null, false);
  }

  public static MediaRouterStaticKitkatContext getWithException(final Object caller) {
    return BlackReflection.create(MediaRouterStaticKitkatContext.class, caller, true);
  }

  public static MediaRouterStaticKitkatContext get(final Object caller) {
    return BlackReflection.create(MediaRouterStaticKitkatContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(MediaRouterStaticKitkatContext.class);
  }
}
