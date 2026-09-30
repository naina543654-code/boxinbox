package black.android.app;

import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.IApplicationThread")
public interface IApplicationThreadOreoContext {
  @BMethodCheckNotProcess
  Method _check_scheduleServiceArgs();

  Void scheduleServiceArgs();
}
