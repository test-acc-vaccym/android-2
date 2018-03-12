package mediatek.android.IoTManager;

import java.util.Date;
import java.util.List;

import com.example.udp_tcp_demo.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public class SmartConnection extends Activity {
	
	private byte AuthModeOpen = 0x00;
	private byte AuthModeShared = 0x01;
	private byte AuthModeAutoSwitch = 0x02;
	private byte AuthModeWPA = 0x03;
	private byte AuthModeWPAPSK = 0x04;
	private byte AuthModeWPANone = 0x05;
	private byte AuthModeWPA2 = 0x06;
	private byte AuthModeWPA2PSK = 0x07;   
	private byte AuthModeWPA1WPA2 = 0x08;
	private byte AuthModeWPA1PSKWPA2PSK = 0x09;
	private static final String TAG = "SmartConnection";
	private Button mButtonStart; 
	private Button mButtonStop; 
	private EditText mEditSSID;
	private EditText mEditPassword;
	private TextView mAuthModeView;
	private CheckBox mCheckBox;
	private int DEFAULT_WAIT_TIME = 1000;
	private WifiManager mWifiManager;
	private String mConnectedSsid;
	private String mConnectedPassword;
	private String mAuthString;
	private byte mAuthMode;
	private Button btn_back;
	private TextView title;
	private ProgressDialog mProgressDialog;
	private SmartConfigMethod smartConfigMethod = null;
	private LoadingDialog loading;
	private TextView m_tvLog;
	private ScrollView scrollView;
	private RadioButton rdBtn1,rdBtn4;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.smartconnection);
		
		smartConfigMethod = new SmartConfigMethod(this,null);
		rdBtn1 = (RadioButton) findViewById(R.id.rdBtn1);
		rdBtn4 = (RadioButton) findViewById(R.id.rdBtn4);
		
		loading = new LoadingDialog(this, R.style.LoadingDialogStyle);
		IntentFilter mFilter = new IntentFilter();
	    mFilter.addAction(SmartConfigMethod.FIND_DEVICE_MESSAGE); 
	    mFilter.addAction(SmartConfigMethod.STOP_CONFIG); 
	    mFilter.addAction(SmartConfigMethod.BACK_CONFIG_DEVICE); 
	    mFilter.addAction(SmartConfigMethod.BACK_FIND_DEVICE); 
	    registerReceiver(mReceiver, mFilter);
		
	    scrollView = (ScrollView) findViewById(R.id.scrollView);
		btn_back=(Button)findViewById(R.id.bnt_global_back1);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		title =  (TextView)findViewById(R.id.title);
//		title.setText("һ������");
//		title.setText("SmartConfig");
		title.setText("Elian");
		
		mButtonStart = (Button)findViewById(R.id.StartButton);
		mButtonStop = (Button)findViewById(R.id.StopButton);
		mEditSSID = (EditText)findViewById(R.id.SSIDEdit);
		mEditPassword = (EditText)findViewById(R.id.PasswordEdit);
		mAuthModeView = (TextView)findViewById(R.id.AuthModeView);
		mCheckBox= (CheckBox)findViewById(R.id.PwdCheck);
		
		m_tvLog = (TextView) findViewById(R.id.tvLog);
		
		mWifiManager = (WifiManager)getApplicationContext().getSystemService (Context.WIFI_SERVICE);
		if(mWifiManager.isWifiEnabled()){
        	WifiInfo WifiInfo = mWifiManager.getConnectionInfo();
        	mConnectedSsid = WifiInfo.getSSID();
			int iLen = mConnectedSsid.length();
			if (mConnectedSsid.startsWith("\"") && mConnectedSsid.endsWith("\""))
			{
				mConnectedSsid = mConnectedSsid.substring(1, iLen - 1);
			}
			mEditSSID.setText(mConnectedSsid);
			Log.d(TAG, "SSID = " + mConnectedSsid);
			
			List<ScanResult> ScanResultlist = mWifiManager.getScanResults();
			for (int i = 0, len = ScanResultlist.size(); i < len; i++) {
				ScanResult AccessPoint = ScanResultlist.get(i);			
				
				if (AccessPoint.SSID.equals(mConnectedSsid))
				{		
					boolean WpaPsk = AccessPoint.capabilities.contains("WPA-PSK");
		        	boolean Wpa2Psk = AccessPoint.capabilities.contains("WPA2-PSK");
					boolean Wpa = AccessPoint.capabilities.contains("WPA-EAP");
		        	boolean Wpa2 = AccessPoint.capabilities.contains("WPA2-EAP");
					
					if (AccessPoint.capabilities.contains("WEP"))
					{
						mAuthString = "OPEN-WEP";
						mAuthMode = AuthModeOpen;
						break;
					}

					if (WpaPsk && Wpa2Psk)
					{
						mAuthString = "WPA-PSK WPA2-PSK";
						mAuthMode = AuthModeWPA1PSKWPA2PSK;
						break;
					}
					else if (Wpa2Psk)
					{
						mAuthString = "WPA2-PSK";
						mAuthMode = AuthModeWPA2PSK;
						break;
					}
					else if (WpaPsk)
					{
						mAuthString = "WPA-PSK";
						mAuthMode = AuthModeWPAPSK;
						break;
					}

					if (Wpa && Wpa2)
					{
						mAuthString = "WPA-EAP WPA2-EAP";
						mAuthMode = AuthModeWPA1WPA2;
						break;
					}
					else if (Wpa2)
					{
						mAuthString = "WPA2-EAP";
						mAuthMode = AuthModeWPA2;
						break;
					}
					else if (Wpa)
					{
						mAuthString = "WPA-EAP";
						mAuthMode = AuthModeWPA;
						break;
					}				
					
					mAuthString = "OPEN";
					mAuthMode = AuthModeOpen;
					
				}
			}
			
			Log.d(TAG, "Auth Mode = " + mAuthString);
			
			mAuthModeView.setText(mAuthString);
		}
		
