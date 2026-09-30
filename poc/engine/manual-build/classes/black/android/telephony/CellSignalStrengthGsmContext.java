package black.android.telephony;

import java.lang.Integer;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.telephony.CellSignalStrengthGsm")
public interface CellSignalStrengthGsmContext {
  @BFieldSetNotProcess
  void _set_mBitErrorRate(final Object value);

  @BFieldCheckNotProcess
  Field _check_mBitErrorRate();

  @BFieldNotProcess
  Integer mBitErrorRate();

  @BFieldSetNotProcess
  void _set_mSignalStrength(final Object value);

  @BFieldCheckNotProcess
  Field _check_mSignalStrength();

  @BFieldNotProcess
  Integer mSignalStrength();
}
