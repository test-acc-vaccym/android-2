package com.example.CommonFunction;

public class Data
{
	static String ip="";
	static String port="";
	static boolean renderFlag=false;
	
	public static void SetIP(String ip)
	{
		Data.ip=ip;
	}
	
	public static String GetIP()
	{
		return ip;
	}
	
	public static void SetPort(String port)
	{
		Data.port=port;
	}
	
	public static String GetPort()
	{
		return port;
	}
	
	public static void SetFlag(boolean flag)
	{
		Data.renderFlag=flag;
	}
	
	public static boolean GetFlag()
	{
		return renderFlag;
	}
}