package black.com.android.internal.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRNativeLibraryHelper {
  public static NativeLibraryHelperStatic getWithException() {
    return BlackReflection.create(NativeLibraryHelperStatic.class, null, true);
  }

  public static NativeLibraryHelperStatic get() {
    return BlackReflection.create(NativeLibraryHelperStatic.class, null, false);
  }

  public static NativeLibraryHelperContext getWithException(final Object caller) {
    return BlackReflection.create(NativeLibraryHelperContext.class, caller, true);
  }

  public static NativeLibraryHelperContext get(final Object caller) {
    return BlackReflection.create(NativeLibraryHelperContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(NativeLibraryHelperContext.class);
  }
}
