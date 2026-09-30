package black.android.net.wifi;

import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.net.wifi.WifiSsid")
public interface WifiSsidStatic {
  @BMethodCheckNotProcess
  Method _check_createFromAsciiEncoded(String asciiEncoded);

  Object createFromAsciiEncoded(String asciiEncoded);
}
