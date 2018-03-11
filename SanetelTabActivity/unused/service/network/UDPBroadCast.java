package com.edroplet.sanetel.services.network;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.edroplet.sanetel.utils.ByteUtils;
import com.edroplet.sanetel.utils.NetworkUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created on 2018/3/8.
 *
 * @author qxs
 */

public class UDPBroadCast extends Thread {
    private MulticastSocket sender = null;
    private DatagramSocket datagramSocket=null;
    private DatagramPacket dj = null;
    private InetAddress group = null;
    private UDPReceiveAndUDPSend mUDPReceiveAndUDPSend;

    private UDPResponseCallback callback;   //自定义接口，用于处理接收到的UDP结果，主要是调用它唯一的方法onResponse,提高结果处理的灵活性

    public String LocalIP;
    public int DEFAULT_PORT=12426;

    private byte[] data = new byte[1024];   //用于发送udp的byte数组

    private boolean isUdpSendFinished=false;

    private Context mContext;

    public UDPBroadCast(Context context,byte[] dataString, UDPResponseCallback callback) {
        mContext=context;
        this.data = dataString;
        this.callback=callback;
    }
    @Override
    public void run() {
        try {
            if(datagramSocket==null){
                datagramSocket = new DatagramSocket(null);
                datagramSocket.setReuseAddress(true);
                datagramSocket.bind(new InetSocketAddress(DEFAULT_PORT));
            }
            sender = new MulticastSocket();
            LocalIP = NetworkUtils.getIPAddress(mContext).getIp();
            // LocalIP=getBroadcastIP(LocalIP);
            Log.i("Data","LocalIp="+LocalIP);
            group = InetAddress.getByName(LocalIP);    //这个是我客户端的IP地址，也可以是客户端所在的网段的广播地址，也可以是虚拟广播地址
            if(data.length != 0) {
                dj = new DatagramPacket(data, data.length, group,DEFAULT_PORT);    //端口号可以自己随便定,发送至的端口号

                datagramSocket.send(dj);    //发送UDP广播
                Log.i("Data", "发送的内容是" + ByteUtils.bytesToHexString(data));
                datagramSocket.close();
                isUdpSendFinished=true;
                ReUDPAndSenTCP();     //用于接收UDP响应的线程
            }

        } catch(IOException e) {
            Log.i("Data","UDPBroadCast已经挂啦");
            e.printStackTrace();
        }
    }

    public void onStop() {
        Log.i("Data","datagramSocket是否连接="+datagramSocket.isConnected()+"，是否关闭="+datagramSocket.isClosed());
        if (!datagramSocket.isConnected() || datagramSocket.isClosed()) {
            Log.i("Data", "datagramSocket已关闭");
            datagramSocket.close();
        }
        this.interrupt();
        Log.i("Data", "线程停止");
    }


    public void ReUDPAndSenTCP(){
        if(isUdpSendFinished){
            if( mUDPReceiveAndUDPSend == null) {
                mUDPReceiveAndUDPSend = new UDPReceiveAndUDPSend(mContext, callback);
                mUDPReceiveAndUDPSend.start();
            }
        }
    }

}
