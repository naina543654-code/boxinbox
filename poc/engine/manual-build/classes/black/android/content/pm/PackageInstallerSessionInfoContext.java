package black.android.content.pm;

import android.graphics.Bitmap;
import java.lang.Boolean;
import java.lang.CharSequence;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.content.pm.PackageInstaller$SessionInfo")
public interface PackageInstallerSessionInfoContext {
  @BFieldSetNotProcess
  void _set_active(final Object value);

  @BFieldCheckNotProcess
  Field _check_active();

  @BFieldNotProcess
  Boolean active();

  @BFieldSetNotProcess
  void _set_appIcon(final Object value);

  @BFieldCheckNotProcess
  Field _check_appIcon();

  @BFieldNotProcess
  Bitmap appIcon();

  @BFieldSetNotProcess
  void _set_appLabel(final Object value);

  @BFieldCheckNotProcess
  Field _check_appLabel();

  @BFieldNotProcess
  CharSequence appLabel();

  @BFieldSetNotProcess
  void _set_appPackageName(final Object value);

  @BFieldCheckNotProcess
  Field _check_appPackageName();

  @BFieldNotProcess
  String appPackageName();

  @BFieldSetNotProcess
  void _set_installerPackageName(final Object value);

  @BFieldCheckNotProcess
  Field _check_installerPackageName();

  @BFieldNotProcess
  String installerPackageName();

  @BFieldSetNotProcess
  void _set_mode(final Object value);

  @BFieldCheckNotProcess
  Field _check_mode();

  @BFieldNotProcess
  Integer mode();

  @BFieldSetNotProcess
  void _set_progress(final Object value);

  @BFieldCheckNotProcess
  Field _check_progress();

  @BFieldNotProcess
  Float progress();

  @BFieldSetNotProcess
  void _set_resolvedBaseCodePath(final Object value);

  @BFieldCheckNotProcess
  Field _check_resolvedBaseCodePath();

  @BFieldNotProcess
  String resolvedBaseCodePath();

  @BFieldSetNotProcess
  void _set_sealed(final Object value);

  @BFieldCheckNotProcess
  Field _check_sealed();

  @BFieldNotProcess
  Boolean sealed();

  @BFieldSetNotProcess
  void _set_sessionId(final Object value);

  @BFieldCheckNotProcess
  Field _check_sessionId();

  @BFieldNotProcess
  Integer sessionId();

  @BFieldSetNotProcess
  void _set_sizeBytes(final Object value);

  @BFieldCheckNotProcess
  Field _check_sizeBytes();

  @BFieldNotProcess
  Long sizeBytes();
}
