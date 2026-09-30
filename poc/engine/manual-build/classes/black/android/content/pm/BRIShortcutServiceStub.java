package black.android.content.pm;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIShortcutServiceStub {
  public static IShortcutServiceStubStatic getWithException() {
    return BlackReflection.create(IShortcutServiceStubStatic.class, null, true);
  }

  public static IShortcutServiceStubStatic get() {
    return BlackReflection.create(IShortcutServiceStubStatic.class, null, false);
  }

  public static IShortcutServiceStubContext getWithException(final Object caller) {
    return BlackReflection.create(IShortcutServiceStubContext.class, caller, true);
  }

  public static IShortcutServiceStubContext get(final Object caller) {
    return BlackReflection.create(IShortcutServiceStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IShortcutServiceStubContext.class);
  }
}
