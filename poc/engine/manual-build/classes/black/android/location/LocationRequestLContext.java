package black.android.location;

import java.lang.Boolean;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.location.LocationRequest")
public interface LocationRequestLContext {
  @BMethodCheckNotProcess
  Method _check_getProvider();

  String getProvider();

  @BFieldSetNotProcess
  void _set_mHideFromAppOps(final Object value);

  @BFieldCheckNotProcess
  Field _check_mHideFromAppOps();

  @BFieldNotProcess
  Boolean mHideFromAppOps();

  @BFieldSetNotProcess
  void _set_mProvider(final Object value);

  @BFieldCheckNotProcess
  Field _check_mProvider();

  @BFieldNotProcess
  String mProvider();

  @BFieldSetNotProcess
  void _set_mWorkSource(final Object value);

  @BFieldCheckNotProcess
  Field _check_mWorkSource();

  @BFieldNotProcess
  Object mWorkSource();
}
