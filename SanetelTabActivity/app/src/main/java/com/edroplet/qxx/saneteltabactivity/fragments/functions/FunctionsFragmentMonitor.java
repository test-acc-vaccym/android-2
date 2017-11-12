package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterFloat;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

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
        CustomTextView tvAntennaAzimuthInfo = (CustomTextView) view.findViewById(R.id.antenna_info_tv_azimuth);
        tvAntennaAzimuthInfo.setFilters(new InputFilter[]{new InputFilterFloat(-360.0, 360.0)});
        tvAntennaAzimuthInfo.setText(getArguments().getString(AntennaAzimuthInfo));
        return view;
    }

    public static class MainMonitorFragmentHolder {
        private SatelliteInfo spi;
    }
}
