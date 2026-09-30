package black.android.content;

import android.os.Bundle;
import android.os.IBinder;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.content.BroadcastReceiver$PendingResult")
public interface BroadcastReceiverPendingResultMContext {
  @BFieldSetNotProcess
  void _set_mAbortBroadcast(final Object value);

  @BFieldCheckNotProcess
  Field _check_mAbortBroadcast();

  @BFieldNotProcess
  Boolean mAbortBroadcast();

  @BFieldSetNotProcess
  void _set_mFinished(final Object value);

  @BFieldCheckNotProcess
  Field _check_mFinished();

  @BFieldNotProcess
  Boolean mFinished();

  @BFieldSetNotProcess
  void _set_mFlags(final Object value);

  @BFieldCheckNotProcess
  Field _check_mFlags();

  @BFieldNotProcess
  Integer mFlags();

  @BFieldSetNotProcess
  void _set_mInitialStickyHint(final Object value);

  @BFieldCheckNotProcess
  Field _check_mInitialStickyHint();

  @BFieldNotProcess
  Boolean mInitialStickyHint();

  @BFieldSetNotProcess
  void _set_mOrderedHint(final Object value);

  @BFieldCheckNotProcess
  Field _check_mOrderedHint();

  @BFieldNotProcess
  Boolean mOrderedHint();

  @BFieldSetNotProcess
  void _set_mResultCode(final Object value);

  @BFieldCheckNotProcess
  Field _check_mResultCode();

  @BFieldNotProcess
  Integer mResultCode();

  @BFieldSetNotProcess
  void _set_mResultData(final Object value);

  @BFieldCheckNotProcess
  Field _check_mResultData();

  @BFieldNotProcess
  String mResultData();

  @BFieldSetNotProcess
  void _set_mResultExtras(final Object value);

  @BFieldCheckNotProcess
  Field _check_mResultExtras();

  @BFieldNotProcess
  Bundle mResultExtras();

  @BFieldSetNotProcess
  void _set_mSendingUser(final Object value);

  @BFieldCheckNotProcess
  Field _check_mSendingUser();

  @BFieldNotProcess
  Integer mSendingUser();

  @BFieldSetNotProcess
  void _set_mToken(final Object value);

  @BFieldCheckNotProcess
  Field _check_mToken();

  @BFieldNotProcess
  IBinder mToken();

  @BFieldSetNotProcess
  void _set_mType(final Object value);

  @BFieldCheckNotProcess
  Field _check_mType();

  @BFieldNotProcess
  Integer mType();
}
