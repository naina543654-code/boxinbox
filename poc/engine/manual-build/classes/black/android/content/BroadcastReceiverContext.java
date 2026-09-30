package black.android.content;

import android.content.BroadcastReceiver;
import java.lang.Object;
import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BParamClassName;

@BClassNameNotProcess("android.content.BroadcastReceiver")
public interface BroadcastReceiverContext {
  @BMethodCheckNotProcess
  Method _check_getPendingResult();

  BroadcastReceiver.PendingResult getPendingResult();

  @BMethodCheckNotProcess
  Method _check_setPendingResult(
      @BParamClassName("android.content.BroadcastReceiver$PendingResult") Object pendingResult);

  Void setPendingResult(
      @BParamClassName("android.content.BroadcastReceiver$PendingResult") Object pendingResult);
}
