package com.example.tcpdemo;

import java.util.Date;

public class BaseVolume {

	public static final String CONNECT_DEVICE = "CONNECT_DEVICE";
	public static final String RETURN_MESSAGE = "RETURN_MESSAGE";
	public static final String DEVICE_MAC = "DEVICE_MAC";
	public static final String DEVICE = "DEVICE";
	public static final String DEVICE_IP = "DEVICE_IP";
	public static final String DEVICE_PORT = "DEVICE_PORT";
	public static final String DEVICE_DATA = "DEVICE_DATA";
	public static final String CONNECT_DEVICE_OK = "CONNECT_DEVICE_OK";
	public static final String CONNECT_DEVICE_ERROR = "CONNECT_DEVICE_ERROR";
	public static final String DB_NAME = "device.db";
	
	public static final String LIGHT_ONE_OPEN = "aa01000100ff0000ff0000ff0000ff000064f5bb";
	public static final String LIGHT_ONE_CLOSE = "aa01000101ff0000ff0000ff0000ff000064f4bb";
	
	public static final String LIGHT_TWO_OPEN = "aa01010100ff0000ff0000ff0000ff000064f4bb";
	public static final String LIGHT_TWO_CLOSE = "aa01010101ff0000ff0000ff0000ff000064f3bb";
	
	public static final String LIGHT_THREE_OPEN = "aa01020100ff0000ff0000ff0000ff000064f3bb";
	public static final String LIGHT_THREE_CLOSE = "aa01020101ff0000ff0000ff0000ff000064f2bb";
	
	public static final String LIGHT_ALL_OPEN = "aa01000100010100020100000000000000646bbb";
	public static final String LIGHT_ALL_CLOSE = "aa01000101010101020101000000000000646ebb";
	
	public static final String UPDATE_CMD = "UPDATE_CMD";
	
	public static final String SEND_DATA = "SEND_DATA";
	public static final String SEND_DATA_MSG = "SEND_DATA_MSG";
	
	public static String getTime(){
		String pattern = "kk:mm:ss SS";//这是日期格式
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(pattern);//设定日期格式
		java.util.Date date = new Date();
		return  df.format(date);
	}
	
}
