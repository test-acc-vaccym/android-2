package cn.itcast.mobilesafe.receiver;

import cn.itcast.mobilesafe.utils.TaskUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class KillProcessReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i("i", " �õ��㲥  ���������");
		TaskUtil.killAllProcess(context);
	}

}
