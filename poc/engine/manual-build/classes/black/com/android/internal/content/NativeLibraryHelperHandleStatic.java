package black.com.android.internal.content;

import java.io.File;
import java.lang.Object;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("com.android.internal.content.NativeLibraryHelper$Handle")
public interface NativeLibraryHelperHandleStatic {
  @BMethodCheckNotProcess
  Method _check_create(File File0);

  Object create(File File0);
}