//		Log.d(TAG, "Get password : " + Password +" by SSID = " + mConnectedSsid);
		mCheckBox.setOnCheckedChangeListener(mShowPaswordListener);
		mButtonStart.setOnClickListener(mButtonStartListener);
		mButtonStop.setOnClickListener(mButtonStopListener);
		mEditPassword.requestFocus();
		
	}
	
	CheckBox.OnCheckedChangeListener mShowPaswordListener = new CheckBox.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			if (isChecked)
			{
				mEditPassword.setInputType(0x90);
			}
			else
			{
				mEditPassword.setInputType(0x81);
			}
		}
	};
	
	Button.OnClickListener mButtonStartListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			String SSID = mEditSSID.getText().toString();
			String Password = mEditPassword.getText().toString();

			Log.d(TAG, "Smart connection with : ssid = " + SSID +" Password = " + Password);
//			loading.show();
			smartConfigMethod.ConfigAndFindDevice(SSID, Password,rdBtn1.isChecked(),rdBtn4.isChecked());
			m_tvLog.setText("");
			mButtonStart.setEnabled(false);
			mButtonStop.setEnabled(true);
		}
	};
	
	Button.OnClickListener mButtonStopListener = new Button.OnClickListener() {

		public void onClick(View arg0) {
			
			Log.d(TAG, "Smart connection Stop ");
			mButtonStart.setEnabled(true);
			mButtonStop.setEnabled(false);	
//			SmartConfigMethod.count = -1;
			SmartConfigMethod.isOperation = false;
//			IoTManager.StopSmartConnection();
		}
	};
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
		    String action = intent.getAction();
            if (action.equals(SmartConfigMethod.FIND_DEVICE_MESSAGE)) {
            	Bundle bundle = intent.getBundleExtra(SmartConfigMethod.NEW_DEVICE);
            	String mac = bundle.getString("mac");
            	String ip = bundle.getString("ip");
            	String bjTime = getTime();
            	m_tvLog.setText(m_tvLog.getText().toString() +"\n"+bjTime+"�ҵ��豸��Mac��"+mac+"\nIP��"+ip);
            	scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            	Toast.makeText(SmartConnection.this, "�ҵ��豸��Mac��"+mac+"��\nIP��"+ip, Toast.LENGTH_SHORT).show();
            }
            else if (action.equals(SmartConfigMethod.STOP_CONFIG)) {
            	loading.dismiss();
            	mButtonStart.setEnabled(true);
    			mButtonStop.setEnabled(false);	
            	Toast.makeText(SmartConnection.this, "��������", Toast.LENGTH_SHORT).show();
            }
            else if (action.equals(SmartConfigMethod.BACK_CONFIG_DEVICE)) {
            	String bjTime = getTime();
            	scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            	m_tvLog.setText(m_tvLog.getText().toString() +"\n"+bjTime+"��������......");
            }
            else if (action.equals(SmartConfigMethod.BACK_FIND_DEVICE)) {
            	String bjTime = getTime();
            	scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            	m_tvLog.setText(m_tvLog.getText().toString() +"\n"+bjTime+"�����豸......");
            }
		}
	};
	
	private String getTime(){
		String pattern = "kk:mm:ss SS";//�������ڸ�ʽ
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(pattern);//�趨���ڸ�ʽ
		java.util.Date date = new Date();
		return  df.format(date);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
		SmartConfigMethod.isOperation = false;
//		SmartConfigMethod.count = -1;
//		SmartConfigMethod.findCount = -1;
	}
	
}
