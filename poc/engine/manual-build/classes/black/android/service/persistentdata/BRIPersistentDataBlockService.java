package black.android.service.persistentdata;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIPersistentDataBlockService {
  public static IPersistentDataBlockServiceStatic getWithException() {
    return BlackReflection.create(IPersistentDataBlockServiceStatic.class, null, true);
  }

  public static IPersistentDataBlockServiceStatic get() {
    return BlackReflection.create(IPersistentDataBlockServiceStatic.class, null, false);
  }

  public static IPersistentDataBlockServiceContext getWithException(final Object caller) {
    return BlackReflection.create(IPersistentDataBlockServiceContext.class, caller, true);
  }

  public static IPersistentDataBlockServiceContext get(final Object caller) {
    return BlackReflection.create(IPersistentDataBlockServiceContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IPersistentDataBlockServiceContext.class);
  }
}
