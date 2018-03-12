package mediatek.android.IoTManager;

import com.example.smartlink_android.helper.NetworkUtils;
import com.mediatek.elian.ElianNative;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

public class SmartConfigMethod {

	
	private  Context context;
	private  String strSSID = "";
	private  String strPWD = "";
	private  int iTimer = -1;
	private final int CONFIG_DEVICE = 111;
	private FindDeviceIP findIP = new FindDeviceIP();
	public static final int FIND_DEVICE = 999;
	public static final String FIND_DEVICE_MESSAGE = "FIND_DEVICE_MESSAGE";
	public static final String NEW_DEVICE = "NEW_DEVICE";
	public static final String STOP_CONFIG = "STOP_CONFIG";
	public static final String BACK_CONFIG_DEVICE = "BACK_CONFIG_DEVICE";
	public static final String BACK_FIND_DEVICE = "BACK_FIND_DEVICE";
	public static boolean isOperation = false;
	private ElianNative elian;
	private String custom = "";
	private Handler parHandler;
	public SmartConfigMethod(Context con,Handler ph) {
		context = con;
		boolean isLoadLib = ElianNative.LoadLib();
		elian = new ElianNative();
		this.parHandler = ph;
	}
	public void ConfigAndFindDevice(String ssid,String pwd,boolean v1,boolean v4) {
		strSSID = ssid;
		strPWD = pwd;
		count = 0;
		isOperation = true;
		
		if (v1) {
			elian.InitSmartConnection(null, 1, 0);
		}
		if (v4) {
			elian.InitSmartConnection(null, 1, 1);
		}
		
		handler1.sendEmptyMessage(CONFIG_DEVICE);
		elian.StartSmartConnection(strSSID, strPWD, custom, (byte)9);
	}
	
	public static int count = -1;
	Handler handler1 = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CONFIG_DEVICE:
				if (isOperation) {
					if(count >= 0 && count < 30 ) {
						handler1.sendEmptyMessageDelayed(CONFIG_DEVICE, 1000 * 1);
						++count;
					}
					else {
						elian.StopSmartConnection();
						parHandler.sendEmptyMessage(NetworkUtils.ELIAN_STOP);
					}
				}
				else {
					elian.StopSmartConnection();
					parHandler.sendEmptyMessage(NetworkUtils.ELIAN_STOP);
				}
				break;
			default:
				break;
			}
		}
	};
	
	public void stopConfig() {
		isOperation = false;
	}
	
}
