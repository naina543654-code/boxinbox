package black.android.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIContentService {
  public static IContentServiceStatic getWithException() {
    return BlackReflection.create(IContentServiceStatic.class, null, true);
  }

  public static IContentServiceStatic get() {
    return BlackReflection.create(IContentServiceStatic.class, null, false);
  }

  public static IContentServiceContext getWithException(final Object caller) {
    return BlackReflection.create(IContentServiceContext.class, caller, true);
  }

  public static IContentServiceContext get(final Object caller) {
    return BlackReflection.create(IContentServiceContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IContentServiceContext.class);
  }
}
