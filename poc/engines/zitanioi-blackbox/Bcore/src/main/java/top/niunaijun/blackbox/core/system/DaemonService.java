package top.niunaijun.blackbox.core.system;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.utils.compat.BuildCompat;


/**
 * Created by Milk on 3/2/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class DaemonService extends Service {
    public static final String TAG = "DaemonService";
    private static final int NOTIFY_ID = BlackBoxCore.getHostPkg().hashCode();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent innerIntent = new Intent(this, DaemonInnerService.class);
        startService(innerIntent);
        if (BuildCompat.isOreo()) {
            showNotification();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private void showNotification() {
        // FIX (2026-10-01): the builder previously set no small icon. On Android 8.0+
        // startForeground() with an iconless notification throws
        // RemoteServiceException$CannotPostForegroundServiceNotificationException
        // ("Bad notification for startForeground"), killing the :black daemon process.
        // Use the host app's launcher icon so the notification shows the real app
        // logo; fall back to a framework icon if the host declares none.
        int icon = getApplicationInfo().icon;
        if (icon == 0) {
            icon = android.R.drawable.ic_dialog_info;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getPackageName() + ".blackbox_core")
                .setSmallIcon(icon)
                .setContentTitle("Privacy Sandbox")
                .setContentText("Sandbox runtime is active")
                .setPriority(NotificationCompat.PRIORITY_MAX);
        startForeground(NOTIFY_ID, builder.build());
    }

    public static class DaemonInnerService extends Service {
        @Override
        public void onCreate() {
            Log.i(TAG, "DaemonInnerService -> onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i(TAG, "DaemonInnerService -> onStartCommand");
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(NOTIFY_ID);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onDestroy() {
            Log.i(TAG, "DaemonInnerService -> onDestroy");
            super.onDestroy();
        }
    }
}
