package cn.itcast.mobilesafe.receiver;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.dao.BlackNumberDao;
import cn.itcast.mobilesafe.engine.GPSInfoService;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsRecevier extends BroadcastReceiver {

	private SharedPreferences sp;
	private DevicePolicyManager devicePolicyManager;
	private BlackNumberDao blackNumberDao;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Log.i("i", "�Ѿ����ص��˶���");
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		blackNumberDao = new BlackNumberDao(context);
		//�жϱ����Ƿ���
		boolean isprotected = sp.getBoolean("isprotected", false);
		if(isprotected){
			devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for(Object pdu:pdus){
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])pdu);
				String body = smsMessage.getDisplayMessageBody();
				String address = smsMessage.getDisplayOriginatingAddress();
				String safe_number = sp.getString("safe_number", "");
				
				if("#*location*#".equals(body)){
					
					//��ȡ�ֻ���λ��
					//��λ�÷��͸���ȫ����
					//�жϹ㲥
					GPSInfoService service = GPSInfoService.getInstance(context);
					service.registenerLocationChangeListener();
					String last_location = service.getLastLocation();
					
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(safe_number, null, "shou ji de wei zhi :" + last_location, null, null);
					
					abortBroadcast();
				}else if("#*lockscreen*#".equals(body)){
					//����
					devicePolicyManager.lockNow();
					//��������
					devicePolicyManager.resetPassword("233223", 0);
					abortBroadcast();
				}else if("#*delete*#".equals(body)){
					//�ָ���������
					devicePolicyManager.wipeData(0);
					abortBroadcast();
				}else if("#*alarm*#".equals(body)){
					//������������
					MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
					mediaPlayer.setVolume(1.0f, 1.0f);
					mediaPlayer.start();
					abortBroadcast();
				}else if(body.contains("6+1") || body.contains("cctv")){
					abortBroadcast();
				}
				Log.i("i", body);
				
				//�ж��ǲ��Ǻ�������һ������
				boolean isBlackNumber = blackNumberDao.isBlackNumber(address);
				if(isBlackNumber){
					abortBroadcast();
				}
			}
		}
	}

}
