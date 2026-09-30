package black.android.content.pm;

import android.content.pm.PackageParser;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.content.pm.SigningInfo")
public interface SigningInfoContext {
  @BFieldSetNotProcess
  void _set_mSigningDetails(final Object value);

  @BFieldCheckNotProcess
  Field _check_mSigningDetails();

  @BFieldNotProcess
  PackageParser.SigningDetails mSigningDetails();
}
