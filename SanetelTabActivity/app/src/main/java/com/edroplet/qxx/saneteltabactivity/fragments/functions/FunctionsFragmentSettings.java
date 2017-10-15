package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.guide.GuideEntryActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.CityLocationListActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.PowerAmplifierSettingsActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.SatelliteListActivity;

/**
 * Created by qxs on 2017/9/19.
 */

public class FunctionsFragmentSettings extends Fragment {
    public static FunctionsFragmentSettings newInstance(String info) {
        Bundle args = new Bundle();
        FunctionsFragmentSettings fragment = new FunctionsFragmentSettings();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.functions_fragment_settings, null);
        view.findViewById(R.id.settings_main_database_satellites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SatelliteListActivity.class));
                // getActivity().finish();
            }
        });
        view.findViewById(R.id.settings_main_database_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CityLocationListActivity.class));
                // getActivity().finish();
            }
        });
        view.findViewById(R.id.settings_main_amplifier_factory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PowerAmplifierSettingsActivity.class));
                // getActivity().finish();
            }
        });
        return view;
    }
}
