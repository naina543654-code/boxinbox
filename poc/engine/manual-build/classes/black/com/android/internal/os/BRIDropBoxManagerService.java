package black.com.android.internal.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIDropBoxManagerService {
  public static IDropBoxManagerServiceStatic getWithException() {
    return BlackReflection.create(IDropBoxManagerServiceStatic.class, null, true);
  }

  public static IDropBoxManagerServiceStatic get() {
    return BlackReflection.create(IDropBoxManagerServiceStatic.class, null, false);
  }

  public static IDropBoxManagerServiceContext getWithException(final Object caller) {
    return BlackReflection.create(IDropBoxManagerServiceContext.class, caller, true);
  }

  public static IDropBoxManagerServiceContext get(final Object caller) {
    return BlackReflection.create(IDropBoxManagerServiceContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IDropBoxManagerServiceContext.class);
  }
}
