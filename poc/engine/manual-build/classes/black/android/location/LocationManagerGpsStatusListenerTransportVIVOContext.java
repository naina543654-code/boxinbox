package black.android.location;

import java.lang.Void;
import java.lang.reflect.Method;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BMethodCheckNotProcess;

@BClassNameNotProcess("android.location.LocationManager$GnssStatusListenerTransport")
public interface LocationManagerGpsStatusListenerTransportVIVOContext {
  @BMethodCheckNotProcess
  Method _check_onSvStatusChanged(int int0, int[] ints1, float[] floats2, float[] floats3,
      float[] floats4, int int5, int int6, int int7, long[] longs8);

  Void onSvStatusChanged(int int0, int[] ints1, float[] floats2, float[] floats3, float[] floats4,
      int int5, int int6, int int7, long[] longs8);
}
