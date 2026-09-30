package black.android.content;

import android.os.IInterface;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.content.ContentResolver")
public interface ContentResolverStatic {
  @BFieldSetNotProcess
  void _set_sContentService(final Object value);

  @BFieldCheckNotProcess
  Field _check_sContentService();

  @BFieldNotProcess
  IInterface sContentService();
}
