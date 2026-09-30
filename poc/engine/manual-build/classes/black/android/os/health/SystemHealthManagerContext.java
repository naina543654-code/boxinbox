package black.android.os.health;

import android.os.IInterface;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.os.health.SystemHealthManager")
public interface SystemHealthManagerContext {
  @BFieldSetNotProcess
  void _set_mBatteryStats(final Object value);

  @BFieldCheckNotProcess
  Field _check_mBatteryStats();

  @BFieldNotProcess
  IInterface mBatteryStats();
}
