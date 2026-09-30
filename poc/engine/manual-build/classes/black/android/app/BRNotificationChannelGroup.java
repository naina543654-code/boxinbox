package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRNotificationChannelGroup {
  public static NotificationChannelGroupStatic getWithException() {
    return BlackReflection.create(NotificationChannelGroupStatic.class, null, true);
  }

  public static NotificationChannelGroupStatic get() {
    return BlackReflection.create(NotificationChannelGroupStatic.class, null, false);
  }

  public static NotificationChannelGroupContext getWithException(final Object caller) {
    return BlackReflection.create(NotificationChannelGroupContext.class, caller, true);
  }

  public static NotificationChannelGroupContext get(final Object caller) {
    return BlackReflection.create(NotificationChannelGroupContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(NotificationChannelGroupContext.class);
  }
}
