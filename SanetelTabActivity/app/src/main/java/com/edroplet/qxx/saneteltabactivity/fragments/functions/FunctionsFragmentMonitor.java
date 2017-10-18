package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.functions.FunctionsActivity;
import com.edroplet.qxx.saneteltabactivity.activities.functions.MonitorHelpActivity;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteParameterItem;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomFAB;

/**
 * Created by qxs on 2017/9/14.
 */

public class FunctionsFragmentMonitor extends Fragment {
    public static final String AntennaAzimuthInfo = "AntennaAzimuthInfo";

    public static FunctionsFragmentMonitor newInstance(@Nullable MainMonitorFragmentHolder mfh){
        Bundle args = new Bundle();
        FunctionsFragmentMonitor fragment = new FunctionsFragmentMonitor();

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.functions_fragment_monitor, null);
        com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView tvAntennaAzimuthInfo = (com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView) view.findViewById(R.id.main_monitor_antenna_tv_azimuth);
        tvAntennaAzimuthInfo.setText(getArguments().getString(AntennaAzimuthInfo));
        return view;
    }

    public static class MainMonitorFragmentHolder {
        private SatelliteParameterItem spi;
    }
}
