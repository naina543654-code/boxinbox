package black.android.app.job;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIJobSchedulerStub {
  public static IJobSchedulerStubStatic getWithException() {
    return BlackReflection.create(IJobSchedulerStubStatic.class, null, true);
  }

  public static IJobSchedulerStubStatic get() {
    return BlackReflection.create(IJobSchedulerStubStatic.class, null, false);
  }

  public static IJobSchedulerStubContext getWithException(final Object caller) {
    return BlackReflection.create(IJobSchedulerStubContext.class, caller, true);
  }

  public static IJobSchedulerStubContext get(final Object caller) {
    return BlackReflection.create(IJobSchedulerStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IJobSchedulerStubContext.class);
  }
}
