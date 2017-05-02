package com.youmi.android.sdk.sample.appoffers.v2.dpa;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import net.youmi.android.appoffers.EarnedPointsNotifier;
import net.youmi.android.appoffers.EarnedPointsOrder;

/**
 * 
 * (示例)简单的自定义积分管理类，在这里使用本地文件进行简单的积分操作， 您可以使用本地文件或使用云端服务器存储积分，并且使用更加安全的方式来进行管理。
 * 
 */
public class MyPointsManager implements EarnedPointsNotifier {

	private static final String KEY_FILE_POINTS="Points";
	private static final String KEY_POINTS="points";
	private static final String KEY_FILE_ORDERS="Orders";
	

	private static MyPointsManager instance;

	public static MyPointsManager getInstance() {
		if (instance == null) {
			instance = new MyPointsManager();
		}

		return instance;
	}

	@Override
	public void onEarnedPoints(Context context,
			List pointsList) {
		// TODO Auto-generated method stub
		try {

			if (pointsList != null) {

				for (int i = 0; i < pointsList.size(); i++) {

					// 将积分存储到自定义积分账户中
					storePoints(context, (EarnedPointsOrder) pointsList.get(i));

					// (可选)处理或存储积分获取记录
					recordOrder(context, (EarnedPointsOrder) pointsList.get(i));

				}

			} else {
				infoMsg("onPullPoints:pointsList is null");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 查询积分
	 * 
	 * @param context
	 * @return
	 */
	public int queryPoints(Context context) {
		SharedPreferences sp = context.getSharedPreferences(KEY_FILE_POINTS,
				Context.MODE_PRIVATE);

		return sp.getInt(KEY_POINTS, 0);
	}

	/**
	 * 消费积分
	 * 
	 * @param context
	 * @param amount
	 * @return
	 */
	public boolean spendPoints(Context context, int amount) {
		if (amount <= 0) {
			return false;
		}

		SharedPreferences sp = context.getSharedPreferences(KEY_FILE_POINTS,
				Context.MODE_PRIVATE);

		int p = sp.getInt(KEY_POINTS, 0);
		if (p < amount) {
			return false;
		}

		p -= amount;

		return sp.edit().putInt(KEY_POINTS, p).commit();
	}

	/**
	 * 奖励积分
	 * 
	 * @param context
	 * @param amount
	 * @return
	 */
	public boolean awardPoints(Context context, int amount) {
		if (amount <= 0) {
			return false;
		}
		SharedPreferences sp = context.getSharedPreferences(KEY_FILE_POINTS,
				Context.MODE_PRIVATE);

		int p = sp.getInt(KEY_POINTS, 0);

		p += amount;

		return sp.edit().putInt(KEY_POINTS, p).commit();
	}


	/**
	 * 存储积分
	 * 
	 * @param context
	 * @param order
	 */
	private void storePoints(Context context, EarnedPointsOrder order) {
		try {
			if (order != null) {

				if (order.getPoints() > 0) {
					// 将积分加入积分账户中，这里假设积分账户是存储在本地

					SharedPreferences sp = context.getSharedPreferences(
							KEY_FILE_POINTS, Context.MODE_PRIVATE);

					int p = sp.getInt(KEY_POINTS, 0);
					p += order.getPoints();

					sp.edit().putInt(KEY_POINTS, p).commit();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void recordOrder(Context context, EarnedPointsOrder order) {
		try {
			if (order != null) {
				// 可以处理这些订单详细信息，这里只是作为简单的记录.
				StringBuilder stringBuilder = new StringBuilder(256);
				stringBuilder.append("[").append("订单号 => ")
						.append(order.getOrderId()).append("]\t[")
						.append("渠道号 => ").append(order.getChannelId())
						.append("]\t[").append("设置的用户Id(md5) => ")
						.append(order.getUserId()).append("]\t[")
						.append("获得的积分 => ").append(order.getPoints())
						.append("]\t[")
						.append("获得积分的类型(1为有收入的积分，2为无收入的积分) => ")
						.append(order.getStatus()).append("]\t[")
						.append("积分的结算时间(格林威治时间，单位秒) => ")
						.append(order.getTime()).append("]\t[")
						.append("本次获得积分的描述信息 => ").append(order.getMessage())
						.append("]");

				String msg = stringBuilder.toString();

				SharedPreferences sp = context.getSharedPreferences(KEY_FILE_ORDERS,
						Context.MODE_PRIVATE);

				Editor editor = sp.edit();
				editor.putString(
						order.getOrderId() != null ? order.getOrderId() : Long
								.toString(System.currentTimeMillis()), msg);

				editor.commit();

				infoMsg(msg);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void errMsg(String msg) {
		Log.e("MyPointsManager", msg);
	}

	private void infoMsg(String msg) {
		Log.e("MyPointsManager", msg);
	}

}
