package black.android.media;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIAudioServiceStub {
  public static IAudioServiceStubStatic getWithException() {
    return BlackReflection.create(IAudioServiceStubStatic.class, null, true);
  }

  public static IAudioServiceStubStatic get() {
    return BlackReflection.create(IAudioServiceStubStatic.class, null, false);
  }

  public static IAudioServiceStubContext getWithException(final Object caller) {
    return BlackReflection.create(IAudioServiceStubContext.class, caller, true);
  }

  public static IAudioServiceStubContext get(final Object caller) {
    return BlackReflection.create(IAudioServiceStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IAudioServiceStubContext.class);
  }
}
