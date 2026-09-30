package black.android.hardware.display;

import android.os.IInterface;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.hardware.display.DisplayManagerGlobal")
public interface DisplayManagerGlobalContext {
  @BFieldSetNotProcess
  void _set_mDm(final Object value);

  @BFieldCheckNotProcess
  Field _check_mDm();

  @BFieldNotProcess
  IInterface mDm();
}
