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
        // query(): honor the caller's projection. Settings.Secure reads with
        // projection {"value"} and takes column 0 — the old {"name","value"}
        // cursor handed it the literal key string "android_id".
        return valueCursor(args, Settings.Secure.ANDROID_ID, androidId);
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
            } else if (arg instanceof Bundle) {
                // R4 audit 2026-10-05: on API 30+ ContentResolver.query packs
                // selection/selectionArgs into the queryArgs Bundle
                // (android:query-arg-sql-selection[-args]) — the key is visible
                // only inside the Bundle.
                if (bundleSelectsKey((Bundle) arg, Settings.Secure.ANDROID_ID)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether a queryArgs Bundle selects the given Settings key,
     * either via selection-args (`name=?`, args {@code [key]}) or a raw
     * selection string containing the key.
     */
    private static boolean bundleSelectsKey(Bundle b, String key) {
        if (b == null || key == null) {
            return false;
        }
        // Literals: ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS
        // ("android:query-arg-sql-selection-args") and QUERY_ARG_SQL_SELECTION
        // ("android:query-arg-sql-selection") — stable framework strings.
        String[] selArgs =
                b.getStringArray("android:query-arg-sql-selection-args");
        if (selArgs != null) {
            for (String s : selArgs) {
                if (key.equals(s)) {
                    return true;
                }
            }
        }
        String selection = b.getString("android:query-arg-sql-selection");
        return selection != null && selection.contains(key);
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
        String key = requestedDevKey(args);
        if (key == null) {
            return null;
        }
        if ("call".equals(methodName)) {
            Bundle result = new Bundle();
            result.putString("value", "0");
            return result;
        }
        // Same projection bug as the android_id path: answer {"value"}.
        return valueCursor(args, key, "0");
    }

    /**
     * Builds a one-row cursor honoring the caller's query() projection.
     * {@code Settings} reads with projection {@code {"value"}} and takes
     * column 0 — a hardcoded {@code {"name","value"}} cursor leaks the key
     * string into the value slot.
     */
    private static android.database.Cursor valueCursor(Object[] args, String key, String value) {
        String[] projection = extractProjection(args);
        if (projection == null || projection.length == 0) {
            projection = new String[]{"name", "value"};
        }
        MatrixCursor cursor = new MatrixCursor(projection, 1);
        Object[] row = new Object[projection.length];
        for (int i = 0; i < projection.length; i++) {
            if ("value".equals(projection[i])) {
                row[i] = value;
            } else if ("name".equals(projection[i])) {
                row[i] = key;
            } else {
                row[i] = null;
            }
        }
        cursor.addRow(row);
        return cursor;
    }

    /**
     * Extracts the caller's projection from a query() invocation.
     *
     * <p>R3 audit 2026-10-05: interception is at the {@code IContentProvider}
     * level, not {@code ContentProvider} — the old code read {@code args[1]}
     * (the Uri) as the projection, so the branch never fired. The arg layout
     * differs by API: {@code query(AttributionSource, Uri, String[], Bundle,
     * ...)} on API 31+, {@code query(String callingPkg, Uri, String[],
     * Bundle, ...)} on API 30 (R4 2026-10-05: the single leading String is
     * the calling package, not two Strings). Instead of hardcoding indices,
     * anchor on the Uri: the projection is the {@code String[]} right after
     * it, and on API 30+ the projection may instead live inside the following
     * {@code queryArgs} Bundle under
     * {@code ContentResolver.QUERY_ARG_SQL_PROJECTION}.
     */
    private static String[] extractProjection(Object[] args) {
        if (args == null) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof android.net.Uri) {
                if (i + 1 < args.length && args[i + 1] instanceof String[]) {
                    String[] proj = (String[]) args[i + 1];
                    if (proj != null) {
                        return proj;
                    }
                }
                if (i + 2 < args.length && args[i + 2] instanceof Bundle) {
                    // Literal: ContentResolver.QUERY_ARG_SQL_PROJECTION
                    // ("android:query-arg-sql-projection") — the SDK constant
                    // is not in android.jar's stubs, but the framework string
                    // is stable.
                    String[] proj = ((Bundle) args[i + 2])
                            .getStringArray("android:query-arg-sql-projection");
                    if (proj != null) {
                        return proj;
                    }
                }
                return null;
            }
        }
        return null;
    }

    /** Returns which dev-mode key was requested, or null. */
    private static String requestedDevKey(Object[] args) {
        if (args == null) {
            return null;
        }
        for (Object arg : args) {
            if (arg instanceof String) {
                String s = (String) arg;
                if ("adb_enabled".equals(s) || "development_settings_enabled".equals(s)) {
                    return s;
                }
            } else if (arg instanceof String[]) {
                for (String s : (String[]) arg) {
                    if ("adb_enabled".equals(s) || "development_settings_enabled".equals(s)) {
                        return s;
                    }
                }
            } else if (arg instanceof Bundle) {
                // R4 audit 2026-10-05: same Bundle-bypass as the Android-ID
                // path — API 30+ query() carries the selection in the Bundle.
                if (bundleSelectsKey((Bundle) arg, "adb_enabled")) {
                    return "adb_enabled";
                }
                if (bundleSelectsKey((Bundle) arg, "development_settings_enabled")) {
                    return "development_settings_enabled";
                }
            }
        }
        return null;
    }
}
