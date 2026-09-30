package black.android.media;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRAudioManager {
  public static AudioManagerStatic getWithException() {
    return BlackReflection.create(AudioManagerStatic.class, null, true);
  }

  public static AudioManagerStatic get() {
    return BlackReflection.create(AudioManagerStatic.class, null, false);
  }

  public static AudioManagerContext getWithException(final Object caller) {
    return BlackReflection.create(AudioManagerContext.class, caller, true);
  }

  public static AudioManagerContext get(final Object caller) {
    return BlackReflection.create(AudioManagerContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(AudioManagerContext.class);
  }
}
