package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.settings.AdministratorLoginActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.AntennaRestartActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.CityLocationListActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.LowNoiseBlockOscillatorActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.PowerAmplifierSettingsActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.ReferenceSatelliteActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.SatelliteListActivity;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;

/**
 * Created by qxs on 2017/9/19.
 */

public class FunctionsFragmentSettings extends Fragment {

    Intent intent;

    public static FunctionsFragmentSettings newInstance(String info) {
        Bundle args = new Bundle();
        FunctionsFragmentSettings fragment = new FunctionsFragmentSettings();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.functions_fragment_settings, null);
        view.findViewById(R.id.main_settings_database_satellites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SatelliteListActivity.class));
                // getActivity().finish();
            }
        });
        view.findViewById(R.id.main_settings_database_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CityLocationListActivity.class));
                // getActivity().finish();
            }
        });
        view.findViewById(R.id.main_settings_amplifier_factory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), PowerAmplifierSettingsActivity.class);
                intent.putExtra(PowerAmplifierSettingsActivity.positionKey, 0);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.main_settings_amplifier_oscillator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), PowerAmplifierSettingsActivity.class);
                intent.putExtra(PowerAmplifierSettingsActivity.positionKey, 1);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.main_settings_amplifier_emit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), PowerAmplifierSettingsActivity.class);
                intent.putExtra(PowerAmplifierSettingsActivity.positionKey, 2);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.main_settings_amplifier_interfere).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), PowerAmplifierSettingsActivity.class);
                intent.putExtra(PowerAmplifierSettingsActivity.positionKey, 3);
                startActivity(intent);
            }
        });
        StatusButton customImageButton = view.findViewById(R.id.main_settings_administrator);
        customImageButton.setText(getString(R.string.main_settings_administrator));
        customImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdministratorLoginActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.main_settings_reference).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ReferenceSatelliteActivity.class));
            }
        });
        view.findViewById(R.id.main_settings_lnb_oscillator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LowNoiseBlockOscillatorActivity.class));
            }
        });
        view.findViewById(R.id.main_settings_antenna_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AntennaRestartActivity.class));
            }
        });
        return view;
    }
}
