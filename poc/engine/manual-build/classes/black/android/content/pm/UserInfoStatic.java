package black.android.content.pm;

import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BConstructorNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.content.pm.UserInfo")
public interface UserInfoStatic {
  @BFieldSetNotProcess
  void _set_FLAG_PRIMARY(final Object value);

  @BFieldCheckNotProcess
  Field _check_FLAG_PRIMARY();

  @BFieldNotProcess
  Integer FLAG_PRIMARY();

  @BConstructorNotProcess
  Object _new(int id, String name, int flags);
}
