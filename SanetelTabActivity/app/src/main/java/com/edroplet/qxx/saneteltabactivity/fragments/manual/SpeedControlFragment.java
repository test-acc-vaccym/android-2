package com.edroplet.qxx.saneteltabactivity.fragments.manual;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;

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
        View view = inflater.inflate(R.layout.fragment_speed_control_top, null);
        TextView tvInfo = (TextView) view.findViewById(R.id.speed_info_now);
        AntennaInfo antennaInfo = getArguments().getParcelable("antennaInfo");

        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Don't click me.please!.", Snackbar.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
