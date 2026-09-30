package black.android.app;

import android.content.Intent;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BConstructorNotProcess;

@BClassNameNotProcess("android.app.ServiceStartArgs")
public interface ServiceStartArgsStatic {
  @BConstructorNotProcess
  ServiceStartArgs _new(boolean boolean0, int int1, int int2, Intent Intent3);
}
