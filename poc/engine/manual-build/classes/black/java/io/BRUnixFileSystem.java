package black.java.io;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRUnixFileSystem {
  public static UnixFileSystemStatic getWithException() {
    return BlackReflection.create(UnixFileSystemStatic.class, null, true);
  }

  public static UnixFileSystemStatic get() {
    return BlackReflection.create(UnixFileSystemStatic.class, null, false);
  }

  public static UnixFileSystemContext getWithException(final Object caller) {
    return BlackReflection.create(UnixFileSystemContext.class, caller, true);
  }

  public static UnixFileSystemContext get(final Object caller) {
    return BlackReflection.create(UnixFileSystemContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(UnixFileSystemContext.class);
  }
}
