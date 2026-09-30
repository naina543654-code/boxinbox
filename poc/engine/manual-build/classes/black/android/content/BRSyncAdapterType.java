package black.android.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRSyncAdapterType {
  public static SyncAdapterTypeStatic getWithException() {
    return BlackReflection.create(SyncAdapterTypeStatic.class, null, true);
  }

  public static SyncAdapterTypeStatic get() {
    return BlackReflection.create(SyncAdapterTypeStatic.class, null, false);
  }

  public static SyncAdapterTypeContext getWithException(final Object caller) {
    return BlackReflection.create(SyncAdapterTypeContext.class, caller, true);
  }

  public static SyncAdapterTypeContext get(final Object caller) {
    return BlackReflection.create(SyncAdapterTypeContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(SyncAdapterTypeContext.class);
  }
}
