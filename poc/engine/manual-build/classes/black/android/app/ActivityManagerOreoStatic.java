package black.android.app;

import android.os.IInterface;
import java.lang.Object;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.ActivityManager")
public interface ActivityManagerOreoStatic {
  @BMethodCheckNotProcess
  Method _check_getService();

  IInterface getService();

  @BFieldSetNotProcess
  void _set_IActivityManagerSingleton(final Object value);

  @BFieldCheckNotProcess
  Field _check_IActivityManagerSingleton();

  @BFieldNotProcess
  Object IActivityManagerSingleton();
}
