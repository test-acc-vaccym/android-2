package com.edroplet.qxx.saneteltabactivity.fragments.manual;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;

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
        return view;
    }
}
