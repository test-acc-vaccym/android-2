package com.youmi.android.sdk.sample.appoffers.v2.lpa;

import net.youmi.android.appoffers.YoumiOffersManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class RewardlessAppSample extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.rewardless);

		// 示例，展示无积分的推荐应用列表
		findViewById(R.id.btn_rewardless_applist).setOnClickListener(
				new View.OnClickListener() {

					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						// 调用打开积分墙接口，第二个参数指定推荐应用列表模式
						YoumiOffersManager.showOffers(RewardlessAppSample.this,
								YoumiOffersManager.TYPE_REWARDLESS_APPLIST);

					}
				});

		// 示例，展示无积分的单一推荐应用
		findViewById(R.id.btn_rewardless_featuredapp).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 调用打开积分墙接口，第二个参数指定单一推荐应用模式
						YoumiOffersManager.showOffers(RewardlessAppSample.this,
								YoumiOffersManager.TYPE_REWARDLESS_FEATUREDAPP);

					}
				});
	}
}
