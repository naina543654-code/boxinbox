package black.android.os;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRMemoryFile {
  public static MemoryFileStatic getWithException() {
    return BlackReflection.create(MemoryFileStatic.class, null, true);
  }

  public static MemoryFileStatic get() {
    return BlackReflection.create(MemoryFileStatic.class, null, false);
  }

  public static MemoryFileContext getWithException(final Object caller) {
    return BlackReflection.create(MemoryFileContext.class, caller, true);
  }

  public static MemoryFileContext get(final Object caller) {
    return BlackReflection.create(MemoryFileContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(MemoryFileContext.class);
  }
}
