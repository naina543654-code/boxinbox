package black.java.lang;

import java.lang.Object;
import java.lang.ThreadGroup;
import java.lang.reflect.Field;
import java.util.List;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("java.lang.ThreadGroup")
public interface ThreadGroupContext {
  @BFieldSetNotProcess
  void _set_groups(final Object value);

  @BFieldCheckNotProcess
  Field _check_groups();

  @BFieldNotProcess
  List<ThreadGroup> groups();

  @BFieldSetNotProcess
  void _set_parent(final Object value);

  @BFieldCheckNotProcess
  Field _check_parent();

  @BFieldNotProcess
  ThreadGroup parent();
}
