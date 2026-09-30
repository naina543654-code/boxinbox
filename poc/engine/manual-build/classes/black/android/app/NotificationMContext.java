package black.android.app;

import android.graphics.drawable.Icon;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.Notification")
public interface NotificationMContext {
  @BFieldSetNotProcess
  void _set_mLargeIcon(final Object value);

  @BFieldCheckNotProcess
  Field _check_mLargeIcon();

  @BFieldNotProcess
  Icon mLargeIcon();

  @BFieldSetNotProcess
  void _set_mSmallIcon(final Object value);

  @BFieldCheckNotProcess
  Field _check_mSmallIcon();

  @BFieldNotProcess
  Icon mSmallIcon();
}
