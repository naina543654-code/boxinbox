package black.android.app;

import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.LoadedApk")
public interface LoadedApkHuaWeiContext {
  @BFieldSetNotProcess
  void _set_mReceiverResource(final Object value);

  @BFieldCheckNotProcess
  Field _check_mReceiverResource();

  @BFieldNotProcess
  Object mReceiverResource();
}
