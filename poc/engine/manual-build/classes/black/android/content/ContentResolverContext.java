package black.android.content;

import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.content.ContentResolver")
public interface ContentResolverContext {
  @BFieldSetNotProcess
  void _set_mPackageName(final Object value);

  @BFieldCheckNotProcess
  Field _check_mPackageName();

  @BFieldNotProcess
  String mPackageName();
}
