package black.android.app;

import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BConstructorNotProcess;

@BClassNameNotProcess("android.app.ActivityThread$ProviderClientRecord")
public interface ActivityThreadProviderClientRecordStatic {
  @BConstructorNotProcess
  ActivityThread.ProviderClientRecord _new();
}
