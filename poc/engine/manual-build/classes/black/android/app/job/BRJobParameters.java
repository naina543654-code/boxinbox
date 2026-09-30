package black.android.app.job;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRJobParameters {
  public static JobParametersStatic getWithException() {
    return BlackReflection.create(JobParametersStatic.class, null, true);
  }

  public static JobParametersStatic get() {
    return BlackReflection.create(JobParametersStatic.class, null, false);
  }

  public static JobParametersContext getWithException(final Object caller) {
    return BlackReflection.create(JobParametersContext.class, caller, true);
  }

  public static JobParametersContext get(final Object caller) {
    return BlackReflection.create(JobParametersContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(JobParametersContext.class);
  }
}
