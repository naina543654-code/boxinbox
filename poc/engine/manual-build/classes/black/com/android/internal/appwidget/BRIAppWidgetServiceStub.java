package black.com.android.internal.appwidget;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIAppWidgetServiceStub {
  public static IAppWidgetServiceStubStatic getWithException() {
    return BlackReflection.create(IAppWidgetServiceStubStatic.class, null, true);
  }

  public static IAppWidgetServiceStubStatic get() {
    return BlackReflection.create(IAppWidgetServiceStubStatic.class, null, false);
  }

  public static IAppWidgetServiceStubContext getWithException(final Object caller) {
    return BlackReflection.create(IAppWidgetServiceStubContext.class, caller, true);
  }

  public static IAppWidgetServiceStubContext get(final Object caller) {
    return BlackReflection.create(IAppWidgetServiceStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IAppWidgetServiceStubContext.class);
  }
}
