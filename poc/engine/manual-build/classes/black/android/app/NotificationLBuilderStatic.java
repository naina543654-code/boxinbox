package black.android.app;

import android.app.Notification;
import android.content.Context;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.Notification$Builder")
public interface NotificationLBuilderStatic {
  @BMethodCheckNotProcess
  Method _check_rebuild(Context Context0, Notification Notification1);

  Notification rebuild(Context Context0, Notification Notification1);
}
