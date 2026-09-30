package black.android.telephony;

import java.lang.Integer;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.telephony.CellIdentityCdma")
public interface CellIdentityCdmaContext {
  @BFieldSetNotProcess
  void _set_mBasestationId(final Object value);

  @BFieldCheckNotProcess
  Field _check_mBasestationId();

  @BFieldNotProcess
  Integer mBasestationId();

  @BFieldSetNotProcess
  void _set_mNetworkId(final Object value);

  @BFieldCheckNotProcess
  Field _check_mNetworkId();

  @BFieldNotProcess
  Integer mNetworkId();

  @BFieldSetNotProcess
  void _set_mSystemId(final Object value);

  @BFieldCheckNotProcess
  Field _check_mSystemId();

  @BFieldNotProcess
  Integer mSystemId();
}
