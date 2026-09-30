package black.android.rms;

import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.rms.HwSysResImpl")
public interface HwSysResImplPContext {
  @BFieldSetNotProcess
  void _set_mWhiteListMap(final Object value);

  @BFieldCheckNotProcess
  Field _check_mWhiteListMap();

  @BFieldNotProcess
  Map<Integer, ArrayList<String>> mWhiteListMap();
}
