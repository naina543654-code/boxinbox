package black.android.content.pm;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRApplicationInfoN {
  public static ApplicationInfoNStatic getWithException() {
    return BlackReflection.create(ApplicationInfoNStatic.class, null, true);
  }

  public static ApplicationInfoNStatic get() {
    return BlackReflection.create(ApplicationInfoNStatic.class, null, false);
  }

  public static ApplicationInfoNContext getWithException(final Object caller) {
    return BlackReflection.create(ApplicationInfoNContext.class, caller, true);
  }

  public static ApplicationInfoNContext get(final Object caller) {
    return BlackReflection.create(ApplicationInfoNContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ApplicationInfoNContext.class);
  }
}
