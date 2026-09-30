package black.android.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRSyncInfo {
  public static SyncInfoStatic getWithException() {
    return BlackReflection.create(SyncInfoStatic.class, null, true);
  }

  public static SyncInfoStatic get() {
    return BlackReflection.create(SyncInfoStatic.class, null, false);
  }

  public static SyncInfoContext getWithException(final Object caller) {
    return BlackReflection.create(SyncInfoContext.class, caller, true);
  }

  public static SyncInfoContext get(final Object caller) {
    return BlackReflection.create(SyncInfoContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(SyncInfoContext.class);
  }
}
