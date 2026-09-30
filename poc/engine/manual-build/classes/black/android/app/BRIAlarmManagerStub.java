package black.android.app;

import java.lang.Class;
import java.lang.Object;
import top.niunaijun.blackreflection.BlackReflection;

public class BRIAlarmManagerStub {
  public static IAlarmManagerStubStatic getWithException() {
    return BlackReflection.create(IAlarmManagerStubStatic.class, null, true);
  }

  public static IAlarmManagerStubStatic get() {
    return BlackReflection.create(IAlarmManagerStubStatic.class, null, false);
  }

  public static IAlarmManagerStubContext getWithException(final Object caller) {
    return BlackReflection.create(IAlarmManagerStubContext.class, caller, true);
  }

  public static IAlarmManagerStubContext get(final Object caller) {
    return BlackReflection.create(IAlarmManagerStubContext.class, caller, false);
  }

  public static Class getRealClass() {
    return top.niunaijun.blackreflection.utils.ClassUtil.classReady(IAlarmManagerStubContext.class);
  }
}
