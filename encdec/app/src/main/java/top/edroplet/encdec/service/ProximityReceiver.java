package top.edroplet.encdec.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import top.edroplet.encdec.R;
import top.edroplet.encdec.activities.system.GpsActivity;

/**
 * 注册一个广播接收者
 * Created by xw on 2017/5/5.
 */

public class ProximityReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 1;
    private WeakReference<GpsActivity> mActivity;
    long lat, lon;
    public ProximityReceiver(GpsActivity activity) {
        mActivity = new WeakReference<GpsActivity>(activity);
    }

    public ProximityReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        if (false) {
            long[] vibratePatern = {0, 300, 200, 300, 200, 300};
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Intent notificationIntent = new Intent(context, GpsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT); // == Intent.FLAG_ACTIVITY_NEW_TASK
            Notification.Builder notificationBuilder = new Notification.Builder(context)
                    .setTicker("near you")
                    .setContentText("Added by")
                    .setContentTitle("beach")
                    .setSmallIcon(R.drawable.icon)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSound(alarmSound)
                    .setVibrate(vibratePatern);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
        if (true){
            String key = LocationManager.KEY_PROXIMITY_ENTERING;
            Bundle results = getResultExtras(true);
            Boolean entering = intent.getBooleanExtra(key, false);

            lat = intent.getLongExtra("location-lat ", lat);
            lon = intent.getLongExtra("location-lon ", lon);
            int ID = intent.getIntExtra("ID", 0);

            Log.i("ProximityIntentReceiver", "coordinate-lat " + lat );
            Log.i("ProximityIntentReceiver", "coordinate-lon " + lon );
            Log.i("ProximityIntentReceiver", String.format("ID: %d entering: %s", ID, entering?"true":"false"));
            GpsActivity a = mActivity.get();
            if (a != null)
                a.onProximityAlert(ID, entering);

            // Vibrate for 2 seconds
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(GpsActivity.PROX_ALERT_INTENT), PendingIntent.FLAG_UPDATE_CURRENT);

            Notification noti = new Notification.Builder(context)
                    .setContentTitle("Location Alert ")
                    .setContentText("Entering Point of Interest")
                    .setSmallIcon(R.drawable.icon)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build(); // available from API level 11 and onwards

            notificationManager.notify(NOTIFICATION_ID, noti);
        }

        boolean isEnter = intent.getBooleanExtra( LocationManager.KEY_PROXIMITY_ENTERING, false);
        if(isEnter){
            Toast.makeText(context, "你已到达南软B1栋附近", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "你已离开南软B1栋附近", Toast.LENGTH_LONG).show();
        }
    }
}
