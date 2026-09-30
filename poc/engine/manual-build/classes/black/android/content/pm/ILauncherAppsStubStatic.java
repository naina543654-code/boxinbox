package black.android.content.pm;

import android.os.IBinder;
import android.os.IInterface;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.content.pm.ILauncherApps$Stub")
public interface ILauncherAppsStubStatic {
  @BMethodCheckNotProcess
  Method _check_asInterface(IBinder binder);

  IInterface asInterface(IBinder binder);
}
