package black.android.app;

import android.os.IBinder;
import android.os.IInterface;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.usage.IUsageStatsManager$Stub")
public interface IUsageStatsManagerStubStatic {
  @BMethodCheckNotProcess
  Method _check_asInterface(IBinder IBinder0);

  IInterface asInterface(IBinder IBinder0);
}
