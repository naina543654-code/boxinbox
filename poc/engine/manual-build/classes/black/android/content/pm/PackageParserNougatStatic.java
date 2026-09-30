package black.android.content.pm;

import android.content.pm.PackageParser;
import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.content.pm.PackageParser")
public interface PackageParserNougatStatic {
  @BMethodCheckNotProcess
  Method _check_collectCertificates(PackageParser.Package p, int flags);

  Void collectCertificates(PackageParser.Package p, int flags);
}
