package black.android.app;

import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.WallpaperManager")
public interface WallpaperManagerStatic {
  @BFieldSetNotProcess
  void _set_sGlobals(final Object value);

  @BFieldCheckNotProcess
  Field _check_sGlobals();

  @BFieldNotProcess
  Object sGlobals();
}
