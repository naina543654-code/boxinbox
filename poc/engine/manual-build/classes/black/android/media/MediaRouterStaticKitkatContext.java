package black.android.media;

import android.os.IInterface;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.media.MediaRouter$Static")
public interface MediaRouterStaticKitkatContext {
  @BFieldSetNotProcess
  void _set_mMediaRouterService(final Object value);

  @BFieldCheckNotProcess
  Field _check_mMediaRouterService();

  @BFieldNotProcess
  IInterface mMediaRouterService();
}
