package black.com.android.internal.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIDropBoxManagerServiceStub {
  public static IDropBoxManagerServiceStubStatic getWithException() {
    return BlackReflection.create(IDropBoxManagerServiceStubStatic.class, null, true);
  }

  public static IDropBoxManagerServiceStubStatic get() {
    return BlackReflection.create(IDropBoxManagerServiceStubStatic.class, null, false);
  }

  public static IDropBoxManagerServiceStubContext getWithException(final Object caller) {
    return BlackReflection.create(IDropBoxManagerServiceStubContext.class, caller, true);
  }

  public static IDropBoxManagerServiceStubContext get(final Object caller) {
    return BlackReflection.create(IDropBoxManagerServiceStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IDropBoxManagerServiceStubContext.class);
  }
}
