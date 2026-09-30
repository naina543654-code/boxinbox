package black.android.view;

import android.graphics.Bitmap;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.view.SurfaceControl")
public interface SurfaceControlStatic {
  @BMethodCheckNotProcess
  Method _check_screnshot(int int0, int int1);

  Bitmap screnshot(int int0, int int1);
}
