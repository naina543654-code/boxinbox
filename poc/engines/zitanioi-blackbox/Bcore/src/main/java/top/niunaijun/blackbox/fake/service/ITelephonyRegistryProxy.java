package top.niunaijun.blackbox.fake.service;

import java.lang.reflect.Method;

import black.android.os.BRServiceManager;
import black.com.android.internal.telephony.BRITelephonyRegistryStub;
import top.niunaijun.blackbox.fake.hook.BinderInvocationStub;
import top.niunaijun.blackbox.fake.hook.MethodHook;
import top.niunaijun.blackbox.fake.hook.ProxyMethod;
import top.niunaijun.blackbox.utils.MethodParameterUtils;

/**
 * Created by Milk on 2021/5/17.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class ITelephonyRegistryProxy extends BinderInvocationStub {
    public ITelephonyRegistryProxy() {
        super(BRServiceManager.get().getService("telephony.registry"));
    }

    @Override
    protected Object getWho() {
        return BRITelephonyRegistryStub.get().asInterface(BRServiceManager.get().getService("telephony.registry"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("telephony.registry");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @ProxyMethod("listenForSubscriber")
    public static class ListenForSubscriber extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            // R4 audit 2026-10-05: the sync cell paths are emptied when a
            // spoof profile is active, but the async listen() path delivered
            // real towers. Strip the cell bits from the event mask so the
            // guest never receives cell callbacks. Layout:
            // listenForSubscriber(int subId, String pkg, IPhoneStateListener,
            //                     int events, boolean notifyNow).
            stripCellListenBits(args, 3);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("listen")
    public static class Listen extends MethodHook {

        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            // R4 audit 2026-10-05: same cell-callback blackout as above.
            // Layout: listen(String pkg, IPhoneStateListener, int events,
            //               boolean notifyNow).
            stripCellListenBits(args, 2);
            return method.invoke(who, args);
        }
    }

    /**
     * Clears {@code LISTEN_CELL_INFO} / {@code LISTEN_CELL_LOCATION} from the
     * event-mask int at {@code eventsIndex} when a spoof profile is active.
     * Literals: {@code PhoneStateListener.LISTEN_CELL_INFO} (0x400) and
     * {@code LISTEN_CELL_LOCATION} (0x10) — stable framework constants.
     */
    private static void stripCellListenBits(Object[] args, int eventsIndex) {
        try {
            if (!top.niunaijun.blackbox.fake.spoof.BSpoofManager.get().isSpoofActive()) {
                return;
            }
            if (args != null && eventsIndex < args.length
                    && args[eventsIndex] instanceof Integer) {
                int events = (Integer) args[eventsIndex];
                args[eventsIndex] = events & ~(0x400 | 0x10);
            }
        } catch (Throwable ignored) {
            // Never break telephony for a filtering failure — worst case the
            // original mask is delivered, same as pre-R4 behavior.
        }
    }
}
