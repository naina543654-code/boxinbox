package black.android.hardware.location;

import android.os.IBinder;
import android.os.IInterface;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.hardware.location.IContextHubService$Stub")
public interface IContextHubServiceStubStatic {
  @BMethodCheckNotProcess
  Method _check_asInterface(IBinder iBinder);

  IInterface asInterface(IBinder iBinder);
}
