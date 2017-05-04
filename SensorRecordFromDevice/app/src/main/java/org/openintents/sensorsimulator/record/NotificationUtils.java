package org.openintents.sensorsimulator.record;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 兼容所有SDK的NotificationUtil
 *
 * @author Mr.Yang on 2016-02-18  20:34.
 * @version 1.0
 * @desc
 */

/**
 * Notification需要使用NotificationManager来管理，一般来讲创建并显示Notification需要以下5个步骤：
 *
 * 1. 通过getSystemService方法获取一个NotificationManager对象
 * 2. 创建一个Notification对象，在这里我们使用兼容较好的NotificationUtils类来创建
 * 3. 由于Notification可以与应用程序脱离，也就是说，即使应用程序被关闭，Notification仍然会显示在状态栏中，
 *      当应用程序再此启动后，又可以重新控制这些Notification，如清除或者替换他们。因此，需要创建一个PendingIntent对象。该对象由Android系统负责维护，因此在应用程序关闭后，该对象仍然不会被释放。
 * 4. 使用Notification类的setLatestEventInfo方法设置详细信息（改方法已经在6.0废弃，可使用提供的工具类来代替）
 * 5. 使用NotificationManager类的notify方法显示Notification。再这一步需要指定标识Notification的唯一ID，
 *      改ID必须相对于同一个NotificationManager对象是唯一的，否则就会覆盖相同ID的Notification。
 */
public class NotificationUtils {
    private static final String TAG = "NotificationUtils";

    public static Notification createNotification(Context context, PendingIntent pendingIntent, String title, String text, int iconId) {
        Notification notification;
        if (isNotificationBuilderSupported()) {
            Log.d(TAG,"isNotificationBuilderSupported");
            notification = buildNotificationWithBuilder(context, pendingIntent, title, text, iconId);
        } else {
            // 低于API 11 Honeycomb
            Log.d(TAG,"buildNotificationPreHoneycomb");
            notification = buildNotificationPreHoneycomb(context, pendingIntent, title, text, iconId);
        }
        return notification;
    }

    public static boolean isNotificationBuilderSupported() {
        try {
            return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) && Class.forName("android.app.Notification.Builder") != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    private static Notification buildNotificationPreHoneycomb(Context context, PendingIntent pendingIntent, String title, String text, int iconId) {
        Notification notification = new Notification(iconId, "", System.currentTimeMillis());
        try {
            // try to call "setLatestEventInfo" if available
            Method m = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
            m.invoke(notification, context, title, text, pendingIntent);
        } catch (Exception e) {
            // do nothing
        }
        return notification;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("deprecation")
    private static Notification buildNotificationWithBuilder(Context context, PendingIntent pendingIntent, String title, String text, int iconId) {
        android.app.Notification.Builder builder = new android.app.Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(iconId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return builder.build();
        } else {
            return builder.getNotification();
        }
    }

    /**
     * 全部兼容
     * http://blog.csdn.net/yangshangwei/article/details/50688221
     */
    public static void showNotification(Context context, Intent intent, int showIcon, String title, String text, CharSequence tickerText) {
        NotificationManager notificationManager;
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        /*
        返回一个PendingIntent对象，这个对象与一个Activity对象关联，这个案例中与当前的Activity关联。
        将Android状态栏滑下来后，单击Notification，就会显示关联的这个Activity。如果Activity已经显示，
        仍然会显示一个新的Activity，并覆盖当前显示的Activity。不过这些显示的Activity都是一样的，
        除了getActivity方法之外，还可以getBroacast和getService方法。
        这两个方法用于单击Notification后发出一条广播或者启动一个服务。
        */
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // 工具类判断版本，通过不同的方式获取Notification
        Notification notification = createNotification(context, pendingIntent, title, text, showIcon);
        notification.tickerText = tickerText;
        notification.when = System.currentTimeMillis();
        // 使用默认的声音
        notification.defaults = Notification.DEFAULT_SOUND;
        // 使用默认的震动 需要添加uses-permission  android.permission.VIBRATE
        notification.defaults = Notification.DEFAULT_VIBRATE;
        // 使用默认的Light
        notification.defaults = Notification.DEFAULT_LIGHTS;
        // 所有的都是用默认值
        notification.defaults = Notification.DEFAULT_ALL;

        notificationManager.notify(showIcon, notification);

        // 5S后，执行取消的方法，即5S后 自动清除该通知栏 ,根据需求考虑是否需要这样
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                notificationManager.cancel(R.drawable.flag_mark_blue);
//            }
//        },5*1000);

    }
}
