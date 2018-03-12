package com.example.tcpdemo;

import java.util.ArrayList;
import mediatek.android.IoTManager.SmartConnection;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tcpdemo.adapter.DeviceAdapter;
import com.example.tcpdemo.smartlink.DeviceInfoCache;
import com.example.tcpdemo.smartlink.FindDeviceInfohlk2;
import com.example.tcpdemo.smartlink.NetworkUtils;
import com.example.tcpdemo.socket.XlinkUtils;
import com.example.udp_tcp_demo.R;

public class DeviceListActivity extends BaseActivity {
	private ListView listview;
	private ProgressBar ProgressBar1;
	int ip;
	private TextView title;
	protected final static int MENU_ADD = Menu.FIRST;
	protected final static int MENU_DELETE = Menu.FIRST + 1;
	protected final static int MENU_VERSION = Menu.FIRST + 2;
	private TextView bntSearch;
	private DeviceAdapter adapter = null;
	private FindDeviceInfohlk2 findDevice = new FindDeviceInfohlk2();
	private ArrayList<DeviceInfoCache> deviceList = new ArrayList<DeviceInfoCache>();
	private RelativeLayout m_rlParent;
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	
    	m_rlParent = (RelativeLayout) findViewById(R.id.rlParent);
		ProgressBar1 = (ProgressBar)findViewById(R.id.progressBar1);
		bntSearch = (TextView) findViewById(R.id.tvFindDevice);
		adapter = new DeviceAdapter(deviceList, this);
    	listview = (ListView)findViewById(R.id.listView);
    	listview.setAdapter(adapter);
    	listview.setOnItemClickListener(new ListView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int postion,
					long arg3) {
				WifiManager wifimanager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
				if(wifimanager.isWifiEnabled())
				{
					
				}
				else
				{
					Toast.makeText(DeviceListActivity.this,getResources().getString(R.string.please_connect_wifi),Toast.LENGTH_LONG).show();
				}
			}    		
    	});
    	findViewById(R.id.tvFindDevice).setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				deviceList.clear();
				adapter.updateList(deviceList);
				
				ProgressBar1.setVisibility(View.VISIBLE);
				bntSearch.setVisibility(View.GONE);
				
				handler.post(findRunnable);
				handler.sendEmptyMessageDelayed(FIND_STOP, 1000 * 5);//ֹ
			}
    	});
    	
    	findViewById(R.id.tvAboutUs).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(DeviceListActivity.this, AboutUsActivity.class));
			}
		});
    	
		
		IntentFilter mFilter = new IntentFilter();
	    mFilter.addAction(BaseVolume.CONNECT_DEVICE_ERROR); 
	    mFilter.addAction(BaseVolume.CONNECT_DEVICE_OK); 
	    registerReceiver(mReceiver, mFilter);
    	
    }
    
    
    private View popupWindowViewLong;
	private PopupWindow m_popupWindowLong;
	private void showSelectConfig() {
			if (popupWindowViewLong == null) {
				LayoutInflater layoutInflater = LayoutInflater.from(this); 
				popupWindowViewLong = layoutInflater.inflate(R.layout.popupwindo_device_long, null);
				popupWindowViewLong.findViewById(R.id.vLeft).setOnClickListener(hidePop);
				popupWindowViewLong.findViewById(R.id.vRight).setOnClickListener(hidePop);
				popupWindowViewLong.findViewById(R.id.vTop).setOnClickListener(hidePop);
				popupWindowViewLong.findViewById(R.id.vBottom).setOnClickListener(hidePop);
				
				popupWindowViewLong.findViewById(R.id.tvSmartConfig).setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						m_popupWindowLong.dismiss();
						Intent intent2 = new Intent(DeviceListActivity.this,SmartConnection.class);
						startActivity(intent2);
					}
				});
				popupWindowViewLong.findViewById(R.id.tvSmartLink).setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						m_popupWindowLong.dismiss();
					}
				});

				popupWindowViewLong.findViewById(R.id.tvSmartLink).setOnClickListener(new Button.OnClickListener(){
					public void onClick(View v) {
						WifiManager wifimanager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
						if(wifimanager.isWifiEnabled()) {
						}
						else {
							Toast.makeText(DeviceListActivity.this,getResources().getString(R.string.please_connect_wifi),Toast.LENGTH_LONG).show();
						}
					}
				});
			}
			if (m_popupWindowLong == null) {
				m_popupWindowLong= new PopupWindow(popupWindowViewLong, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); 
			}
			m_popupWindowLong.setFocusable(true);
			m_popupWindowLong.showAtLocation(m_rlParent, Gravity.CENTER, 0, 0);
	}
	
	private View.OnClickListener hidePop = new View.OnClickListener() {
		public void onClick(View v) {
			if (m_popupWindowLong != null) {
				if (m_popupWindowLong.isShowing()) {
					m_popupWindowLong.dismiss();
				}
			}
		}
	};
    
    Runnable findRunnable = new Runnable() {
		public void run() {
			findDevice.startFindCommand(DeviceListActivity.this, handler);
			handler.postDelayed(this, 1000);
		}
	};
    
	private final int FIND_STOP = 111;
	Handler handler = new Handler(){   
	    public void handleMessage(Message msg) {
	    	switch (msg.what) {
			case NetworkUtils.FIND_DEVICE_MAC:
				Bundle bundle = (Bundle) msg.obj;
				
				DeviceInfoCache newDevice = (DeviceInfoCache) bundle.getSerializable(NetworkUtils.DEVIEC_INFO);
				updateList(newDevice);
				
				break;
			case FIND_STOP:
				
				ProgressBar1.setVisibility(View.GONE);
				bntSearch.setVisibility(View.VISIBLE);
				
				handler.removeCallbacks(findRunnable);
				
				break;

			default:
				break;
			}
	    }
	      
	};  
	
	private void updateList(DeviceInfoCache newDevice) {
		boolean isHave = false;
		for (DeviceInfoCache device : deviceList) {
			if (device.getMac().equalsIgnoreCase(newDevice.getMac()) || device.getIp().equalsIgnoreCase(newDevice.getIp())) {
				isHave = true;
			}
		}
		if (!isHave) {
			deviceList.add(newDevice);
			adapter.updateList(deviceList);
		}
		
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
		    String action = intent.getAction();
		    // 连接成功！
            if (action.equals(BaseVolume.CONNECT_DEVICE_OK)) {
            	String deviceUser = intent.getStringExtra(BaseVolume.DEVICE_MAC);
            	Toast.makeText(DeviceListActivity.this, deviceUser+"连接成功！", Toast.LENGTH_SHORT).show();
            	
        		for (DeviceInfoCache device : deviceList) {
            		if (device.getMac().equals(deviceUser)) {
            			device.setConnectiong(false);
            		}
				}
        		adapter.updateList(deviceList);
        		// 跳转页面
            }
            else if (action.equals(BaseVolume.CONNECT_DEVICE_ERROR)) {
            	String deviceUser = intent.getStringExtra(BaseVolume.DEVICE_MAC);
            	Toast.makeText(DeviceListActivity.this, deviceUser+"连接失败！", Toast.LENGTH_SHORT).show();
            	for (DeviceInfoCache device : deviceList) {
            		if (device.getMac().equals(deviceUser)) {
            			device.setConnectiong(false);
            		}
				}
            	adapter.updateList(deviceList);
            }
		}
    };
	
	
    protected void onDestroy() {
    	super.onDestroy();
    	
    	unregisterReceiver(mReceiver);
    	
    }
    
}