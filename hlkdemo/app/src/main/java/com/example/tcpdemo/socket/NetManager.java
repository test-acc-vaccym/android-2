package com.example.tcpdemo.socket;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetManager
{
	static NetManager s_m = null;
	
	private Context context;
	
	private NetManager(Context context)
	{
		this.context = context;
	}
	
	public static synchronized NetManager instance(Context context)
	{
		if (s_m == null)
		{
			s_m = new NetManager(context);
		}
		return s_m;
	}
	
	/**
	 *
	 * @return
	 */
	public boolean isNetworkConnected()
	{
		if (context == null)
		{
			return false;
		}
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null)
		{
			return false;
		} else
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
			{
				for (int i = 0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * @return
	 */
	public boolean isWifiConnected()
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null)
			{
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	/**
	 * 锟叫讹拷MOBILE锟斤拷锟斤拷锟角凤拷锟斤拷锟�
	 * @return
	 */
	public boolean isMobileConnected()
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null)
			{
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	public int getConnectedType()
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable())
			{
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}
	
	/**
	 * 判断是否有wifi连接
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }
     
        return false ;
    }
	
}
