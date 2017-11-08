package com.edroplet.qxx.saneteltabactivity.fragments.manual;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;
import com.edroplet.qxx.saneteltabactivity.beans.PresetAngleInfo;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterFloat;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

/**
 * Created by qxs on 2017/9/19.
 */

public class LocationControlFragment extends Fragment {

    public static LocationControlFragment newInstance(PresetAngleInfo presetAngleInfo) {

        Bundle args = new Bundle();
        args.putSerializable("presetAngleInfo",presetAngleInfo);
        LocationControlFragment fragment = new LocationControlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // 预置
    private CustomEditText etAzimuth;
    private CustomEditText etPitch;
    private CustomEditText etPolarization;
    // 当前
    private CustomTextView tvAzimuth;
    private CustomTextView tvPitch;
    private CustomTextView tvPolarization;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.functions_fragment_application_manual_location_control, null);

        etAzimuth =  view.findViewById(R.id.main_application_manual_location_edit_setting_azimuth);
        etPitch =  view.findViewById(R.id.main_application_manual_location_edit_setting_pitch);
        etPolarization =  view.findViewById(R.id.main_application_manual_location_edit_setting_polarization);

        etAzimuth.setFilters(new InputFilter[]{new InputFilterFloat(0,360,3)});
        etPitch.setFilters(new InputFilter[]{new InputFilterFloat(0,360,3)});
        etPolarization.setFilters(new InputFilter[]{new InputFilterFloat(0,360,3)});
        
        tvAzimuth = view.findViewById(R.id.main_application_manual_location_tv_azimuth);
        tvPitch = view.findViewById(R.id.main_application_manual_location_tv_pitch);
        tvPolarization = view.findViewById(R.id.main_application_manual_location_tv_polarization);

        PresetAngleInfo presetAngleInfo = (PresetAngleInfo) getArguments().getSerializable("presetAngleInfo");
        if (presetAngleInfo != null){
            etAzimuth.setText(String.valueOf(presetAngleInfo.getAzimuth()));
            etPitch.setText(String.valueOf(presetAngleInfo.getPitch()));
            etPolarization.setText(String.valueOf(presetAngleInfo.getPolarization()));
        }

        final CustomButton rotate = (CustomButton) view.findViewById(R.id.location_control_rotate);
        // 自动聚焦
        rotate.requestFocus();
        /*
        rotate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (rotate.getText() == getString(R.string.location_control_rotate_start)) {
                    // TODO 发送开始旋转的指令
                    rotate.setText(R.string.location_control_rotate_stop);
                }
                else {
                    // TODO 发送停止旋转的指令
                    rotate.setText(R.string.location_control_rotate_start);
                }
            }
        });
        */

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rotate.getText() == getString(R.string.location_control_rotate_start)) {
                    rotate.setText(R.string.location_control_rotate_stop);
                }
                else {
                    rotate.setText(R.string.location_control_rotate_start);
                }
            }
        });
        return view;
    }
}
