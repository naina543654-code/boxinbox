package black.android.content.pm;

import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.content.pm.ApplicationInfo")
public interface ApplicationInfoPContext {
  @BMethodCheckNotProcess
  Method _check_setHiddenApiEnforcementPolicy(int int0);

  Void setHiddenApiEnforcementPolicy(int int0);

  @BFieldSetNotProcess
  void _set_splitClassLoaderNames(final Object value);

  @BFieldCheckNotProcess
  Field _check_splitClassLoaderNames();

  @BFieldNotProcess
  String[] splitClassLoaderNames();
}
