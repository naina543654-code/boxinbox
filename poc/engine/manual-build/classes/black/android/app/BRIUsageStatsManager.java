package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIUsageStatsManager {
  public static IUsageStatsManagerStatic getWithException() {
    return BlackReflection.create(IUsageStatsManagerStatic.class, null, true);
  }

  public static IUsageStatsManagerStatic get() {
    return BlackReflection.create(IUsageStatsManagerStatic.class, null, false);
  }

  public static IUsageStatsManagerContext getWithException(final Object caller) {
    return BlackReflection.create(IUsageStatsManagerContext.class, caller, true);
  }

  public static IUsageStatsManagerContext get(final Object caller) {
    return BlackReflection.create(IUsageStatsManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IUsageStatsManagerContext.class);
  }
}
