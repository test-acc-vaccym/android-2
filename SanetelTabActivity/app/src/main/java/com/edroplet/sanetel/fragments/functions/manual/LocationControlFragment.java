package com.edroplet.sanetel.fragments.functions.manual;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.PresetAngleInfo;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.beans.monitor.MonitorInfo;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.InputFilterFloat;
import com.edroplet.sanetel.view.BroadcastReceiverFragment;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;
import com.edroplet.sanetel.view.custom.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.edroplet.sanetel.beans.monitor.MonitorInfo.MonitorInfoAction;
import static com.edroplet.sanetel.beans.monitor.MonitorInfo.MonitorInfoData;

/**
 * Created by qxs on 2017/9/19.
 * 位置控制
 */

public class LocationControlFragment extends BroadcastReceiverFragment {
    private static final String TAG = LocationControlFragment.class.getSimpleName();
    public static final String FreshUIAction = "com.edroplet.sanetel.FreshUIAction";

    public static final String KEY_CALCULATED_PREPARE_ANGULAR_INFO="KEY_CALCULATED_PREPARE_ANGULAR_INFO";
    public static final String KEY_PREPARE_AZIMUTH="KEY_PREPARE_AZIMUTH";
    public static final String KEY_PREPARE_PITCH="KEY_PREPARE_PITCH";
    public static final String KEY_PREPARE_POLARIZATION="KEY_PREPARE_POLARIZATION";
    public static final String KEY_LocationControlRotate="KEY_LocationControlRotate";

    public static LocationControlFragment newInstance(PresetAngleInfo presetAngleInfo) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_CALCULATED_PREPARE_ANGULAR_INFO,presetAngleInfo);
        LocationControlFragment fragment = new LocationControlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // 预置
    @BindView(R.id.main_control_manual_location_edit_setting_azimuth)
    CustomEditText etAzimuth;
    @BindView(R.id.main_control_manual_location_edit_setting_pitch)
    CustomEditText etPitch;
    @BindView(R.id.main_control_manual_location_edit_setting_polarization)
    CustomEditText etPolarization;

    // 当前
    @BindView(R.id.main_control_manual_location_tv_azimuth)
    CustomTextView tvAzimuth;
    @BindView(R.id.main_control_manual_location_tv_pitch)
    CustomTextView tvPitch;
    @BindView(R.id.main_control_manual_location_tv_polarization)
    CustomTextView tvPolarization;

    // 操作按钮
    @BindView(R.id.location_control_rotate)
    CustomButton rotate;

    Context context;
    Unbinder unbinder;
    Boolean isRotate = false;
    String preAZ = "0.000";
    String preEL = "0.000";
    String prePOL = "0.000";
    String preRv = "0.000";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = getContext();
        String[] action = {MonitorInfoAction, FreshUIAction};
        setAction(action);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        String ac = intent.getAction();
        if (ac != null && ac == FreshUIAction){

        }else {
            // 3)	一直从监视指令$cmd,sys state, ….*ff<CR><LF>中获取角度和AGC信息并显示。
            MonitorInfo monitorInfo = MonitorInfo.parseMonitorInfo(context, intent.getStringExtra(MonitorInfoData));
            preAZ = String.valueOf(monitorInfo.getAZ(context));
            preEL = String.valueOf(monitorInfo.getEL(context));
            prePOL = String.valueOf(monitorInfo.getPOL(context));
            preRv = String.valueOf(monitorInfo.getPrepareRV(context));
            updateUI();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_functions_control_manual_location_control, null);
        if (null == view){
            return null;
        }
        unbinder = ButterKnife.bind(this,view);
        context = getContext();

        etAzimuth.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.azimuthMin,InputFilterFloat.azimuthMax,InputFilterFloat.angleValidBit)});
        etPitch.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.pitchMin,InputFilterFloat.pitchMax,InputFilterFloat.angleValidBit)});
        etPolarization.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.polarizationMin,InputFilterFloat.polarizationMax,InputFilterFloat.angleValidBit)});

        PresetAngleInfo presetAngleInfo = (PresetAngleInfo) getArguments().getSerializable(KEY_CALCULATED_PREPARE_ANGULAR_INFO);
        if (presetAngleInfo != null){
            preAZ = String.valueOf(presetAngleInfo.getAzimuth());
            preEL = String.valueOf(presetAngleInfo.getPitch());
            prePOL = String.valueOf(presetAngleInfo.getPolarization());
            updateUI();
        }

        // 自动聚焦
        showRotate();

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRotate) {
                    preAZ = etAzimuth.getText().toString();
                    preEL = etPitch.getText().toString();
                    prePOL = etPolarization.getText().toString();

                    CustomSP.putString(context, KEY_PREPARE_AZIMUTH, preAZ);
                    CustomSP.putString(context, KEY_PREPARE_PITCH, preEL);
                    CustomSP.putString(context, KEY_PREPARE_POLARIZATION, prePOL);

                    rotate.setText(R.string.location_control_rotate_stop);
                    // 2018/1/3 发送开始旋转指令
                    // 1)	点击开始旋转发送$$cmd,manual position,方位,俯仰,备用,极化角*ff<CR><LF>手动控制指令
                    // 把设置框值发送到便携站；
                    // 设置前，请检查数据的完整性。如果用户没有点击设置，无指令下发。
                    Protocol.sendMessage(context, String.format(Protocol.cmdManualPosition, preAZ, preEL,preRv, prePOL));
                    isRotate = true;
                } else {
                    rotate.setText(R.string.location_control_rotate_start);
                    // 2)	旋转过程中点停止发送$cmd,stop search *ff<CR><LF>停止寻星指令；
                    Protocol.sendMessage(context, Protocol.cmdStopSearch);
                    isRotate = false;
                }
                CustomSP.putBoolean(context,KEY_LocationControlRotate,isRotate);
            }
        });
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume: ");
        super.onResume();

        preAZ = CustomSP.getString(context, KEY_PREPARE_AZIMUTH, preAZ);
        preEL = CustomSP.getString(context, KEY_PREPARE_PITCH, preEL);
        prePOL = CustomSP.getString(context, KEY_PREPARE_POLARIZATION, prePOL);
        updateUI();
        showRotate();
    }

    void updateUI(){
        etAzimuth.setText(preAZ);
        etPitch.setText(preEL);
        etPolarization.setText(prePOL);
    }

    void showRotate(){
        if (CustomSP.getBoolean(context,KEY_LocationControlRotate, false)){
            rotate.setText(R.string.location_control_rotate_stop);
        }else{
            rotate.setText(R.string.location_control_rotate_start);
        }
        // 每次都自动聚焦
        rotate.requestFocus();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }

}
