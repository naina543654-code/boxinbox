package black.android.app;

import java.io.File;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BConstructorNotProcess;

@BClassNameNotProcess("android.app.SharedPreferencesImpl")
public interface SharedPreferencesImplStatic {
  @BConstructorNotProcess
  SharedPreferencesImpl _new(File File0, int int1);
}
