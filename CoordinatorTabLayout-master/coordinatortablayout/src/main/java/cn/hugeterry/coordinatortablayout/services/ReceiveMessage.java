package cn.hugeterry.coordinatortablayout.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import cn.hugeterry.coordinatortablayout.BaseActivity;

/**
 * Created by qxs on 2017/9/4.
 */

public class ReceiveMessage extends Service {
    // @Override
    // public int onStartCommand(Intent intent, int flags, int startId) {
    // // TODO Auto-generated method stub
    // return super.onStartCommand(intent, flags, startId);
    // }

    private SocketChannel client = null;
    private InetSocketAddress isa = null;
    private String message = "";
    private Activity mActivity;

    public void onCreate() {
        System.out.println("----- onCreate---------");
        super.onCreate();

        ConnectToServer();
        StartServerListener();

    }

    public void onDestroy() {
        super.onDestroy();
        DisConnectToServer();
    }

    public void SetActivity(Activity act){
        this.mActivity = act;
    }

    public void onStart(Intent intent, int startId) {
        System.out.println("----- onStart---------");
        super.onStart(intent, startId);
    }

    /*
     * IBinder方法 , LocalBinder 类,mBinder接口这三项用于
     * Activity进行Service的绑定，点击发送消息按钮之后触发绑定 并通过Intent将Activity中的EditText的值
     * 传送到Service中向服务器发送
     */
    public IBinder onBind(Intent intent) {
        System.out.println("----- onBind---------");

        //        message = intent.getStringExtra("chatmessage");
        //        if (message.length() > 0) {
        //            SendMessageToServer(message);
        //        }
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public ReceiveMessage getService() {
            return ReceiveMessage.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    // 用于链接服务器端
    public void ConnectToServer() {
        try {

            client = SocketChannel.open();
            //isa = new InetSocketAddress("10.0.2.2", 9005);
            isa = new InetSocketAddress("211.141.230.246", 6666);
            client.connect(isa);
            client.configureBlocking(false);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    // 断开与服务器端的链接
    public void DisConnectToServer() {
        try {
            client.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 启动服务器端的监听线程，从Server端接收消息
    public void StartServerListener() {
        ServerListener a = new ServerListener();
        a.start();
    }

    // 向Server端发送消息
    public void SendMessageToServer(String msg) {
        System.out.println("Send:" + msg);
        try {
            ByteBuffer bytebuf = ByteBuffer.allocate(1024);
            bytebuf = ByteBuffer.wrap(msg.getBytes("UTF-8"));
            client.write(bytebuf);
            bytebuf.flip();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" SendMessageToServer IOException===");
        }
    }

    private void shownotification(String tab) {
        System.out.println("shownotification=====" + tab);
        NotificationManager barmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification msg = new Notification(
                android.R.drawable.stat_notify_chat, "A Message Coming!",
                System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, BaseActivity.class), PendingIntent.FLAG_ONE_SHOT);
//        msg.setLatestEventInfo(this, "Message", "Message:" + tab, contentIntent);
        barmanager.notify(0, msg);
    }

    // 发送广播信息
    private void sendMsg(String msg){
        // 指定广播目标的 action （注：指定了此 action 的 receiver 会接收此广播）
        Intent intent = new Intent("com.archfree.demo.msg");
        // 需要传递的参数
        intent.putExtra("msg", msg);
        // 发送广播
        this.sendBroadcast(intent);
    }

    private class ServerListener extends Thread {
        //private    ByteBuffer buf = ByteBuffer.allocate(1024);
        public void run() {

            try {
                // 无线循环，监听服务器,如果有不为空的信息送达，则更新Activity的UI
                while (true) {
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    //buf.clear();
                    client.read(buf);
                    buf.flip();
                    Charset charset = Charset.forName("UTF-8");
                    CharsetDecoder decoder = charset.newDecoder();
                    CharBuffer charBuffer;
                    charBuffer = decoder.decode(buf);
                    String result = charBuffer.toString();
                    if (result.length() > 0)
                    {// recvData(result);
                        sendMsg(result);
                        //System.out.println("+++++="+result);

                        //shownotification(result);
                    }


                    // System.out.println("++++++++++++++++++="+result);
                }
            } catch (CharacterCodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
