package black.android.os.storage;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRStorageVolume {
  public static StorageVolumeStatic getWithException() {
    return BlackReflection.create(StorageVolumeStatic.class, null, true);
  }

  public static StorageVolumeStatic get() {
    return BlackReflection.create(StorageVolumeStatic.class, null, false);
  }

  public static StorageVolumeContext getWithException(final Object caller) {
    return BlackReflection.create(StorageVolumeContext.class, caller, true);
  }

  public static StorageVolumeContext get(final Object caller) {
    return BlackReflection.create(StorageVolumeContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(StorageVolumeContext.class);
  }
}
