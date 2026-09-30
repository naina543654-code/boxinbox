package black.android.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.ActivityThread$ActivityClientRecord")
public interface ActivityThreadActivityClientRecordContext {
  @BFieldSetNotProcess
  void _set_activity(final Object value);

  @BFieldCheckNotProcess
  Field _check_activity();

  @BFieldNotProcess
  Activity activity();

  @BFieldSetNotProcess
  void _set_activityInfo(final Object value);

  @BFieldCheckNotProcess
  Field _check_activityInfo();

  @BFieldNotProcess
  ActivityInfo activityInfo();

  @BFieldSetNotProcess
  void _set_intent(final Object value);

  @BFieldCheckNotProcess
  Field _check_intent();

  @BFieldNotProcess
  Intent intent();

  @BFieldSetNotProcess
  void _set_isTopResumedActivity(final Object value);

  @BFieldCheckNotProcess
  Field _check_isTopResumedActivity();

  @BFieldNotProcess
  Boolean isTopResumedActivity();

  @BFieldSetNotProcess
  void _set_token(final Object value);

  @BFieldCheckNotProcess
  Field _check_token();

  @BFieldNotProcess
  IBinder token();

  @BFieldSetNotProcess
  void _set_packageInfo(final Object value);

  @BFieldCheckNotProcess
  Field _check_packageInfo();

  @BFieldNotProcess
  Object packageInfo();
}
