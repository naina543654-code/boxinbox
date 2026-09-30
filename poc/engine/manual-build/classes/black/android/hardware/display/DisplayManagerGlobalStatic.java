package black.android.hardware.display;

import java.lang.Object;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.hardware.display.DisplayManagerGlobal")
public interface DisplayManagerGlobalStatic {
  @BMethodCheckNotProcess
  Method _check_getInstance();

  Object getInstance();
}
