package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIUsageStatsManagerStub {
  public static IUsageStatsManagerStubStatic getWithException() {
    return BlackReflection.create(IUsageStatsManagerStubStatic.class, null, true);
  }

  public static IUsageStatsManagerStubStatic get() {
    return BlackReflection.create(IUsageStatsManagerStubStatic.class, null, false);
  }

  public static IUsageStatsManagerStubContext getWithException(final Object caller) {
    return BlackReflection.create(IUsageStatsManagerStubContext.class, caller, true);
  }

  public static IUsageStatsManagerStubContext get(final Object caller) {
    return BlackReflection.create(IUsageStatsManagerStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IUsageStatsManagerStubContext.class);
  }
}
