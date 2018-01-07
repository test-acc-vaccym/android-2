package com.edroplet.sanetel.fragments.functions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.beans.SatelliteInfo;
import com.edroplet.sanetel.beans.monitor.EquipmentInfo;
import com.edroplet.sanetel.beans.monitor.MonitorInfo;
import com.edroplet.sanetel.beans.monitor.TemperatureInfo;
import com.edroplet.sanetel.beans.status.FaultCondition;
import com.edroplet.sanetel.beans.status.RunningInfo;
import com.edroplet.sanetel.view.BroadcastReceiverFragment;
import com.edroplet.sanetel.view.custom.CustomTextView;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/14.
 * 状态页面
 * 1)	发送$cmd,get temp*ff<CR><LF>获取便携站温湿度信息指令直至返回$cmd,temp data,...*ff<CR><LF>温湿度信息；
 * 2)	发送$cmd,get equip info*ff<CR><LF>获取设备信息指令直至返回$cmd, equip info,...*ff<CR><LF>设备信息（连接Wifi后该指令只需请求成功一次便不请求）；
 * 3)	一直从监视指令$cmd,sys state, ….*ff<CR><LF>中故障位获取故障状并显示。
 */

public class FunctionsFragmentStatus extends BroadcastReceiverFragment {
    public static FunctionsFragmentStatus newInstance(@Nullable MainMonitorFragmentHolder mfh){
        Bundle args = new Bundle();
        FunctionsFragmentStatus fragment = new FunctionsFragmentStatus();

        fragment.setArguments(args);
        return fragment;
    }
    // 连接状态显示内容
    @BindArray(R.array.communicate_status_array)
    String[] communicateStatus;
    // 错误状态显示内容
    @BindArray(R.array.fault_condition_array)
    String[] faultConditions;

    // 通信状况
    @BindView(R.id.id_main_status_communicate_internet)
    CustomTextView internetStatus;
    @BindView(R.id.id_main_status_communicate_handset)
    CustomTextView serialPortStatus;
    @BindView(R.id.id_main_status_communicate_bd)
    CustomTextView bdCommunicateStatus;
    @BindView(R.id.id_main_status_communicate_beacon)
    CustomTextView beaconCommunicateStatus;
    @BindView(R.id.id_main_status_communicate_clinometer)
    CustomTextView clinometerCommunicateStatus;
    @BindView(R.id.id_main_status_communicate_power)
    CustomTextView powerCommunicateStatus;
    @BindView(R.id.id_main_status_communicate_lnb)
    CustomTextView lnbCommunicateStatus;

    // 设备状况
    @BindView(R.id.id_main_status_device_motor_encoder)
    CustomTextView motorEncoderStatus;
    @BindView(R.id.id_main_status_device_azimuth_holzer_switch)
    CustomTextView azimuthHolzerSwitchStatus;
    @BindView(R.id.id_main_status_device_azimuth_locking_holzer_switch)
    CustomTextView azimuthHolzerLockerSwitchStatus;
    @BindView(R.id.id_main_status_device_pitch_motor)
    CustomTextView pitchMotorStatus;
    @BindView(R.id.id_main_status_device_pitch_holzer_switch)
    CustomTextView pitchHolzerSwitchStatus;
    @BindView(R.id.id_main_status_device_pitch_locking_holzer_switch)
    CustomTextView pitchHolzerLockerSwitchStatus;

    @BindView(R.id.id_main_status_device_polarize_motor)
    CustomTextView polarizeMotorStatus;
    @BindView(R.id.id_main_status_device_polarization_potentiometer)
    CustomTextView polarizePotentiometerStatus;

    // 运行情况
    // 节能
    @BindView(R.id.id_main_status_running_energy_state)
    CustomTextView runningEnergyState;
    @BindArray(R.array.energy_state_array)
    String[] energyStates;

    @BindView(R.id.id_main_status_running_pitch_lock_state)
    CustomTextView pitchLockState;
    @BindView(R.id.id_main_status_running_azimuth_lock_state)
    CustomTextView azimuthLockState;
    @BindArray(R.array.locker_state_array)
    String[] lockerStates;


    // 俯仰电气/俯仰软件限位
    @BindView(R.id.id_main_status_running_pitch_electric_low_limit)
    CustomTextView pitchElectricLowLimit;
    @BindView(R.id.id_main_status_running_pitch_electric_high_limit)
    CustomTextView pitchElectricHighLimit;
    @BindView(R.id.id_main_status_running_pitch_soft_low_limit)
    CustomTextView pitchSoftLowLimit;
    @BindView(R.id.id_main_status_running_pitch_soft_high_limit)
    CustomTextView pitchSoftHighLimit;

    // 方位/极化限位
    @BindView(R.id.id_main_status_running_azimuth_soft_low_limit)
    CustomTextView azimuthSoftLowLimit;
    @BindView(R.id.id_main_status_running_azimuth_soft_high_limit)
    CustomTextView azimuthSoftHighLimit;
    @BindView(R.id.id_main_status_running_polar_soft_low_limit)
    CustomTextView polarSoftLowLimit;
    @BindView(R.id.id_main_status_running_polar_soft_high_limit)
    CustomTextView polarSoftHighLimit;

    @BindArray(R.array.limit_state_array)
    String[] limitStates;

