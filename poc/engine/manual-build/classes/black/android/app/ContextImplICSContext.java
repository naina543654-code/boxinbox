package black.android.app;

import java.io.File;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.ContextImpl")
public interface ContextImplICSContext {
  @BFieldSetNotProcess
  void _set_mExternalCacheDir(final Object value);

  @BFieldCheckNotProcess
  Field _check_mExternalCacheDir();

  @BFieldNotProcess
  File mExternalCacheDir();

  @BFieldSetNotProcess
  void _set_mExternalFilesDir(final Object value);

  @BFieldCheckNotProcess
  Field _check_mExternalFilesDir();

  @BFieldNotProcess
  File mExternalFilesDir();
}
