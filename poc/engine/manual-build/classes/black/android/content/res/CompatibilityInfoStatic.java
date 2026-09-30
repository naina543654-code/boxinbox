package black.android.content.res;

import android.content.pm.ApplicationInfo;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BConstructorNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.content.res.CompatibilityInfo")
public interface CompatibilityInfoStatic {
  @BFieldSetNotProcess
  void _set_DEFAULT_COMPATIBILITY_INFO(final Object value);

  @BFieldCheckNotProcess
  Field _check_DEFAULT_COMPATIBILITY_INFO();

  @BFieldNotProcess
  Object DEFAULT_COMPATIBILITY_INFO();

  @BConstructorNotProcess
  CompatibilityInfo _new(ApplicationInfo ApplicationInfo0, int int1, int int2, boolean boolean3);

  @BConstructorNotProcess
  CompatibilityInfo _new(ApplicationInfo ApplicationInfo0, int int1, int int2, boolean boolean3,
      int int4);
}
