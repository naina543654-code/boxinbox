package black.android.net.wifi;

import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.net.wifi.WifiScanner")
public interface WifiScannerStatic {
  @BFieldSetNotProcess
  void _set_GET_AVAILABLE_CHANNELS_EXTRA(final Object value);

  @BFieldCheckNotProcess
  Field _check_GET_AVAILABLE_CHANNELS_EXTRA();

  @BFieldNotProcess
  String GET_AVAILABLE_CHANNELS_EXTRA();
}
