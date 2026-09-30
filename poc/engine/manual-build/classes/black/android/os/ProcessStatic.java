package black.android.os;

import java.lang.String;
import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.os.Process")
public interface ProcessStatic {
  @BMethodCheckNotProcess
  Method _check_setArgV0(String String0);

  Void setArgV0(String String0);
}
