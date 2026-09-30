package black.android.app;

import android.app.PendingIntent;
import android.content.Context;
import java.lang.CharSequence;
import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.Notification")
public interface NotificationContext {
  @BMethodCheckNotProcess
  Method _check_setLatestEventInfo(Context Context0, CharSequence CharSequence1,
      CharSequence CharSequence2, PendingIntent PendingIntent3);

  Void setLatestEventInfo(Context Context0, CharSequence CharSequence1, CharSequence CharSequence2,
      PendingIntent PendingIntent3);
}
