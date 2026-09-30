package black.android.content.pm;

import android.content.pm.Signature;
import android.os.Bundle;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import java.util.List;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.content.pm.PackageParser$Package")
public interface PackageParserPackageContext {
  @BFieldSetNotProcess
  void _set_activities(final Object value);

  @BFieldCheckNotProcess
  Field _check_activities();

  @BFieldNotProcess
  List activities();

  @BFieldSetNotProcess
  void _set_mAppMetaData(final Object value);

  @BFieldCheckNotProcess
  Field _check_mAppMetaData();

  @BFieldNotProcess
  Bundle mAppMetaData();

  @BFieldSetNotProcess
  void _set_mSharedUserId(final Object value);

  @BFieldCheckNotProcess
  Field _check_mSharedUserId();

  @BFieldNotProcess
  String mSharedUserId();

  @BFieldSetNotProcess
  void _set_mSignatures(final Object value);

  @BFieldCheckNotProcess
  Field _check_mSignatures();

  @BFieldNotProcess
  Signature[] mSignatures();

  @BFieldSetNotProcess
  void _set_mSigningDetails(final Object value);

  @BFieldCheckNotProcess
  Field _check_mSigningDetails();

  @BFieldNotProcess
  Object mSigningDetails();

  @BFieldSetNotProcess
  void _set_mVersionCode(final Object value);

  @BFieldCheckNotProcess
  Field _check_mVersionCode();

  @BFieldNotProcess
  Integer mVersionCode();

  @BFieldSetNotProcess
  void _set_packageName(final Object value);

  @BFieldCheckNotProcess
  Field _check_packageName();

  @BFieldNotProcess
  String packageName();

  @BFieldSetNotProcess
  void _set_permissionGroups(final Object value);

  @BFieldCheckNotProcess
  Field _check_permissionGroups();

  @BFieldNotProcess
  List permissionGroups();

  @BFieldSetNotProcess
  void _set_permissions(final Object value);

  @BFieldCheckNotProcess
  Field _check_permissions();

  @BFieldNotProcess
  List permissions();

  @BFieldSetNotProcess
  void _set_protectedBroadcasts(final Object value);

  @BFieldCheckNotProcess
  Field _check_protectedBroadcasts();

  @BFieldNotProcess
  List<String> protectedBroadcasts();

  @BFieldSetNotProcess
  void _set_providers(final Object value);

  @BFieldCheckNotProcess
  Field _check_providers();

  @BFieldNotProcess
  List providers();

  @BFieldSetNotProcess
  void _set_receivers(final Object value);

  @BFieldCheckNotProcess
  Field _check_receivers();

  @BFieldNotProcess
  List receivers();

  @BFieldSetNotProcess
  void _set_requestedPermissions(final Object value);

  @BFieldCheckNotProcess
  Field _check_requestedPermissions();

  @BFieldNotProcess
  List<String> requestedPermissions();

  @BFieldSetNotProcess
  void _set_services(final Object value);

  @BFieldCheckNotProcess
  Field _check_services();

  @BFieldNotProcess
  List services();
}
