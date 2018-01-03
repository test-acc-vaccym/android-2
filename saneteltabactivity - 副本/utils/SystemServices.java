package com.edroplet.qxx.saneteltabactivity.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager.WifiLock;
import android.view.View;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.main.MainWifiSettingHelpActivity;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;
import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.beans.LockerInfo;
import com.edroplet.qxx.saneteltabactivity.beans.SavingInfo;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by qxs on 2017/10/11.
 */

public class SystemServices {
    public static final int REQUEST_WIFI_CONNECT_MANAGER = 10000;
    public static final int REQUEST_WIFI_CONNECT_HELP = 10001;
    public static final int REQUEST_WIFI_CONNECT_MANAGER_DIRECT_IN = 10002;
    public static final String XWWT_PREFIX = "XWWT-";

    public static String getConnectWifiSsid(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo", wifiInfo.toString());
        Log.d("SSID",wifiInfo.getSSID());
        return wifiInfo.getSSID();
    }
    public static void startWifiManager(Activity activity, int requestId){
        // context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));//进入无线网络配置界面
        Intent intent= new Intent(Settings.ACTION_WIFI_SETTINGS);//进入无线网络配置界面
        // Intent intent= new Intent(activity, WifiManagerActivity.class);
        activity.startActivityForResult(intent, requestId);//进入无线网络配置界面
    }

