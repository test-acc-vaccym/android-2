package com.youmi.android.sdk.sample;

import net.youmi.android.AdManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 本示例程序展示三种布局方式。 该Activity是示例程序的入口界面,请在此类中使用 static{}
 * 代码块调用net.youmi.net.AdManager.init()方法进行app初始化。
 * 
 * @author 有米广告
 * 
 */
public class YoumiSDKSample extends Activity {

	 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 应用Id 应用密码 广告请求间隔(s) 测试模式
		AdManager.init(this,"537ef88653a2993c", "b9e10bcfe994a9fb", 30, true);

		setContentView(R.layout.main);

		// 调用代码布局示例
		findViewById(R.id.btnCode).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(YoumiSDKSample.this,
								CodeSample.class);

						startActivity(intent);
					}
				});

		// 调用XML布局示例

		findViewById(R.id.btnXml).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(YoumiSDKSample.this,
								XmlSample.class);

						startActivity(intent);
					}
				});

		// 调用悬浮布局示例

		findViewById(R.id.btnSuspensoid).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(YoumiSDKSample.this,
								SuspensoidSample.class);

						startActivity(intent);
					}
				});

	}
}