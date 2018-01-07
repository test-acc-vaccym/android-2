package com.edroplet.sanetel.control;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.service.autofill.SaveInfo;
import android.support.annotation.IdRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.AntennaInfo;
import com.edroplet.sanetel.beans.LocationInfo;
import com.edroplet.sanetel.beans.LockerInfo;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.beans.SavingInfo;
import com.edroplet.sanetel.beans.monitor.MonitorInfo;
import com.edroplet.sanetel.beans.status.FaultCondition;
import com.edroplet.sanetel.beans.status.RunningInfo;
import com.edroplet.sanetel.view.StatusButton;

import java.util.Timer;
import static com.edroplet.sanetel.view.StatusButton.*;

/**
 * Created by qxs on 2017/9/19.
 * 参考 http://blog.csdn.net/dreamintheworld/article/details/39314121/
 */

public class StatusBarControl {
    private static ActionBar actionBar;
    private static Activity mActivity;
    private static Timer timer = new Timer();

    private static StatusButton commStateButton;
    private static StatusButton antennaStateButton;
    private static StatusButton bdStateButton;
    private static StatusButton lockerStateButton;
    private static StatusButton energyStateButton;

    public static void setupToolbar(AppCompatActivity activity, @IdRes int resId){

        Toolbar toolbar = (Toolbar) activity.findViewById(resId);
        activity.setSupportActionBar(toolbar);
        mActivity = activity;

        toolbar.setBackgroundColor(ResourcesCompat.getColor(activity.getResources(), R.color.title_background, null));
        //actionBar的设置(使用自定义的设置)
        actionBar = activity.getSupportActionBar();
        if (actionBar != null) {

            // 返回箭头（默认不显示）
            actionBar.setDisplayHomeAsUpEnabled(false);
            // 左侧图标点击事件使能
            actionBar.setHomeButtonEnabled(true);
            // 使左上角图标(系统)是否显示
            actionBar.setDisplayShowHomeEnabled(false);
            // 修改图标
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                actionBar.setHomeAsUpIndicator(R.drawable.back);
            }
            // 显示标题
            actionBar.setDisplayShowTitleEnabled(false);

            // 获取屏幕参数
            //            WindowManager wm = activity.getWindowManager();
            //            DisplayMetrics metric = new DisplayMetrics();
            //            wm.getDefaultDisplay().getMetrics(metric);
            //            int width = metric.widthPixels;     // 屏幕宽度（像素）
            //            int height = metric.heightPixels;   // 屏幕高度（像素）
            //            float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
            //            int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240
            //            int properWidth = (int)(width*0.9);
            //            ActionBar.LayoutParams alp = new ActionBar.LayoutParams(properWidth,ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            //            LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //            View view=inflater.inflate(R.layout.status_bar, null);

            // 使用自定义layout,显示自定义视图
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.status_bar);
        }
        // 一个布局内的所有控件可以获取到焦点
        //   for (int i = 0; i < toolbar.getChildCount(); i++) {
        //       View v = toolbar.getChildAt(i);
        //       v.setFocusable(true);
        //   }
        final AppCompatActivity thisActivity = activity;
        // 使用drawable资源但不为其设置theme主题
        // ab.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.status_background,null));
        //关键下面两句话，设置了回退按钮，及点击事件的效果
        /** 不需要返回键了，需求天天变啊！！！
        activity.findViewById(android.R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisActivity.finish();
            }
        });
        */
        /*
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisActivity.finish();

            }
        });*/
        toolbar.hideOverflowMenu();

        if (null == timer){
            timer = new Timer();
        }
        // 初始化button
        initButton();
        /*
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO: 2017/11/13 更新状态
                initButton();
                if (null != commStateButton) {
                    commStateButton.setButtonState(BUTTON_STATE_ABNORMAL);
                    commStateButton.setText(R.string.communication_state_connected);
                }

                if (null != antennaStateButton) {
                    antennaStateButton.setButtonState(BUTTON_STATE_NORMAL);
                    antennaStateButton.setText(R.string.antenna_state_exploded);
                }
                if (null != bdStateButton) {
                    bdStateButton.setButtonState(BUTTON_STATE_ABNORMAL);
                    bdStateButton.setText(R.string.gnss_state_disabled);
                }
                if (null != lockerStateButton) {
                    lockerStateButton.setButtonState(BUTTON_STATE_NORMAL);
                    lockerStateButton.setText(R.string.locker_state_released);
                }

                if (null != energyStateButton){
                    energyStateButton.setButtonState(BUTTON_STATE_NORMAL);
                    energyStateButton.setText(R.string.energy_state_charged);
                }
            }
        },0, 1000);
        */
    }

    public static void setTitle(String title){
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    private static void initButton(){
        commStateButton =  (StatusButton)mActivity.findViewById(R.id.status_bar_button_communication_state);
        antennaStateButton =  (StatusButton)mActivity.findViewById(R.id.status_bar_button_antenna_state);
        bdStateButton =  (StatusButton)mActivity.findViewById(R.id.status_bar_button_gnss_state);
        lockerStateButton =  (StatusButton)mActivity.findViewById(R.id.status_bar_button_locker_state);
        energyStateButton =  (StatusButton)mActivity.findViewById(R.id.status_bar_button_power_state);
    }

    private StatusBarControlBroadcast statusBarControlBroadcast;
    private void initBroadcast(Activity activity){
        // 实例化IntentFilter对象
        IntentFilter filter = new IntentFilter();
        filter.addAction(MonitorInfo.MonitorInfoAction);
        statusBarControlBroadcast = new StatusBarControlBroadcast();
        activity.registerReceiver(statusBarControlBroadcast,filter);
    }

    private class StatusBarControlBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 监视信息，包含状态信息
            if (MonitorInfo.MonitorInfoAction.equals(action)){
                String rawData =intent.getStringExtra(MonitorInfo.MonitorInfoData);
                MonitorInfo monitorInfo = MonitorInfo.parseMonitorInfo(context, rawData);
                // 获取状态，更新UI
                // 连接状态, 获取wifi连接状态？跟基站通信状态？
                FaultCondition faultCondition = FaultCondition.parseFaultCondition(context, String.valueOf(monitorInfo.getFaultCondition()));
                int communicateState = faultCondition.WifiCommunication;
                if (0 == communicateState){
                    commStateButton.setText(R.string.communication_state_connected);
                    commStateButton.setButtonState(BUTTON_STATE_NORMAL);
                    FaultCondition.setWifiCommunication(context, 0);
                }else {
                    commStateButton.setText(R.string.communication_state_disconnected);
                    commStateButton.setButtonState(BUTTON_STATE_ABNORMAL);
                    FaultCondition.setWifiCommunication(context, 1);
                }
                // 天线状态
                int antennaState = monitorInfo.getTraceState();
                AntennaInfo.setAntennaState(context, antennaState);
                if (null!=antennaStateButton){
                    switch (antennaState){
                        case AntennaInfo.AntennaSearchSatellitesStatus.EXPLODED:
                            antennaStateButton.setText(R.string.antenna_state_exploded);
                            antennaStateButton.setButtonState(BUTTON_STATE_NORMAL);
                            break;
                    }
                }
                // bd状态
                int gnssState = monitorInfo.getBdState();
                if (null != bdStateButton){
                    if (gnssState == LocationInfo.GnssState.LOCATED){
                        bdStateButton.setText(R.string.gnss_state_enabled);
                        bdStateButton.setButtonState(BUTTON_STATE_NORMAL);
                        LocationInfo.setGnssState(context,LocationInfo.GnssState.LOCATED);
                    }else {
                        bdStateButton.setText(R.string.gnss_state_disabled);
                        bdStateButton.setButtonState(BUTTON_STATE_ABNORMAL);
                        LocationInfo.setGnssState(context,LocationInfo.GnssState.NOTLOCATED);
                    }
                }
                // 获取flag， 包含节能和锁紧信息
                RunningInfo runningInfo = RunningInfo.parseRunningInfo(String.valueOf(monitorInfo.getFlag()));
                // 节能状态
                if (null != energyStateButton){
                    int energySaveState = runningInfo.energyInfo;
                    if (energySaveState == SavingInfo.SAVING_STATE_OPEN){
                        energyStateButton.setText(R.string.energy_state_saved);
                        energyStateButton.setButtonState(BUTTON_STATE_ABNORMAL);
                        SavingInfo.setSavingState(context, SavingInfo.SAVING_STATE_OPEN);
                    }else {
                        energyStateButton.setText(R.string.energy_state_charged);
                        energyStateButton.setButtonState(BUTTON_STATE_NORMAL);
                        SavingInfo.setSavingState(context, SavingInfo.SAVING_STATE_CLOSE);
                    }
                }
                // 锁紧状态
                if (null != lockerStateButton){
                    // 俯仰锁紧
                    int lockerButtonPitchState = runningInfo.pitchLockerInfo;
                    // 方位锁紧
                    int lockerButtonAzimuthState = runningInfo.azimuthLockerInfo;
                    if (lockerButtonAzimuthState == LockerInfo.LOCKER_STATE_LOCKED || lockerButtonPitchState == LockerInfo.LOCKER_STATE_LOCKED){
                        lockerStateButton.setText(R.string.locker_state_locked);
                        lockerStateButton.setButtonState(BUTTON_STATE_ABNORMAL);
                        LockerInfo.setLockerState(context,LockerInfo.LOCKER_STATE_LOCKED);
                    }else {
                        lockerStateButton.setText(R.string.locker_state_released);
                        lockerStateButton.setButtonState(BUTTON_STATE_NORMAL);
                        LockerInfo.setLockerState(context,LockerInfo.LOCKER_STATE_LOCKED);
                    }
                }
            }
        }
    }
}
