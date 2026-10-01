package top.niunaijun.blackbox.fake.spoof;

import top.niunaijun.blackbox.fake.hook.IInjectHook;

/**
 * HookManager injector for telephony operator identity spoofing at the
 * {@link android.telephony.TelephonyManager} public-API choke point.
 *
 * <p>Registered via {@code addInjector(new TelephonyApiSpoofInjector());} —
 * installs the Pine hooks in {@link BTelephonyApiSpoof}. Install failures are
 * fail-open (real operator values remain visible) and never throw out of
 * {@link #injectHook()}.
 */
public class TelephonyApiSpoofInjector implements IInjectHook {

    @Override
    public void injectHook() {
        BTelephonyApiSpoof.install();
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}
