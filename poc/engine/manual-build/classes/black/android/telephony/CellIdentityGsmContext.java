package black.android.telephony;

import java.lang.Integer;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.telephony.CellIdentityGsm")
public interface CellIdentityGsmContext {
  @BFieldSetNotProcess
  void _set_mCid(final Object value);

  @BFieldCheckNotProcess
  Field _check_mCid();

  @BFieldNotProcess
  Integer mCid();

  @BFieldSetNotProcess
  void _set_mLac(final Object value);

  @BFieldCheckNotProcess
  Field _check_mLac();

  @BFieldNotProcess
  Integer mLac();

  @BFieldSetNotProcess
  void _set_mMcc(final Object value);

  @BFieldCheckNotProcess
  Field _check_mMcc();

  @BFieldNotProcess
  Integer mMcc();

  @BFieldSetNotProcess
  void _set_mMnc(final Object value);

  @BFieldCheckNotProcess
  Field _check_mMnc();

  @BFieldNotProcess
  Integer mMnc();
}
