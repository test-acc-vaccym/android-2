package com.edroplet.sanetel.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.edroplet.sanetel.beans.AmplifierInfo;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.beans.monitor.EquipmentInfo;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.SystemServices;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer; 
import java.nio.CharBuffer; 
import java.nio.channels.SocketChannel; 
import java.nio.charset.CharacterCodingException; 
import java.nio.charset.Charset; 
import java.nio.charset.CharsetDecoder;
import com.edroplet.sanetel.beans.monitor.MonitorInfo;

import static com.edroplet.sanetel.activities.settings.ReferenceSatelliteActivity.ACTION_RECEIVE_REFERENCE_INFO;
import static com.edroplet.sanetel.activities.settings.ReferenceSatelliteActivity.KEY_RECEIVE_REFERENCE_INFO_DATA;
import static com.edroplet.sanetel.beans.AmplifierInfo.AmplifierInfoAction;
import static com.edroplet.sanetel.beans.AmplifierInfo.AmplifierInfoData;
import static com.edroplet.sanetel.beans.monitor.EquipmentInfo.EquipmentInfoAction;
import static com.edroplet.sanetel.beans.monitor.EquipmentInfo.EquipmentInfoData;
import static com.edroplet.sanetel.beans.monitor.TemperatureInfo.TemperatureInfoAction;
import static com.edroplet.sanetel.beans.monitor.TemperatureInfo.TemperatureInfoData;
import static com.edroplet.sanetel.fragments.guide.GuideFragmentDestination.GuideDestinationAction;
import static com.edroplet.sanetel.fragments.guide.GuideFragmentDestination.GuideDestinationData;
import static com.edroplet.sanetel.fragments.guide.GuideFragmentLocation.LocationGetPositionAction;
import static com.edroplet.sanetel.fragments.guide.GuideFragmentLocation.LocationGetPositionData;
import static com.edroplet.sanetel.fragments.guide.GuideFragmentSearchModeSetting.SearchModeAction;
import static com.edroplet.sanetel.fragments.guide.GuideFragmentSearchModeSetting.SearchModeData;
import static com.edroplet.sanetel.fragments.settings.SettingsFragmentAmplifierInterfere.AmplifierInterfereAction;
import static com.edroplet.sanetel.fragments.settings.SettingsFragmentAmplifierInterfere.AmplifierInterfereData;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentAmplifierManufacturer.AmplifierManufacturerAction;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentAmplifierManufacturer.AmplifierManufacturerData;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentAmplifierMonitor.AmplifierMonitorAction;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentAmplifierMonitor.AmplifierMonitorData;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentAmplifierOscillator.AmplifierOscillatorAction;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentAmplifierOscillator.AmplifierOscillatorData;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentAntennaIncriminate.AntennaIncriminateAction;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentAntennaIncriminate.AntennaIncriminateData;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentBandSelect.BandTypeAction;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentBandSelect.BandTypeData;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentIPSettings.IPSettingsAction;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentIPSettings.IPSettingsData;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentLNBOscillator.LNBOscAction;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentLNBOscillator.LNBOscData;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentNetworkProtocolSettings.NetworkProtocolSettingsAction;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentNetworkProtocolSettings.NetworkProtocolSettingsData;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentSearchingRange.SearchingRangeAction;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentSearchingRange.SearchingRangeData;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentSerialProtocolSettings.SerialProtocolAction;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentSerialProtocolSettings.SerialProtocolData;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentWifiSettings.WifiSettingsAction;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentWifiSettings.WifiSettingsData;

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
        Log.e(TAG, "startActionReceive");
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
        Log.e(TAG, "startActionSend");
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
        Log.e(TAG, "onHandleIntent");
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
        if (sleepForConnect) {
            Log.e(TAG, "handleActionReceive");
            //次线程里操作网络请求数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (null == client || !client.isConnected() || !client.isOpen()) {
                        ConnectToServer();
                        StartServerListener();
                    }
                    getSystemState(cmd);
                    // 在listen中组包
                }
            }).start();
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSend(final String cmd, String param2) {
        if (sleepForConnect) {
            Log.e(TAG, "handleActionSend");
            if (null == client || !client.isConnected() || !client.isOpen()) {

                //次线程里操作网络请求数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ConnectToServer();
                        StartServerListener();
                        getSystemState(cmd);
                        // 在listen中组包
                    }
                }).start();
            } else {
                getSystemState(cmd);
            }
        }
    }

    private void getSystemState(final String cmd){
        if (sleepForConnect) {
            Log.e(TAG, "getSystemState, cmd is :" + cmd);
            if (null == client || !client.isConnected() || !client.isOpen()) {
                //次线程里操作网络请求数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ConnectToServer();
                        StartServerListener();
                        SendMessageToServer(cmd);
                        // 在listen中组包
                    }
                }).start();
            } else {
                SendMessageToServer(cmd);
            }
        }
    }

    InetSocketAddress isa;
    boolean sleepForConnect = false;
    private void  ConnectToServer(){
        try {

            String ipWIfi = SystemServices.getIPAddress(this).getIp();

            Log.d(TAG, "ConnectToServer, cmd client :" + (client==null? "" : client.toString()));
            if (null == client || !client.isOpen() || !client.isConnected()) {
                client = SocketChannel.open();
                String ip = CustomSP.getString(mContext, CustomSP.KeyIPSettingsAddress, ipWIfi);
                // 判断是否设置过IP
                while (ip.equals(ipWIfi)){
                    Log.d(TAG, "ConnectToServer, ip is:" + ipWIfi );
                    // 2017/11/25 ip不设置正确，一直等待
                    sleepForConnect = true;
                    Thread.sleep(1000);

                    ipWIfi = SystemServices.getIPAddress(this).getIp();

                    ip = CustomSP.getString(mContext, CustomSP.KeyIPSettingsAddress, ipWIfi);
                }
                sleepForConnect = false;

                int port = CustomSP.getInt(mContext, CustomSP.KeyIPSettingsPort, CustomSP.DefaultPort);
                while (!client.isConnected()) {
                    Log.w(TAG, "ConnectToServer, client is not open, open now");
                    // 2017/11/25 client没有open，一直等待
                    Thread.sleep(1000);

                    ipWIfi = SystemServices.getIPAddress(this).getIp();

                    ip = CustomSP.getString(mContext, CustomSP.KeyIPSettingsAddress, ipWIfi);
                    try {
                        isa = new InetSocketAddress(ip, port);
                        client.connect(isa);
                        // 设置阻塞
                        client.configureBlocking(false);
                    } catch (IOException ioe) {
                        Log.e(TAG, "ConnectToServer ERROR. "+ioe.toString());
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, e.toString());
            DisConnectToServer();
        }
    }

    private void StartServerListener(){
        ServerListener sl = new ServerListener();
        sl.start();
    }

    // 向Server端发送消息 
    public void SendMessageToServer(String msg) {
        Log.d(TAG, "SendMessageToServer, msg is :"+msg);
        try {
            if (null != client && client.isConnected()) {
                ByteBuffer bytebuf = ByteBuffer.allocate(1024);
                bytebuf = ByteBuffer.wrap(msg.getBytes("UTF-8"));
                client.write(bytebuf);
                bytebuf.flip();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    private void DisConnectToServer(){
        try{
            if(null != client && !client.isOpen()){
                client.close();
                client = null;
            }
        }catch(IOException e){
            e.printStackTrace();
            client = null;
        }
    }

    private void sendMsg(String msg){
        Log.e(TAG, "sendMsg, msg is :"+msg);
        Intent intent = new Intent(ACTION_DATA_RESULT);
        // 这里区分broadcast
        if (msg.startsWith(Protocol.cmdGetSystemStateResultHead)){
            intent.setAction(MonitorInfo.MonitorInfoAction);
            intent.putExtra(MonitorInfo.MonitorInfoData,msg);
        } else if (msg.startsWith(Protocol.cmdGetBucInfoResultHead)) {
            intent.setAction(AmplifierInfoAction);
            intent.putExtra(AmplifierInfoData,msg);
        } else if (msg.startsWith(Protocol.cmdGetRefDataResultHead)) {
            intent.setAction(ACTION_RECEIVE_REFERENCE_INFO);
            intent.putExtra(KEY_RECEIVE_REFERENCE_INFO_DATA,msg);
        } else if (msg.startsWith(Protocol.cmdGetTemperatureResultHead)) {
            intent.setAction(TemperatureInfoAction);
            intent.putExtra(TemperatureInfoData,msg);
        } else if (msg.startsWith(Protocol.cmdGetEquipmentInfoResultHead)) {
            intent.setAction(EquipmentInfoAction);
            intent.putExtra(EquipmentInfoData,msg);
        } else if (msg.startsWith(Protocol.cmdGetTrackModeResultHead)) {
            intent.setAction(SearchModeAction);
            intent.putExtra(SearchModeData,msg);
        } else if (msg.startsWith(Protocol.cmdGetTargetStateResultHead)) {
            intent.setAction(GuideDestinationAction);
            intent.putExtra(GuideDestinationData,msg);
        } else if (msg.startsWith(Protocol.cmdGetPositionResultHead)) {
            intent.setAction(LocationGetPositionAction);
            intent.putExtra(LocationGetPositionData,msg);
        } else if (msg.startsWith(Protocol.cmdGetBucFactoryResultHead)) {
            intent.setAction(AmplifierManufacturerAction);
            intent.putExtra(AmplifierManufacturerData,msg);
        } else if (msg.startsWith(Protocol.cmdGetBucLfResultHead)) {
            intent.setAction(AmplifierOscillatorAction);
            intent.putExtra(AmplifierOscillatorData,msg);
        } else if (msg.startsWith(Protocol.cmdGetProtectStateResultHead)) {
            intent.setAction(AmplifierInterfereAction);
            intent.putExtra(AmplifierInterfereData,msg);
        } else if (msg.startsWith(Protocol.cmdGetBucInfoSwitchResultHead)) {
            intent.setAction(AmplifierMonitorAction);
            intent.putExtra(AmplifierMonitorData,msg);
        } else if (msg.startsWith(Protocol.cmdGetLnbLfResultHead)) {
            intent.setAction(LNBOscAction);
            intent.putExtra(LNBOscData,msg);
        } else if (msg.startsWith(Protocol.cmdGetCalibAntResultHead)) {
            intent.setAction(AntennaIncriminateAction);
            intent.putExtra(AntennaIncriminateData,msg);
        } else if (msg.startsWith(Protocol.cmdGetSearchRangeResultHead)) {
            intent.setAction(SearchingRangeAction);
            intent.putExtra(SearchingRangeData,msg);
        } else if (msg.startsWith(Protocol.cmdGetBandResultHead)) {
            intent.setAction(BandTypeAction);
            intent.putExtra(BandTypeData,msg);
        } else if (msg.startsWith(Protocol.cmdGetWifiNameResultHead)) {
            intent.setAction(WifiSettingsAction);
            intent.putExtra(WifiSettingsData,msg);
        } else if (msg.startsWith(Protocol.cmdGetIPResultHead)) {
            intent.setAction(IPSettingsAction);
            intent.putExtra(IPSettingsData,msg);
        } else if (msg.startsWith(Protocol.cmdGetNetUseridResultHead)) {
            intent.setAction(NetworkProtocolSettingsAction);
            intent.putExtra(NetworkProtocolSettingsData,msg);
        } else if (msg.startsWith(Protocol.cmdGetComUseridResultHead)) {
            intent.setAction(SerialProtocolAction);
            intent.putExtra(SerialProtocolData,msg);
        } else {
                // 解析message, 不能在服务中解析
                //
                // 指定广播目标的 action （注：指定了此 action 的 receiver 会接收此广播）
                intent.setAction(ACTION_DATA_RESULT);
                // 需要传递的参数
                // 此处传送的数据的是集合类型，也可以有其他的类型：intent.put
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_PARAM_RESULT_DATA, msg);
                // bundle.putString(EXTRA_PARAM_RESULT_CMD, msg);
                intent.putExtras(bundle);
        }
        // 发送广播
        this.sendBroadcast(intent);
    }

    private class ServerListener extends Thread {
        public void run() {
            try { 
                // 无限循环，监听服务器,如果有不为空的信息送达，则更新Activity的UI
                while (true) {

                    //buf.clear();
                    if (client != null && client.isConnected()) {

                        ByteBuffer buf = ByteBuffer.allocate(8192);
                        client.read(buf);
                        buf.flip();
                        Charset charset = Charset.forName("UTF-8");
                        CharsetDecoder decoder = charset.newDecoder();
                        CharBuffer charBuffer;
                        charBuffer = decoder.decode(buf);
                        String result = charBuffer.toString();
                        Log.e(TAG, result);
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
        Log.d(TAG, "onDestroy");
        DisConnectToServer();
    }

}
