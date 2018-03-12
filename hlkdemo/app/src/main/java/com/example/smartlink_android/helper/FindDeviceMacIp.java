package com.example.smartlink_android.helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FindDeviceMacIp {

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
		    		String udpIP = "255.255.255.255";
		    		if (udpIP == null)
		    			return;
		    		try {
		    			
		    			if(udpSocket == null)
		    				udpSocket = new DatagramSocket();
		    			
		    			InetAddress broadcastAddr;
		    			broadcastAddr = InetAddress.getByName(udpIP);

//		    			String order = "HLK";
		    			String order = "hlk2";
		    			byte[] data = order.getBytes("utf8");
		    			dataPacket = new DatagramPacket(data, data.length, broadcastAddr,
		    					DEFAULT_PORT);
		    			udpSocket.send(dataPacket);

		    			Log.e("FindDeviceIP", "发送UDP："+order);
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
	
	/** 解析UDP返回的数据 */
	public DeviceInfoCache getSomething(String udpresult,String ip) {
		DeviceInfoCache deviceInfo = new DeviceInfoCache();
		// HLK-M30_wx(V1.00(Aug 21 2017))(28:f3:66:1c:bf:1b)+192.168.11.140
		// 是以前的微信版本
		if (udpresult.indexOf("V") > 0 && udpresult.indexOf("wx") > 0) {
			String strName = udpresult.substring(0, udpresult.indexOf(")")+2);
			String strMac = udpresult.substring(udpresult.lastIndexOf("("), udpresult.lastIndexOf(")")+1);
			String strMaxVerNumber = udpresult.substring(udpresult.indexOf("(")+1, udpresult.indexOf("(")+5);
			String strTime = udpresult.substring(udpresult.indexOf("(")+5+1, udpresult.indexOf(")"));
			deviceInfo.setName(strName);
			deviceInfo.setIp(ip);
			deviceInfo.setMac(strMac);
			deviceInfo.setMaxVerNumber(strMaxVerNumber);
			deviceInfo.setTime(strTime);
			// airkiss 微信版本
			deviceInfo.setVerType("c");
			deviceInfo.setCustom("");
			
		}
		// 新版本
		else {
			// HLK-M30(a.1.00.120170907160537)(8c:88:2b:00:24:15)
			String str1 = udpresult.substring(udpresult.indexOf("(")+1, udpresult.indexOf(")"));
			String strVerType = str1.substring(0, str1.indexOf("."));
			String strMaxVerNumber = str1.substring(str1.indexOf(".")+1, str1.lastIndexOf("."));
			String strCustom = str1.substring(str1.lastIndexOf(".")+1).substring(0, 1);
			String strTime = str1.substring(str1.lastIndexOf(".")+1).substring(1);
			String strName = udpresult.substring(0, udpresult.indexOf(")")+1);
			String strMac = udpresult.substring(udpresult.indexOf(")")+1);
			
			deviceInfo.setName(strName);
			deviceInfo.setIp(ip);
			deviceInfo.setMac(strMac);
			deviceInfo.setVerType(strVerType);
			deviceInfo.setMaxVerNumber(strMaxVerNumber);
			deviceInfo.setCustom(strCustom);
			deviceInfo.setTime(strTime);
		}
		
		
		
		
		return deviceInfo;
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
