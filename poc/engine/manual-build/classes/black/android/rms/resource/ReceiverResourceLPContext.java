package black.android.rms.resource;

import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.rms.resource.ReceiverResource")
public interface ReceiverResourceLPContext {
  @BFieldSetNotProcess
  void _set_mResourceConfig(final Object value);

  @BFieldCheckNotProcess
  Field _check_mResourceConfig();

  @BFieldNotProcess
  Object mResourceConfig();
}
