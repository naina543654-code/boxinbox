package black.android.app;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.IBinder;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BParamClassName;

@BClassNameNotProcess("android.app.ActivityThread")
public interface ActivityThreadContext {
  @BMethodCheckNotProcess
  Method _check_getApplicationThread();

  IBinder getApplicationThread();

  @BMethodCheckNotProcess
  Method _check_getHandler();

  Handler getHandler();

  @BMethodCheckNotProcess
  Method _check_getProcessName();

  String getProcessName();

  @BMethodCheckNotProcess
  Method _check_getSystemContext();

  Object getSystemContext();

  @BMethodCheckNotProcess
  Method _check_getActivityClient(IBinder token);

  Object getActivityClient(IBinder token);

  @BMethodCheckNotProcess
  Method _check_getLaunchingActivity(IBinder token);

  Object getLaunchingActivity(IBinder token);

  @BMethodCheckNotProcess
  Method _check_getPackageInfo(ApplicationInfo ai,
      @BParamClassName("android.content.res.CompatibilityInfo") Object compatInfo, int flags);

  Object getPackageInfo(ApplicationInfo ai,
      @BParamClassName("android.content.res.CompatibilityInfo") Object compatInfo, int flags);

  @BMethodCheckNotProcess
  Method _check_performNewIntents(IBinder IBinder0, List List1);

  Void performNewIntents(IBinder IBinder0, List List1);

  @BMethodCheckNotProcess
  Method _check_sendActivityResult(IBinder IBinder0, String String1, int int2, int int3,
      Intent Intent4);

  Void sendActivityResult(IBinder IBinder0, String String1, int int2, int int3, Intent Intent4);

  @BFieldSetNotProcess
  void _set_mAppThread(final Object value);

  @BFieldCheckNotProcess
  Field _check_mAppThread();

  @BFieldNotProcess
  Object mAppThread();

  @BFieldSetNotProcess
  void _set_mActivities(final Object value);

  @BFieldCheckNotProcess
  Field _check_mActivities();

  @BFieldNotProcess
  Map<IBinder, Object> mActivities();

  @BFieldSetNotProcess
  void _set_mBoundApplication(final Object value);

  @BFieldCheckNotProcess
  Field _check_mBoundApplication();

  @BFieldNotProcess
  Object mBoundApplication();

  @BFieldSetNotProcess
  void _set_mH(final Object value);

  @BFieldCheckNotProcess
  Field _check_mH();

  @BFieldNotProcess
  Handler mH();

  @BFieldSetNotProcess
  void _set_mInitialApplication(final Object value);

  @BFieldCheckNotProcess
  Field _check_mInitialApplication();

  @BFieldNotProcess
  Application mInitialApplication();

  @BFieldSetNotProcess
  void _set_mInstrumentation(final Object value);

  @BFieldCheckNotProcess
  Field _check_mInstrumentation();

  @BFieldNotProcess
  Instrumentation mInstrumentation();

  @BFieldSetNotProcess
  void _set_mPackages(final Object value);

  @BFieldCheckNotProcess
  Field _check_mPackages();

  @BFieldNotProcess
  Map<String, WeakReference<?>> mPackages();

  @BFieldSetNotProcess
  void _set_mProviderMap(final Object value);

  @BFieldCheckNotProcess
  Field _check_mProviderMap();

  @BFieldNotProcess
  Map<?, ?> mProviderMap();

  @BFieldSetNotProcess
  void _set_mLocalProvidersByName(final Object value);

  @BFieldCheckNotProcess
  Field _check_mLocalProvidersByName();

  @BFieldNotProcess
  Map<?, ?> mLocalProvidersByName();
}
