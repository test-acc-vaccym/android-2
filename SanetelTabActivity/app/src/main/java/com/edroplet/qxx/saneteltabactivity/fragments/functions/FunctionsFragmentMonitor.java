package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.edroplet.qxx.saneteltabactivity.beans.AmplifierInfo;
import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.beans.monitor.MonitorInfo;
import com.edroplet.qxx.saneteltabactivity.services.communicate.CommunicateService;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterFloat;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.edroplet.qxx.saneteltabactivity.beans.monitor.MonitorInfo.KEY_MONITOR_LAST_SATELLITE_AGC;
import static com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentDestination.KEY_DESTINATION_SATELLITE_NAME;
import static com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentDestination.KEY_DESTINATION_SATELLITE_POLARIZATION;
import static com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentSearchModeSetting.Mode_Beacon;
import static com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator.AdministratorFragmentAmplifierMonitor.KEY_AMPLIFIER_MONITOR;
import static com.edroplet.qxx.saneteltabactivity.utils.CustomSP.KEY_SEARCHING_MODE;
import static com.edroplet.qxx.saneteltabactivity.utils.MLog.TAG;

/**
 * Created by qxs on 2017/9/14.
 */

public class FunctionsFragmentMonitor extends Fragment {
    public static final String AntennaAzimuthInfo = "AntennaAzimuthInfo";
    private static long Schedule; // ms

    public static FunctionsFragmentMonitor newInstance(@Nullable MainMonitorFragmentHolder mfh){
        Bundle args = new Bundle();
        FunctionsFragmentMonitor fragment = new FunctionsFragmentMonitor();

        fragment.setArguments(args);
        return fragment;
    }

    // dvb目标星
    @BindView(R.id.monitor_dvb_satellite_info)
    LinearLayout dvbSatelliteInfo;

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
    // 信标目标星
    @BindView(R.id.monitor_beacon_satellite_info)
    LinearLayout beaconSatelliteInfo;

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

    // 天线
    @BindView(R.id.antenna_info_tv_prepare_azimuth)
    CustomTextView tvPrepareAzimuth;
    @BindView(R.id.antenna_info_tv_prepare_pitch)
    CustomTextView tvPreparePitch;
    @BindView(R.id.antenna_info_tv_prepare_polarization)
    CustomTextView tvPreparePolarization;
    @BindView(R.id.antenna_info_tv_azimuth)
    CustomTextView tvAzimuth;
    @BindView(R.id.antenna_info_tv_pitch)
    CustomTextView tvPitch;
    @BindView(R.id.antenna_info_tv_polarization)
    CustomTextView tvPolarization;

    // 位置
    @BindView(R.id.main_monitor_location_tv_longitude)
    CustomTextView tvLongitude;
    @BindView(R.id.main_monitor_location_tv_latitude)
    CustomTextView tvLatitude;

    //  功放
    @BindView(R.id.monitor_amplifier_info)
    LinearLayout monitorAmplifierInfo;

    @BindView(R.id.main_monitor_amplifier_status_tv_state)
    CustomTextView tvAmplifierState;
    @BindView(R.id.main_monitor_amplifier_tv_value_output)
    CustomTextView tvAmplifierOutput;
    @BindView(R.id.main_monitor_amplifier_tv_temperature_state)
    CustomTextView tvAmplifierTemperatureState;
    @BindView(R.id.main_monitor_amplifier_tv_temperature)
    CustomTextView tvAmplifierTemperature;
    @BindView(R.id.main_monitor_amplifier_tv_status_pll)
    CustomTextView tvAmplifierPLLStatus;
    @BindView(R.id.main_monitor_amplifier_status_satellite_protect_state)
    CustomTextView tvAmplifierProtectStatus;


    // 状态栏
    // 从故障状态和标志位获取状态显示
    StatusButton statusButtonAntennaState;
    StatusButton statusButtonGnssState;
    StatusButton statusButtonLockerState;
    StatusButton statusButtonEnergyState;
    StatusButton statusButtonCommunicateState;

