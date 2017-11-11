package com.example.appupdate;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AppVersion av = new AppVersion();
		av.setApkName("AppUpdate.apk");
		av.setSha1("FCDA0D0E1E7D620A75DA02A131E2FFEDC1742AC8");
		av.setAppName("博客园");
		av.setUrl("http://down.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk");
		av.setContent("1、测试升级;2、测试升级2！！;3、一大波功能！");
		av.setVerCode(2);
		av.setVersionName("1.1");
		AppUpdateUtils.init(MainActivity.this, av, true,false);
		AppUpdateUtils.upDate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
