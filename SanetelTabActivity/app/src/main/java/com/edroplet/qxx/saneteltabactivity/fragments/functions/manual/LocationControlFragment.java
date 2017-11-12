package com.edroplet.qxx.saneteltabactivity.fragments.functions.manual;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.PresetAngleInfo;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterFloat;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qxs on 2017/9/19.
 * 位置控制
 */

public class LocationControlFragment extends Fragment {
    public static final String KEY_CALCULATED_PREPARE_ANGULAR_INFO="KEY_CALCULATED_PREPARE_ANGULAR_INFO";
    public static final String KEY_PREPARE_AZIMUTH="KEY_PREPARE_AZIMUTH";
    public static final String KEY_PREPARE_PITCH="KEY_PREPARE_PITCH";
    public static final String KEY_PREPARE_POLARIZATION="KEY_PREPARE_POLARIZATION";

    public static LocationControlFragment newInstance(PresetAngleInfo presetAngleInfo) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_CALCULATED_PREPARE_ANGULAR_INFO,presetAngleInfo);
        LocationControlFragment fragment = new LocationControlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // 预置
    @BindView(R.id.main_application_manual_location_edit_setting_azimuth)
    CustomEditText etAzimuth;
    @BindView(R.id.main_application_manual_location_edit_setting_pitch)
    CustomEditText etPitch;
    @BindView(R.id.main_application_manual_location_edit_setting_polarization)
    CustomEditText etPolarization;

    // 当前
    @BindView(R.id.main_application_manual_location_tv_azimuth)
    CustomTextView tvAzimuth;
    @BindView(R.id.main_application_manual_location_tv_pitch)
    CustomTextView tvPitch;
    @BindView(R.id.main_application_manual_location_tv_polarization)
    CustomTextView tvPolarization;

    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.functions_fragment_application_manual_location_control, null);
        if (null == view){
            return null;
        }
        ButterKnife.bind(this,view);
        context = getContext();


        etAzimuth.setFilters(new InputFilter[]{new InputFilterFloat(-360,360,3)});
        etPitch.setFilters(new InputFilter[]{new InputFilterFloat(-360,360,3)});
        etPolarization.setFilters(new InputFilter[]{new InputFilterFloat(-360,360,3)});

        PresetAngleInfo presetAngleInfo = (PresetAngleInfo) getArguments().getSerializable(KEY_CALCULATED_PREPARE_ANGULAR_INFO);
        if (presetAngleInfo != null){
            etAzimuth.setText(String.valueOf(presetAngleInfo.getAzimuth()));
            etPitch.setText(String.valueOf(presetAngleInfo.getPitch()));
            etPolarization.setText(String.valueOf(presetAngleInfo.getPolarization()));
        }

        final CustomButton rotate = (CustomButton) view.findViewById(R.id.location_control_rotate);
        // 自动聚焦
        rotate.requestFocus();

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rotate.getText() == getString(R.string.location_control_rotate_start)) {
                    CustomSP.putString(context, KEY_PREPARE_AZIMUTH, etAzimuth.getText().toString());
                    CustomSP.putString(context, KEY_PREPARE_PITCH, etPitch.getText().toString());
                    CustomSP.putString(context, KEY_PREPARE_POLARIZATION, etPolarization.getText().toString());

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
