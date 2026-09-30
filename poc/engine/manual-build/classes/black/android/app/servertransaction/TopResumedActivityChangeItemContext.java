package black.android.app.servertransaction;

import java.lang.Boolean;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.servertransaction.TopResumedActivityChangeItem")
public interface TopResumedActivityChangeItemContext {
  @BFieldSetNotProcess
  void _set_mOnTop(final Object value);

  @BFieldCheckNotProcess
  Field _check_mOnTop();

  @BFieldNotProcess
  Boolean mOnTop();
}
