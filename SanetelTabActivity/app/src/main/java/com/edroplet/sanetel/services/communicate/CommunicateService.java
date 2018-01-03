package com.edroplet.sanetel.services.communicate;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.edroplet.sanetel.fragments.functions.FunctionsFragmentMonitor;
import com.edroplet.sanetel.services.CommunicateWithDeviceService;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CommunicateService extends Service {

    public static final String SERVICE_NAME = CommunicateWithDeviceService.class.getName();
    private static final String TAG = CommunicateService.class.getSimpleName();

    /**
     * 定时唤醒的时间间隔，5分钟
     */
    private final static int ALARM_INTERVAL = 10000; //每秒钟执行一次 5 * 60 * 1000;
    private final static int COMMUNICATE_REQUEST_CODE = 60000;

    private final static int COMMUNICATE_SERVICE_ID = -1001;

    @Override
    public void onCreate() {
        super.onCreate();

        // 这里动态注册广播， 静态注册的没法接收
        if (null == communicateDataReceiver && CommunicateDataReceiver.isAlive(this)) {
            communicateDataReceiver = new CommunicateDataReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(CommunicateDataReceiver.ACTION_KEEP_ALIVE);
            intentFilter.addAction(CommunicateDataReceiver.ACTION_RECEIVE_DATA);
            intentFilter.addAction(CommunicateDataReceiver.ACTION_SAVE_FILE);
            intentFilter.addAction(CommunicateDataReceiver.ACTION_STOP_SAVE);
            intentFilter.addAction(CommunicateDataReceiver.ACTION_SEND_DATA);
            intentFilter.addAction(CommunicateDataReceiver.ACTION_SCREEN_OFF);
            intentFilter.addAction(CommunicateDataReceiver.ACTION_SCREEN_ON);
            intentFilter.addAction(CommunicateDataReceiver.ACTION_POWER_CONNECTED);
            intentFilter.addAction(CommunicateDataReceiver.ACTION_BOOT_COMPLETED);
            intentFilter.addAction(CommunicateWithDeviceService.ACTION_DATA_RESULT);
            intentFilter.addAction(FunctionsFragmentMonitor.ACTION_RECEIVE_MONITOR_INFO);
            intentFilter.addAction(FunctionsFragmentMonitor.ACTION_RECEIVE_AMPLIFIER_INFO);
            registerReceiver(communicateDataReceiver, intentFilter);
        }
    }

    CommunicateDataReceiver communicateDataReceiver;
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (communicateDataReceiver != null) {
            unregisterReceiver(communicateDataReceiver);
            communicateDataReceiver = null;
        }
        startService(new Intent(CommunicateService.this, CommunicateService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(COMMUNICATE_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, CommunicateInnerService.class);
            startService(innerIntent);
            startForeground(COMMUNICATE_SERVICE_ID, new Notification());
        }

        //发送唤醒广播来促使挂掉的UI进程重新启动起来
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(CommunicateDataReceiver.ACTION_KEEP_ALIVE);
        // alarmIntent.setAction(CommunicateDataReceiver.ACTION_KEEP_ALIVE);
        PendingIntent operation = PendingIntent.getBroadcast(this, COMMUNICATE_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_INTERVAL, operation);

        return START_STICKY;
    }


    public static class CommunicateInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(COMMUNICATE_SERVICE_ID, new Notification());
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }
}
