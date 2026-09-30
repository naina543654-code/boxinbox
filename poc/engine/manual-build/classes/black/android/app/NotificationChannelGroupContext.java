package black.android.app;

import android.app.NotificationChannel;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import java.util.List;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.NotificationChannelGroup")
public interface NotificationChannelGroupContext {
  @BFieldSetNotProcess
  void _set_mChannels(final Object value);

  @BFieldCheckNotProcess
  Field _check_mChannels();

  @BFieldNotProcess
  List<NotificationChannel> mChannels();

  @BFieldSetNotProcess
  void _set_mId(final Object value);

  @BFieldCheckNotProcess
  Field _check_mId();

  @BFieldNotProcess
  String mId();
}
