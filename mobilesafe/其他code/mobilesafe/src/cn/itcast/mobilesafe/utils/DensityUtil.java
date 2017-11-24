package cn.itcast.mobilesafe.utils;

import android.content.Context;

public class DensityUtil {

	/**
	 * ��px ת��Ϊdip
	 * @param context
	 * @param px
	 * @return
	 */
	public static int px2dip(Context context,float px){
		float density = context.getResources().getDisplayMetrics().density;//�ܶ�
		int dip = (int) (px * density);
		return dip;
	}
}
