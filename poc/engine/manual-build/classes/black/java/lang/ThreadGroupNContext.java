package black.java.lang;

import java.lang.Integer;
import java.lang.Object;
import java.lang.ThreadGroup;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("java.lang.ThreadGroup")
public interface ThreadGroupNContext {
  @BFieldSetNotProcess
  void _set_groups(final Object value);

  @BFieldCheckNotProcess
  Field _check_groups();

  @BFieldNotProcess
  ThreadGroup[] groups();

  @BFieldSetNotProcess
  void _set_ngroups(final Object value);

  @BFieldCheckNotProcess
  Field _check_ngroups();

  @BFieldNotProcess
  Integer ngroups();

  @BFieldSetNotProcess
  void _set_parent(final Object value);

  @BFieldCheckNotProcess
  Field _check_parent();

  @BFieldNotProcess
  ThreadGroup parent();
}
