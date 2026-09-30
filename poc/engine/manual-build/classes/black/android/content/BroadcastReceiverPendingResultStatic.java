package black.android.content;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.IBinder;
import java.lang.String;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BConstructorNotProcess;

@BClassNameNotProcess("android.content.BroadcastReceiver$PendingResult")
public interface BroadcastReceiverPendingResultStatic {
  @BConstructorNotProcess
  BroadcastReceiver.PendingResult _new(int resultCode, String resultData, Bundle resultExtras,
      int type, boolean ordered, boolean sticky, IBinder token, int userId);
}
