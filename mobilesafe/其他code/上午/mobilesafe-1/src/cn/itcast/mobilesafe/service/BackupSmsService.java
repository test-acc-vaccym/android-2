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
				//1 �õ����еĶ���
				//2 ����һ��xml�ļ�
				List<SmsInfo> smsInfos = smsInfoService.getSmsInfos();
				
				try {
					smsInfoService.createXml(smsInfos);
					//����һ��֪ͨ�����û��������
					Notification notification = new Notification(R.drawable.notification, "���ű������", System.currentTimeMillis());
					Intent intent = new Intent(getApplicationContext(),MainActivity.class);
					PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 100, intent, 0);
					notification.setLatestEventInfo(getApplicationContext(), "��ʾ��Ϣ", "���ű������", contentIntent);
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					nm.notify(100, notification);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//looper��һ����Ϣ��,����Ϣ���У�MessageQueue�������ȡ��Ϣ,����Ϣ����Handler����
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "���ű���ʧ��", 0).show();
					Looper.loop();
				}
				stopSelf();//ֹͣ����
			};
		}.start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
