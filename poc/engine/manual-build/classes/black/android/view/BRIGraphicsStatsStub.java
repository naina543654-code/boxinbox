package black.android.view;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIGraphicsStatsStub {
  public static IGraphicsStatsStubStatic getWithException() {
    return BlackReflection.create(IGraphicsStatsStubStatic.class, null, true);
  }

  public static IGraphicsStatsStubStatic get() {
    return BlackReflection.create(IGraphicsStatsStubStatic.class, null, false);
  }

  public static IGraphicsStatsStubContext getWithException(final Object caller) {
    return BlackReflection.create(IGraphicsStatsStubContext.class, caller, true);
  }

  public static IGraphicsStatsStubContext get(final Object caller) {
    return BlackReflection.create(IGraphicsStatsStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IGraphicsStatsStubContext.class);
  }
}
