package black.android.content.pm;

import android.os.Parcelable;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.Void;
import java.lang.reflect.Method;
import java.util.List;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.content.pm.ParceledListSlice")
public interface ParceledListSliceContext {
  @BMethodCheckNotProcess
  Method _check_append(Object item);

  Boolean append(Object item);

  @BMethodCheckNotProcess
  Method _check_getList();

  List<?> getList();

  @BMethodCheckNotProcess
  Method _check_isLastSlice();

  Boolean isLastSlice();

  @BMethodCheckNotProcess
  Method _check_populateList();

  Parcelable populateList();

  @BMethodCheckNotProcess
  Method _check_setLastSlice(boolean b);

  Void setLastSlice(boolean b);
}
