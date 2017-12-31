package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import butterknife.BindView;

/**
 * Created by qxs on 2017/9/14.
 * 状态页面
 */

public class FunctionsFragmentStatus extends Fragment {
    public static final String AntennaAzimuthInfo = "AntennaAzimuthInfo";

    public static FunctionsFragmentStatus newInstance(@Nullable MainMonitorFragmentHolder mfh){
        Bundle args = new Bundle();
        FunctionsFragmentStatus fragment = new FunctionsFragmentStatus();

        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.id_main_status_communicate_internet)
    CustomTextView internetStatus;
    @BindView(R.id.id_main_status_communicate_serial_port)
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
    @BindView(R.id.id_main_status_running_pitch_lock_state)
    CustomTextView pitchLockState;
    @BindView(R.id.id_main_status_running_azimuth_lock_state)
    CustomTextView azimuthLockState;

    // 俯仰电气/俯仰软件限位
    @BindView(R.id.id_main_status_running_pitch_electric_low_limit)
    CustomTextView pitchElectricLowLimit;
    @BindView(R.id.id_main_status_running_pitch_electric_high_limit)
    CustomTextView pitchElectricHighLimit;
    @BindView(R.id.id_main_status_running_pitch_soft_low_limit)
    CustomTextView pitchESoftLowLimit;
    @BindView(R.id.id_main_status_running_pitch_soft_high_limit)
    CustomTextView pitchESoftHighLimit;

    // 方位/极化限位
    @BindView(R.id.id_main_status_running_azimuth_soft_low_limit)
    CustomTextView azimuthSoftLowLimit;
    @BindView(R.id.id_main_status_running_azimuth_soft_high_limit)
    CustomTextView azimuthSoftHighLimit;
    @BindView(R.id.id_main_status_running_polar_soft_low_limit)
    CustomTextView polarSoftLowLimit;
    @BindView(R.id.id_main_status_running_polar_soft_high_limit)
    CustomTextView polarSoftHighLimit;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_functions_status, null);

        return view;
    }

    public static class MainMonitorFragmentHolder {
        private SatelliteInfo spi;
    }
}
