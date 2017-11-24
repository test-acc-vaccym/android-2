package cn.itcast.mobilesafe.activity;

import net.youmi.android.AdManager;
import net.youmi.android.AdView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.adapter.MainAdapter;

public class MainActivity extends Activity {
	
	private GridView gv_main;
	private MainAdapter mAdapter ;
	private LinearLayout ll_ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// Ӧ��Id Ӧ������ ���������(s) ����ģʽ
		AdManager.init(this,"537ef88653a2993c", "b9e10bcfe994a9fb", 30, true);
		setContentView(R.layout.main);
		ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
		
		
		AdView adView = new AdView(this);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		ll_ad.addView(adView, params);
		
		gv_main = (GridView) findViewById(R.id.gv_main);
		mAdapter = new MainAdapter(this);
		
		gv_main.setAdapter(mAdapter);
		
		gv_main.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mAdapter.notifyDataSetChanged();//��listview�Զ�ˢ��  ��ʵ������getview()�ٴε���
	}
	
	private final class MyOnItemClickListener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = null;
			switch (position) {
			case 0://�ֻ�����
				intent = new Intent(getApplicationContext(),LostProtectedActivity.class);
				startActivity(intent);
				break;
			case 1://ͨѶ��ʿ
				intent = new Intent(getApplicationContext(),BlackNumberListActivity.class);
				startActivity(intent);
				break;
			case 2://�������
				intent = new Intent(getApplicationContext(),AppManagerActivity.class);
				startActivity(intent);
				break;
			case 3://�������
				intent = new Intent(getApplicationContext(),TaskManagerActivity.class);
				startActivity(intent);
				break;
			case 4://��������
				intent = new Intent(getApplicationContext(),TrafficManagerActivity.class);
				startActivity(intent);
				break;
			case 7://�߼�����
				intent = new Intent(getApplicationContext(),AtoolsActivity.class);
				startActivity(intent);
				break;
			case 8://��������
				intent = new Intent(getApplicationContext(),SettingCenterActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
		}
		
	}
}
