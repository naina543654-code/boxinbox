package black.android.nfc;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRINfcAdapter {
  public static INfcAdapterStatic getWithException() {
    return BlackReflection.create(INfcAdapterStatic.class, null, true);
  }

  public static INfcAdapterStatic get() {
    return BlackReflection.create(INfcAdapterStatic.class, null, false);
  }

  public static INfcAdapterContext getWithException(final Object caller) {
    return BlackReflection.create(INfcAdapterContext.class, caller, true);
  }

  public static INfcAdapterContext get(final Object caller) {
    return BlackReflection.create(INfcAdapterContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(INfcAdapterContext.class);
  }
}
