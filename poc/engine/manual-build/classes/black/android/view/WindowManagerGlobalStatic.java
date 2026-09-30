package black.android.view;

import android.os.IInterface;
import java.lang.Integer;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.view.WindowManagerGlobal")
public interface WindowManagerGlobalStatic {
  @BFieldSetNotProcess
  void _set_ADD_PERMISSION_DENIED(final Object value);

  @BFieldCheckNotProcess
  Field _check_ADD_PERMISSION_DENIED();

  @BFieldNotProcess
  Integer ADD_PERMISSION_DENIED();

  @BFieldSetNotProcess
  void _set_sWindowManagerService(final Object value);

  @BFieldCheckNotProcess
  Field _check_sWindowManagerService();

  @BFieldNotProcess
  IInterface sWindowManagerService();
}
