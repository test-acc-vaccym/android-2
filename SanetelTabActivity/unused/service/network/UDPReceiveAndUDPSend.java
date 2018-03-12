package com.edroplet.sanetel.services.network;

import android.content.Context;
import android.util.Log;
import org.apache.http.conn.util.InetAddressUtils;

import com.edroplet.sanetel.utils.ByteUtils;
import com.edroplet.sanetel.utils.NetworkUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * Created on 2018/3/8.
 *
 * @author qxs
 */

public class UDPReceiveAndUDPSend extends Thread {
    UDPResponseCallback mUDPResponseCallback;
    private DatagramSocket datagramSocket=null;
    public DatagramPacket dp;
    private String mIpAddress;
    private String mPort;
    private static final int DEFAULT_PORT=12426;
    private Context mContext;

    UDPReceiveAndUDPSend(Context context, UDPResponseCallback callback){
        this.mUDPResponseCallback=callback;
        mContext = context;
    }

    @Override
    public void run() {
        StringBuffer sb;
        byte[] data = new byte[1024];
        try {
            if(datagramSocket==null) {
                datagramSocket=new DatagramSocket(null);
                datagramSocket.setReuseAddress(true);
                datagramSocket.bind(new InetSocketAddress(DEFAULT_PORT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                dp = new DatagramPacket(data, data.length);
                if (datagramSocket != null) {
                    datagramSocket.receive(dp);
                }
            } catch (Exception e) {
                Log.i("Data","UDPReceive 已经挂啦");
                e.printStackTrace();
            }
            if(dp != null){
                String response= ByteUtils.bytesToHexString(dp.getData());
                Log.i("Data", "UDP response = " + response);
                byte[] data1=dp.getData();
                Log.i("Data", "The Ip=" + mIpAddress);
                Log.i("Data","The Port = "+mPort);
                if (mIpAddress != null) {
                    final String quest_ip = dp.getAddress().toString().substring(1);   //从UDP包中解析出IP地址
                    String host_ip = NetworkUtils.getIPAddress(mContext).getIp();   //若udp包的ip地址 是 本机的ip地址的话，丢掉这个包(不处理)
                    if ((!host_ip.equals("")) && host_ip.equals(quest_ip.substring(1))) {
                        continue;
                    }
                    if(mIpAddress.equals(HLKProtocol.LocalHost)){
                        mUDPResponseCallback.onResponse(null,false);
                    }
                    sb=new StringBuffer();
                    sb.append(mIpAddress);
                    sb.append(",");
                    sb.append(mPort);
                    Log.i("Data","UDP result="+sb.toString());
                    mUDPResponseCallback.onResponse(sb.toString(), true);     //将IP地址传递回去
                    datagramSocket.close();
                    break;
                }

            }

        }
    }

}
