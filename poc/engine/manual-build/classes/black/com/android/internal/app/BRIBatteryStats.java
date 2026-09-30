package black.com.android.internal.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIBatteryStats {
  public static IBatteryStatsStatic getWithException() {
    return BlackReflection.create(IBatteryStatsStatic.class, null, true);
  }

  public static IBatteryStatsStatic get() {
    return BlackReflection.create(IBatteryStatsStatic.class, null, false);
  }

  public static IBatteryStatsContext getWithException(final Object caller) {
    return BlackReflection.create(IBatteryStatsContext.class, caller, true);
  }

  public static IBatteryStatsContext get(final Object caller) {
    return BlackReflection.create(IBatteryStatsContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IBatteryStatsContext.class);
  }
}
