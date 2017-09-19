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

public class AngleCalculateFragment extends Fragment {
    public static AngleCalculateFragment newInstance(AntennaInfo antennaInfo) {
        Bundle args = new Bundle();
        AngleCalculateFragment fragment = new AngleCalculateFragment();
        args.putParcelable("antennaInfo", antennaInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_angle_calculate, null);
        AntennaInfo antennaInfo = getArguments().getParcelable("antennaInfo");

        return view;
    }
}
