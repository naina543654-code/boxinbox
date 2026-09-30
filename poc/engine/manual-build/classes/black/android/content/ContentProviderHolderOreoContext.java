package black.android.content;

import android.content.pm.ProviderInfo;
import android.os.IInterface;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.ContentProviderHolder")
public interface ContentProviderHolderOreoContext {
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
}
