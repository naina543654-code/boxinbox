package black.android.webkit;

import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.webkit.IWebViewUpdateService")
public interface IWebViewUpdateServiceContext {
  @BMethodCheckNotProcess
  Method _check_getCurrentWebViewPackageName();

  String getCurrentWebViewPackageName();

  @BMethodCheckNotProcess
  Method _check_waitForAndGetProvider();

  Object waitForAndGetProvider();
}
