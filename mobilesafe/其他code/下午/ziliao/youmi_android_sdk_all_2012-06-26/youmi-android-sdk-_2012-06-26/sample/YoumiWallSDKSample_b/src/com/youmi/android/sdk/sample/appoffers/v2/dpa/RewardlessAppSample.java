package com.youmi.android.sdk.sample.appoffers.v2.dpa;

import com.youmi.android.sdk.sample.appoffers.v2.dpa.R;

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

						// 调用打开应用推荐列表接口，第二个参数指定推荐应用列表模式,由于没有使用积分，所以第三个参数可以传null
						YoumiOffersManager.showOffers(RewardlessAppSample.this,
								YoumiOffersManager.TYPE_REWARDLESS_APPLIST,null);

					}
				});

		// 示例，展示无积分的单一推荐应用
		findViewById(R.id.btn_rewardless_featuredapp).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 调用打开应用推荐接口，第二个参数指定单一推荐应用模式，所以第三个参数可以传null
						YoumiOffersManager.showOffers(RewardlessAppSample.this,
								YoumiOffersManager.TYPE_REWARDLESS_FEATUREDAPP,null);

					}
				});
	}
}
