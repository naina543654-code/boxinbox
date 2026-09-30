package black.android.app;

import android.content.Intent;
import android.os.IBinder;
import java.lang.Boolean;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.IActivityManager")
public interface IActivityManagerLContext {
  @BMethodCheckNotProcess
  Method _check_finishActivity(IBinder IBinder0, int int1, Intent Intent2, boolean boolean3);

  Boolean finishActivity(IBinder IBinder0, int int1, Intent Intent2, boolean boolean3);
}
