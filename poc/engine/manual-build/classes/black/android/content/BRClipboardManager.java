package black.android.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRClipboardManager {
  public static ClipboardManagerStatic getWithException() {
    return BlackReflection.create(ClipboardManagerStatic.class, null, true);
  }

  public static ClipboardManagerStatic get() {
    return BlackReflection.create(ClipboardManagerStatic.class, null, false);
  }

  public static ClipboardManagerContext getWithException(final Object caller) {
    return BlackReflection.create(ClipboardManagerContext.class, caller, true);
  }

  public static ClipboardManagerContext get(final Object caller) {
    return BlackReflection.create(ClipboardManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ClipboardManagerContext.class);
  }
}
