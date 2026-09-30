package black.android.content;

import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.content.AttributionSourceState")
public interface AttributionSourceStateContext {
  @BFieldSetNotProcess
  void _set_packageName(final Object value);

  @BFieldCheckNotProcess
  Field _check_packageName();

  @BFieldNotProcess
  String packageName();

  @BFieldSetNotProcess
  void _set_uid(final Object value);

  @BFieldCheckNotProcess
  Field _check_uid();

  @BFieldNotProcess
  Integer uid();
}
