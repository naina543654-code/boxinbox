package black.android.providers;

import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.provider.Settings$System")
public interface SettingsSystemStatic {
  @BFieldSetNotProcess
  void _set_sNameValueCache(final Object value);

  @BFieldCheckNotProcess
  Field _check_sNameValueCache();

  @BFieldNotProcess
  Object sNameValueCache();
}
