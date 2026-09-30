package black.android.app;

import java.lang.Object;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.LoadedApk$ReceiverDispatcher$InnerReceiver")
public interface LoadedApkReceiverDispatcherInnerReceiverContext {
  @BFieldSetNotProcess
  void _set_mDispatcher(final Object value);

  @BFieldCheckNotProcess
  Field _check_mDispatcher();

  @BFieldNotProcess
  WeakReference<?> mDispatcher();
}
