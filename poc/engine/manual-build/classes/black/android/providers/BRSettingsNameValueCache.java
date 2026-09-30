package black.android.providers;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRSettingsNameValueCache {
  public static SettingsNameValueCacheStatic getWithException() {
    return BlackReflection.create(SettingsNameValueCacheStatic.class, null, true);
  }

  public static SettingsNameValueCacheStatic get() {
    return BlackReflection.create(SettingsNameValueCacheStatic.class, null, false);
  }

  public static SettingsNameValueCacheContext getWithException(final Object caller) {
    return BlackReflection.create(SettingsNameValueCacheContext.class, caller, true);
  }

  public static SettingsNameValueCacheContext get(final Object caller) {
    return BlackReflection.create(SettingsNameValueCacheContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(SettingsNameValueCacheContext.class);
  }
}
