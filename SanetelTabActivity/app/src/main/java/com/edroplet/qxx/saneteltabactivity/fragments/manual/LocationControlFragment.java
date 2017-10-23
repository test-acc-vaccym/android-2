package com.edroplet.qxx.saneteltabactivity.fragments.manual;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;

/**
 * Created by qxs on 2017/9/19.
 */

public class LocationControlFragment extends Fragment {

    public static LocationControlFragment newInstance(AntennaInfo antennaInfo) {

        Bundle args = new Bundle();
        args.putParcelable("antennaInfo",antennaInfo);
        LocationControlFragment fragment = new LocationControlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_control, null);
        AntennaInfo antennaInfo = getArguments().getParcelable("antennaInfo");
        final CustomButton rotate = (CustomButton) view.findViewById(R.id.location_control_rotate);
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
