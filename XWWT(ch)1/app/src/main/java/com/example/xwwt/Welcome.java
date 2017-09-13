//主启动界面
package com.example.xwwt;

import android.app.Activity;
//import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;


public class Welcome extends Activity implements Runnable 
{

	//是否是第一次使用
	private boolean isFirstUse;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_page);
		/**
		 * 启动一个延迟线程
		 */
		new Thread(this).start();
	}

	public void run() 
	{
		try {
			Thread.sleep(1500);// 延迟1.5秒进入

			// 读取SharedPreferences中需要的数据
			SharedPreferences preferences = getSharedPreferences("isFirstUse", MODE_WORLD_READABLE);
			isFirstUse = preferences.getBoolean("isFirstUse", true);

			/**
			 * 如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
			 */
			if (isFirstUse){
				Intent aIntent = new Intent(Welcome.this, guide_activity.class);
				startActivity(aIntent);
			} 
			else {
				Intent aIntent = new Intent();
				aIntent.setClass(Welcome.this, Login_Activity.class);
				startActivity(aIntent);
			}
			finish();

			// 实例化Editor对象
			Editor editor = preferences.edit();
			// 存入数据
			editor.putBoolean("isFirstUse", false);
			// 提交修改
			editor.commit();

		} catch (InterruptedException e){
		
		}
	}
}