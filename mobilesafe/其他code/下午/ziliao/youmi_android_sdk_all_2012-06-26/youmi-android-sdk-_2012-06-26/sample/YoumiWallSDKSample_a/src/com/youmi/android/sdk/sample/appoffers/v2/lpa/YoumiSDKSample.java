package com.youmi.android.sdk.sample.appoffers.v2.lpa;

import net.youmi.android.appoffers.CheckStatusNotifier;
import net.youmi.android.appoffers.YoumiOffersManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * 示例程序主Activity，请在此Activity的onCreate中初始化积分墙。
 * 
 * @author 有米广告
 * 
 */
public class YoumiSDKSample extends Activity implements CheckStatusNotifier// [可选]实现检查App及设备状态接口
{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 初始化积分墙，请务必在主Activity中调用该方法进行初始化
		YoumiOffersManager.init(this, "ace3f9cb6217d08c", "91822d7382df9076");

		// 加载视图
		setContentView(R.layout.main);

		// 打开积分墙示例(使用积分)
		findViewById(R.id.btn_to_reward_offers).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(YoumiSDKSample.this,
								RewardOffersSample.class);

						startActivity(intent);

					}
				});

		// 打开推荐应用示例(不使用积分)
		findViewById(R.id.btn_to_rewardless_offers).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(YoumiSDKSample.this,
								RewardlessAppSample.class);

						startActivity(intent);

					}
				});

		// [可选]检查App及当前设备状态
		findViewById(R.id.btn_checkstatus).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						YoumiOffersManager.checkStatus(YoumiSDKSample.this,
								YoumiSDKSample.this);
					}
				});

	}

	@Override
	public void onCheckStatusResponse(Context context, boolean isAppInvalid,
			boolean isInTestMode, boolean isDeviceInvalid) {
		// TODO Auto-generated method stub
		// 检查App及当前设备状态回调
		try {
			((TextView) findViewById(R.id.tv_tip))
					.setText(new StringBuilder(256)
							.append("检查App及当前设备状态成功\n=>>App状态:")
							.append(isAppInvalid ? "[异常]" : "[正常]")
							.append("\n=>>是否为测试模式:")
							.append(isInTestMode ? "[测试模式]" : "[正常模式]")
							.append("\n=>>当前设备状态:")
							.append(isDeviceInvalid ? "[异常]" : "[正常]")
							.append("\n只有三个状态都为正常时，才可以获得收入。但无论状态是否异常，用户完成积分墙模式下的Offer后都可以获得积分。")
							.append("\n\n如果您使用的是积分墙模式并且希望所有设备都可以获得积分，可以不调用该检查接口或不处理检查结果。")
							.append("\n如果您使用的是积分墙模式并且希望在保证有收入的情况下用户才能够获得相应的积分，那么您应该在使用积分墙前，先调用此接口进行状态判断，如果状态都为正常时才启用积分墙。")
							.append("\n\n如果App状态不正常或为\"测试模式\"，请确认是否已经上传应用到有米主站并通过审核，上传应用前，请先忽略该状态检查接口，正常调用积分墙，以配合审核人员进行审核。")
							.append("\n\n在调用状态检查接口前，请务必先进行初始化。该接口成功调用一次即可，不需要多次调用。")
							.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onCheckStatusConnectionFailed(Context context) {
		// TODO Auto-generated method stub
		try {

			((TextView) findViewById(R.id.tv_tip))
					.setText("检查App及当前设备状态失败，请检查网络配置并重新调用检查接口");

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
