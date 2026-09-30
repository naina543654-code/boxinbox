package black.com.android.internal.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRNativeLibraryHelperHandle {
  public static NativeLibraryHelperHandleStatic getWithException() {
    return BlackReflection.create(NativeLibraryHelperHandleStatic.class, null, true);
  }

  public static NativeLibraryHelperHandleStatic get() {
    return BlackReflection.create(NativeLibraryHelperHandleStatic.class, null, false);
  }

  public static NativeLibraryHelperHandleContext getWithException(final Object caller) {
    return BlackReflection.create(NativeLibraryHelperHandleContext.class, caller, true);
  }

  public static NativeLibraryHelperHandleContext get(final Object caller) {
    return BlackReflection.create(NativeLibraryHelperHandleContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(NativeLibraryHelperHandleContext.class);
  }
}
