package com.edroplet.qxx.saneteltabactivity.fragments.functions.manual;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;
import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qxs on 2017/9/19.
 * 步进控制
 */

public class StepControlFragment extends Fragment implements View.OnClickListener{
    public static StepControlFragment newInstance(AntennaInfo antennaInfo) {
        Bundle args = new Bundle();
        StepControlFragment fragment = new StepControlFragment();
        args.putParcelable("antennaInfo", antennaInfo);
        fragment.setArguments(args);
        return fragment;
    }

    // 当前数值
    @BindView(R.id.main_control_speed_tv_azimuth)
    CustomTextView textViewAzimuth;

    @BindView(R.id.main_control_speed_tv_pitch)
    CustomTextView textViewPitch;

    @BindView(R.id.main_control_speed_tv_polarization)
    CustomTextView textViewPolarization;

    @BindView(R.id.main_control_speed_tv_agc)
    CustomTextView textViewAgc;

    // 角速度
    @BindView(R.id.top_angular_velocity_radio_group)
    CustomRadioGroupWithCustomRadioButton angularVelocityGroup;

    @BindView(R.id.top_angular_velocity_1)
    CustomRadioButton angularVelocity1;

    @BindView(R.id.top_angular_velocity_2)
    CustomRadioButton angularVelocity2;

    @BindView(R.id.top_angular_velocity_3)
    CustomRadioButton angularVelocity3;

    @BindView(R.id.top_angular_velocity_4)
    CustomRadioButton angularVelocity4;

    @BindView(R.id.top_angular_velocity_5)
    CustomRadioButton angularVelocity5;

    @BindView(R.id.top_angular_velocity_6)
    CustomRadioButton angularVelocity6;

    @BindView(R.id.top_angular_velocity_custom_val)
    CustomEditText angularVelocityCustomValue;

    // 操作按键
    @BindView(R.id.manual_direction_left)
    CustomButton directionLeft;

    @BindView(R.id.manual_direction_right)
    CustomButton directionRight;

    @BindView(R.id.manual_pitch_up)
    CustomButton pitchUp;

    @BindView(R.id.manual_pitch_down)
    CustomButton pitchDown;

    @BindView(R.id.manual_polarization_up)
    CustomButton polarizationUp;

    @BindView(R.id.manual_polarization_down)
    CustomButton polarizationDown;

    @BindView(R.id.manual_pause)
    CustomButton pause;

    // TODO: 2017/11/11 注册广播，接收当前数值信息

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_functions_control_manual_speed_control, null);

        ButterKnife.bind(this, view);

        directionLeft.setOnClickListener(this);
        directionRight.setOnClickListener(this);
        pitchDown.setOnClickListener(this);
        pitchUp.setOnClickListener(this);
        polarizationDown.setOnClickListener(this);
        polarizationUp.setOnClickListener(this);
        pause.setOnClickListener(this);

        AntennaInfo antennaInfo = getArguments().getParcelable("antennaInfo");

        return view;
    }


    @Override
    public void onClick(View v) {

        float angluarVelocity;

        if (angularVelocity1.isChecked()){
            angluarVelocity = ConvertUtil.convertToFloat(angularVelocity1.getText().toString(), 0.0f) ;
        } else if (angularVelocity2.isChecked()){
            angluarVelocity = ConvertUtil.convertToFloat(angularVelocity2.getText().toString(), 0.0f) ;
        } else if (angularVelocity3.isChecked()){
            angluarVelocity = ConvertUtil.convertToFloat(angularVelocity3.getText().toString(), 0.0f) ;
        } else if (angularVelocity4.isChecked()){
            angluarVelocity = ConvertUtil.convertToFloat(angularVelocity4.getText().toString(), 0.0f) ;
        } else if (angularVelocity5.isChecked()){
            angluarVelocity = ConvertUtil.convertToFloat(angularVelocity5.getText().toString(), 0.0f) ;
        } else if (angularVelocity6.isChecked()){
            angluarVelocity = ConvertUtil.convertToFloat(angularVelocityCustomValue.getText().toString(), 0.0f) ;
        }

        switch (v.getId()){
            case R.id.manual_direction_left:
                // TODO: 2017/11/11 发送命令方位角减小
                break;
            case R.id.manual_direction_right:
                // TODO: 2017/11/11 发送命令方位角增加
                break;
            case R.id.manual_pitch_down:
                // TODO: 2017/11/11 发送命令俯仰角减小
                break;
            case R.id.manual_pitch_up:
                // TODO: 2017/11/11 发送命令俯仰角增加
                break;
            case R.id.manual_polarization_down:
                // TODO: 2017/11/11 发送命令极化角减小
                break;
            case R.id.manual_polarization_up:
                // TODO: 2017/11/11 发送命令极化角增加
                break;
            case R.id.manual_pause:
                // TODO: 2017/11/11 发送命令暂停
                break;
        }
    }
}
