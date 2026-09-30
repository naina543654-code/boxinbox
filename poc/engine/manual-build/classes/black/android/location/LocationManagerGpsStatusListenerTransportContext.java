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

@BClassNameNotProcess("android.location.LocationManager$GpsStatusListenerTransport")
public interface LocationManagerGpsStatusListenerTransportContext {
  @BMethodCheckNotProcess
  Method _check_onFirstFix(int int0);

  Void onFirstFix(int int0);

  @BMethodCheckNotProcess
  Method _check_onGpsStarted();

  Void onGpsStarted();

  @BMethodCheckNotProcess
  Method _check_onNmeaReceived(long long0, String String1);

  Void onNmeaReceived(long long0, String String1);

  @BFieldSetNotProcess
  void _set_mListener(final Object value);

  @BFieldCheckNotProcess
  Field _check_mListener();

  @BFieldNotProcess
  Object mListener();

  @BFieldSetNotProcess
  void _set_mNmeaListener(final Object value);

  @BFieldCheckNotProcess
  Field _check_mNmeaListener();

  @BFieldNotProcess
  Object mNmeaListener();

  @BFieldSetNotProcess
  void _set_this$0(final Object value);

  @BFieldCheckNotProcess
  Field _check_this$0();

  @BFieldNotProcess
  Object this$0();
}
