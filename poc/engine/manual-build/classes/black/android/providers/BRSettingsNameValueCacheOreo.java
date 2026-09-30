package black.android.providers;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRSettingsNameValueCacheOreo {
  public static SettingsNameValueCacheOreoStatic getWithException() {
    return BlackReflection.create(SettingsNameValueCacheOreoStatic.class, null, true);
  }

  public static SettingsNameValueCacheOreoStatic get() {
    return BlackReflection.create(SettingsNameValueCacheOreoStatic.class, null, false);
  }

  public static SettingsNameValueCacheOreoContext getWithException(final Object caller) {
    return BlackReflection.create(SettingsNameValueCacheOreoContext.class, caller, true);
  }

  public static SettingsNameValueCacheOreoContext get(final Object caller) {
    return BlackReflection.create(SettingsNameValueCacheOreoContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(SettingsNameValueCacheOreoContext.class);
  }
}
