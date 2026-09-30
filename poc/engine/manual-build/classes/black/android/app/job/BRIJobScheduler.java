package black.android.app.job;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIJobScheduler {
  public static IJobSchedulerStatic getWithException() {
    return BlackReflection.create(IJobSchedulerStatic.class, null, true);
  }

  public static IJobSchedulerStatic get() {
    return BlackReflection.create(IJobSchedulerStatic.class, null, false);
  }

  public static IJobSchedulerContext getWithException(final Object caller) {
    return BlackReflection.create(IJobSchedulerContext.class, caller, true);
  }

  public static IJobSchedulerContext get(final Object caller) {
    return BlackReflection.create(IJobSchedulerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IJobSchedulerContext.class);
  }
}
