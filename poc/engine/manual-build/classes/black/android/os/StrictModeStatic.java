package black.android.os;

import java.lang.Integer;
import java.lang.Object;
import java.lang.Void;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.os.StrictMode")
public interface StrictModeStatic {
  @BMethodCheckNotProcess
  Method _check_disableDeathOnFileUriExposure();

  Void disableDeathOnFileUriExposure();

  @BFieldSetNotProcess
  void _set_DETECT_VM_FILE_URI_EXPOSURE(final Object value);

  @BFieldCheckNotProcess
  Field _check_DETECT_VM_FILE_URI_EXPOSURE();

  @BFieldNotProcess
  Integer DETECT_VM_FILE_URI_EXPOSURE();

  @BFieldSetNotProcess
  void _set_PENALTY_DEATH_ON_FILE_URI_EXPOSURE(final Object value);

  @BFieldCheckNotProcess
  Field _check_PENALTY_DEATH_ON_FILE_URI_EXPOSURE();

  @BFieldNotProcess
  Integer PENALTY_DEATH_ON_FILE_URI_EXPOSURE();

  @BFieldSetNotProcess
  void _set_sVmPolicyMask(final Object value);

  @BFieldCheckNotProcess
  Field _check_sVmPolicyMask();

  @BFieldNotProcess
  Integer sVmPolicyMask();
}
