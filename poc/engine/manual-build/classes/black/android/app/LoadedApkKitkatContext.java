package black.android.app;

import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.LoadedApk")
public interface LoadedApkKitkatContext {
  @BFieldSetNotProcess
  void _set_mDisplayAdjustments(final Object value);

  @BFieldCheckNotProcess
  Field _check_mDisplayAdjustments();

  @BFieldNotProcess
  Object mDisplayAdjustments();
}
