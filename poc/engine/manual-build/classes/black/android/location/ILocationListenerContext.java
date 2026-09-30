package black.android.location;

import android.location.Location;
import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.location.ILocationListener")
public interface ILocationListenerContext {
  @BMethodCheckNotProcess
  Method _check_onLocationChanged(Location Location0);

  Void onLocationChanged(Location Location0);
}
