package com.youmi.android.sdk.sample;

import net.youmi.android.AdView;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 本例子展示 在横屏情况下的广告条悬浮布局方式。
 * 
 * @author 有米广告
 * 
 */
public class SuspensoidSample extends Activity {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 本例子展示 在横屏情况下的广告条悬浮布局方式。
		//
		// 注意 *:
		// 新版广告条的LayoutParams的设置规则如下:
		// 高度应该设置为WRAP_CONTENT
		// 宽度可以设置为FILL_PARENT或WRAP_CONTENT

		// 当宽度设置为FILL_PARENT时，广告条的宽度取决于当前屏幕状态的宽度。
		// 当宽度设置为WRAP_CONTENT时，广告条的宽度与屏幕宽高中最小一边的值一致。

		// 注意 *:
		// 广告条的背景透明度设置只对背景色有效,因此透明度的设置只能文字广告有效，对于图片广告无效。

		// 本例子将初始化3个广告条实例
		// 第一个广告条宽度为FILL_PARENT,并且悬浮在顶端。
		// 第二个广告条宽度为WRAP_CONTENT,悬浮在屏幕中间。
		// 第三个广告条宽度为WRAP_CONTENT,悬浮在屏幕右下端。

		
		
		// 为方便演示，将屏幕设置为全屏模式
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

		// setContentView
		setContentView(R.layout.sample);

		
		/////////////////////////////////////////////////////////////////////////
		// 初始化第一个广告条[顶部]
		AdView adViewTop = new AdView(this);

		// 布局
		FrameLayout.LayoutParams lp_Top = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);

		// 设置广告出现的位置(悬浮于顶部)

		lp_Top.gravity = Gravity.TOP;

		// 将广告视图加入Activity中
		addContentView(adViewTop, lp_Top);

		
		
		/////////////////////////////////////////////////////////////////////////
		// 初始化第二个广告条[中间]
		AdView adViewCenter = new AdView(this);

		// 布局
		FrameLayout.LayoutParams lp_Center = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);

		// 设置广告出现的位置(悬浮于屏幕中间)
		lp_Center.gravity = Gravity.CENTER;

		// 将广告视图加入Activity中
		addContentView(adViewCenter, lp_Center);

		
		
		/////////////////////////////////////////////////////////////////////////
		// 初始化第三个广告条[右下角]
		AdView adViewLeftBottom = new AdView(this, Color.GRAY, Color.WHITE,
				100);

		FrameLayout.LayoutParams lp_Left_Bottom = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);

		// 设置广告出现的位置(悬浮于右下角)
		lp_Left_Bottom.gravity = Gravity.BOTTOM | Gravity.RIGHT;

		// 将广告视图加入Activity中
		addContentView(adViewLeftBottom, lp_Left_Bottom);

	}
	
	
	
	
	
	@Override
	protected void onResume() {
		/**
		 * 设置为横屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		super.onResume();
	}

}
