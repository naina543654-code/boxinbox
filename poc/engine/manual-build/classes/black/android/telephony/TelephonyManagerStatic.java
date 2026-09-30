package black.android.telephony;

import android.os.IInterface;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.telephony.TelephonyManager")
public interface TelephonyManagerStatic {
  @BMethodCheckNotProcess
  Method _check_getSubscriberInfoService();

  Object getSubscriberInfoService();

  @BFieldSetNotProcess
  void _set_sServiceHandleCacheEnabled(final Object value);

  @BFieldCheckNotProcess
  Field _check_sServiceHandleCacheEnabled();

  @BFieldNotProcess
  Boolean sServiceHandleCacheEnabled();

  @BFieldSetNotProcess
  void _set_sIPhoneSubInfo(final Object value);

  @BFieldCheckNotProcess
  Field _check_sIPhoneSubInfo();

  @BFieldNotProcess
  IInterface sIPhoneSubInfo();
}
