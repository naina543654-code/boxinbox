package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRPendingIntentJBMR2 {
  public static PendingIntentJBMR2Static getWithException() {
    return BlackReflection.create(PendingIntentJBMR2Static.class, null, true);
  }

  public static PendingIntentJBMR2Static get() {
    return BlackReflection.create(PendingIntentJBMR2Static.class, null, false);
  }

  public static PendingIntentJBMR2Context getWithException(final Object caller) {
    return BlackReflection.create(PendingIntentJBMR2Context.class, caller, true);
  }

  public static PendingIntentJBMR2Context get(final Object caller) {
    return BlackReflection.create(PendingIntentJBMR2Context.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(PendingIntentJBMR2Context.class);
  }
}
