package black.android.telephony;

import java.lang.Integer;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.telephony.CellSignalStrengthCdma")
public interface CellSignalStrengthCdmaContext {
  @BFieldSetNotProcess
  void _set_mCdmaDbm(final Object value);

  @BFieldCheckNotProcess
  Field _check_mCdmaDbm();

  @BFieldNotProcess
  Integer mCdmaDbm();

  @BFieldSetNotProcess
  void _set_mCdmaEcio(final Object value);

  @BFieldCheckNotProcess
  Field _check_mCdmaEcio();

  @BFieldNotProcess
  Integer mCdmaEcio();

  @BFieldSetNotProcess
  void _set_mEvdoDbm(final Object value);

  @BFieldCheckNotProcess
  Field _check_mEvdoDbm();

  @BFieldNotProcess
  Integer mEvdoDbm();

  @BFieldSetNotProcess
  void _set_mEvdoEcio(final Object value);

  @BFieldCheckNotProcess
  Field _check_mEvdoEcio();

  @BFieldNotProcess
  Integer mEvdoEcio();

  @BFieldSetNotProcess
  void _set_mEvdoSnr(final Object value);

  @BFieldCheckNotProcess
  Field _check_mEvdoSnr();

  @BFieldNotProcess
  Integer mEvdoSnr();
}
