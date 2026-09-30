package black.android.view;

import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.view.CompatibilityInfoHolder")
public interface CompatibilityInfoHolderContext {
  @BMethodCheckNotProcess
  Method _check_set();

  Void set();
}
