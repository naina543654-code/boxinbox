package black.android.content.pm;

import android.content.pm.PackageParser;
import android.util.DisplayMetrics;
import java.io.File;
import java.lang.String;
import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.content.pm.PackageParser")
public interface PackageParserContext {
  @BMethodCheckNotProcess
  Method _check_collectCertificates(PackageParser.Package p, int flags);

  Void collectCertificates(PackageParser.Package p, int flags);

  @BMethodCheckNotProcess
  Method _check_parsePackage(File File0, String String1, DisplayMetrics DisplayMetrics2, int int3);

  PackageParser.Package parsePackage(File File0, String String1, DisplayMetrics DisplayMetrics2,
      int int3);
}
