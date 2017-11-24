package cn.itcast.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import cn.itcast.mobilesafe.MobileSafeApplication;
import cn.itcast.mobilesafe.activity.EnterPasswordActivity;
import cn.itcast.mobilesafe.dao.AppLockDao;
import cn.itcast.mobilesafe.inter.IService;
import cn.itcast.mobilesafe.utils.Logger;

public class AppLockService extends Service {
	
	private ActivityManager am;
	private AppLockDao appLockDao;
	private List<String> appLocks;
	private List<String> tempAppLocks;//������ʱ������Ӧ��
	
	private MyContentObserver observer;
	
	//�ֻ������ͽ���֪���ķ���
	private KeyguardManager keyguardManager;
	
	private LockScreenReceiver receiver;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		appLockDao = new AppLockDao(this);
		appLocks = appLockDao.findAll();
		tempAppLocks = new ArrayList<String>();
		
		//ע��һ�����ݹ۲���
		observer = new MyContentObserver(new Handler());
		Uri uri = Uri.parse("content://applock/applock");
		getContentResolver().registerContentObserver(uri, true, observer);
		
		//ע�������㲥������
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		receiver = new LockScreenReceiver();
		registerReceiver(receiver, filter);
		
		new Thread(){
			public void run() {
				//����һ�����Ź�
				while(true){
					
					boolean isLockScreen = keyguardManager.inKeyguardRestrictedInputMode();
					if(isLockScreen){
						SystemClock.sleep(500);
						Logger.i("i", " ���ڴ�������ģʽ������");
					}else{
						Logger.i("i", " ���ڴ��ڼ���ģʽ������");
						//�õ���ǰ�������е�����ջ
						List<RunningTaskInfo>  runningTaskInfos = am.getRunningTasks(1);
						//�õ�ǰ̨��ʾ������ջ
						RunningTaskInfo runningTaskInfo = runningTaskInfos.get(0);
						ComponentName component = runningTaskInfo.topActivity;
						String packageName = component.getPackageName();
						//�жϵ�ǰ��Ӧ�ó����Ƿ��ڳ���������
						if(appLocks.contains(packageName)){
							
							if(!tempAppLocks.contains(packageName)){
								MobileSafeApplication application = (MobileSafeApplication) getApplication();
								application.setPackname(packageName);
								//������������Ľ���
								Intent intent = new Intent(getApplicationContext(),EnterPasswordActivity.class);
								//����Ҳ�ǲ�����������ջ
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
						}
						SystemClock.sleep(500);
					}
				}
			};
		}.start();
	}

	private final class MyContentObserver extends ContentObserver{

		public MyContentObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			appLocks = appLockDao.findAll();
			tempAppLocks.clear();
		}
	}
	
	
	private final class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			tempAppLocks.clear();
		}
		
	}
	
	private MyBinder ibinder = new MyBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return ibinder;
	}

	private final class MyBinder extends Binder implements IService{

		public void addTemp(String packageName) {
			// TODO Auto-generated method stub
			addTempAppLock(packageName);
		}
		
	}
	
	private void addTempAppLock(String packageName){
		tempAppLocks.add(packageName);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		//ȡ������
		getContentResolver().unregisterContentObserver(observer);
		
		//ȡ�������Ĺ㲥����
		unregisterReceiver(receiver);
	}
}
