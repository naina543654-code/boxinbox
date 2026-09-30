package black.android.location.provider;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRProviderProperties {
  public static ProviderPropertiesStatic getWithException() {
    return BlackReflection.create(ProviderPropertiesStatic.class, null, true);
  }

  public static ProviderPropertiesStatic get() {
    return BlackReflection.create(ProviderPropertiesStatic.class, null, false);
  }

  public static ProviderPropertiesContext getWithException(final Object caller) {
    return BlackReflection.create(ProviderPropertiesContext.class, caller, true);
  }

  public static ProviderPropertiesContext get(final Object caller) {
    return BlackReflection.create(ProviderPropertiesContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(ProviderPropertiesContext.class);
  }
}
