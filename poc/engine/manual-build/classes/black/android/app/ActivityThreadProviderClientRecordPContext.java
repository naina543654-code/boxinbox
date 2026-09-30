package black.android.app;

import android.os.IInterface;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.ActivityThread$ProviderClientRecord")
public interface ActivityThreadProviderClientRecordPContext {
  @BFieldSetNotProcess
  void _set_mNames(final Object value);

  @BFieldCheckNotProcess
  Field _check_mNames();

  @BFieldNotProcess
  String[] mNames();

  @BFieldSetNotProcess
  void _set_mProvider(final Object value);

  @BFieldCheckNotProcess
  Field _check_mProvider();

  @BFieldNotProcess
  IInterface mProvider();
}