    private Context context;
    private int searchingMode;
    View mView;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.functions_fragment_monitor, null);
        mView = view;
        unbinder = ButterKnife.bind(this, view);
        dvbSatelliteAgc = view.findViewById(R.id.main_monitor_dvb_satellite_tv_agc);
        context = getContext();

        Schedule = getResources().getInteger(R.integer.get_monitor_info_schedule_timer);

        // 信标模式和dvb模式分别展示
        searchingMode = CustomSP.getInt(context, KEY_SEARCHING_MODE, Mode_Beacon);

        String name = CustomSP.getString(context, KEY_DESTINATION_SATELLITE_NAME, getString(R.string.main_monitor_satellite_name_hint));
        String sPolarization = CustomSP.getString(context,KEY_DESTINATION_SATELLITE_POLARIZATION,getString(R.string.main_monitor_satellite_polarization_hint));
        if (Mode_Beacon == searchingMode){
            beaconSatelliteInfo.setVisibility(View.VISIBLE);
            dvbSatelliteInfo.setVisibility(View.GONE);
            beaconSatelliteName.setText(name);
            beaconSatellitePolarization.setText(sPolarization);
        }else {
            beaconSatelliteInfo.setVisibility(View.GONE);
            dvbSatelliteInfo.setVisibility(View.VISIBLE);
            dvbSatelliteName.setText(name);
            dvbSatellitePolarization.setText(sPolarization);
        }

        // 无功放不显示
        int selected = CustomSP.getInt(context, KEY_AMPLIFIER_MONITOR,R.id.administrator_amplifier_monitor_none);
        if (R.id.administrator_amplifier_monitor_none == selected){
            monitorAmplifierInfo.setVisibility(View.GONE);
        }else{
            monitorAmplifierInfo.setVisibility(View.VISIBLE);
        }

        initView();
        return view;
    }

    private void initView(){
        // 上次的监视数据
        if (null != dvbSatelliteAgc){
            dvbSatelliteAgc.setText(CustomSP.getString(context, KEY_MONITOR_LAST_SATELLITE_AGC, getString(R.string.main_monitor_satellite_agc_hint)));
        }
        Activity activity = getActivity();
        // 在FindViewById中通过getActivity()获取到父控件ID
        statusButtonAntennaState = (StatusButton)activity .findViewById (R.id.status_bar_button_antenna_state);
        statusButtonGnssState = (StatusButton)activity .findViewById (R.id.status_bar_button_gnss_state);
        statusButtonLockerState = (StatusButton)activity .findViewById (R.id.status_bar_button_locker_state);
        statusButtonEnergyState = (StatusButton)activity .findViewById (R.id.status_bar_button_power_state);
        statusButtonCommunicateState = (StatusButton)activity .findViewById (R.id.status_bar_button_communication_state);
    }


    public static class MainMonitorFragmentHolder {
        private SatelliteInfo spi;
    }

    // 定时器， 定时获取监视信息
    private Timer monitorTimer = new Timer(true);
    TimerTask monitorTimerTask = new RequestTimerTask();

    public static final String ACTION_RECEIVE_MONITOR_INFO="com.edroplet.broadcast.ACTION_RECEIVE_MONITOR_INFO";
    public static final String ACTION_RECEIVE_AMPLIFIER_INFO="com.edroplet.broadcast.ACTION_RECEIVE_AMPLIFIER_INFO";
    public static final String KEY_RECEIVE_MONITOR_INFO_DATA="com.edroplet.broadcast.KEY_RECEIVE_MONITOR_INFO_DATA";
    public static final String KEY_RECEIVE_AMPLIFIER_INFO_DATA="com.edroplet.broadcast.KEY_RECEIVE_AMPLIFIER_INFO_DATA";



    //通过继承 BroadcastReceiver建立动态广播接收器
    private class MonitorReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_RECEIVE_MONITOR_INFO.equals(intent.getAction())) {
                String rawData =intent.getStringExtra(KEY_RECEIVE_MONITOR_INFO_DATA);
                //通过土司验证接收到广播
                //Toast t = Toast.makeText(context, getString(R.string.main_bottom_nav_monitor)+"：" + rawData, Toast.LENGTH_SHORT);
                // t.setGravity(Gravity.TOP, 0, 0);//方便录屏，将土司设置在屏幕顶端
                //t.show();
                MonitorInfo monitorInfo = MonitorInfo.parseMonitorInfo(rawData);
                // 卫星信息
                String agc = String.valueOf(monitorInfo.getAgc());
                CustomSP.putString(context, KEY_MONITOR_LAST_SATELLITE_AGC, agc);
                String carrier = String.valueOf(monitorInfo.getCarrier());
                CustomSP.putString(context, KEY_MONITOR_LAST_SATELLITE_AGC, agc);
                String beacon = String.valueOf(monitorInfo.getBeacon());
                String dvb = String.valueOf(monitorInfo.getDvb());
                String satelliteLongitude = String.valueOf(monitorInfo.getSatelliteLogitude());
                // dvb模式
                if (Mode_Beacon != searchingMode) {
                    dvbSatelliteAgc.setText(agc);
                    dvbSatelliteCarrier.setText(carrier);
                    dvbSatelliteDvb.setText(dvb);
                    dvbSatelliteLongitude.setText(satelliteLongitude);
                }else {
                    // 信标模式
                    beaconSatelliteAgc.setText(agc);
                    beaconSatelliteBeacon.setText(beacon);
                    beaconSatelliteLongitude.setText(satelliteLongitude);
                }
                // 天线信息
                String azuimth = String.valueOf(monitorInfo.getAZ());
                String pitch = String.valueOf(monitorInfo.getEL());
                String reserve = String.valueOf(monitorInfo.getRV());
                String polarization = String.valueOf(monitorInfo.getPOL());

                String preAzuimth = String.valueOf(monitorInfo.getPrepareAZ());
                String prePitch = String.valueOf(monitorInfo.getPrepareEL());
                String preReserve = String.valueOf(monitorInfo.getPrepareRV());
                String prePolarization = String.valueOf(monitorInfo.getPreparePOL());
                tvPrepareAzimuth.setText(preAzuimth);
                tvAzimuth.setText(azuimth);
                tvPreparePitch.setText(prePitch);
                tvPitch.setText(pitch);
                tvPolarization.setText(polarization);
                tvPreparePolarization.setText(prePolarization);
                // 位置信息
                String longitude = String.valueOf(monitorInfo.getLongitude());
                String latitude = String.valueOf(monitorInfo.getLatitude());
                tvLongitude.setText(longitude);
                tvLatitude.setText(latitude);

                // 状态栏信息
                // 已经在那边更新了
                // GNSS状态, 自动刷新，由status button 接收广播自动更新
                int bdState = monitorInfo.getBdState();
                if (statusButtonGnssState != null) {
                    if (bdState == LocationInfo.GnssState.NOTLOCATED) {
                        statusButtonGnssState.setText(R.string.gnss_state_disabled);
                        statusButtonGnssState.setButtonState(StatusButton.BUTTON_STATE_ABNORMAL);
                    } else {
                        statusButtonGnssState.setText(R.string.gnss_state_enabled);
                        statusButtonGnssState.setButtonState(StatusButton.BUTTON_STATE_NORMAL);
                    }
                }
                // 通知刷新UI
                mView.postInvalidate();
            } else if (ACTION_RECEIVE_AMPLIFIER_INFO.equals(intent.getAction())){
                //
                String rawData =intent.getStringExtra(KEY_RECEIVE_AMPLIFIER_INFO_DATA);
                // 功放信息
                AmplifierInfo amplifierInfo = AmplifierInfo.parseAmplifierInfo(rawData);
                // 功放状态
                tvAmplifierState.setText(amplifierInfo.getAmplifierState().equals("1") ?
                        getString(R.string.main_monitor_amplifier_status_state_open)
                        : getString(R.string.main_monitor_amplifier_status_state_closed));
                // 功放输出
                tvAmplifierOutput.setText(amplifierInfo.getAmplifierOutput());
                // 温控状态
                tvAmplifierTemperatureState.setText(amplifierInfo.getAmplifierTemperatureState().equals("1")?
                        getString(R.string.main_monitor_amplifier_status_temperature_state_normal)
                        : getString(R.string.main_monitor_amplifier_status_temperature_state_abnormal)
                );
                // 温度
                tvAmplifierTemperature.setText(amplifierInfo.getAmplifierTemperature());
                // 锁相环
                tvAmplifierPLLStatus.setText(amplifierInfo.getAmplifierPLLStatus().equals("1")?
                        getString(R.string.main_monitor_amplifier_status_pll_state_normal)
                        : getString(R.string.main_monitor_amplifier_status_pll_state_abnormal));
                // 临幸保护
                tvAmplifierProtectStatus.setText(amplifierInfo.getAmplifierProtectStatus().equals("1")?
                        getString(R.string.main_monitor_amplifier_status_satellite_protect_state_open)
                        : getString(R.string.main_monitor_amplifier_status_satellite_protect_state_close));

                // 通知刷新UI
                mView.postInvalidate();
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
        filter.addAction(ACTION_RECEIVE_AMPLIFIER_INFO);
        // 注册广播接收器
        monitorReceiver = new MonitorReceiver();
        getActivity().registerReceiver(monitorReceiver, filter);
        if (null != monitorTimer) {
            if (monitorTimerTask != null) {
                monitorTimerTask.cancel();
            }
            monitorTimerTask = new RequestTimerTask();
            monitorTimer.schedule(monitorTimerTask, 0,Schedule);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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
            Log.w(TAG,"timer on schedule");
            MonitorInfo.getMonitorInfoFromServer(getContext());
        }
    }

    /**
     * onDestroyView中进行解绑操作
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
