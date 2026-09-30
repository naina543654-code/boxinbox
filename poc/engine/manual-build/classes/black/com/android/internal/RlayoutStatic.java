package black.com.android.internal;

import java.lang.Integer;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("com.android.internal.R$layout")
public interface RlayoutStatic {
  @BFieldSetNotProcess
  void _set_resolver_list(final Object value);

  @BFieldCheckNotProcess
  Field _check_resolver_list();

  @BFieldNotProcess
  Integer resolver_list();
}
