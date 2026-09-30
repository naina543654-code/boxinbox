package black.android.os.storage;

import java.io.File;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.os.storage.StorageVolume")
public interface StorageVolumeContext {
  @BFieldSetNotProcess
  void _set_mInternalPath(final Object value);

  @BFieldCheckNotProcess
  Field _check_mInternalPath();

  @BFieldNotProcess
  File mInternalPath();

  @BFieldSetNotProcess
  void _set_mPath(final Object value);

  @BFieldCheckNotProcess
  Field _check_mPath();

  @BFieldNotProcess
  File mPath();
}
