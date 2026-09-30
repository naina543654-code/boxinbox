package black.android.os.storage;

import android.os.storage.StorageVolume;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.os.storage.StorageManager")
public interface StorageManagerStatic {
  @BMethodCheckNotProcess
  Method _check_getVolumeList(int int0, int int1);

  StorageVolume[] getVolumeList(int int0, int int1);
}
