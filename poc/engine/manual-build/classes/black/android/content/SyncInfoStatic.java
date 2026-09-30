package black.android.content;

import android.accounts.Account;
import java.lang.String;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BConstructorNotProcess;

@BClassNameNotProcess("android.content.SyncInfo")
public interface SyncInfoStatic {
  @BConstructorNotProcess
  SyncInfo _new(int int0, Account Account1, String String2, long long3);
}
