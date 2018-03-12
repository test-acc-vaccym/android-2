package com.example.tcpdemo.smartlink;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;


import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FindDeviceInfohlk2 {

	@SuppressWarnings("unused")
	private Context con;
	private final int DEFAULT_PORT = 988;
	private DatagramSocket udpSocket ;
	private static final int MAX_DATA_PACKET_LENGTH = 40;  
	@SuppressWarnings("unused")
	private byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];  
	public  void startFindCommand(final Context context,final Handler handler) {
		con = context;
		 new Thread(new Runnable() {  
		        @SuppressWarnings("unused")
				@Override  
		        public void run() {  
		        	DatagramPacket dataPacket = null;
		    		String udpIP = "192.168.16.255";
//		    		String udpIP = getUdpServiceIP(context);
		    		if (udpIP == null)
		    			return;
		    		try {
		    			
		    			if(udpSocket == null)
		    				udpSocket = new DatagramSocket();
		    			
		    			InetAddress broadcastAddr;
		    			broadcastAddr = InetAddress.getByName(udpIP);
		    			String order = "hlk2";
		    			byte[] data = order.getBytes("utf8");
		    			dataPacket = new DatagramPacket(data, data.length, broadcastAddr,
		    					DEFAULT_PORT);
		    			udpSocket.send(dataPacket);
		    			while(true) {
		    				byte[] dataReceive = new byte[1024];
			    			DatagramPacket packetReceive = new DatagramPacket(dataReceive,
			    					dataReceive.length);
			    			udpSocket.setSoTimeout(1000*5);
			    			udpSocket.receive(packetReceive);
			    			String udpresult = new String(packetReceive.getData(),
			    					packetReceive.getOffset(), packetReceive.getLength());
			    			String resultIP = packetReceive.getAddress().getHostAddress();
			    			Log.e("FindDeviceIP", "返回数据："+udpresult+"+"+resultIP);
			    			
			    			DeviceInfoCache newDevice = getSomething(udpresult, resultIP);
			    			if (newDevice != null) {
			    				Bundle bundle = new Bundle();
			    				bundle.putSerializable(NetworkUtils.DEVIEC_INFO, newDevice);
			    				Message msg = new Message();
			    				msg.what = NetworkUtils.FIND_DEVICE_MAC;
			    				msg.obj = bundle;
			    				handler.sendMessage(msg);
			    			}
			    			
		    			}
	    				
		    		} catch (IOException e) {
		    			e.printStackTrace();
		    		}
		        }  
		    }).start();  
	
	}
	
	/** 解析UDP返回的数�?*/
	public DeviceInfoCache getSomething(String udpresult,String ip) {
		DeviceInfoCache deviceInfo = new DeviceInfoCache();
		
		String strName = udpresult.substring(0, udpresult.indexOf("("));
		String strVer = udpresult.substring(udpresult.indexOf("(")+1, udpresult.lastIndexOf("(")-1);
		String strMac = udpresult.substring(udpresult.lastIndexOf("(")+1,udpresult.length() - 1);
		
		if (strName.equals("HLK-M30") || strName.equals("HLK-M30_wx")) {
			// 说明格式为：HLK-M30(b.1.00.420170920153348)(48:02:2a:f6:39:59)(S:34530)
			if (strMac.indexOf("S") >= 0) {
				strVer = udpresult.substring(udpresult.indexOf("(")+1, udpresult.indexOf(")")-1);
				strMac = udpresult.substring(udpresult.indexOf(")")+1);
				// 只针对老版本的wx版本格式 HLK-M30_wx(V1.00(Feb 16 2017))(20:f4:1b:16:99:da)(S:95419)
				if (strMac.startsWith(")")) {
					strMac = strMac.substring(1);
					strVer = udpresult.substring(udpresult.indexOf("(")+1, udpresult.indexOf(")")+1);
				}
				
			}
		}
		
		deviceInfo.setName(strName);
		deviceInfo.setIp(ip);
		deviceInfo.setVerType(strVer);
		deviceInfo.setMac(strMac);
		
		if (strName.equals("HLK-7688A")) {
			String strInfo = "";
			String str1 = strVer.substring(0,1);
			String str2 = strVer.substring(1,5);
			if (str1.equalsIgnoreCase("v")) {
				strInfo += "普通版本,"+str2;
			}
			else if (str1.equalsIgnoreCase("d")) {
				strInfo += "二次开发版本,"+str2;
			}
			else if (str1.equalsIgnoreCase("o")) {
				strInfo += "Openwrt,"+str2;
			}
			else {
				strInfo += "客户定制："+str1+","+str2;
			}
			deviceInfo.setVerInfo(strInfo);
		}
		else if (strName.equals("HLK-M30")) {
			String strInfo = "";
			String str1 = strVer.substring(0,1);
			if (str1.equalsIgnoreCase("a")) {
				strInfo += "airkiss普通,";
			}
			else if (str1.equalsIgnoreCase("b")) {
				strInfo += "elian普通,";
			}
			else if (str1.equalsIgnoreCase("c")) {
				strInfo += "airkiss微信,";
			}
			else if (str1.equalsIgnoreCase("d")) {
				strInfo += "airkiss云智易,";
			}
			
			String custom = strVer.substring(strVer.lastIndexOf(".")+1).substring(0, 1);
			if (custom.equals("1")) {
				strInfo += "非定制版";
			}
			else if (custom.equals("2")) {
				strInfo += "定制版";
			}
			else if (custom.equals("4")) {
				strInfo += "自动生成版";
			}
			
			deviceInfo.setVerInfo(strInfo);
		}
		else if (strName.equals("HLK-8266")) {
			String strInfo = "";
			String str1 = strVer.substring(0,1);
			if (str1.equalsIgnoreCase("a")) {
				strInfo += "普通AT,";
			}
			else if (str1.equalsIgnoreCase("b")) {
				strInfo += "云智易,";
			}
			
			String custom = strVer.substring(strVer.lastIndexOf(".")+1).substring(0, 1);
			if (custom.equals("1")) {
				strInfo += "非定制版";
			}
			else if (custom.equals("2")) {
				strInfo += "定制版";
			}
			else if (custom.equals("4")) {
				strInfo += "自动生成版";
			}
			
			deviceInfo.setVerInfo(strInfo);
		}
		
		
		// HLK-M30_wx(V1.00(Aug 21 2017))(28:f3:66:1c:bf:1b)
		// 是以前的微信版本
//		if (udpresult.indexOf("V") > 0) {
//			
//			String strName = udpresult.substring(0, udpresult.indexOf(")")+2);
//			String strMac = udpresult.substring(udpresult.lastIndexOf("("), udpresult.lastIndexOf(")")+1);
//			String strMaxVerNumber = udpresult.substring(udpresult.indexOf("(")+1, udpresult.indexOf("(")+5);
//			String strTime = udpresult.substring(udpresult.indexOf("(")+5+1, udpresult.indexOf(")"));
//			deviceInfo.setName(strName);
//			deviceInfo.setIp(ip);
//			deviceInfo.setMac(strMac);
//			deviceInfo.setMaxVerNumber(strMaxVerNumber);
//			deviceInfo.setTime(strTime);
//			// airkiss 微信版本
//			deviceInfo.setVerType("");
//			deviceInfo.setCustom("");
//		}
//		// 新版�?
//		else {
//			// HLK-M30(a.1.00.120170907160537)(8c:88:2b:00:24:15)
//			String str1 = udpresult.substring(udpresult.indexOf("(")+1, udpresult.indexOf(")"));
//			String strVerType = str1.substring(0, str1.indexOf("."));
//			String strMaxVerNumber = str1.substring(str1.indexOf(".")+1, str1.lastIndexOf("."));
//			String strCustom = str1.substring(str1.lastIndexOf(".")+1).substring(0, 1);
//			String strTime = str1.substring(str1.lastIndexOf(".")+1).substring(1);
//			String strName = udpresult.substring(0, udpresult.indexOf(")")+1);
//			String strMac = udpresult.substring(udpresult.indexOf(")")+1);
//			
//			deviceInfo.setName(strName);
//			deviceInfo.setIp(ip);
//			deviceInfo.setMac(strMac);
//			deviceInfo.setVerType(strVerType);
//			deviceInfo.setMaxVerNumber(strMaxVerNumber);
//			deviceInfo.setCustom(strCustom);
//			deviceInfo.setTime(strTime);
//		}
		
		return deviceInfo;
		
	}
	
	
	private String getUdpServiceIP(Context context) {
		String udpServiceIP = "";
		String ip = getIP(context);
		if (ip == null)
			return ip;
		if (ip != null && ip.startsWith("192.")) {
			String[] strarray = ip.split("\\.");

			for (int i = 0; i < strarray.length - 1; i++) {
				udpServiceIP += strarray[i] + ".";
			}
			udpServiceIP += "255";
		}
		return udpServiceIP;
	}
	
	private String getIP(Context context) {
		@SuppressWarnings("static-access")
		WifiManager wifiService = (WifiManager) context
				.getApplicationContext().getSystemService(context.WIFI_SERVICE);
		WifiInfo wifiinfo = wifiService.getConnectionInfo();
		int wifiAd = wifiinfo.getIpAddress();
		if (wifiAd == 0)
			return null;
		return intToIp(wifiAd);
	}
	
	private String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}
	
	
	
	
	
	
	
	
}
