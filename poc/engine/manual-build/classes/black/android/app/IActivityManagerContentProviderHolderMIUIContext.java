package black.android.app;

import android.content.pm.ProviderInfo;
import android.os.IInterface;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.IActivityManager$ContentProviderHolder")
public interface IActivityManagerContentProviderHolderMIUIContext {
  @BFieldSetNotProcess
  void _set_info(final Object value);

  @BFieldCheckNotProcess
  Field _check_info();

  @BFieldNotProcess
  ProviderInfo info();

  @BFieldSetNotProcess
  void _set_noReleaseNeeded(final Object value);

  @BFieldCheckNotProcess
  Field _check_noReleaseNeeded();

  @BFieldNotProcess
  Boolean noReleaseNeeded();

  @BFieldSetNotProcess
  void _set_provider(final Object value);

  @BFieldCheckNotProcess
  Field _check_provider();

  @BFieldNotProcess
  IInterface provider();

  @BFieldSetNotProcess
  void _set_waitProcessStart(final Object value);

  @BFieldCheckNotProcess
  Field _check_waitProcessStart();

  @BFieldNotProcess
  Boolean waitProcessStart();
}
