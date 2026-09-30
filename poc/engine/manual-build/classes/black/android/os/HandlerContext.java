package black.android.os;

import android.os.Handler;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.os.Handler")
public interface HandlerContext {
  @BFieldSetNotProcess
  void _set_mCallback(final Object value);

  @BFieldCheckNotProcess
  Field _check_mCallback();

  @BFieldNotProcess
  Handler.Callback mCallback();
}
