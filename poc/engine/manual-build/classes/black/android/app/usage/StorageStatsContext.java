package black.android.app.usage;

import java.lang.Long;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.app.usage.StorageStats")
public interface StorageStatsContext {
  @BFieldSetNotProcess
  void _set_cacheBytes(final Object value);

  @BFieldCheckNotProcess
  Field _check_cacheBytes();

  @BFieldNotProcess
  Long cacheBytes();

  @BFieldSetNotProcess
  void _set_codeBytes(final Object value);

  @BFieldCheckNotProcess
  Field _check_codeBytes();

  @BFieldNotProcess
  Long codeBytes();

  @BFieldSetNotProcess
  void _set_dataBytes(final Object value);

  @BFieldCheckNotProcess
  Field _check_dataBytes();

  @BFieldNotProcess
  Long dataBytes();
}
