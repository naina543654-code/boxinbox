package black.libcore.io;

import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("libcore.io.Libcore")
public interface LibcoreStatic {
  @BFieldSetNotProcess
  void _set_os(final Object value);

  @BFieldCheckNotProcess
  Field _check_os();

  @BFieldNotProcess
  Object os();
}
