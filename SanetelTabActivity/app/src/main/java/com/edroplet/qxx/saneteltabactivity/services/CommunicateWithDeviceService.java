package com.edroplet.qxx.saneteltabactivity.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer; 
import java.nio.CharBuffer; 
import java.nio.channels.SocketChannel; 
import java.nio.charset.CharacterCodingException; 
import java.nio.charset.Charset; 
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 * 与服务器进行通信， 获取结果
 */
public class CommunicateWithDeviceService extends IntentService {
    public static final String SERVICE_NAME = CommunicateWithDeviceService.class.getName();
    private static final String TAG = CommunicateWithDeviceService.class.getSimpleName();
    // TODO: Rename actions, choose action names that describe tasks that this
    // TODO: ①定义ACTION常量，action_news和action_weather，即“新闻”和“天气”
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_CMD_RECEIVE = "com.edroplet.sanetel.services.action.RECEIVE";
    private static final String ACTION_CMD_SEND = "com.edroplet.sanetel.services.action.SEND";

    // TODO: Rename parameters
    // TODO: ②启动Service，需要传一些参数
    public static final String EXTRA_PARAM_SEND_CMD = "com.edroplet.sanetel.services.extra.SEND_CMD";
    public static final String EXTRA_PARAM_SEND_DATA = "com.edroplet.sanetel.services.extra.SEND_DATA";

    //TODO :③获得结果之后需要用Broadcast将结果发送给Activity以更新UI视图
    public static final String ACTION_DATA_RESULT = "com.edroplet.sanetel.service.ACTION_DATA_RESULT";
    public static final String ACTION_CMD_RESULT = "com.edroplet.sanetel.service.ACTION_CMD_RESULT";
    //TODO :④发送广播，需要传入的一些参数
    public static final String EXTRA_PARAM_RESULT_DATA = "com.edroplet.sanetel.service.extra.RESULT.DATA";
    public static final String EXTRA_PARAM_RESULT_CMD = "com.edroplet.sanetel.service.extra.RESULT.CMD";

    private static String ipAddress = "";
    private static int ipPort = 0;

    private static SocketChannel client = null;
    private static Context mContext = null;

    public CommunicateWithDeviceService() {
        super("CommunicateWithDeviceService");
    }
    private static Intent intentCommunicateWithDeviceService;
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionReceive(Context context, String cmd, String data) {
        if (null == mContext) mContext = context;

        if (null == intentCommunicateWithDeviceService || !SystemServices.isServiceRunning(CommunicateWithDeviceService.SERVICE_NAME, context)) {
            intentCommunicateWithDeviceService = new Intent(context, CommunicateWithDeviceService.class);
        }
        intentCommunicateWithDeviceService.setAction(ACTION_CMD_RECEIVE);
        intentCommunicateWithDeviceService.putExtra(EXTRA_PARAM_SEND_CMD, cmd);
        intentCommunicateWithDeviceService.putExtra(EXTRA_PARAM_SEND_DATA, data);
        context.startService(intentCommunicateWithDeviceService);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSend(Context context, String cmd, String data) {
        if (null == mContext) mContext = context;

        if (null == intentCommunicateWithDeviceService || !SystemServices.isServiceRunning(CommunicateWithDeviceService.SERVICE_NAME, context)){
            intentCommunicateWithDeviceService = new Intent(context, CommunicateWithDeviceService.class);
        }
        intentCommunicateWithDeviceService.setAction(ACTION_CMD_SEND);
        intentCommunicateWithDeviceService.putExtra(EXTRA_PARAM_SEND_CMD, cmd);
        intentCommunicateWithDeviceService.putExtra(EXTRA_PARAM_SEND_DATA, data);
        context.startService(intentCommunicateWithDeviceService);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CMD_RECEIVE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM_SEND_CMD);
                final String param2 = intent.getStringExtra(EXTRA_PARAM_SEND_DATA);
                handleActionReceive(param1, param2);
            } else if (ACTION_CMD_SEND.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM_SEND_CMD);
                final String param2 = intent.getStringExtra(EXTRA_PARAM_SEND_DATA);
                handleActionSend(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionReceive(final String cmd, final String parameters) {
        //次线程里操作网络请求数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null == client || !client.isConnected()|| !client.isOpen()){
                    ConnectToServer();
                    StartServerListener();
                }
                getSystemState(cmd);
                // 在listen中组包
            }
        }).start();
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSend(String param1, String param2) {
        if (null == client){
            ConnectToServer();
            StartServerListener();
        }
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void getSystemState(String cmd){
        if (null == client || !client.isConnected()|| !client.isOpen()) {
            ConnectToServer();
            StartServerListener();
        }
        SendMessageToServer(cmd);
    }
    InetSocketAddress isa;
    private void  ConnectToServer(){
        try {
            if (null == client || !client.isOpen()) {
                client = SocketChannel.open();
                String ip = CustomSP.getString(mContext, CustomSP.KeyIPSettingsAddress, CustomSP.DefaultIP);
                int port = CustomSP.getInt(mContext, CustomSP.KeyIPSettingsPort, CustomSP.DefaultPort);

                isa = new InetSocketAddress(ip, port);
                client.connect(isa);
                // 设置阻塞
                client.configureBlocking(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    private void StartServerListener(){
        ServerListener sl = new ServerListener();
        sl.start();
    }

    // 向Server端发送消息 
    public void SendMessageToServer(String msg) { 
        System.out.println("Send:" + msg); 
        try {
            if (null != client && client.isConnected()) {
                ByteBuffer bytebuf = ByteBuffer.allocate(1024);
                bytebuf = ByteBuffer.wrap(msg.getBytes("UTF-8"));
                client.write(bytebuf);
                bytebuf.flip();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
            System.out.println(" SendMessageToServer IOException===");
        } 
    }

    private void DisConnectToServer(){
        try{
            if(null != client|| !client.isOpen()){
                client.close();
                client = null;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void sendMsg(String msg){
        // 解析message, 不能在服务中解析
        //
        // 指定广播目标的 action （注：指定了此 action 的 receiver 会接收此广播）
        Intent intent = new Intent(ACTION_DATA_RESULT);
        // 需要传递的参数
        // 此处传送的数据的是集合类型，也可以有其他的类型：intent.put
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PARAM_RESULT_DATA, msg);
        // bundle.putString(EXTRA_PARAM_RESULT_CMD, msg);
        intent.putExtras(bundle);
        // 发送广播
        this.sendBroadcast(intent);
    }

    private class ServerListener extends Thread {
        public void run() {
            try { 
                // 无限循环，监听服务器,如果有不为空的信息送达，则更新Activity的UI
                while (true) {

                    //buf.clear();
                    if (client != null) {
                        if (!client.isConnected()){
                            client.connect(isa);
                        }
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        client.read(buf);
                        buf.flip();
                        Charset charset = Charset.forName("UTF-8");
                        CharsetDecoder decoder = charset.newDecoder();
                        CharBuffer charBuffer;
                        charBuffer = decoder.decode(buf);
                        String result = charBuffer.toString();
                        Log.e(CommunicateWithDeviceService.class.getSimpleName(),result);
                        if (result.length() > 0) {
                            sendMsg(result);
                        }
                    }
                }
            } catch (CharacterCodingException e) {
                e.printStackTrace(); 
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        // DisConnectToServer();
    }

}
