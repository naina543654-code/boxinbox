package black.android.content.pm;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRUserInfo {
  public static UserInfoStatic getWithException() {
    return BlackReflection.create(UserInfoStatic.class, null, true);
  }

  public static UserInfoStatic get() {
    return BlackReflection.create(UserInfoStatic.class, null, false);
  }

  public static UserInfoContext getWithException(final Object caller) {
    return BlackReflection.create(UserInfoContext.class, caller, true);
  }

  public static UserInfoContext get(final Object caller) {
    return BlackReflection.create(UserInfoContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(UserInfoContext.class);
  }
}
