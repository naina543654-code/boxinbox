package black.android.app;

import java.lang.Object;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.ActivityClient")
public interface ActivityClientStatic {
  @BMethodCheckNotProcess
  Method _check_getInstance();

  Object getInstance();

  @BMethodCheckNotProcess
  Method _check_getActivityClientController();

  Object getActivityClientController();

  @BMethodCheckNotProcess
  Method _check_setActivityClientController(Object iInterface);

  Object setActivityClientController(Object iInterface);
}
