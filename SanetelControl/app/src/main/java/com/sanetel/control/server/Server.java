package com.sanetel.control.server;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.text.style.UpdateAppearance;
import android.util.Log;

import com.sanetel.control.bean.ServerInfo;

import java.nio.channels.SocketChannel;
import java.util.List;

import static com.sanetel.control.BuildConfig.DEBUG;
import static com.sanetel.control.bean.ConstData.*;

/**
 * Created by qxs on 2017/8/14.
 */

public class Server {

    final Context context;

    ServerInfo serverInfo;
    SharedPreferences serverSettings;
    public Server(Context context)
    {
        this.context=context;
    }

    public ServerInfo GetServerInfo(){
        serverInfo = new ServerInfo();
        //        打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
        serverSettings = context.getSharedPreferences("server_setting", 0);
        String ip = serverSettings.getString("server_ip","");
        String port = serverSettings.getString("server_port","");
        String id = serverSettings.getString("server_type","0");
        serverInfo.SetIP(ip);
        serverInfo.SetPort(port);
        serverInfo.SetID(id);
        return serverInfo;
    }

    public void SetServerInfo(ServerInfo si){
        //        打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
        serverSettings = context.getSharedPreferences("server_setting", 0);
        //        让setting处于编辑状态
        SharedPreferences.Editor editor = serverSettings.edit();
        //        存放数据
        editor.putString("server_ip",si.GetIP());
        editor.putString("server_port",si.GetPort());
        editor.putString("server_type",si.GetID());
        editor.apply();
    }

    public void SetServerInfo(String ip, String port, String type){
        //        打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
        serverSettings = context.getSharedPreferences("server_setting", 0);
        //        让setting处于编辑状态
        SharedPreferences.Editor editor = serverSettings.edit();
        //        存放数据
        editor.putString("server_ip",ip);
        editor.putString("server_port",port);
        editor.putString("server_type", type);
        editor.apply();
    }

    public void Clear(){
        //        打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
        serverSettings = context.getSharedPreferences("server_setting", 0);
        //        让setting处于编辑状态
        SharedPreferences.Editor editor = serverSettings.edit();
        editor.clear();
        editor.apply();
    }

    public void Delete(String key_name){
        //        打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
        serverSettings = context.getSharedPreferences("server_setting", 0);
        //        让setting处于编辑状态
        SharedPreferences.Editor editor = serverSettings.edit();
        editor.remove(key_name);
        editor.apply();
    }
    private final static String TAG = "Sanetel";
    public String msg = "";
    public class MessageReciver extends BroadcastReceiver{
       @Override
       public void onReceive(Context context, Intent intent) {
           if (DEBUG){
               Log.i(TAG, "onReceive: " + intent);
           }
           msg = intent.getStringExtra("message");
       }
   }
    private MessageReciver messageReciver;
    private ReceiveMessageService receiveMessageService = new ReceiveMessageService();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            receiveMessageService = ((ReceiveMessageService.LocalBinder)service).getSerice();

            if (DEBUG){
                Log.i(TAG, "onServiceConnected");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            receiveMessageService = null;
        }
    };

    protected void Destroy() {
        context.unbindService(serviceConnection);
        context.unregisterReceiver(messageReciver);
    }

    /***
     * Android L (lollipop, API 21) introduced a new problem when trying to invoke implicit intent,
     * "java.lang.IllegalArgumentException: Service Intent must be explicit"
     *
     * If you are using an implicit intent, and know only 1 target would answer this intent,
     * This method will help you turn the implicit intent into the explicit form.
     *
     * Inspired from SO answer: http://stackoverflow.com/a/26318757/1446466
     * @param context
     * @param implicitIntent - The original implicit intent
     * @return Explicit Intent created from the implicit original intent
     */
    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

    public void Connect(){
        messageReciver = new MessageReciver();
        //        为广播注册接口
        IntentFilter intentFilter= new IntentFilter(BROADCAST_NAME);
        // 以编程方式注册 BroadcastReceiver 。配置方式注册 BroadcastReceiver 的例子见
        // AndroidManifest.xml 文件
        // 一般在 OnStart 时注册，在 OnStop 时取消注册
        context.registerReceiver(messageReciver,intentFilter);

        // 注册服务
        // Service Intent must be explicit, 必须显示声明
        final Intent intent = new Intent();
        intent.setAction(SERVICE_RECEIVE);
        final Intent eintent = new Intent(createExplicitFromImplicitIntent(context,intent));
        context.bindService(eintent,serviceConnection, Context.BIND_AUTO_CREATE);
    }
}
