package black.android.app;

import android.content.ComponentName;
import android.os.IBinder;
import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.IServiceConnection")
public interface IServiceConnectionOContext {
  @BMethodCheckNotProcess
  Method _check_connected(ComponentName ComponentName0, IBinder IBinder1, boolean boolean2);

  Void connected(ComponentName ComponentName0, IBinder IBinder1, boolean boolean2);
}
