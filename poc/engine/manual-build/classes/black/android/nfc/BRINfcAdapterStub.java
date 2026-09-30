package black.android.nfc;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRINfcAdapterStub {
  public static INfcAdapterStubStatic getWithException() {
    return BlackReflection.create(INfcAdapterStubStatic.class, null, true);
  }

  public static INfcAdapterStubStatic get() {
    return BlackReflection.create(INfcAdapterStubStatic.class, null, false);
  }

  public static INfcAdapterStubContext getWithException(final Object caller) {
    return BlackReflection.create(INfcAdapterStubContext.class, caller, true);
  }

  public static INfcAdapterStubContext get(final Object caller) {
    return BlackReflection.create(INfcAdapterStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(INfcAdapterStubContext.class);
  }
}
