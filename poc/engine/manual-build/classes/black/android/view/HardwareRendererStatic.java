package black.android.view;

import java.io.File;
import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.view.HardwareRenderer")
public interface HardwareRendererStatic {
  @BMethodCheckNotProcess
  Method _check_setupDiskCache(File File0);

  Void setupDiskCache(File File0);
}
