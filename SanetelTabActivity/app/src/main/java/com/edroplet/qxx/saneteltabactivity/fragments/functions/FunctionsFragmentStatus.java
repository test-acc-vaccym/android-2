package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteParameterItem;

/**
 * Created by qxs on 2017/9/14.
 */

public class FunctionsFragmentStatus extends Fragment {
    public static final String AntennaAzimuthInfo = "AntennaAzimuthInfo";

    public static FunctionsFragmentStatus newInstance(@Nullable MainMonitorFragmentHolder mfh){
        Bundle args = new Bundle();
        FunctionsFragmentStatus fragment = new FunctionsFragmentStatus();

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.functions_fragment_status, null);

        return view;
    }

    public static class MainMonitorFragmentHolder {
        private SatelliteParameterItem spi;
    }
}
