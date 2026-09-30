package black.android.net;

import java.lang.String;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BConstructorNotProcess;

@BClassNameNotProcess("android.net.NetworkInfo")
public interface NetworkInfoStatic {
  @BConstructorNotProcess
  NetworkInfo _new(int int0, int int1, String String2, String String3);

  @BConstructorNotProcess
  NetworkInfo _new(int int0);
}
