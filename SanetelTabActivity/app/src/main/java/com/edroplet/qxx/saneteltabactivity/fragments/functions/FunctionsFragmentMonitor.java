package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.beans.monitor.MonitorInfo;
import com.edroplet.qxx.saneteltabactivity.services.communicate.CommunicateService;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterFloat;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator.AdministratorFragmentAmplifierMonitor.KEY_AMPLIFIER_MONITOR;
import static com.edroplet.qxx.saneteltabactivity.utils.CustomSP.KEY_SEARCHING_MODE;
import static com.edroplet.qxx.saneteltabactivity.utils.MLog.TAG;

/**
 * Created by qxs on 2017/9/14.
 */

public class FunctionsFragmentMonitor extends Fragment {
    public static final String AntennaAzimuthInfo = "AntennaAzimuthInfo";

    public static FunctionsFragmentMonitor newInstance(@Nullable MainMonitorFragmentHolder mfh){
        Bundle args = new Bundle();
        FunctionsFragmentMonitor fragment = new FunctionsFragmentMonitor();

        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.main_monitor_dvb_satellite_tv_agc)
    CustomTextView dvbSatelliteAgc;
    @BindView(R.id.main_monitor_dvb_satellite_tv_carrier)
    CustomTextView dvbSatelliteCarrier;
    @BindView(R.id.main_monitor_dvb_satellite_tv_dvb)
    CustomTextView dvbSatelliteDvb;
    @BindView(R.id.main_monitor_dvb_satellite_tv_longitude)
    CustomTextView dvbSatelliteLongitude;
    @BindView(R.id.main_monitor_dvb_satellite_tv_name)
    CustomTextView dvbSatelliteName;
    @BindView(R.id.main_monitor_dvb_satellite_tv_polarization)
    CustomTextView dvbSatellitePolarization;

    @BindView(R.id.main_monitor_beacon_satellite_tv_agc)
    CustomTextView beaconSatelliteAgc;
    @BindView(R.id.main_monitor_beacon_satellite_tv_beacon)
    CustomTextView beaconSatelliteBeacon;
    @BindView(R.id.main_monitor_beacon_satellite_tv_longitude)
    CustomTextView beaconSatelliteLongitude;
    @BindView(R.id.main_monitor_beacon_satellite_tv_name)
    CustomTextView beaconSatelliteName;
    @BindView(R.id.main_monitor_beacon_satellite_tv_polarization)
    CustomTextView beaconSatellitePolarization;

    @BindView(R.id.monitor_beacon_satellite_info)
    LinearLayout beaconSatelliteInfo;
    @BindView(R.id.monitor_dvb_satellite_info)
    LinearLayout dvbSatelliteInfo;

    @BindView(R.id.monitor_amplifier_info)
    LinearLayout monitorAmplifierInfo;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.functions_fragment_monitor, null);

        ButterKnife.bind(this, view);
        dvbSatelliteAgc = view.findViewById(R.id.main_monitor_dvb_satellite_tv_agc);
        context = getContext();

        CustomTextView tvAntennaAzimuthInfo = (CustomTextView) view.findViewById(R.id.antenna_info_tv_azimuth);
        tvAntennaAzimuthInfo.setText(getArguments().getString(AntennaAzimuthInfo));

        // 信标模式和dvb模式分别展示
        int searchingMode = CustomSP.getInt(context, KEY_SEARCHING_MODE, 0);
        if (0 == searchingMode){
            beaconSatelliteInfo.setVisibility(View.VISIBLE);
            dvbSatelliteInfo.setVisibility(View.GONE);
        }else {
            beaconSatelliteInfo.setVisibility(View.GONE);
            dvbSatelliteInfo.setVisibility(View.VISIBLE);
        }

        // 无功放不显示
        int selected = CustomSP.getInt(context, KEY_AMPLIFIER_MONITOR,R.id.administrator_amplifier_monitor_none);
        if (R.id.administrator_amplifier_monitor_none == selected){
            monitorAmplifierInfo.setVisibility(View.GONE);
        }else{
            monitorAmplifierInfo.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public static class MainMonitorFragmentHolder {
        private SatelliteInfo spi;
    }

    // 定时器， 定时获取监视信息
    private Timer monitorTimer = new Timer(true);
    TimerTask monitorTimerTask = new TimerTask() {
        @Override
        public void run() {
        }
    };

    public static final String ACTION_RECEIVE_MONITOR_INFO="com.edroplet.broadcast.ACTION_RECEIVE_MONITOR_INFO";
    public static final String KEY_RECEIVE_MONITOR_INFO_DATA="com.edroplet.broadcast.KEY_RECEIVE_MONITOR_INFO_DATA";
    //通过继承 BroadcastReceiver建立动态广播接收器
    private class MonitorReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_RECEIVE_MONITOR_INFO.equals(intent.getAction())) {
                String rawData =intent.getStringExtra(KEY_RECEIVE_MONITOR_INFO_DATA);
                        //通过土司验证接收到广播
                Toast t = Toast.makeText(context, "动态广播：" + rawData, Toast.LENGTH_SHORT);
                t.setGravity(Gravity.TOP, 0, 0);//方便录屏，将土司设置在屏幕顶端
                t.show();
                MonitorInfo monitorInfo = MonitorInfo.parseMonitorInfo(rawData);
                if (null != dvbSatelliteAgc){
                    dvbSatelliteAgc.setText(String.valueOf(monitorInfo.getAgc()));
                }
                // TODO: 2017/11/20 update other textview
                getView().postInvalidate();
            }
        }
    }

    private MonitorReceiver monitorReceiver;
    private void init(){
        if (!SystemServices.isServiceRunning(CommunicateService.SERVICE_NAME, getContext())){
            Intent intentCommunicateService = new Intent(context, CommunicateService.class);
            context.startService(intentCommunicateService);
        }
        // 实例化IntentFilter对象
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_RECEIVE_MONITOR_INFO);
        // 注册广播接收器
        monitorReceiver = new MonitorReceiver();
        getActivity().registerReceiver(monitorReceiver, filter);
        cancelTimer();
        monitorTimer = new Timer();
        // monitorTimer.schedule(monitorTimerTask, 0,30000);
        monitorTimer.schedule(new RequestTimerTask(), 0,3000);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (monitorTimer == null){
            monitorTimer = new Timer(true);
        }
        init();
    }
    private void cancelTimer(){
        if (monitorTimer != null){
            monitorTimer.cancel();
            monitorTimer.purge();
            monitorTimer = null;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != monitorReceiver) {
            getActivity().unregisterReceiver(monitorReceiver);
            monitorReceiver = null;
        }
        cancelTimer();
    }

    @Override
    public void onPause() {
        /*动态注册需在Acticity生命周期onPause通过
         *unregisterReceiver()方法移除广播接收器，
         * 优化内存空间，避免内存溢出
         */
        super.onPause();
        if (null != monitorReceiver) {
            getActivity().unregisterReceiver(monitorReceiver);
            monitorReceiver = null;
        }
        cancelTimer();
    }

    class RequestTimerTask extends TimerTask {
        public void run() {
            Log.d(TAG,"timer on schedule");
            MonitorInfo.getMonitorInfoFromServer(getContext());
            cancelTimer();
        }
    }
}
