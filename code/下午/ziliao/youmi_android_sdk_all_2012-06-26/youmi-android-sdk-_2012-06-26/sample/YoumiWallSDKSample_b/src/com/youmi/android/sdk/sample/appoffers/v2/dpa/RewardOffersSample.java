package com.youmi.android.sdk.sample.appoffers.v2.dpa;

import com.youmi.android.sdk.sample.appoffers.v2.dpa.R;

import net.youmi.android.appoffers.YoumiOffersManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RewardOffersSample extends Activity {

	/**
	 * 用于显示积分
	 */
	TextView tvPoints;

	/**
	 * 用于打开积分墙
	 */
	Button btnShowOffers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 进行视图初始化
		setContentView(R.layout.reward_offers);

		// 初始化一些View的实例
		tvPoints = (TextView) findViewById(R.id.tv_offers_points);
		btnShowOffers = (Button) findViewById(R.id.btn_show_offers);

		// 打开积分墙
		btnShowOffers.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// 调用打开积分墙接口，第二个参数指定为积分墙模式,第三个参数传入EarnedPointsNotifier
				YoumiOffersManager.showOffers(RewardOffersSample.this,
						YoumiOffersManager.TYPE_REWARD_OFFERS,
						MyPointsManager.getInstance());

			}
		});

		// 奖励10积分示例
		findViewById(R.id.btn_award).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						// 奖励积分示例
						MyPointsManager.getInstance().awardPoints(
								RewardOffersSample.this, 10);

						showPoints();

					}
				});

		// 消费10积分示例
		findViewById(R.id.btn_spend).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						// 消费积分示例
						MyPointsManager.getInstance().spendPoints(
								RewardOffersSample.this, 10);

						showPoints();

					}
				});

		// 刷新积分
		findViewById(R.id.btn_refresh).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						showPoints();

					}
				});

	}

	/**
	 * 示例：在TextView上显示积分
	 */
	void showPoints() {
		try {

			// 查询积分示例
			tvPoints.setText(Integer.toString(MyPointsManager.getInstance()
					.queryPoints(this)));

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		showPoints();

	}

}