    // 环境信息
    @BindView(R.id.id_main_status_environment_temperature)
    CustomTextView temperatureStatus;
    @BindView(R.id.id_main_status_environment_humidity)
    CustomTextView humidityStatus;

    // 版本信息
    @BindView(R.id.id_main_status_version_software)
    CustomTextView softwareVersion;
    @BindView(R.id.id_main_status_version_hardware)
    CustomTextView hardwareVersion;

    Unbinder unbinder;
    View view;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String[] action = {TemperatureInfo.TemperatureInfoAction, EquipmentInfo.EquipmentInfoAction, MonitorInfo.MonitorInfoAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        context = getContext();
        // 设备信息
        Protocol.sendMessage(context,Protocol.cmdGetEquipmentInfo);
        // 温度湿度信息
        Protocol.sendMessage(context,Protocol.cmdGetTemperature);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_functions_status, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public static class MainMonitorFragmentHolder {
        private SatelliteInfo spi;
    }

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        if (null != intent) {
            String action = intent.getAction();
            if (null != action) {
                switch (action) {
                    case MonitorInfo.MonitorInfoAction:
                        processMonitorData(intent);
                        break;
                    case TemperatureInfo.TemperatureInfoAction:
                        processTemperatureData(intent);
                        break;
                    case EquipmentInfo.EquipmentInfoAction:
                        processEquipmentData(intent);
                        break;
                }
                // 刷新界面
                view.invalidate();
            }
        }
    }

    void processMonitorData(Intent intent){
        String rawData = intent.getStringExtra(MonitorInfo.MonitorInfoData);
        MonitorInfo monitorInfo = MonitorInfo.parseMonitorInfo(context, rawData);
        FaultCondition faultCondition = FaultCondition.parseFaultCondition(context, String.valueOf(monitorInfo.getFaultCondition()));
        // 通信状况
        internetStatus.setText(communicateStatus[faultCondition.WifiCommunication]);
        serialPortStatus.setText(communicateStatus[faultCondition.HandsetCommunication]);
        bdCommunicateStatus.setText(communicateStatus[faultCondition.GNSSCommunication]);
        beaconCommunicateStatus.setText(communicateStatus[faultCondition.BeaconCommunication]);
        clinometerCommunicateStatus.setText(communicateStatus[faultCondition.DipCommunication]);
        powerCommunicateStatus.setText(communicateStatus[faultCondition.AmplifierCommunication]);
        lnbCommunicateStatus.setText(communicateStatus[faultCondition.Reserve2Communication]);
        // 设备状况
        motorEncoderStatus.setText(faultConditions[faultCondition.AzimuthMotorStatus]);
        azimuthHolzerSwitchStatus.setText(faultConditions[faultCondition.AzimuthLockHolzerStatus]);
        azimuthHolzerLockerSwitchStatus.setText(faultConditions[faultCondition.AzimuthLockHolzerStatus]);
        pitchMotorStatus.setText(faultConditions[faultCondition.PitchHolzerStatus]);
        pitchHolzerLockerSwitchStatus.setText(faultConditions[faultCondition.PitchLockHolzerStatus]);
        pitchHolzerSwitchStatus.setText(faultConditions[faultCondition.PitchHolzerStatus]);
        polarizeMotorStatus.setText(faultConditions[faultCondition.PolMotorStatus]);
        polarizePotentiometerStatus.setText(faultConditions[faultCondition.PolPotentiometerStatus]);

        // 运行情况
        RunningInfo runningInfo = RunningInfo.parseRunningInfo(String.valueOf(monitorInfo.getFlag()));
        runningEnergyState.setText(energyStates[runningInfo.energyInfo]);
        pitchLockState.setText(lockerStates[runningInfo.pitchLockerInfo]);
        azimuthLockState.setText(lockerStates[runningInfo.azimuthLockerInfo]);

        pitchElectricLowLimit.setText(limitStates[runningInfo.pitchElectricLowLimit]);
        pitchElectricHighLimit.setText(limitStates[runningInfo.pitchElectricHighLimit]);
        pitchSoftLowLimit.setText(limitStates[runningInfo.pitchSoftLowLimit]);
        pitchSoftHighLimit.setText(limitStates[runningInfo.pitchSoftHighLimit]);
        azimuthSoftLowLimit.setText(limitStates[runningInfo.azimuthSoftLowLimit]);
        azimuthSoftHighLimit.setText(limitStates[runningInfo.azimuthSoftHighLimit]);
        polarSoftLowLimit.setText(limitStates[runningInfo.polarSoftLowLimit]);
        polarSoftHighLimit.setText(limitStates[runningInfo.polarSoftHighLimit]);

    }

    void processTemperatureData(Intent intent){
        String rawData = intent.getStringExtra(TemperatureInfo.TemperatureInfoData);
        TemperatureInfo temperatureInfo = TemperatureInfo.processTemperatureInfo(context, rawData);
        temperatureStatus.setText(temperatureInfo.getTemperature());
        humidityStatus.setText(temperatureInfo.getHumidity());
    }

    void processEquipmentData(Intent intent){
        String rawData = intent.getStringExtra(EquipmentInfo.EquipmentInfoData);
        EquipmentInfo equipmentInfo = EquipmentInfo.processEquipmentInfo(context, rawData);
        hardwareVersion.setText(equipmentInfo.getHardVer());
        softwareVersion.setText(equipmentInfo.getSoftReleaseTime());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
