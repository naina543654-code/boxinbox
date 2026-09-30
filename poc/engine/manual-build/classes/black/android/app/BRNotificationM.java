package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRNotificationM {
  public static NotificationMStatic getWithException() {
    return BlackReflection.create(NotificationMStatic.class, null, true);
  }

  public static NotificationMStatic get() {
    return BlackReflection.create(NotificationMStatic.class, null, false);
  }

  public static NotificationMContext getWithException(final Object caller) {
    return BlackReflection.create(NotificationMContext.class, caller, true);
  }

  public static NotificationMContext get(final Object caller) {
    return BlackReflection.create(NotificationMContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(NotificationMContext.class);
  }
}
