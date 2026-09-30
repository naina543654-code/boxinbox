package black.android.app;

import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.ActivityClient")
public interface ActivityClientContext {
  @BFieldSetNotProcess
  void _set_INTERFACE_SINGLETON(final Object value);

  @BFieldCheckNotProcess
  Field _check_INTERFACE_SINGLETON();

  @BFieldNotProcess
  Object INTERFACE_SINGLETON();
}
