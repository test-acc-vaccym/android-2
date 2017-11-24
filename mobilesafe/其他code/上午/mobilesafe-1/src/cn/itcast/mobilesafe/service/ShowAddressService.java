package cn.itcast.mobilesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.engine.AddressQueryService;
//�Ե绰�ĺ���״̬���м�������ʾ���������
public class ShowAddressService extends Service {

	private TelephonyManager tm;
	private MyPhoneStateListener listener;
	private WindowManager wm;
	private LayoutInflater mInflater;
	private View view;
	private TextView tv_number;
	private TextView tv_address;
	private RelativeLayout rl_show_address;
	private AddressQueryService addressQueryService;
	private SharedPreferences sp;
	
	private boolean isadd = false;//��������ʾ�Ƿ���ʵ�������
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		//���ڹ������
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		//�����绰�ĺ���״̬
		listener = new MyPhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		view = mInflater.inflate(R.layout.show_address, null);
		rl_show_address = (RelativeLayout) view.findViewById(R.id.rl_show_address);
		tv_number = (TextView) view.findViewById(R.id.tv_number);
		tv_address = (TextView) view.findViewById(R.id.tv_address);
		
		addressQueryService = new AddressQueryService();
	}
	
	
	private final class MyPhoneStateListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				Toast.makeText(getApplicationContext(), "��ʾ������", 1).show();
				//���ݲ�ͬ�ķ�����öԲ���ָ����ͬ�ı���
				int address_bg = sp.getInt("address_bg", R.drawable.call_locate_orange);
				rl_show_address.setBackgroundResource(address_bg);
				
				//��ѯ������
				String address = addressQueryService.query(incomingNumber);
				tv_address.setText(address);
				
				//���ݺ����ѯ��ϵ��
				String name = null;
				Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(incomingNumber));
				Cursor c = getContentResolver().query(uri, 
						new String[]{PhoneLookup.DISPLAY_NAME}, 
						null, null, null);
				if(c.moveToFirst()){
					name = c.getString(0);
				}
				c.close();
				
				if(name != null){
					tv_number.setText(name);
				}else{
					tv_number.setText(incomingNumber);
				}
				
	            final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
	            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
	            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
	                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
	                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
	            int x = sp.getInt("x", 0);
	            int y = sp.getInt("y", 0);
	            params.x = params.x + x;
	            params.y = params.y + y;
	            params.format = PixelFormat.TRANSLUCENT;
	            params.type = WindowManager.LayoutParams.TYPE_TOAST;
	            //��Ӳ���
	            wm.addView(view, params);
	            isadd = true;
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if(isadd){
					wm.removeView(view);
				}
				break;

			default:
				break;
			}
		}
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//ȡ���绰״̬�ļ���
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
	}
}
