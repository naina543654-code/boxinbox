package black.android.app;

import android.content.Intent;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.ServiceStartArgs")
public interface ServiceStartArgsContext {
  @BFieldSetNotProcess
  void _set_args(final Object value);

  @BFieldCheckNotProcess
  Field _check_args();

  @BFieldNotProcess
  Intent args();

  @BFieldSetNotProcess
  void _set_flags(final Object value);

  @BFieldCheckNotProcess
  Field _check_flags();

  @BFieldNotProcess
  Integer flags();

  @BFieldSetNotProcess
  void _set_startId(final Object value);

  @BFieldCheckNotProcess
  Field _check_startId();

  @BFieldNotProcess
  Integer startId();

  @BFieldSetNotProcess
  void _set_taskRemoved(final Object value);

  @BFieldCheckNotProcess
  Field _check_taskRemoved();

  @BFieldNotProcess
  Boolean taskRemoved();
}
