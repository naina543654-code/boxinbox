package black.android.widget;

import android.content.pm.ApplicationInfo;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import java.util.ArrayList;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.widget.RemoteViews")
public interface RemoteViewsContext {
  @BFieldSetNotProcess
  void _set_mActions(final Object value);

  @BFieldCheckNotProcess
  Field _check_mActions();

  @BFieldNotProcess
  ArrayList<Object> mActions();

  @BFieldSetNotProcess
  void _set_mApplication(final Object value);

  @BFieldCheckNotProcess
  Field _check_mApplication();

  @BFieldNotProcess
  ApplicationInfo mApplication();

  @BFieldSetNotProcess
  void _set_mPackage(final Object value);

  @BFieldCheckNotProcess
  Field _check_mPackage();

  @BFieldNotProcess
  String mPackage();
}
