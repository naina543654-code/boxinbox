package black.android.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRSyncAdapterTypeN {
  public static SyncAdapterTypeNStatic getWithException() {
    return BlackReflection.create(SyncAdapterTypeNStatic.class, null, true);
  }

  public static SyncAdapterTypeNStatic get() {
    return BlackReflection.create(SyncAdapterTypeNStatic.class, null, false);
  }

  public static SyncAdapterTypeNContext getWithException(final Object caller) {
    return BlackReflection.create(SyncAdapterTypeNContext.class, caller, true);
  }

  public static SyncAdapterTypeNContext get(final Object caller) {
    return BlackReflection.create(SyncAdapterTypeNContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(SyncAdapterTypeNContext.class);
  }
}
