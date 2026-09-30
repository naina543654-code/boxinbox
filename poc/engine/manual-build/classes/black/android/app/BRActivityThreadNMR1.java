package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRActivityThreadNMR1 {
  public static ActivityThreadNMR1Static getWithException() {
    return BlackReflection.create(ActivityThreadNMR1Static.class, null, true);
  }

  public static ActivityThreadNMR1Static get() {
    return BlackReflection.create(ActivityThreadNMR1Static.class, null, false);
  }

  public static ActivityThreadNMR1Context getWithException(final Object caller) {
    return BlackReflection.create(ActivityThreadNMR1Context.class, caller, true);
  }

  public static ActivityThreadNMR1Context get(final Object caller) {
    return BlackReflection.create(ActivityThreadNMR1Context.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ActivityThreadNMR1Context.class);
  }
}
