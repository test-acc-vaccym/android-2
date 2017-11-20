package com.edroplet.qxx.saneteltabactivity.services.communicate;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.edroplet.qxx.saneteltabactivity.beans.CollectHistoryFileInfo;
import com.edroplet.qxx.saneteltabactivity.services.CommunicateWithDeviceService;
import com.edroplet.qxx.saneteltabactivity.utils.DateTime;
import com.edroplet.qxx.saneteltabactivity.utils.FileUtils;

import java.io.IOException;

import static com.edroplet.qxx.saneteltabactivity.services.CommunicateWithDeviceService.*;


/**
 * Created by qxs on 2017/11/15.
 * receive data from sanetel wifi device
 */

public class CommunicateDataReceiver extends BroadcastReceiver {

    private final static String TAG = CommunicateDataReceiver.class.getSimpleName();
    private final static int RECEIVE_SERVICE_ID = -1111;

    public final static String ACTION_KEEP_ALIVE = "com.edroplet.sanetel.data.KEEP_ALIVE";
    public final static String ACTION_RECEIVE_DATA = "com.edroplet.sanetel.data.RECEIVE_DATA";
    public final static String ACTION_SAVE_FILE = "com.edroplet.sanetel.data.SAVE_FILE";
    public final static String ACTION_STOP_SAVE = "com.edroplet.sanetel.data.STOP_SAVE";
    public final static String ACTION_SEND_DATA = "com.edroplet.sanetel.data.SEND_DATA";
    public final static String ACTION_SCREEN_ON =("android.intent.action.SCREEN_ON");
    public final static String ACTION_SCREEN_OFF =("android.intent.action.SCREEN_OFF");
    public final static String ACTION_POWER_CONNECTED =("android.intent.action.ACTION_POWER_CONNECTED");
    public final static String ACTION_BOOT_COMPLETED =("android.intent.action.BOOT_COMPLETED");

    private static boolean saveFile = true;
    String sendCmd = null;
    String sendData = null;

    private static Context mContext;
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (mContext == null){
            mContext = context;
        }
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        Log.e(TAG,action);
        if (null != bundle) {
            sendCmd = bundle.getString(EXTRA_PARAM_SEND_CMD);
            sendData = bundle.getString(EXTRA_PARAM_SEND_DATA);
        } else {
            if (intent.hasExtra(EXTRA_PARAM_SEND_CMD))
                sendCmd = bundle.getString(EXTRA_PARAM_SEND_CMD);
            if (intent.hasExtra(EXTRA_PARAM_SEND_DATA))
                sendData = bundle.getString(EXTRA_PARAM_SEND_DATA);
        }
        if (null == sendCmd ){
            sendCmd = "";
        }
        if (null == sendData ){
            sendData = "";
        }

        // todo 每次操作都记录文件，直到停止服务
        Intent notifyIntent = new Intent(context, ReceiveNotifyService.class);
        if (null != notifyIntent && isInAction(action)) {
            if (bundle != null) {
                notifyIntent.putExtras(bundle);
            }
            notifyIntent.putExtra("action", action);
            context.startService(notifyIntent);
        }

        if (ACTION_RECEIVE_DATA.equals(action)){
            CommunicateWithDeviceService.startActionReceive(context, sendCmd, sendData);
        }else if (ACTION_SEND_DATA.equals(action)){
            CommunicateWithDeviceService.startActionSend(context,sendCmd, sendData);
        }else if (ACTION_SAVE_FILE.equals(action) || ACTION_DATA_RESULT.equals(action)){
            // receiver的生命周期很短，不能在此进行IO操作
            // 耗时的操作在服务中完成
        }else if (ACTION_STOP_SAVE.equals(action)){
            saveFile = false;
        }
    }

    private static boolean isInAction(String action){
        return  ACTION_RECEIVE_DATA.equals(action) || ACTION_DATA_RESULT.equals(action)
                ||ACTION_SEND_DATA.equals(action)  || ACTION_SAVE_FILE.equals(action)
                || ACTION_STOP_SAVE.equals(action) || ACTION_STOP_SAVE.equals(action);
    }

    /**
     * not impments here
     */
    public static class ReceiveNotifyService extends Service{
        Intent innerIntent;
        String receiveCmd = null;
        String receiveData = null;
        String sendCmd = null;
        String sendData = null;

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
                startForeground(RECEIVE_SERVICE_ID, new Notification());
            }else {
                if (null == innerIntent) {
                    innerIntent = new Intent(this, ReceiveInnerService.class);
                    startService(innerIntent);
                    startForeground(RECEIVE_SERVICE_ID, new Notification());
                }
            }
            if (intent != null) {
                String action = intent.getStringExtra("action");
                if (isInAction(action)) {
                    Bundle bundle = intent.getExtras();
                    if (null != bundle) {
                        receiveCmd = bundle.getString(EXTRA_PARAM_RESULT_CMD);
                        receiveData = bundle.getString(EXTRA_PARAM_RESULT_DATA);
                        sendCmd = bundle.getString(EXTRA_PARAM_SEND_CMD);
                        sendData = bundle.getString(EXTRA_PARAM_SEND_DATA);
                    } else {
                        if (intent.hasExtra(EXTRA_PARAM_SEND_CMD))
                            sendCmd = bundle.getString(EXTRA_PARAM_SEND_CMD);
                        if (intent.hasExtra(EXTRA_PARAM_SEND_DATA))
                            sendData = bundle.getString(EXTRA_PARAM_SEND_DATA);

                        if (intent.hasExtra(EXTRA_PARAM_RESULT_CMD))
                            receiveCmd = bundle.getString(EXTRA_PARAM_RESULT_CMD);
                        if (intent.hasExtra(EXTRA_PARAM_RESULT_DATA))
                            receiveData = bundle.getString(EXTRA_PARAM_RESULT_DATA);
                    }
                    if (null == sendCmd) {
                        sendCmd = "";
                    }
                    if (null == sendData) {
                        sendData = "";
                    }
                    if (null == receiveCmd) {
                        receiveCmd = "";
                    }
                    if (null == receiveData) {
                        receiveData = "";
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: 2017/11/17 保存文件
                            // 每次操作都保存文件最好， 收和发的数据
                            // if (receiveCmd.equals("") && saveFile){
                            if (saveFile && mContext != null) {
                                CollectHistoryFileInfo collectHistoryFileInfo = new CollectHistoryFileInfo(mContext);
                                String newestFile = collectHistoryFileInfo.getNewestCollectFile();
                                try {
                                    FileUtils.saveFile(newestFile, DateTime.getCurrentDateTime() + "\n"
                                            + (receiveCmd.isEmpty()?"":"receiveCmd:"+receiveCmd + "\n")
                                            + (receiveData.isEmpty()?"":"receiveData:" + receiveData + "\n")
                                            + (sendCmd.isEmpty()?"":"sendCmd:" + sendCmd +"\n" )
                                            + (sendData.isEmpty()?"":"sendData:" + sendData +"\n" ), true);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            // TODO: 2017/11/17 更新UI
                        }
                    }, "saveFile").start();
                }
            }
            return START_STICKY;
        }

        private void processMessage(){
            // TODO: 2017/11/17 处理返回结果
        }
        
        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    public static class ReceiveInnerService extends Service {
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(RECEIVE_SERVICE_ID, new Notification());
            stopSelf();
            return super.onStartCommand(intent,flags,startId);
        }

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
