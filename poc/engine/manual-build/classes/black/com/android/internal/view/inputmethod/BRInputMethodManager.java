package black.com.android.internal.view.inputmethod;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRInputMethodManager {
  public static InputMethodManagerStatic getWithException() {
    return BlackReflection.create(InputMethodManagerStatic.class, null, true);
  }

  public static InputMethodManagerStatic get() {
    return BlackReflection.create(InputMethodManagerStatic.class, null, false);
  }

  public static InputMethodManagerContext getWithException(final Object caller) {
    return BlackReflection.create(InputMethodManagerContext.class, caller, true);
  }

  public static InputMethodManagerContext get(final Object caller) {
    return BlackReflection.create(InputMethodManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(InputMethodManagerContext.class);
  }
}
