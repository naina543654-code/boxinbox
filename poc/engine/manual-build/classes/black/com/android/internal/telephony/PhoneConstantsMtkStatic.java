package black.com.android.internal.telephony;

import java.lang.Integer;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("com.android.internal.telephony.PhoneConstants")
public interface PhoneConstantsMtkStatic {
  @BFieldSetNotProcess
  void _set_GEMINI_SIM_NUM(final Object value);

  @BFieldCheckNotProcess
  Field _check_GEMINI_SIM_NUM();

  @BFieldNotProcess
  Integer GEMINI_SIM_NUM();
}
