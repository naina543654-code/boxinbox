package black.android.app;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.Instrumentation")
public interface InstrumentationContext {
  @BMethodCheckNotProcess
  Method _check_execStartActivity(Context Context0, IBinder IBinder1, IBinder IBinder2,
      Activity Activity3, Intent Intent4, int int5, Bundle Bundle6);

  Instrumentation.ActivityResult execStartActivity(Context Context0, IBinder IBinder1,
      IBinder IBinder2, Activity Activity3, Intent Intent4, int int5, Bundle Bundle6);
}
