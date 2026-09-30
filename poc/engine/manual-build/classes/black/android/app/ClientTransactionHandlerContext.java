package black.android.app;

import android.os.IBinder;
import java.lang.Object;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.ClientTransactionHandler")
public interface ClientTransactionHandlerContext {
  @BMethodCheckNotProcess
  Method _check_getActivityClient(IBinder IBinder0);

  Object getActivityClient(IBinder IBinder0);
}
