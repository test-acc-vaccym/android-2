package com.edroplet.sanetel.fragments.functions.manual;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.AntennaInfo;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.beans.monitor.MonitorInfo;
import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.view.BroadcastReceiverFragment;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;
import com.edroplet.sanetel.view.custom.CustomRadioButton;
import com.edroplet.sanetel.view.custom.CustomRadioGroupWithCustomRadioButton;
import com.edroplet.sanetel.view.custom.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.edroplet.sanetel.beans.monitor.MonitorInfo.MonitorInfoAction;
import static com.edroplet.sanetel.beans.monitor.MonitorInfo.MonitorInfoData;

/**
 * Created by qxs on 2017/9/19.
 * 步进控制
 */

public class StepControlFragment extends BroadcastReceiverFragment implements View.OnClickListener{
    public static StepControlFragment newInstance(AntennaInfo antennaInfo) {
        Bundle args = new Bundle();
        StepControlFragment fragment = new StepControlFragment();
        args.putParcelable("antennaInfo", antennaInfo);
        fragment.setArguments(args);
        return fragment;
    }

    // 当前数值
    @BindView(R.id.main_control_speed_tv_azimuth)
    CustomTextView tvAzimuth;

    @BindView(R.id.main_control_speed_tv_pitch)
    CustomTextView tvPitch;

    @BindView(R.id.main_control_speed_tv_polarization)
    CustomTextView tvPolarization;

    @BindView(R.id.main_control_speed_tv_agc)
    CustomTextView tvAgc;

    // 角速度
    @BindView(R.id.step_angular_velocity_radio_group)
    CustomRadioGroupWithCustomRadioButton angularVelocityGroup;

    @BindView(R.id.step_angular_velocity_custom_val)
    CustomEditText angularVelocityCustomValue;

    // 操作按键
    @BindView(R.id.manual_step_pause)
    CustomButton pause;

    int [] angularVelocityIds = {
            R.id.step_angular_velocity_1, R.id.step_angular_velocity_2,
            R.id.step_angular_velocity_3, R.id.step_angular_velocity_4,
            R.id.step_angular_velocity_5, R.id.step_angular_velocity_6};

    int [] operateIds = {
            R.id.manual_step_direction_left,R.id.manual_step_direction_right,
            R.id.manual_step_pitch_up,R.id.manual_step_pitch_down,
            R.id.manual_step_reserve_up, R.id.manual_step_reserve_down,
            R.id.manual_step_polarization_up,R.id.manual_step_polarization_down
    };

    SparseIntArray mapAngularVelocity = new SparseIntArray(6);
    SparseIntArray mapOperate = new SparseIntArray(6);

    View view;
    Context context;
    Unbinder unbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = getContext();
        String[] action = {MonitorInfoAction};
        setAction(action);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        // 3)	一直从监视指令$cmd,sys state, ….*ff<CR><LF>中获取角度和AGC信息并显示。
        MonitorInfo monitorInfo = MonitorInfo.parseMonitorInfo(context, intent.getStringExtra(MonitorInfoData));
        tvAzimuth.setText(String.valueOf(monitorInfo.getAZ(context)));
        tvPitch.setText(String.valueOf(monitorInfo.getEL(context)));
        tvPolarization.setText(String.valueOf(monitorInfo.getPOL(context)));
        tvAgc.setText(String.valueOf(monitorInfo.getAgc(context)));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_functions_control_step_control, null);

        unbinder = ButterKnife.bind(this, view);

        int i = 0;
        for (int id: angularVelocityIds){
            mapAngularVelocity.put(i++, id);
        }
        int j = 0;
        for (int id: operateIds){
            mapOperate.put(j++,id);
            view.findViewById(id).setOnClickListener(this);
        }
        pause.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        float angularVelocity;
        int id = angularVelocityGroup.getCheckedRadioButtonId();
        int pos =  mapAngularVelocity.indexOfKey(id);
        int maxPos = mapAngularVelocity.size() - 1;
        if (pos == maxPos){
            String angularVelocityString = ((CustomRadioButton) view.findViewById(id)).getText().toString();
            angularVelocity  = ConvertUtil.convertToFloat(angularVelocityString, 0.0f);
        }else{
            angularVelocity  = ConvertUtil.convertToFloat(angularVelocityCustomValue.getText().toString(), 0.0f);
        }
        int operateId = v.getId();
        int operatePos = mapOperate.indexOfKey(operateId);
        if (operatePos == -1){
            // 暂停
            Protocol.sendMessage(getContext(),Protocol.cmdStopSearch);
        }else{
            // 控制
            Protocol.sendMessage(getContext(), String.format(Protocol.cmdManualStep,String.valueOf(operatePos + 1),angularVelocity));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }
}
