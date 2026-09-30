package black.android.location.provider;

import java.lang.Boolean;
import java.lang.Object;
import java.lang.reflect.Field;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldCheckNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldNotProcess;
import top.niunaijun.blackreflection.annotation.BFieldSetNotProcess;

@BClassNameNotProcess("android.location.provider.ProviderProperties")
public interface ProviderPropertiesContext {
  @BFieldSetNotProcess
  void _set_mHasNetworkRequirement(final Object value);

  @BFieldCheckNotProcess
  Field _check_mHasNetworkRequirement();

  @BFieldNotProcess
  Boolean mHasNetworkRequirement();

  @BFieldSetNotProcess
  void _set_mHasSatelliteRequirement(final Object value);

  @BFieldCheckNotProcess
  Field _check_mHasSatelliteRequirement();

  @BFieldNotProcess
  Boolean mHasSatelliteRequirement();

  @BFieldSetNotProcess
  void _set_mHasCellRequirement(final Object value);

  @BFieldCheckNotProcess
  Field _check_mHasCellRequirement();

  @BFieldNotProcess
  Boolean mHasCellRequirement();

  @BFieldSetNotProcess
  void _set_mHasMonetaryCost(final Object value);

  @BFieldCheckNotProcess
  Field _check_mHasMonetaryCost();

  @BFieldNotProcess
  Boolean mHasMonetaryCost();

  @BFieldSetNotProcess
  void _set_mHasAltitudeSupport(final Object value);

  @BFieldCheckNotProcess
  Field _check_mHasAltitudeSupport();

  @BFieldNotProcess
  Boolean mHasAltitudeSupport();

  @BFieldSetNotProcess
  void _set_mHasSpeedSupport(final Object value);

  @BFieldCheckNotProcess
  Field _check_mHasSpeedSupport();

  @BFieldNotProcess
  Boolean mHasSpeedSupport();

  @BFieldSetNotProcess
  void _set_mHasBearingSupport(final Object value);

  @BFieldCheckNotProcess
  Field _check_mHasBearingSupport();

  @BFieldNotProcess
  Boolean mHasBearingSupport();
}
