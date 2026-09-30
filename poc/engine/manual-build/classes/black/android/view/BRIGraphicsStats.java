package black.android.view;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIGraphicsStats {
  public static IGraphicsStatsStatic getWithException() {
    return BlackReflection.create(IGraphicsStatsStatic.class, null, true);
  }

  public static IGraphicsStatsStatic get() {
    return BlackReflection.create(IGraphicsStatsStatic.class, null, false);
  }

  public static IGraphicsStatsContext getWithException(final Object caller) {
    return BlackReflection.create(IGraphicsStatsContext.class, caller, true);
  }

  public static IGraphicsStatsContext get(final Object caller) {
    return BlackReflection.create(IGraphicsStatsContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IGraphicsStatsContext.class);
  }
}
