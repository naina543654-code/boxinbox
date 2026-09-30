package black.android.webkit;

import java.lang.Boolean;
import java.lang.Object;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.webkit.WebViewFactory")
public interface WebViewFactoryStatic {
  @BMethodCheckNotProcess
  Method _check_getUpdateService();

  Object getUpdateService();

  @BFieldSetNotProcess
  void _set_sWebViewSupported(final Object value);

  @BFieldCheckNotProcess
  Field _check_sWebViewSupported();

  @BFieldNotProcess
  Boolean sWebViewSupported();
}
