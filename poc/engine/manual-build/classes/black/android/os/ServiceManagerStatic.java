package black.android.os;

import android.os.IBinder;
import android.os.IInterface;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.os.ServiceManager")
public interface ServiceManagerStatic {
  @BMethodCheckNotProcess
  Method _check_addService(String String0, IBinder IBinder1);

  Void addService(String String0, IBinder IBinder1);

  @BMethodCheckNotProcess
  Method _check_checkService();

  IBinder checkService();

  @BMethodCheckNotProcess
  Method _check_getIServiceManager();

  IInterface getIServiceManager();

  @BMethodCheckNotProcess
  Method _check_getService(String name);

  IBinder getService(String name);

  @BMethodCheckNotProcess
  Method _check_listServices();

  String[] listServices();

  @BFieldSetNotProcess
  void _set_sCache(final Object value);

  @BFieldCheckNotProcess
  Field _check_sCache();

  @BFieldNotProcess
  Map<String, IBinder> sCache();

  @BFieldSetNotProcess
  void _set_sServiceManager(final Object value);

  @BFieldCheckNotProcess
  Field _check_sServiceManager();

  @BFieldNotProcess
  IInterface sServiceManager();
}
