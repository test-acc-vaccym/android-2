package com.example.mytt.dao;

import com.example.mytt.dao.DBContent.DeviceInfo.Columns;
import com.example.mytt.dao.DBContent.DeviceInfo.ColumnsLog;

public class DBContent {

	/**
	 * �豸�б�
	 * @author Administrator
	 *
	 */
	public static class DeviceInfo {
		
public static final String TABLE_NAME = "DeviceInfo";
		
		public static final String LOG_NAME = "LogInfo";
		
		public static final String COM_NAME = "CommandInfo";
		
		public static final String CMD_LISTENER = "CmdListenerInfo";
		
		public static class Columns {
			public static final String id = "ID";
			public static final String deviceName = "deviceName";
			public static final String devicePwd = "devicePwd";
			public static final String deviceMacAddress = "deviceMacAddress";
			public static final String deviceType = "deviceType";
		}
		
		public static class ColumnsLog {
			public static final String id = "LogID";
			public static final String LogName = "LogName";
			public static final String LogState = "LogState";
			public static final String LogTime = "LogTime";
		}
		
		public static class VCommandData{
			public static final String id = "CommandID";
			public static final String ComMac = "ComMac";
			public static final String ComName = "ComName";
			public static final String ComData = "ComData";
			
		}
		
		public static class CmdListener{
			public static final String id = "ListenerID";
			public static final String CmdName= "CmdName";
			public static final String listData= "ListData";
			public static final String sendData = "SendData";
			public static final String listenerState = "ListenerState";
			
		}
		
		public static String getCreateSQL() {
			return "CREATE TABLE " + TABLE_NAME + "(" + //
					"'"+Columns.id+"' INTEGER PRIMARY KEY AUTOINCREMENT ," + 
	                "'"+Columns.deviceName+"' TEXT NOT NULL ," +
	                "'"+Columns.devicePwd+"' TEXT NOT NULL ," +
	                "'"+Columns.deviceMacAddress+"' TEXT NOT NULL," +
	                "'"+Columns.deviceType+"' TEXT NOT NULL" +
	                ")";
		}
		
		public static String getCreateLogSQL() {
			return "CREATE TABLE " + LOG_NAME + "(" + //
					"'"+ColumnsLog.id+"' INTEGER PRIMARY KEY AUTOINCREMENT ," + 
	                "'"+ColumnsLog.LogName+"' TEXT NOT NULL ," +
	                "'"+ColumnsLog.LogState+"' TEXT NOT NULL ," +
	                "'"+ColumnsLog.LogTime+"' TEXT NOT NULL " +
	                ")";
		}
		
		public static String getCreateCommandSQL() {
			return "CREATE TABLE " + COM_NAME + "(" + //
					"'"+VCommandData.id+"' INTEGER PRIMARY KEY AUTOINCREMENT ," + 
	                "'"+VCommandData.ComMac+"' TEXT NOT NULL ," +
	                "'"+VCommandData.ComName+"' TEXT NOT NULL ," +
	                "'"+VCommandData.ComData+"' TEXT NOT NULL " +
	                ")";
		}
		
		public static String getCreateListenerSQL() {
			return "CREATE TABLE " + CMD_LISTENER + "(" + //
					"'"+CmdListener.id+"' INTEGER PRIMARY KEY AUTOINCREMENT ," + 
	                "'"+CmdListener.CmdName+"' TEXT NOT NULL ," +
	                "'"+CmdListener.listData+"' TEXT NOT NULL ," +
	                "'"+CmdListener.sendData+"' TEXT NOT NULL, " +
	                "'"+CmdListener.listenerState+"' TEXT NOT NULL " +
	                ")";
		}
		
	}
}
