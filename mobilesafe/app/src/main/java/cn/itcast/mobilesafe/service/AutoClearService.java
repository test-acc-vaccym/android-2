package cn.itcast.mobilesafe.service;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.activity.MainActivity;
import cn.itcast.mobilesafe.utils.TaskUtil;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;

public class AutoClearService extends Service {

	private SharedPreferences sp;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);

		// sdk 2.0 及其以后版本使用的方法是 startForeground 与 stopForeground，之前版本使用的是 setForeground ，因此如果你应用程序的最低运行环境要求是 2.0，那么这里可以直接运用新方法，如果运行环境是2.0以下，那么为了保证向后兼容性，这里必须使用反射技术来调用新方法。
		// 移到onStartCommand里
		// setForeground(true);//设置为前台进程
		Notification.Builder notifyBuilder = new Notification.Builder(getApplicationContext());
		notifyBuilder.setSmallIcon(R.drawable.notification); //设置图标
		notifyBuilder.setTicker("显示第二个通知");
		notifyBuilder.setContentTitle("通知"); //设置标题
		notifyBuilder.setContentText("点击查看详细内容"); //消息内容
		notifyBuilder.setWhen(System.currentTimeMillis()); //发送时间
		notifyBuilder.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
		notifyBuilder.setAutoCancel(true);//打开程序后图标消失
		notifyBuilder.setOngoing(true);
		// 该通知只是起到 “通知”的作用，不希望用户点击后有相应的跳转，那么，intent,pendingIntent这几行代码可以不写
		// Intent intent =new Intent (MainActivity.this,Center.class);
		// PendingIntent pendingIntent =PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
		// notifyBuilder.setContentIntent(pendingIntent);
		Notification notification = notifyBuilder.build();
		// notificationManager.notify(124, notification1); // 通过通知管理器发送通知

		startForeground(1, notification);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		LockScreenReceiver receiver = new LockScreenReceiver();
		registerReceiver(receiver, filter);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private final class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//清理内存的操作
			boolean isautoclear = sp.getBoolean("isautoclear", false);
			if(isautoclear){
				TaskUtil.killAllProcess(context);
			}
		}
		
	}

}
