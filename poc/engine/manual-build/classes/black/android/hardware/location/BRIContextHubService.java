package black.android.hardware.location;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIContextHubService {
  public static IContextHubServiceStatic getWithException() {
    return BlackReflection.create(IContextHubServiceStatic.class, null, true);
  }

  public static IContextHubServiceStatic get() {
    return BlackReflection.create(IContextHubServiceStatic.class, null, false);
  }

  public static IContextHubServiceContext getWithException(final Object caller) {
    return BlackReflection.create(IContextHubServiceContext.class, caller, true);
  }

  public static IContextHubServiceContext get(final Object caller) {
    return BlackReflection.create(IContextHubServiceContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IContextHubServiceContext.class);
  }
}
