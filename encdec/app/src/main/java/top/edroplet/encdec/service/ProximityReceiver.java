package top.edroplet.encdec.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

/**
 * 注册一个广播接收者
 * Created by xw on 2017/5/5.
 */

public class ProximityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isEnter = intent.getBooleanExtra( LocationManager.KEY_PROXIMITY_ENTERING, false);
        if(isEnter) Toast.makeText(context, "你已到达南软B1栋附近", Toast.LENGTH_LONG).show();
        else Toast.makeText(context, "你已离开南软B1栋附近", Toast.LENGTH_LONG).show();
    }
}
