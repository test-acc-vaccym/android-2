package cn.itcast.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.adapter.MainAdapter;

public class MainActivity extends Activity {
	
	private GridView gv_main;
	private MainAdapter mAdapter ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		gv_main = (GridView) findViewById(R.id.gv_main);
		mAdapter = new MainAdapter(this);
		
		gv_main.setAdapter(mAdapter);
		
		gv_main.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mAdapter.notifyDataSetChanged();//让listview自动刷新  其实就是让getview()再次调用
	}
	
	private final class MyOnItemClickListener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = null;
			switch (position) {
			case 0://手机防盗
				intent = new Intent(getApplicationContext(),LostProtectedActivity.class);
				startActivity(intent);
				break;
			case 1://通讯卫士
				intent = new Intent(getApplicationContext(),BlackNumberListActivity.class);
				startActivity(intent);
				break;
			case 2://软件管理
				intent = new Intent(getApplicationContext(),AppManagerActivity.class);
				startActivity(intent);
				break;
			case 3://任务管理
				intent = new Intent(getApplicationContext(),TaskManagerActivity.class);
				startActivity(intent);
				break;
			case 4://流量管理
				intent = new Intent(getApplicationContext(),TrafficManagerActivity.class);
				startActivity(intent);
				break;
			case 7://高级工具
				intent = new Intent(getApplicationContext(),AtoolsActivity.class);
				startActivity(intent);
				break;
			case 8://设置中心
				intent = new Intent(getApplicationContext(),SettingCenterActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
		}
		
	}
}
