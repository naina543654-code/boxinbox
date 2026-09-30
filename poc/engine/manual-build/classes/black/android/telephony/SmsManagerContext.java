package black.android.telephony;

import java.lang.Boolean;
import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.telephony.SmsManager")
public interface SmsManagerContext {
  @BMethodCheckNotProcess
  Method _check_getAutoPersisting();

  Boolean getAutoPersisting();

  @BMethodCheckNotProcess
  Method _check_setAutoPersisting(boolean boolean0);

  Void setAutoPersisting(boolean boolean0);
}
