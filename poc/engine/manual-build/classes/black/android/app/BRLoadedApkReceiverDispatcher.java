package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRLoadedApkReceiverDispatcher {
  public static LoadedApkReceiverDispatcherStatic getWithException() {
    return BlackReflection.create(LoadedApkReceiverDispatcherStatic.class, null, true);
  }

  public static LoadedApkReceiverDispatcherStatic get() {
    return BlackReflection.create(LoadedApkReceiverDispatcherStatic.class, null, false);
  }

  public static LoadedApkReceiverDispatcherContext getWithException(final Object caller) {
    return BlackReflection.create(LoadedApkReceiverDispatcherContext.class, caller, true);
  }

  public static LoadedApkReceiverDispatcherContext get(final Object caller) {
    return BlackReflection.create(LoadedApkReceiverDispatcherContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(LoadedApkReceiverDispatcherContext.class);
  }
}
