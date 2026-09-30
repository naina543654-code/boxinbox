package black.android.app.job;

import android.os.IBinder;
import android.os.PersistableBundle;
import java.lang.Integer;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.job.JobParameters")
public interface JobParametersContext {
  @BFieldSetNotProcess
  void _set_callback(final Object value);

  @BFieldCheckNotProcess
  Field _check_callback();

  @BFieldNotProcess
  IBinder callback();

  @BFieldSetNotProcess
  void _set_extras(final Object value);

  @BFieldCheckNotProcess
  Field _check_extras();

  @BFieldNotProcess
  PersistableBundle extras();

  @BFieldSetNotProcess
  void _set_jobId(final Object value);

  @BFieldCheckNotProcess
  Field _check_jobId();

  @BFieldNotProcess
  Integer jobId();
}
