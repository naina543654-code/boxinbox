package black.android.net;

import android.net.NetworkInfo;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.net.NetworkInfo")
public interface NetworkInfoContext {
  @BFieldSetNotProcess
  void _set_mDetailedState(final Object value);

  @BFieldCheckNotProcess
  Field _check_mDetailedState();

  @BFieldNotProcess
  NetworkInfo.DetailedState mDetailedState();

  @BFieldSetNotProcess
  void _set_mIsAvailable(final Object value);

  @BFieldCheckNotProcess
  Field _check_mIsAvailable();

  @BFieldNotProcess
  Boolean mIsAvailable();

  @BFieldSetNotProcess
  void _set_mNetworkType(final Object value);

  @BFieldCheckNotProcess
  Field _check_mNetworkType();

  @BFieldNotProcess
  Integer mNetworkType();

  @BFieldSetNotProcess
  void _set_mState(final Object value);

  @BFieldCheckNotProcess
  Field _check_mState();

  @BFieldNotProcess
  NetworkInfo.State mState();

  @BFieldSetNotProcess
  void _set_mTypeName(final Object value);

  @BFieldCheckNotProcess
  Field _check_mTypeName();

  @BFieldNotProcess
  String mTypeName();
}
