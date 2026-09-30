package black.android.app.servertransaction;

import java.lang.Object;
import java.lang.reflect.Field;
import java.util.List;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.servertransaction.ActivityResultItem")
public interface ActivityResultItemContext {
  @BFieldSetNotProcess
  void _set_mResultInfoList(final Object value);

  @BFieldCheckNotProcess
  Field _check_mResultInfoList();

  @BFieldNotProcess
  List mResultInfoList();
}
