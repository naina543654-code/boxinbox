package black.java.io;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRFileSystem {
  public static FileSystemStatic getWithException() {
    return BlackReflection.create(FileSystemStatic.class, null, true);
  }

  public static FileSystemStatic get() {
    return BlackReflection.create(FileSystemStatic.class, null, false);
  }

  public static FileSystemContext getWithException(final Object caller) {
    return BlackReflection.create(FileSystemContext.class, caller, true);
  }

  public static FileSystemContext get(final Object caller) {
    return BlackReflection.create(FileSystemContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(FileSystemContext.class);
  }
}
