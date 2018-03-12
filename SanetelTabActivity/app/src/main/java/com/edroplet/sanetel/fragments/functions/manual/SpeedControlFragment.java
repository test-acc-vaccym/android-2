package com.edroplet.sanetel.fragments.functions.manual;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.AntennaInfo;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.beans.monitor.MonitorInfo;
import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.utils.InputFilterFloat;
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
 * 速度控制
 */

public class SpeedControlFragment extends BroadcastReceiverFragment implements View.OnClickListener{
    public static SpeedControlFragment newInstance(AntennaInfo antennaInfo) {
        Bundle args = new Bundle();
        SpeedControlFragment fragment = new SpeedControlFragment();
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
    @BindView(R.id.top_angular_velocity_radio_group)
    CustomRadioGroupWithCustomRadioButton angularVelocityGroup;

    int [] angularVelocityIds = {
            R.id.top_angular_velocity_1, R.id.top_angular_velocity_2,
            R.id.top_angular_velocity_3, R.id.top_angular_velocity_4,
            R.id.top_angular_velocity_5, R.id.top_angular_velocity_6};

    int [] operateIds = {
            R.id.manual_direction_left,R.id.manual_direction_right,
            R.id.manual_pitch_up,R.id.manual_pitch_down,
            R.id.manual_reserve_up, R.id.manual_reserve_down,
            R.id.manual_polarization_up,R.id.manual_polarization_down
    };

    @BindView(R.id.top_angular_velocity_custom_val)
    CustomEditText angularVelocityCustomValue;

    @BindView(R.id.manual_pause)
    CustomButton pause;

    SparseIntArray mapAngularVelocity = new SparseIntArray(6);
    SparseIntArray mapOperate = new SparseIntArray(6);

    static View view;
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
        view = inflater.inflate(R.layout.fragment_functions_control_manual_speed_control, null);

        unbinder = ButterKnife.bind(this, view);

        angularVelocityCustomValue.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.azimuthMin,InputFilterFloat.azimuthMax,InputFilterFloat.angleValidBit)});

        // 监听焦点获取到后，选择该选项
        angularVelocityCustomValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            angularVelocityGroup.check(R.id.top_angular_velocity_6);
            }
        });

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


    static float angularVelocity = 0.0f;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manual_step_direction_left:
            case R.id.manual_step_direction_right:
            case R.id.manual_step_pitch_up:
            case R.id.manual_step_pitch_down:
            case R.id.manual_step_polarization_up:
            case R.id.manual_step_polarization_down:
            case R.id.manual_step_pause:
                int id = angularVelocityGroup.getCheckedRadioButtonId();
                int pos = mapAngularVelocity.indexOfValue(id);
                int maxPos = mapAngularVelocity.size() - 1;
                if (pos != maxPos) {
                    Log.e("CustomRadioButton", "id is: " + id );
					CustomRadioButton crb = (CustomRadioButton) v.findViewById(id);
					if (crb != null){
						String angularVelocityString = crb.getText().toString();
						angularVelocity = ConvertUtil.convertToFloat(angularVelocityString, 0.0f);
					}
                } else {
                    angularVelocity = ConvertUtil.convertToFloat(angularVelocityCustomValue.getText().toString(), 0.0f);
                }
                int operateId = v.getId();
                int operatePos = mapOperate.indexOfValue(operateId);
                if (operatePos == -1) {
                    // 暂停
                    Protocol.sendMessage(getContext(), Protocol.cmdStopSearch);
                } else {
                    // 控制
                    Protocol.sendMessage(getContext(), String.format(Protocol.cmdManualVel, String.valueOf(operatePos + 1), angularVelocity));
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }
}
