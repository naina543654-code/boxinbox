package black.android.location;

import android.os.IInterface;
import java.lang.Object;
import java.lang.reflect.Field;
import java.util.HashMap;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.location.LocationManager")
public interface LocationManagerContext {
  @BFieldSetNotProcess
  void _set_mGnssNmeaListeners(final Object value);

  @BFieldCheckNotProcess
  Field _check_mGnssNmeaListeners();

  @BFieldNotProcess
  HashMap mGnssNmeaListeners();

  @BFieldSetNotProcess
  void _set_mGnssStatusListeners(final Object value);

  @BFieldCheckNotProcess
  Field _check_mGnssStatusListeners();

  @BFieldNotProcess
  HashMap mGnssStatusListeners();

  @BFieldSetNotProcess
  void _set_mGpsNmeaListeners(final Object value);

  @BFieldCheckNotProcess
  Field _check_mGpsNmeaListeners();

  @BFieldNotProcess
  HashMap mGpsNmeaListeners();

  @BFieldSetNotProcess
  void _set_mGpsStatusListeners(final Object value);

  @BFieldCheckNotProcess
  Field _check_mGpsStatusListeners();

  @BFieldNotProcess
  HashMap mGpsStatusListeners();

  @BFieldSetNotProcess
  void _set_mListeners(final Object value);

  @BFieldCheckNotProcess
  Field _check_mListeners();

  @BFieldNotProcess
  HashMap mListeners();

  @BFieldSetNotProcess
  void _set_mNmeaListeners(final Object value);

  @BFieldCheckNotProcess
  Field _check_mNmeaListeners();

  @BFieldNotProcess
  HashMap mNmeaListeners();

  @BFieldSetNotProcess
  void _set_mService(final Object value);

  @BFieldCheckNotProcess
  Field _check_mService();

  @BFieldNotProcess
  IInterface mService();
}
