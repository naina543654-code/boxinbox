package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRLoadedApkReceiverDispatcherInnerReceiver {
  public static LoadedApkReceiverDispatcherInnerReceiverStatic getWithException() {
    return BlackReflection.create(LoadedApkReceiverDispatcherInnerReceiverStatic.class, null, true);
  }

  public static LoadedApkReceiverDispatcherInnerReceiverStatic get() {
    return BlackReflection.create(LoadedApkReceiverDispatcherInnerReceiverStatic.class, null, false);
  }

  public static LoadedApkReceiverDispatcherInnerReceiverContext getWithException(
      final Object caller) {
    return BlackReflection.create(LoadedApkReceiverDispatcherInnerReceiverContext.class, caller, true);
  }

  public static LoadedApkReceiverDispatcherInnerReceiverContext get(final Object caller) {
    return BlackReflection.create(LoadedApkReceiverDispatcherInnerReceiverContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(LoadedApkReceiverDispatcherInnerReceiverContext.class);
  }
}
