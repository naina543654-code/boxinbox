package black.android.content;

import android.content.Intent;
import android.os.Bundle;
import java.lang.String;
import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.content.IIntentReceiver")
public interface IIntentReceiverContext {
  @BMethodCheckNotProcess
  Method _check_performReceive(Intent intent, int resultCode, String data, Bundle extras,
      boolean ordered, boolean sticky, int sendingUser);

  Void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered,
      boolean sticky, int sendingUser);
}
