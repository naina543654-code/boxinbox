package black.com.android.internal.content;

import java.lang.Boolean;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("com.android.internal.content.NativeLibraryHelper$Handle")
public interface NativeLibraryHelperHandleContext {
  @BFieldSetNotProcess
  void _set_extractNativeLibs(final Object value);

  @BFieldCheckNotProcess
  Field _check_extractNativeLibs();

  @BFieldNotProcess
  Boolean extractNativeLibs();
}
