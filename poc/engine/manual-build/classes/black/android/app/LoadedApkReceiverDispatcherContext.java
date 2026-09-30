package black.android.app;

import android.content.BroadcastReceiver;
import android.content.IIntentReceiver;
import android.os.IInterface;
import java.lang.Object;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.app.LoadedApk$ReceiverDispatcher")
public interface LoadedApkReceiverDispatcherContext {
  @BMethodCheckNotProcess
  Method _check_getIIntentReceiver();

  IInterface getIIntentReceiver();

  @BFieldSetNotProcess
  void _set_mIIntentReceiver(final Object value);

  @BFieldCheckNotProcess
  Field _check_mIIntentReceiver();

  @BFieldNotProcess
  IIntentReceiver mIIntentReceiver();

  @BFieldSetNotProcess
  void _set_mReceiver(final Object value);

  @BFieldCheckNotProcess
  Field _check_mReceiver();

  @BFieldNotProcess
  BroadcastReceiver mReceiver();
}
