package black.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BParamClassName;

@BClassNameNotProcess("android.app.IActivityManager")
public interface IActivityManagerContext {
  @BMethodCheckNotProcess
  Method _check_getTaskForActivity(IBinder IBinder0, boolean boolean1);

  Integer getTaskForActivity(IBinder IBinder0, boolean boolean1);

  @BMethodCheckNotProcess
  Method _check_overridePendingTransition(IBinder IBinder0, String String1, int int2, int int3);

  Void overridePendingTransition(IBinder IBinder0, String String1, int int2, int int3);

  @BMethodCheckNotProcess
  Method _check_setRequestedOrientation(IBinder IBinder0, int int1);

  Void setRequestedOrientation(IBinder IBinder0, int int1);

  @BMethodCheckNotProcess
  Method _check_startActivities();

  Integer startActivities();

  @BMethodCheckNotProcess
  Method _check_startActivity(@BParamClassName("android.app.IApplicationThread") Object caller,
      String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho,
      int requestCode, int startFlags,
      @BParamClassName("android.app.ProfilerInfo") Object profilerInfo, Bundle bOptions);

  Integer startActivity(@BParamClassName("android.app.IApplicationThread") Object caller,
      String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho,
      int requestCode, int startFlags,
      @BParamClassName("android.app.ProfilerInfo") Object profilerInfo, Bundle bOptions);
}
