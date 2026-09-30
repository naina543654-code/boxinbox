package black.android.content.pm;

import android.content.pm.PackageParser;
import android.content.pm.SigningInfo;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BConstructorNotProcess;

@BClassNameNotProcess("android.content.pm.SigningInfo")
public interface SigningInfoStatic {
  @BConstructorNotProcess
  SigningInfo _new(PackageParser.SigningDetails SigningDetails0);
}
