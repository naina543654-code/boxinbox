package black.android.app;

import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.Notification")
public interface NotificationOContext {
  @BFieldSetNotProcess
  void _set_mChannelId(final Object value);

  @BFieldCheckNotProcess
  Field _check_mChannelId();

  @BFieldNotProcess
  String mChannelId();

  @BFieldSetNotProcess
  void _set_mGroupKey(final Object value);

  @BFieldCheckNotProcess
  Field _check_mGroupKey();

  @BFieldNotProcess
  String mGroupKey();
}
