package com.example.tcpdemo;


import com.example.udp_tcp_demo.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 欢迎界面
 * @author Denny
 *
 */
public class WelcomeActivity extends BaseActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		new Thread(){
			public void run() {
				handler.sendEmptyMessageDelayed(1, 1500);
			}
		}.start();
		
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			startActivity(new Intent(WelcomeActivity.this, DeviceListActivity.class));
			finish();
		}
		
	};
	
	protected void onDestroy() {
		super.onStop();
		
	};
	
}
