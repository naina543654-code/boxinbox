package black.com.android.internal.net;

import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import java.util.List;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("com.android.internal.net.VpnConfig")
public interface VpnConfigContext {
  @BFieldSetNotProcess
  void _set_user(final Object value);

  @BFieldCheckNotProcess
  Field _check_user();

  @BFieldNotProcess
  String user();

  @BFieldSetNotProcess
  void _set_disallowedApplications(final Object value);

  @BFieldCheckNotProcess
  Field _check_disallowedApplications();

  @BFieldNotProcess
  List<String> disallowedApplications();

  @BFieldSetNotProcess
  void _set_allowedApplications(final Object value);

  @BFieldCheckNotProcess
  Field _check_allowedApplications();

  @BFieldNotProcess
  List<String> allowedApplications();
}
