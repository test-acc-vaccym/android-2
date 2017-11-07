package com.edroplet.qxx.saneteltabactivity.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CommunicateWithDeviceService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // TODO: ①定义ACTION常量，action_news和action_weather，即“新闻”和“天气”
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_RECEIVE = "com.edroplet.qxx.saneteltabactivity.services.action.RECEIVE";
    private static final String ACTION_SEND = "com.edroplet.qxx.saneteltabactivity.services.action.SEND";

    // TODO: Rename parameters
    // TODO: ②启动Service，需要传一些参数
    private static final String EXTRA_PARAM1 = "com.edroplet.qxx.saneteltabactivity.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.edroplet.qxx.saneteltabactivity.services.extra.PARAM2";

    //TODO :③获得结果之后需要用Broadcast将结果发送给Activity以更新UI视图
    private static final String NEWS_RESULT = "com.czz.manager.service.RESULT.NEWS";
    private static final String WEATHER_RESULT = "com.czz.manager.service.RESULT.WEATHER";
    //TODO :④发送广播，需要传入的一些参数
    private static final String EXTRA_PARAM3 = "com.czz.manager.service.extra.PARAM3";
    private static final String EXTRA_PARAM4 = "com.czz.manager.service.extra.PARAM4";

    public CommunicateWithDeviceService() {
        super("CommunicateWithDeviceService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionReceive(Context context, String param1, String param2) {
        Intent intent = new Intent(context, CommunicateWithDeviceService.class);
        intent.setAction(ACTION_RECEIVE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSend(Context context, String param1, String param2) {
        Intent intent = new Intent(context, CommunicateWithDeviceService.class);
        intent.setAction(ACTION_SEND);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RECEIVE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionReceive(param1, param2);
            } else if (ACTION_SEND.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionSend(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionReceive(String param1, String param2) {
        // TODO: Handle action Foo
        //次线程里操作网络请求数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<AntennaInfo> arrayList = getSystemState();
                Intent intent = new Intent(NEWS_RESULT);
                // 此处传送的数据的是集合类型，也可以有其他的类型：intent.put
                intent.putParcelableArrayListExtra(EXTRA_PARAM3, arrayList);
                sendBroadcast(intent);
            }
        }).start();
    }

    private ArrayList<AntennaInfo> getSystemState(){
        return new ArrayList<AntennaInfo>();
    }
    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSend(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
