package black.android.content;

import android.os.IInterface;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.content.IntentSender")
public interface IntentSenderContext {
  @BFieldSetNotProcess
  void _set_mTarget(final Object value);

  @BFieldCheckNotProcess
  Field _check_mTarget();

  @BFieldNotProcess
  IInterface mTarget();
}
