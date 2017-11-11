package com.edroplet.qxx.saneteltabactivity.fragments.functions.manual;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;

import static com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentLocation.mOnCheckedChangeListener;

/**
 * Created by qxs on 2017/9/19.
 */

public class SpeedControlFragment extends Fragment {
    public static SpeedControlFragment newInstance(AntennaInfo antennaInfo) {
        Bundle args = new Bundle();
        SpeedControlFragment fragment = new SpeedControlFragment();
        args.putParcelable("antennaInfo", antennaInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.functions_fragment_application_manual_speed_control, null);
        com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView tvInfo = (com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView) view.findViewById(R.id.speed_info_now);
        AntennaInfo antennaInfo = getArguments().getParcelable("antennaInfo");

        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Don't click me.please!.", Snackbar.LENGTH_SHORT).show();
            }
        });

        /** 只取上下结构 这段舍弃
        CustomRadioButton crbStep = view.findViewById(R.id.main_application_manual_speed_movement_step);
        crbStep.setOnCheckedChangeListener(mOnCheckedChangeListener);
        CustomRadioButton crbContinuous = view.findViewById(R.id.main_application_manual_speed_movement_continuous);
        crbContinuous.setOnCheckedChangeListener(mOnCheckedChangeListener);
        */

        CustomRadioButton crbStepTop = view.findViewById(R.id.main_application_manual_speed_movement_step_top);
        crbStepTop.setOnCheckedChangeListener(mOnCheckedChangeListener);
        CustomRadioButton crbContinuousTop = view.findViewById(R.id.main_application_manual_speed_movement_continuous_top);
        crbContinuousTop.setOnCheckedChangeListener(mOnCheckedChangeListener);
        return view;
    }

}
