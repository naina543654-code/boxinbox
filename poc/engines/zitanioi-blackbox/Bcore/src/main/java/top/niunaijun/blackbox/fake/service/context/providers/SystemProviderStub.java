package top.niunaijun.blackbox.fake.service.context.providers;

import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.IInterface;
import android.provider.Settings;

import java.lang.reflect.Method;

import black.android.content.BRAttributionSource;
import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.fake.hook.ClassInvocationStub;
import top.niunaijun.blackbox.fake.spoof.BSpoofManager;
import top.niunaijun.blackbox.utils.compat.ContextCompat;

/**
 * Created by Milk on 4/8/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 *
 * Track B: intercepts Settings.Secure.ANDROID_ID reads ("settings" provider is wrapped
 * by this stub via ContentProviderDelegate.update, per guest process) and returns the
 * per-identity androidId from the spoof profile. Inactive profile -> pass through to
 * the real provider (the guest then sees the host's real Android ID, honestly).
 */
public class SystemProviderStub extends ClassInvocationStub implements BContentProvider {
    private IInterface mBase;

    @Override
    public IInterface wrapper(IInterface contentProviderProxy, String appPkg) {
        mBase = contentProviderProxy;
        injectHook();
        return (IInterface) getProxyInvocation();
    }

    @Override
    protected Object getWho() {
        return mBase;
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {

    }

    @Override
    protected void onBindMethod() {

    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("asBinder".equals(method.getName())) {
            return method.invoke(mBase, args);
        }
        // Track B: spoof Settings.Secure.ANDROID_ID for guests with an active profile.
        Object spoofedAndroidId = trySpoofAndroidId(method.getName(), args);
        if (spoofedAndroidId != null) {
            return spoofedAndroidId;
        }
        // Track F: a dev device with adb/development settings on is itself a
        // tell. Report them off for guests with an active profile.
        Object spoofedDevSetting = trySpoofDevSetting(method.getName(), args);
        if (spoofedDevSetting != null) {
            return spoofedDevSetting;
        }
        if (args != null && args.length > 0) {
            Object arg = args[0];
            if (arg instanceof String) {
                args[0] = BlackBoxCore.getHostPkg();
            } else if (arg.getClass().getName().equals(BRAttributionSource.getRealClass().getName())) {
                ContextCompat.fixAttributionSourceState(arg, BlackBoxCore.getHostUid());
            }
        }
        return method.invoke(mBase, args);
    }

    /**
     * Returns a spoofed result when this is an ANDROID_ID read (Settings.Secure
     * query with selectionArgs containing "android_id", or a provider call such as
     * GET_secure with the "android_id" key) and a spoof profile is active;
     * null otherwise (caller continues to the real provider).
     */
    private Object trySpoofAndroidId(String methodName, Object[] args) {
        if (!"query".equals(methodName) && !"call".equals(methodName)) {
            return null;
        }
        BSpoofManager spoof = BSpoofManager.get();
        if (!spoof.isSpoofActive()) {
            return null;
        }
        String androidId = spoof.getAndroidId();
        if (androidId == null || !isAndroidIdRequest(args)) {
            return null;
        }
        if ("call".equals(methodName)) {
            // SettingsProvider call() returns a Bundle holding the value under "value".
            Bundle result = new Bundle();
            result.putString("value", androidId);
            return result;
        }
        // query(): Settings.Secure reads go through NameValueCache with a
        // name/value cursor; hand back a one-row cursor for android_id.
        MatrixCursor cursor = new MatrixCursor(new String[]{"name", "value"}, 1);
        cursor.addRow(new Object[]{Settings.Secure.ANDROID_ID, androidId});
        return cursor;
    }

    private boolean isAndroidIdRequest(Object[] args) {
        if (args == null) {
            return false;
        }
        for (Object arg : args) {
            if (arg instanceof String) {
                if (Settings.Secure.ANDROID_ID.equals(arg)) {
                    return true;
                }
            } else if (arg instanceof String[]) {
                for (String s : (String[]) arg) {
                    if (Settings.Secure.ANDROID_ID.equals(s)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns a spoofed "0" result for developer-mode indicators
     * ({@code adb_enabled}, {@code development_settings_enabled}) when a
     * spoof profile is active; null otherwise (caller continues to the
     * real provider).
     */
    private Object trySpoofDevSetting(String methodName, Object[] args) {
        if (!"query".equals(methodName) && !"call".equals(methodName)) {
            return null;
        }
        BSpoofManager spoof = BSpoofManager.get();
        if (!spoof.isSpoofActive()) {
            return null;
        }
        if (!isDevSettingRequest(args)) {
            return null;
        }
        if ("call".equals(methodName)) {
            Bundle result = new Bundle();
            result.putString("value", "0");
            return result;
        }
        MatrixCursor cursor = new MatrixCursor(new String[]{"name", "value"}, 1);
        cursor.addRow(new Object[]{"adb_enabled", "0"});
        return cursor;
    }

    private boolean isDevSettingRequest(Object[] args) {
        if (args == null) {
            return false;
        }
        for (Object arg : args) {
            if (arg instanceof String) {
                String s = (String) arg;
                if ("adb_enabled".equals(s) || "development_settings_enabled".equals(s)) {
                    return true;
                }
            } else if (arg instanceof String[]) {
                for (String s : (String[]) arg) {
                    if ("adb_enabled".equals(s) || "development_settings_enabled".equals(s)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
