package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRNotificationLBuilder {
  public static NotificationLBuilderStatic getWithException() {
    return BlackReflection.create(NotificationLBuilderStatic.class, null, true);
  }

  public static NotificationLBuilderStatic get() {
    return BlackReflection.create(NotificationLBuilderStatic.class, null, false);
  }

  public static NotificationLBuilderContext getWithException(final Object caller) {
    return BlackReflection.create(NotificationLBuilderContext.class, caller, true);
  }

  public static NotificationLBuilderContext get(final Object caller) {
    return BlackReflection.create(NotificationLBuilderContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(NotificationLBuilderContext.class);
  }
}
