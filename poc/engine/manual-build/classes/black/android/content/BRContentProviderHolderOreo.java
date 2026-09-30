package black.android.content;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRContentProviderHolderOreo {
  public static ContentProviderHolderOreoStatic getWithException() {
    return BlackReflection.create(ContentProviderHolderOreoStatic.class, null, true);
  }

  public static ContentProviderHolderOreoStatic get() {
    return BlackReflection.create(ContentProviderHolderOreoStatic.class, null, false);
  }

  public static ContentProviderHolderOreoContext getWithException(final Object caller) {
    return BlackReflection.create(ContentProviderHolderOreoContext.class, caller, true);
  }

  public static ContentProviderHolderOreoContext get(final Object caller) {
    return BlackReflection.create(ContentProviderHolderOreoContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ContentProviderHolderOreoContext.class);
  }
}
