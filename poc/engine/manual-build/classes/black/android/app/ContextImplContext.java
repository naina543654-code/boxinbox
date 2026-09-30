package black.android.app;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.ContextImpl")
public interface ContextImplContext {
  @BMethodCheckNotProcess
  Method _check_getReceiverRestrictedContext();

  Context getReceiverRestrictedContext();

  @BMethodCheckNotProcess
  Method _check_setOuterContext(Context Context0);

  Void setOuterContext(Context Context0);

  @BMethodCheckNotProcess
  Method _check_getAttributionSource();

  Object getAttributionSource();

  @BFieldSetNotProcess
  void _set_mBasePackageName(final Object value);

  @BFieldCheckNotProcess
  Field _check_mBasePackageName();

  @BFieldNotProcess
  String mBasePackageName();

  @BFieldSetNotProcess
  void _set_mContentResolver(final Object value);

  @BFieldCheckNotProcess
  Field _check_mContentResolver();

  @BFieldNotProcess
  ContentResolver mContentResolver();

  @BFieldSetNotProcess
  void _set_mPackageInfo(final Object value);

  @BFieldCheckNotProcess
  Field _check_mPackageInfo();

  @BFieldNotProcess
  Object mPackageInfo();

  @BFieldSetNotProcess
  void _set_mPackageManager(final Object value);

  @BFieldCheckNotProcess
  Field _check_mPackageManager();

  @BFieldNotProcess
  PackageManager mPackageManager();
}
