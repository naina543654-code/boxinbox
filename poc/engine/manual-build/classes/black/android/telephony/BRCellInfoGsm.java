package black.android.telephony;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRCellInfoGsm {
  public static CellInfoGsmStatic getWithException() {
    return BlackReflection.create(CellInfoGsmStatic.class, null, true);
  }

  public static CellInfoGsmStatic get() {
    return BlackReflection.create(CellInfoGsmStatic.class, null, false);
  }

  public static CellInfoGsmContext getWithException(final Object caller) {
    return BlackReflection.create(CellInfoGsmContext.class, caller, true);
  }

  public static CellInfoGsmContext get(final Object caller) {
    return BlackReflection.create(CellInfoGsmContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(CellInfoGsmContext.class);
  }
}
