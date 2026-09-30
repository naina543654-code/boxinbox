package black.android.telephony;

import android.telephony.CellIdentityGsm;
import android.telephony.CellSignalStrengthGsm;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.telephony.CellInfoGsm")
public interface CellInfoGsmContext {
  @BFieldSetNotProcess
  void _set_mCellIdentityGsm(final Object value);

  @BFieldCheckNotProcess
  Field _check_mCellIdentityGsm();

  @BFieldNotProcess
  CellIdentityGsm mCellIdentityGsm();

  @BFieldSetNotProcess
  void _set_mCellSignalStrengthGsm(final Object value);

  @BFieldCheckNotProcess
  Field _check_mCellSignalStrengthGsm();

  @BFieldNotProcess
  CellSignalStrengthGsm mCellSignalStrengthGsm();
}
