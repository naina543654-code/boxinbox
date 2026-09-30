package black.android.location;

import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.location.LocationManager$GnssStatusListenerTransport")
public interface LocationManagerGnssStatusListenerTransportContext {
  @BMethodCheckNotProcess
  Method _check_onFirstFix(int int0);

  Void onFirstFix(int int0);

  @BMethodCheckNotProcess
  Method _check_onGnssStarted();

  Void onGnssStarted();

  @BMethodCheckNotProcess
  Method _check_onNmeaReceived(long long0, String String1);

  Void onNmeaReceived(long long0, String String1);

  @BFieldSetNotProcess
  void _set_mGpsListener(final Object value);

  @BFieldCheckNotProcess
  Field _check_mGpsListener();

  @BFieldNotProcess
  Object mGpsListener();

  @BFieldSetNotProcess
  void _set_mGpsNmeaListener(final Object value);

  @BFieldCheckNotProcess
  Field _check_mGpsNmeaListener();

  @BFieldNotProcess
  Object mGpsNmeaListener();

  @BFieldSetNotProcess
  void _set_this$0(final Object value);

  @BFieldCheckNotProcess
  Field _check_this$0();

  @BFieldNotProcess
  Object this$0();
}
