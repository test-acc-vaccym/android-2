package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterFloat;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentSearchModeSetting.KEY_SEARCHING_MODE;
import static com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator.AdministratorFragmentAmplifierMonitor.KEY_AMPLIFIER_MONITOR;

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

    @BindView(R.id.main_monitor_dvb_satellite_tv_agc)
    CustomTextView dvbSatelliteAgc;
    @BindView(R.id.main_monitor_dvb_satellite_tv_carrier)
    CustomTextView dvbSatelliteCarrier;
    @BindView(R.id.main_monitor_dvb_satellite_tv_dvb)
    CustomTextView dvbSatelliteDvb;
    @BindView(R.id.main_monitor_dvb_satellite_tv_longitude)
    CustomTextView dvbSatelliteLongitude;
    @BindView(R.id.main_monitor_dvb_satellite_tv_name)
    CustomTextView dvbSatelliteName;
    @BindView(R.id.main_monitor_dvb_satellite_tv_polarization)
    CustomTextView dvbSatellitePolarization;

    @BindView(R.id.main_monitor_beacon_satellite_tv_agc)
    CustomTextView beaconSatelliteAgc;
    @BindView(R.id.main_monitor_beacon_satellite_tv_beacon)
    CustomTextView beaconSatelliteBeacon;
    @BindView(R.id.main_monitor_beacon_satellite_tv_longitude)
    CustomTextView beaconSatelliteLongitude;
    @BindView(R.id.main_monitor_beacon_satellite_tv_name)
    CustomTextView beaconSatelliteName;
    @BindView(R.id.main_monitor_beacon_satellite_tv_polarization)
    CustomTextView beaconSatellitePolarization;

    @BindView(R.id.monitor_beacon_satellite_info)
    LinearLayout beaconSatelliteInfo;
    @BindView(R.id.monitor_dvb_satellite_info)
    LinearLayout dvbSatelliteInfo;

    @BindView(R.id.monitor_amplifier_info)
    LinearLayout monitorAmplifierInfo;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.functions_fragment_monitor, null);

        ButterKnife.bind(this, view);
        context = getContext();

        CustomTextView tvAntennaAzimuthInfo = (CustomTextView) view.findViewById(R.id.antenna_info_tv_azimuth);
        tvAntennaAzimuthInfo.setFilters(new InputFilter[]{new InputFilterFloat(-360.0, 360.0)});
        tvAntennaAzimuthInfo.setText(getArguments().getString(AntennaAzimuthInfo));

        // 信标模式和dvb模式分别展示
        int searchingMode = CustomSP.getInt(context, KEY_SEARCHING_MODE, 0);
        if (0 == searchingMode){
            beaconSatelliteInfo.setVisibility(View.VISIBLE);
            dvbSatelliteInfo.setVisibility(View.GONE);
        }else {
            beaconSatelliteInfo.setVisibility(View.GONE);
            dvbSatelliteInfo.setVisibility(View.VISIBLE);
        }

        // 无功放不显示
        int selected = CustomSP.getInt(context, KEY_AMPLIFIER_MONITOR,R.id.administrator_amplifier_monitor_none);
        if (R.id.administrator_amplifier_monitor_none == selected){
            monitorAmplifierInfo.setVisibility(View.GONE);
        }else{
            monitorAmplifierInfo.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public static class MainMonitorFragmentHolder {
        private SatelliteInfo spi;
    }
}
