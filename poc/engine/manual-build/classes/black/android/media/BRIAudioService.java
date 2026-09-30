package black.android.media;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIAudioService {
  public static IAudioServiceStatic getWithException() {
    return BlackReflection.create(IAudioServiceStatic.class, null, true);
  }

  public static IAudioServiceStatic get() {
    return BlackReflection.create(IAudioServiceStatic.class, null, false);
  }

  public static IAudioServiceContext getWithException(final Object caller) {
    return BlackReflection.create(IAudioServiceContext.class, caller, true);
  }

  public static IAudioServiceContext get(final Object caller) {
    return BlackReflection.create(IAudioServiceContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IAudioServiceContext.class);
  }
}
