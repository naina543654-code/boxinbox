package black.android.content.pm;

import android.content.ComponentName;
import android.content.IntentFilter;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import java.util.List;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.content.pm.PackageParser$Component")
public interface PackageParserComponentContext {
  @BFieldSetNotProcess
  void _set_className(final Object value);

  @BFieldCheckNotProcess
  Field _check_className();

  @BFieldNotProcess
  String className();

  @BFieldSetNotProcess
  void _set_componentName(final Object value);

  @BFieldCheckNotProcess
  Field _check_componentName();

  @BFieldNotProcess
  ComponentName componentName();

  @BFieldSetNotProcess
  void _set_intents(final Object value);

  @BFieldCheckNotProcess
  Field _check_intents();

  @BFieldNotProcess
  List<IntentFilter> intents();
}
