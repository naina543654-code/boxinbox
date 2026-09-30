package black.android.util;

import java.lang.Object;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.util.Singleton")
public interface SingletonContext {
  @BMethodCheckNotProcess
  Method _check_get();

  Object get();

  @BFieldSetNotProcess
  void _set_mInstance(final Object value);

  @BFieldCheckNotProcess
  Field _check_mInstance();

  @BFieldNotProcess
  Object mInstance();
}
