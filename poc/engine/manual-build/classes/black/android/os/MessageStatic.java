package black.android.os;

import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.os.Message")
public interface MessageStatic {
  @BMethodCheckNotProcess
  Method _check_updateCheckRecycle(int int0);

  Void updateCheckRecycle(int int0);
}
