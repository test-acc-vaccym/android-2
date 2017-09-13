package com.example.xwwt;

import android.app.Application;

public class MyApplication extends  Application {
	private static MyApplication instance;

	String ip="";
	String port="";
	
	
	@Override
	public void onCreate() {
		ip="";
		port="";
		super.onCreate();
		instance = this;
	}

	public static MyApplication getInstance(){
		// ���ﲻ���ж�instance�Ƿ�Ϊ��
		return instance;
	}
	
	public void SetIP(String ip)
	{
		this.ip=ip;
	}
	
	public String GetIP()
	{
		return this.ip;
	}
	
	public void SetPort(String port)
	{
		this.port=port;
	}
	
	public String GetPort()
	{
		return this.port;
	}
}