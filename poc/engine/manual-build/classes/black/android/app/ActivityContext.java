package black.android.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import java.lang.Boolean;
import java.lang.Integer;
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

@BClassNameNotProcess("android.app.Activity")
public interface ActivityContext {
  @BMethodCheckNotProcess
  Method _check_onActivityResult(int int0, int int1, Intent Intent2);

  Void onActivityResult(int int0, int int1, Intent Intent2);

  @BFieldSetNotProcess
  void _set_mActivityInfo(final Object value);

  @BFieldCheckNotProcess
  Field _check_mActivityInfo();

  @BFieldNotProcess
  ActivityInfo mActivityInfo();

  @BFieldSetNotProcess
  void _set_mEmbeddedID(final Object value);

  @BFieldCheckNotProcess
  Field _check_mEmbeddedID();

  @BFieldNotProcess
  String mEmbeddedID();

  @BFieldSetNotProcess
  void _set_mFinished(final Object value);

  @BFieldCheckNotProcess
  Field _check_mFinished();

  @BFieldNotProcess
  Boolean mFinished();

  @BFieldSetNotProcess
  void _set_mParent(final Object value);

  @BFieldCheckNotProcess
  Field _check_mParent();

  @BFieldNotProcess
  Activity mParent();

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
  Intent mResultData();

  @BFieldSetNotProcess
  void _set_mToken(final Object value);

  @BFieldCheckNotProcess
  Field _check_mToken();

  @BFieldNotProcess
  IBinder mToken();
}
