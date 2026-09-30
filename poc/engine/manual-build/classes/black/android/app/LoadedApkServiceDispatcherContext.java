package black.android.app;

import android.content.Context;
import android.content.ServiceConnection;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.LoadedApk$ServiceDispatcher")
public interface LoadedApkServiceDispatcherContext {
  @BFieldSetNotProcess
  void _set_mConnection(final Object value);

  @BFieldCheckNotProcess
  Field _check_mConnection();

  @BFieldNotProcess
  ServiceConnection mConnection();

  @BFieldSetNotProcess
  void _set_mContext(final Object value);

  @BFieldCheckNotProcess
  Field _check_mContext();

  @BFieldNotProcess
  Context mContext();
}
