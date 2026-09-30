package black.android.app;

import android.os.IBinder;
import java.lang.Void;
import java.lang.reflect.Method;
import java.util.List;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.ActivityThread")
public interface ActivityThreadQContext {
  @BMethodCheckNotProcess
  Method _check_handleNewIntent(IBinder IBinder0, List List1);

  Void handleNewIntent(IBinder IBinder0, List List1);
}
