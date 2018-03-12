package mediatek.android.IoTManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * UDP灞�煙缃戣澶嘔P
 * @author Administrator
 *
 */
public class FindDeviceIP {

	private Context con;
	private final int DEFAULT_PORT = 988;
	private DatagramSocket udpSocket ;
	private static final int MAX_DATA_PACKET_LENGTH = 40;  
	private byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];  
	public  void sendUdpCommand(final Context context,final Handler handler) {
		
		con = context;
		 new Thread(new Runnable() {  
		        @Override  
		        public void run() {  
		        	DatagramPacket dataPacket = null;
		    		String udpIP = getUdpServiceIP(context);
		    		if (udpIP == null)
		    			return;
		    		try {
		    			
		    			if(udpSocket == null)
		    				udpSocket = new DatagramSocket();
		    			
		    			InetAddress broadcastAddr;
		    			broadcastAddr = InetAddress.getByName(udpIP);

//		    			String order = "HLK";
		    			String order = "hlkATat+mac=?";
		    			byte[] data = order.getBytes("utf8");
		    			dataPacket = new DatagramPacket(data, data.length, broadcastAddr,
		    					DEFAULT_PORT);
		    			udpSocket.send(dataPacket);
		    			while(true) {
			    			byte[] dataReceive = new byte[256];
			    			DatagramPacket packetReceive = new DatagramPacket(dataReceive,
			    					dataReceive.length);
			    			udpSocket.setSoTimeout(1000*2);
			    			udpSocket.receive(packetReceive);
			    			String udpresult = new String(packetReceive.getData(),
			    					packetReceive.getOffset(), packetReceive.getLength());
			    			String ip = packetReceive.getAddress().getHostAddress();
			    			Log.e("FindDeviceIP", "扎到设备："+udpresult+"+"+ip);
			    			
			    			String deviceMac = getMac(udpresult);
		    				Bundle bundle = new Bundle();
		    				bundle.putString("mac",deviceMac);
		    				bundle.putString("ip", ip);
		    				con.sendBroadcast(new Intent(SmartConfigMethod.FIND_DEVICE_MESSAGE).
		    						putExtra(SmartConfigMethod.NEW_DEVICE, bundle));
		    			}
		    		} catch (IOException e) {
//		    			udpSocket = null;
		    			e.printStackTrace();
		    		}
		    	//	udpSocket.close();
		        }  
		    }).start();  
		
	
	}
	
	private String getMac(String str) {
		String mac = "";
		String[] strMac = str.split(",");
		String one = Integer.toHexString(Integer.parseInt(strMac[0])).length() == 2 ? Integer.toHexString(Integer.parseInt(strMac[0])): "0"+ Integer.toHexString(Integer.parseInt(strMac[0]));
		String two = Integer.toHexString(Integer.parseInt(strMac[1])).length() == 2 ? Integer.toHexString(Integer.parseInt(strMac[1])): "0"+ Integer.toHexString(Integer.parseInt(strMac[1]));
		String three = Integer.toHexString(Integer.parseInt(strMac[2])).length() == 2 ? Integer.toHexString(Integer.parseInt(strMac[2])): "0"+ Integer.toHexString(Integer.parseInt(strMac[2]));
		String four = Integer.toHexString(Integer.parseInt(strMac[3])).length() == 2 ? Integer.toHexString(Integer.parseInt(strMac[3])): "0"+ Integer.toHexString(Integer.parseInt(strMac[3]));
		String five = Integer.toHexString(Integer.parseInt(strMac[4])).length() == 2 ? Integer.toHexString(Integer.parseInt(strMac[4])): "0"+ Integer.toHexString(Integer.parseInt(strMac[4]));
		String six = Integer.toHexString(Integer.parseInt(strMac[5])).length() == 2 ? Integer.toHexString(Integer.parseInt(strMac[5])): "0"+ Integer.toHexString(Integer.parseInt(strMac[5]));

		mac = one + two + three + four + five + six;
		return mac;
	}
	
	public void M30Brocast(String addr,String command)
	{
		String udpresult;
		byte[] DataReceive= new byte[512];
		DatagramPacket pack = new DatagramPacket(DataReceive,DataReceive.length);
        MulticastSocket ms=null;
		try {
			ms = new MulticastSocket();
			InetAddress address = InetAddress.getByName(addr);
			byte out[] = command.getBytes();  
			DatagramPacket dataPacket = new DatagramPacket(out, out.length, address, 988);  
			ms.send(dataPacket);
			ms.setSoTimeout(500);
			ms.receive(pack);
			udpresult = new String(pack.getData(), pack.getOffset(), pack.getLength());
//			Log.e("M30Brocast", "M30Brocast ,udpresult锛�+udpresult);
			ms.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
         
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
		else {
			udpServiceIP = "255.255.255.255";
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
