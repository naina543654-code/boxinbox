package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRNotificationChannel {
  public static NotificationChannelStatic getWithException() {
    return BlackReflection.create(NotificationChannelStatic.class, null, true);
  }

  public static NotificationChannelStatic get() {
    return BlackReflection.create(NotificationChannelStatic.class, null, false);
  }

  public static NotificationChannelContext getWithException(final Object caller) {
    return BlackReflection.create(NotificationChannelContext.class, caller, true);
  }

  public static NotificationChannelContext get(final Object caller) {
    return BlackReflection.create(NotificationChannelContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(NotificationChannelContext.class);
  }
}
