package cn.itcast.mobilesafe.service;

import java.util.List;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.activity.MainActivity;
import cn.itcast.mobilesafe.domain.SmsInfo;
import cn.itcast.mobilesafe.engine.SmsInfoService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public class BackupSmsService extends Service {
	
	private SmsInfoService smsInfoService;
	private NotificationManager nm;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		smsInfoService = new SmsInfoService(this);
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		new Thread(){
			public void run() {
				//1 得到所有的短信
				//2 生成一个xml文件
				List<SmsInfo> smsInfos = smsInfoService.getSmsInfos();
				
				try {
					smsInfoService.createXml(smsInfos);
					//发送一个通知告诉用户备份完成
					Notification.Builder notifyBuilder = new Notification.Builder(getApplicationContext());
					notifyBuilder.setSmallIcon(R.drawable.callsms); //设置图标
					notifyBuilder.setTicker("短信备份完毕");
					notifyBuilder.setContentTitle("提示信息"); //设置标题
					notifyBuilder.setContentText("短信备份完毕"); //消息内容
					notifyBuilder.setWhen(System.currentTimeMillis()); //发送时间
					notifyBuilder.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
					notifyBuilder.setAutoCancel(true);//打开程序后图标消失
					notifyBuilder.setOngoing(true);
					// 该通知只是起到 “通知”的作用，不希望用户点击后有相应的跳转，那么，intent,pendingIntent这几行代码可以不写
					Intent intent = new Intent(getApplicationContext(),MainActivity.class);
					PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 100, intent, 0);
					notifyBuilder.setContentIntent(contentIntent);
					Notification notification = notifyBuilder.build();
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					nm.notify(100, notification);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//looper是一个消息泵,从消息队列（MessageQueue）里面抽取消息,把消息交给Handler处理
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "短信备份失败", 0).show();
					Looper.loop();
				}
				stopSelf();//停止服务
			};
		}.start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
