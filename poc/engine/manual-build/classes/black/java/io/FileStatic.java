package black.java.io;

import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("java.io.File")
public interface FileStatic {
  @BFieldSetNotProcess
  void _set_fs(final Object value);

  @BFieldCheckNotProcess
  Field _check_fs();

  @BFieldNotProcess
  Object fs();
}
