package black.android.os;

import android.os.Parcel;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.os.Bundle")
public interface BundleICSContext {
  @BFieldSetNotProcess
  void _set_mParcelledData(final Object value);

  @BFieldCheckNotProcess
  Field _check_mParcelledData();

  @BFieldNotProcess
  Parcel mParcelledData();
}
