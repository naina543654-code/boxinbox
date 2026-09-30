package black.android.graphics;

import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.graphics.Compatibility")
public interface CompatibilityStatic {
  @BMethodCheckNotProcess
  Method _check_setTargetSdkVersion(int targetSdkVersion);

  Void setTargetSdkVersion(int targetSdkVersion);
}