    public static void checkConnectedSsid(final Context context, String ssid, final Activity activity,
                                          DialogInterface.OnClickListener onCancelClickListener,
                                          final int requestId){
        String currentSSID = getConnectWifiSsid(context);
        if (!currentSSID.startsWith(XWWT_PREFIX)) {
            final RandomDialog randomDialog = new RandomDialog(context);
            randomDialog.onConfirmEDropletDialogBuilder(context.getString(R.string.not_connected_wifi_prompt) + ssid, "?",
                    new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startWifiManager(activity, requestId);
                }
            }, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    activity.startActivityForResult(new Intent(context, MainWifiSettingHelpActivity.class), REQUEST_WIFI_CONNECT_HELP);
                }
            },onCancelClickListener);
        }
    }

    public  static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)(context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivityManager != null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null){
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    public static class WifiAdmin {
        // 定义WifiManager对象
        private WifiManager mWifiManager;
        // 定义WifiInfo对象
        private WifiInfo mWifiInfo;
        // 扫描出的网络连接列表
        private List<ScanResult> mWifiList;
        // 网络连接列表
        private List<WifiConfiguration> mWifiConfiguration;
        // 定义一个WifiLock
        WifiLock mWifiLock;

        // 构造器
        public WifiAdmin(Context context) {
            // 取得WifiManager对象
            mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            // 取得WifiInfo对象
            mWifiInfo = mWifiManager.getConnectionInfo();
        }

        // 打开WIFI
        public void openWifi(Context context) {
            if (!mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(true);
            }else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                Toast.makeText(context,"亲，Wifi正在开启，不用再开了", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"亲，Wifi已经开启,不用再开了", Toast.LENGTH_SHORT).show();
            }
        }

        // 关闭WIFI
        public void closeWifi(Context context) {
            if (mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(false);
            }else if(mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED){
                Toast.makeText(context,"亲，Wifi已经关闭，不用再关了", Toast.LENGTH_SHORT).show();
            }else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
                Toast.makeText(context,"亲，Wifi正在关闭，不用再关了", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"请重新关闭", Toast.LENGTH_SHORT).show();
            }
        }

        // 检查当前WIFI状态
        public void checkState(Context context) {
            if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
                Toast.makeText(context,"Wifi正在关闭", Toast.LENGTH_SHORT).show();
            } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
                Toast.makeText(context,"Wifi已经关闭", Toast.LENGTH_SHORT).show();
            } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                Toast.makeText(context,"Wifi正在开启", Toast.LENGTH_SHORT).show();
            } else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                Toast.makeText(context,"Wifi已经开启", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context,"没有获取到WiFi状态", Toast.LENGTH_SHORT).show();
            }
        }

        // 锁定WifiLock
        public void acquireWifiLock() {
            mWifiLock.acquire();
        }

        // 解锁WifiLock
        public void releaseWifiLock() {
            // 判断时候锁定
            if (mWifiLock.isHeld()) {
                mWifiLock.acquire();
            }
        }

        // 创建一个WifiLock
        public void creatWifiLock() {
            mWifiLock = mWifiManager.createWifiLock("Test");
        }

        // 得到配置好的网络
        public List<WifiConfiguration> getConfiguration() {
            return mWifiConfiguration;
        }

        // 指定配置好的网络进行连接
        public void connectConfiguration(int index) {
            // 索引大于配置好的网络索引返回
            if (index > mWifiConfiguration.size()) {
                return;
            }
            // 连接配置好的指定ID的网络
            mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                    true);
        }

        public void startScan(Context context) {
            mWifiManager.startScan();
            // 得到扫描结果
            List<ScanResult> results = mWifiManager.getScanResults();
            // 得到配置好的网络连接
            mWifiConfiguration = mWifiManager.getConfiguredNetworks();
            if (results == null) {
                if(mWifiManager.getWifiState()== WifiManager.WIFI_STATE_ENABLED){
                    Toast.makeText(context,"当前区域没有无线网络", Toast.LENGTH_SHORT).show();
                }else if(mWifiManager.getWifiState()== WifiManager.WIFI_STATE_ENABLING){
                    Toast.makeText(context,"WiFi正在开启，请稍后重新点击扫描", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"WiFi没有开启，无法扫描", Toast.LENGTH_SHORT).show();
                }
            }else {
                mWifiList = new ArrayList();
                for(ScanResult result : results){
                    if (result.SSID == null || result.SSID.length() == 0 || result.capabilities.contains("[IBSS]")) {
                        continue;
                    }
                    boolean found = false;
                    for(ScanResult item:mWifiList){
                        if(item.SSID.equals(result.SSID)&&item.capabilities.equals(result.capabilities)){
                            found = true;break;
                        }
                    }
                    if(!found){
                        mWifiList.add(result);
                    }
                }
            }
        }

        // 得到网络列表
        public List<ScanResult> getWifiList() {
            return mWifiList;
        }

        // 查看扫描结果
        public StringBuilder lookUpScan() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < mWifiList.size(); i++) {
                stringBuilder
                        .append("Index_" + new Integer(i + 1).toString() + ":");
                // 将ScanResult信息转换成一个字符串包
                // 其中把包括：BSSID、SSID、capabilities、frequency、level
                stringBuilder.append((mWifiList.get(i)).toString());
                stringBuilder.append("/n");
            }
            return stringBuilder;
        }

        // 得到MAC地址
        public String getMacAddress() {
            return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
        }

        // 得到接入点的BSSID
        public String getBSSID() {
            return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
        }

        // 得到IP地址
        public int getIPAddress() {
            return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
        }

        // 得到连接的ID
        public int getNetworkId() {
            return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
        }

        // 得到WifiInfo的所有信息包
        public String getWifiInfo() {
            return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
        }

        // 添加一个网络并连接
        public void addNetwork(WifiConfiguration wcg) {
            int wcgID = mWifiManager.addNetwork(wcg);
            boolean b =  mWifiManager.enableNetwork(wcgID, true);
            System.out.println("a--" + wcgID);
            System.out.println("b--" + b);
        }

        // 断开指定ID的网络
        public void disconnectWifi(int netId) {
            mWifiManager.disableNetwork(netId);
            mWifiManager.disconnect();
        }
        public void removeWifi(int netId) {
            disconnectWifi(netId);
            mWifiManager.removeNetwork(netId);
        }

        //然后是一个实际应用方法，只验证过没有密码的情况：

        public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type)
        {
            WifiConfiguration config = new WifiConfiguration();
            config.allowedAuthAlgorithms.clear();
            config.allowedGroupCiphers.clear();
            config.allowedKeyManagement.clear();
            config.allowedPairwiseCiphers.clear();
            config.allowedProtocols.clear();
            config.SSID = "\"" + SSID + "\"";

            WifiConfiguration tempConfig = this.IsExsits(SSID);
            if(tempConfig != null) {
                mWifiManager.removeNetwork(tempConfig.networkId);
            }

            if(Type == 1) //WIFICIPHER_NOPASS
            {
                config.wepKeys[0] = "";
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
            }
            if(Type == 2) //WIFICIPHER_WEP
            {
                config.hiddenSSID = true;
                config.wepKeys[0]= "\""+Password+"\"";
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
            }
            if(Type == 3) //WIFICIPHER_WPA
            {
                config.preSharedKey = "\""+Password+"\"";
                config.hiddenSSID = true;
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.status = WifiConfiguration.Status.ENABLED;
            }
            return config;
        }

        private WifiConfiguration IsExsits(String SSID)
        {
            List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
            for (WifiConfiguration existingConfig : existingConfigs)
            {
                if (existingConfig.SSID.equals("\""+SSID+"\""))
                {
                    return existingConfig;
                }
            }
            return null;
        }
    }

    public static void copyAssetsFiles2FileDir(Context context,String filename){
        InputStream is=null;
        Writer writer = null;
        try {
            is=context.getAssets().open(filename);
            int filesize=is.available();
            byte[] buffer=new byte[0];
            if(filesize>0){
                buffer=new byte[filesize];
            }
            OutputStream out = context.openFileOutput(filename,
                    Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);

            int t=0;
            while((t=is.read(buffer))!=-1){
                writer.write(new String(buffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(null!=is){
                try{
                    is.close();
                }catch(Exception e){}
            }

            if (writer != null) {

                try{
                    writer.close();
                }catch(Exception e){}
            }

        }
    }

    /**
     * 重启整个APP
     * @param context
     * @param Delayed 延迟多少毫秒
     */
    public static void restartAPP(final Context context,long Delayed){

        /**开启一个新的服务，用来重启本APP
        Intent intent1=new Intent(context,KillSelfService.class);
        intent1.putExtra("PackageName",context.getPackageName());
        intent1.putExtra("Delayed",Delayed);
        context.startService(intent1);

        杀死整个进程
        android.os.Process.killProcess(android.os.Process.myPid());
         */
        /*  http://blog.csdn.net/ifangler/article/details/44100193
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
        System.exit(0);
        * */

        TimerTask task = new TimerTask(){
            public void run(){
                Intent i = context.getPackageManager()
                        .getLaunchIntentForPackage(context.getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, Delayed);
    }

    public static void delay(long delay){
        try { Thread.sleep(delay); } catch (InterruptedException e) {}
        /*
        new Handler().postDelayed(new Runnable(){
            public void run() {
                //execute the task
            }
        }, delay);
        */
    }
    public static void restoreAPP(final Context context, long Delayed){

        // 删除缓存
        File privateCacheDir = context.getCacheDir();
        String[] cacheFiles = privateCacheDir.list();
        for (String f : cacheFiles){
                FileUtils.DeleteFolder(f);
        }

        // 删除files
        String[] files = context.fileList();
        for (String f : files){
            context.deleteFile(f);
            // FileUtils.DeleteFolder(f);
        }
        // 删除database
        String[] privateDatabaseDir = context.databaseList();
        for (String f : privateDatabaseDir){
            // FileUtils.DeleteFolder(f);
            context.deleteDatabase(f);
        }

        // 删除 shared_prefs目录
        CustomSP.clear(context);
        clearSharedPreferences(context);

        // 删除自定义dir

        // 重启app
        restartAPP(context,  Delayed);
    }

    public static void clearSharedPreferences(Context ctx){
        File dir = new File(ctx.getFilesDir().getParent() + "/shared_prefs/");
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            // clear each of the prefrances
            ctx.getSharedPreferences(children[i].replace(".xml", ""), Context.MODE_PRIVATE).edit().clear().commit();
        }
        // Make sure it has enough time to save all the commited changes
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        for (int i = 0; i < children.length; i++) {
            // delete the files
            new File(dir, children[i]).delete();
        }
    }

    public static boolean isServiceRunning(String servicename, Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo>  infos = am.getRunningServices(100);
        for(ActivityManager.RunningServiceInfo info: infos){
            if(servicename.equals(info.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
