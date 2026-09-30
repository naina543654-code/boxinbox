package black.android.service.notification;

import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.service.notification.StatusBarNotification")
public interface StatusBarNotificationContext {
  @BFieldSetNotProcess
  void _set_id(final Object value);

  @BFieldCheckNotProcess
  Field _check_id();

  @BFieldNotProcess
  Integer id();

  @BFieldSetNotProcess
  void _set_opPkg(final Object value);

  @BFieldCheckNotProcess
  Field _check_opPkg();

  @BFieldNotProcess
  String opPkg();

  @BFieldSetNotProcess
  void _set_pkg(final Object value);

  @BFieldCheckNotProcess
  Field _check_pkg();

  @BFieldNotProcess
  String pkg();

  @BFieldSetNotProcess
  void _set_tag(final Object value);

  @BFieldCheckNotProcess
  Field _check_tag();

  @BFieldNotProcess
  String tag();
}
