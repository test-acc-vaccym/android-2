package com.sanetel.control.server;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.sanetel.control.bean.ServerInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import static com.sanetel.control.BuildConfig.DEBUG;
import static com.sanetel.control.bean.ConstData.*;

public class ReceiveMessageService extends Service {
    private SocketChannel client=null;
    private InetSocketAddress isa = null;
    private String message = "";
    private ServerInfo si = null;
    private IBinder binder = new LocalBinder();

    public ReceiveMessageService() {
    }
    public class LocalBinder extends Binder{
        ReceiveMessageService getSerice(){
            return ReceiveMessageService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Server s = new Server(this);
        si = s.GetServerInfo();
        ConnectToServer();
        StarServerListener();
    }

    @Override
    public IBinder onBind(Intent intent){
        // TODO: Return the communication channel to the service.
        return binder;
        //  throw new UnsupportedOperationException("Not yet implemented");
    }

    // 与服务器建立连接
    public void ConnectToServer(){
        try {

            client = SocketChannel.open();
            isa = new InetSocketAddress(si.GetIP(), Integer.valueOf(si.GetPort()));
            client.connect(isa);
            // 不阻塞
            client.configureBlocking(false);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // 断开与服务器的连接
    public  void DisconnectToServer(){
        try{
            client.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // 向Activity 发送广播消息
    public void SendMessage(String msg){
        Intent intent = new Intent(BROADCAST_NAME);
        intent.putExtra("message",msg);
        this.sendBroadcast(intent);
    }

    private class ServerListener extends Thread{
        public void run(){
            try {
                while (true){
                    ByteBuffer bb = ByteBuffer.allocate(2048);
                    client.read(bb);
                    bb.flip();
                    Charset charset = Charset.forName("UTF-8");
                    CharsetDecoder cd = charset.newDecoder();
                    CharBuffer cb;
                    cb = cd.decode(bb);
                    String msg = cb.toString();
                    if (!msg.isEmpty()){
                        //
                        SendMessage(msg);
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void StarServerListener(){
        ServerListener sl = new ServerListener();
        sl.run();
    }

    // 向Server端发送消息
    public void SendMessageToServer(String msg) {
        if (DEBUG)
            Log.i(TAG, "SendMessageToServer: " + msg);
        try {
            ByteBuffer bytebuf = ByteBuffer.allocate(1024);
            bytebuf = ByteBuffer.wrap(msg.getBytes("UTF-8"));
            client.write(bytebuf);
            bytebuf.flip();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(" SendMessageToServer IOException===");
        }
    }

// TODO 通知栏 咱们用不到
//    private void ShowNotification(String tab) {
//        System.out.println("shownotification=====" + tab);
//        NotificationManager barmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification msg = new Notification(
//                android.R.drawable.stat_notify_chat, "A Message Coming!",
//                System.currentTimeMillis());
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(SERVICE_RECEIVE), PendingIntent.FLAG_ONE_SHOT);
//        msg.setLatestEventInfo(this, "Message", "Message:" + tab, contentIntent);
//        barmanager.notify(0, msg);
//    }
}
