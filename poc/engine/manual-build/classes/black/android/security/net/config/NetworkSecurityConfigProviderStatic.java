package black.android.security.net.config;

import android.content.Context;
import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.security.net.config.NetworkSecurityConfigProvider")
public interface NetworkSecurityConfigProviderStatic {
  @BMethodCheckNotProcess
  Method _check_install(Context Context0);

  Void install(Context Context0);
}
