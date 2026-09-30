package black.android.app;

import android.os.IInterface;
import java.lang.Integer;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.AlarmManager")
public interface AlarmManagerContext {
  @BFieldSetNotProcess
  void _set_mService(final Object value);

  @BFieldCheckNotProcess
  Field _check_mService();

  @BFieldNotProcess
  IInterface mService();

  @BFieldSetNotProcess
  void _set_mTargetSdkVersion(final Object value);

  @BFieldCheckNotProcess
  Field _check_mTargetSdkVersion();

  @BFieldNotProcess
  Integer mTargetSdkVersion();
}
