package black.android.content.pm;

import android.graphics.Bitmap;
import android.net.Uri;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.content.pm.PackageInstaller$SessionParams")
public interface PackageInstallerSessionParamsLOLLIPOPContext {
  @BFieldSetNotProcess
  void _set_abiOverride(final Object value);

  @BFieldCheckNotProcess
  Field _check_abiOverride();

  @BFieldNotProcess
  String abiOverride();

  @BFieldSetNotProcess
  void _set_appIcon(final Object value);

  @BFieldCheckNotProcess
  Field _check_appIcon();

  @BFieldNotProcess
  Bitmap appIcon();

  @BFieldSetNotProcess
  void _set_appIconLastModified(final Object value);

  @BFieldCheckNotProcess
  Field _check_appIconLastModified();

  @BFieldNotProcess
  Long appIconLastModified();

  @BFieldSetNotProcess
  void _set_appLabel(final Object value);

  @BFieldCheckNotProcess
  Field _check_appLabel();

  @BFieldNotProcess
  String appLabel();

  @BFieldSetNotProcess
  void _set_appPackageName(final Object value);

  @BFieldCheckNotProcess
  Field _check_appPackageName();

  @BFieldNotProcess
  String appPackageName();

  @BFieldSetNotProcess
  void _set_installFlags(final Object value);

  @BFieldCheckNotProcess
  Field _check_installFlags();

  @BFieldNotProcess
  Integer installFlags();

  @BFieldSetNotProcess
  void _set_installLocation(final Object value);

  @BFieldCheckNotProcess
  Field _check_installLocation();

  @BFieldNotProcess
  Integer installLocation();

  @BFieldSetNotProcess
  void _set_mode(final Object value);

  @BFieldCheckNotProcess
  Field _check_mode();

  @BFieldNotProcess
  Integer mode();

  @BFieldSetNotProcess
  void _set_originatingUri(final Object value);

  @BFieldCheckNotProcess
  Field _check_originatingUri();

  @BFieldNotProcess
  Uri originatingUri();

  @BFieldSetNotProcess
  void _set_referrerUri(final Object value);

  @BFieldCheckNotProcess
  Field _check_referrerUri();

  @BFieldNotProcess
  Uri referrerUri();

  @BFieldSetNotProcess
  void _set_sizeBytes(final Object value);

  @BFieldCheckNotProcess
  Field _check_sizeBytes();

  @BFieldNotProcess
  Long sizeBytes();
}
