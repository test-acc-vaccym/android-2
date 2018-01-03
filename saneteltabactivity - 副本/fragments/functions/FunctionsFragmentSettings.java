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

/**
 * Created by qxs on 2017/9/19.
 * 主页面设置
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
        final View view = inflater.inflate(R.layout.fragment_functions_settings, null);
        // 数据库
        // 卫星库
        view.findViewById(R.id.main_settings_database_satellites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SatelliteListActivity.class));
                // getActivity().finish();
            }
        });
        // 城市库
        view.findViewById(R.id.main_settings_database_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CityLocationListActivity.class));
                // getActivity().finish();
            }
        });

        // 功放
        // 发射开关
        view.findViewById(R.id.main_settings_amplifier_emit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), PowerAmplifierSettingsActivity.class);
                intent.putExtra(PowerAmplifierSettingsActivity.positionKey, PowerAmplifierSettingsActivity.emitPosition);
                startActivity(intent);
            }
        });
        // 邻星干扰
        view.findViewById(R.id.main_settings_amplifier_interfere).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), PowerAmplifierSettingsActivity.class);
                intent.putExtra(PowerAmplifierSettingsActivity.positionKey, PowerAmplifierSettingsActivity.interfererPosition);
                startActivity(intent);
            }
        });
        // 参考寻星
        // 参考星
        view.findViewById(R.id.main_settings_reference).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ReferenceSatelliteActivity.class));
            }
        });

        // 管理员设置
        view.findViewById(R.id.main_settings_administrator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdministratorLoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
