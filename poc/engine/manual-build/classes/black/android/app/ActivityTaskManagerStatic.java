package black.android.app;

import java.lang.Object;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.ActivityTaskManager")
public interface ActivityTaskManagerStatic {
  @BMethodCheckNotProcess
  Method _check_getService();

  Object getService();

  @BFieldSetNotProcess
  void _set_IActivityTaskManagerSingleton(final Object value);

  @BFieldCheckNotProcess
  Field _check_IActivityTaskManagerSingleton();

  @BFieldNotProcess
  Object IActivityTaskManagerSingleton();
}
