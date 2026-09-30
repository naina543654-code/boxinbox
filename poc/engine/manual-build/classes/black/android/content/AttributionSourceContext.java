package black.android.content;

import java.lang.Object;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.content.AttributionSource")
public interface AttributionSourceContext {
  @BMethodCheckNotProcess
  Method _check_getNext();

  Object getNext();

  @BFieldSetNotProcess
  void _set_mAttributionSourceState(final Object value);

  @BFieldCheckNotProcess
  Field _check_mAttributionSourceState();

  @BFieldNotProcess
  Object mAttributionSourceState();
}
