package black.android.app.servertransaction;

import java.lang.Integer;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.servertransaction.ActivityLifecycleItem")
public interface ActivityLifecycleItemContext {
  @BMethodCheckNotProcess
  Method _check_getTargetState();

  Integer getTargetState();
}
