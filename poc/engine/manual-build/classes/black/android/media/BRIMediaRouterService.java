package black.android.media;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIMediaRouterService {
  public static IMediaRouterServiceStatic getWithException() {
    return BlackReflection.create(IMediaRouterServiceStatic.class, null, true);
  }

  public static IMediaRouterServiceStatic get() {
    return BlackReflection.create(IMediaRouterServiceStatic.class, null, false);
  }

  public static IMediaRouterServiceContext getWithException(final Object caller) {
    return BlackReflection.create(IMediaRouterServiceContext.class, caller, true);
  }

  public static IMediaRouterServiceContext get(final Object caller) {
    return BlackReflection.create(IMediaRouterServiceContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IMediaRouterServiceContext.class);
  }
}
