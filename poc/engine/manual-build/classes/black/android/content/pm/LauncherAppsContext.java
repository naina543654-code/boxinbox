package black.android.content.pm;

import android.content.pm.PackageManager;
import android.os.IInterface;
import android.os.UserManager;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.content.pm.LauncherApps")
public interface LauncherAppsContext {
  @BFieldSetNotProcess
  void _set_mPm(final Object value);

  @BFieldCheckNotProcess
  Field _check_mPm();

  @BFieldNotProcess
  PackageManager mPm();

  @BFieldSetNotProcess
  void _set_mService(final Object value);

  @BFieldCheckNotProcess
  Field _check_mService();

  @BFieldNotProcess
  IInterface mService();

  @BFieldSetNotProcess
  void _set_mUserManager(final Object value);

  @BFieldCheckNotProcess
  Field _check_mUserManager();

  @BFieldNotProcess
  UserManager mUserManager();
}
