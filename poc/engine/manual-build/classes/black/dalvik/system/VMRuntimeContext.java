package black.dalvik.system;

import java.lang.Boolean;
import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("dalvik.system.VMRuntime")
public interface VMRuntimeContext {
  @BMethodCheckNotProcess
  Method _check_is64Bit();

  Boolean is64Bit();

  @BMethodCheckNotProcess
  Method _check_isJavaDebuggable();

  Boolean isJavaDebuggable();

  @BMethodCheckNotProcess
  Method _check_setTargetSdkVersion(int int0);

  Void setTargetSdkVersion(int int0);
}
