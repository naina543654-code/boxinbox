package black.android.rms.resource;

import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import java.util.List;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.rms.resource.ReceiverResource")
public interface ReceiverResourceNContext {
  @BFieldSetNotProcess
  void _set_mWhiteList(final Object value);

  @BFieldCheckNotProcess
  Field _check_mWhiteList();

  @BFieldNotProcess
  List<String> mWhiteList();
}
