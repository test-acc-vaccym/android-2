package com.edroplet.qxx.saneteltabactivity.fragments.manual;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.functions.ManualActivity;
import com.edroplet.qxx.saneteltabactivity.adapters.SpinnerAdapter2;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;
import com.edroplet.qxx.saneteltabactivity.beans.Cities;
import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.beans.PresetAngleInfo;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.beans.Satellites;
import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterMinMax;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

/**
 * Created by qxs on 2017/9/19.
 */

public class AngleCalculateFragment extends Fragment implements View.OnClickListener {
    
    // 本地位置
    private Spinner spinnerLocationProvince;
    private Spinner spinnerLocationCity;
    private Cities cities;
    private String selectedProvince;
    private String selectedCity;

    private CustomRadioGroupWithCustomRadioButton customRadioGroupWithCustomRadioButton;

    //
    // 本地位置
    private CustomEditText localLongitude;
    private CustomEditText localLatitude;
    private Spinner localLongitudeUnit;
    private Spinner localLatitudeUnit;

    // 卫星选择
    Spinner satelliteSelect;
    Spinner satellitePolarizationSelect;
    private Satellites satellites;
    private String selectedName;
    private String selectedPolarization;

    // 计算结果
    private CustomEditText tvAzimuth;
    private CustomEditText tvPitch;
    private CustomEditText tvPolarization;


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
        View view = inflater.inflate(R.layout.functions_fragment_application_manual_speed_angle_calculate, null);
        AntennaInfo antennaInfo = getArguments().getParcelable("antennaInfo");


        tvAzimuth = view.findViewById(R.id.main_application_manual_angle_calculate_tv_setting_azimuth);
        tvPitch = view.findViewById(R.id.main_application_manual_angle_calculate_tv_setting_pitch);
        tvPolarization = view.findViewById(R.id.main_application_manual_angle_calculate_tv_setting_polarization);

        // 按键
        view.findViewById(R.id.angle_calculate_operate_calculate).setOnClickListener(this);
        view.findViewById(R.id.angle_calculate_operate_clear).setOnClickListener(this);
        view.findViewById(R.id.angle_calculate_operate_use).setOnClickListener(this);

        initSatelliteSelect(view);
        initCitySelect(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.angle_calculate_operate_calculate:
                tvAzimuth.setText(String.valueOf(satelliteLongitude + satelliteLatitude + satelliteBeacon
                        + satelliteDvb + satelliteThreshold + cityLongitude + cityLatitude));
                tvPitch.setText(String.valueOf(satelliteLongitude + satelliteLatitude + satelliteBeacon
                        + satelliteDvb + satelliteThreshold + cityLongitude - cityLatitude));
                tvPolarization.setText(String.valueOf(satelliteLongitude + satelliteLatitude + satelliteBeacon
                        + satelliteDvb + satelliteThreshold - cityLongitude - cityLatitude));
                break;
            case R.id.angle_calculate_operate_clear:
                tvAzimuth.setText("");
                tvPitch.setText("");
                tvPolarization.setText("");
                localLongitude.setText("");
                localLatitude.setText("");
                break;
            case R.id.angle_calculate_operate_use:
                float azimuth = ConvertUtil.convertToFloat(tvAzimuth.getText().toString(), 0.000f);
                float pitch= ConvertUtil.convertToFloat(tvPitch.getText().toString(), 0.000f);
                float polarization= ConvertUtil.convertToFloat(tvPolarization.getText().toString(), 0.000f);

                /*
                PresetAngleInfo presetAngleInfo = new PresetAngleInfo();
                presetAngleInfo.setAzimuth(azimuth)
                        .setPitch(pitch)
                        .setPolarization(polarization);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment fragment = LocationControlFragment.newInstance(presetAngleInfo);
                fm.beginTransaction().replace(R.id.manual_viewpager, fragment).commit();
                */

                Intent intent = new Intent(getContext(), ManualActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(ManualActivity.POSITION, 1);
                bundle.putFloat(ManualActivity.PRESET_AZIMUTH, azimuth);
                bundle.putFloat(ManualActivity.PRESET_PITCH, pitch);
                bundle.putFloat(ManualActivity.PRESET_POLARIZATION, polarization);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                getActivity().finish();
                break;
        }
    }

    private float satelliteLongitude = 0.000f;
    private float satelliteLatitude = 0.000f;
    private float satelliteBeacon = 0.000f;
    private float satelliteDvb = 0.000f;
    private float satelliteThreshold = 0.000f;

    private String satellitePolarization = "";

    private float cityLongitude = 0.000f;
    private float cityLatitude = 0.000f;

    private void initSatelliteSelect(View view){
        satelliteSelect = view.findViewById(R.id.angle_calculate_spinner_satellites);
        satellitePolarizationSelect =  view.findViewById(R.id.angle_calculate_spinner_satellites_local);

        try {
            satellites = new Satellites(getContext());
            String[] satelliteNameArray = satellites.getSatelliteNameArray();
            if (satelliteNameArray.length > 0) {
                satelliteSelect.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, satelliteNameArray));
                // 读取配置中的值
                selectedName = satelliteNameArray[0];

                String[] polarizationArray = satellites.getSatellitePolarizationArray(selectedName);
                if (polarizationArray.length > 0) {
                    satellitePolarizationSelect.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, polarizationArray));
                    // 读取配置中的值
                    selectedPolarization = polarizationArray[0];

                    SatelliteInfo satelliteInfo = satellites.getSatelliteInfoBySatelliteNamePolarization(selectedName, selectedPolarization);
                    if (satelliteInfo != null) {
                        satellitePolarization = satelliteInfo.polarization;
                        satelliteLongitude = ConvertUtil.convertToFloat(satelliteInfo.longitude, 0.000f);
                        satelliteBeacon = ConvertUtil.convertToFloat(satelliteInfo.beacon, 0.000f);
                        satelliteThreshold = ConvertUtil.convertToFloat(satelliteInfo.threshold, 0.000f);
                        satelliteDvb = ConvertUtil.convertToFloat(satelliteInfo.symbolRate, 0.000f);
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }


        satelliteSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedName = (String)satelliteSelect.getItemAtPosition(position);
                String[] polarizationArray = satellites.getSatellitePolarizationArray(selectedName);
                if (polarizationArray.length > 0) {
                    satellitePolarizationSelect.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, polarizationArray));
                    selectedPolarization = polarizationArray[0];

                    if (!selectedPolarization.isEmpty()) {
                        SatelliteInfo satelliteInfo = satellites.getSatelliteInfoBySatelliteNamePolarization(selectedName, selectedPolarization);
                        if (satelliteInfo != null) {
                            satellitePolarization = satelliteInfo.polarization;
                            satelliteLongitude = ConvertUtil.convertToFloat(satelliteInfo.longitude, 0.000f);
                            satelliteBeacon = ConvertUtil.convertToFloat(satelliteInfo.beacon, 0.000f);
                            satelliteThreshold = ConvertUtil.convertToFloat(satelliteInfo.threshold, 0.000f);
                            satelliteDvb = ConvertUtil.convertToFloat(satelliteInfo.symbolRate, 0.000f);
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        satellitePolarizationSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 读取配置中的值
                selectedPolarization = (String)satellitePolarizationSelect.getItemAtPosition(position);
                if (!selectedPolarization.isEmpty()) {
                    SatelliteInfo satelliteInfo = satellites.getSatelliteInfoBySatelliteNamePolarization(selectedName, selectedPolarization);
                    if (satelliteInfo != null) {
                        satellitePolarization = satelliteInfo.polarization;
                        satelliteLongitude = ConvertUtil.convertToFloat(satelliteInfo.longitude, 0.000f);
                        satelliteBeacon = ConvertUtil.convertToFloat(satelliteInfo.beacon, 0.000f);
                        satelliteThreshold = ConvertUtil.convertToFloat(satelliteInfo.threshold, 0.000f);
                        satelliteDvb = ConvertUtil.convertToFloat(satelliteInfo.symbolRate, 0.000f);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void initCitySelect(View view){

        spinnerLocationProvince = view.findViewById(R.id.angle_calculate_spinner_province);
        spinnerLocationCity = view.findViewById(R.id.angle_calculate_spinner_city);
        localLatitude = view.findViewById(R.id.angle_calculate_local_latitude);
        localLatitudeUnit = view.findViewById(R.id.angle_calculate_local_latitude_unit);
        localLongitude = view.findViewById(R.id.angle_calculate_local_longitude);
        localLongitudeUnit = view.findViewById(R.id.angle_calculate_local_longitude_unit);

        localLongitude.setFilters(new InputFilter[]{ new InputFilterMinMax("-180", "180")});
        localLatitude.setFilters(new InputFilter[]{ new InputFilterMinMax("-90", "90")});

        localLatitudeUnit.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1,
                android.R.id.text1, getContext().getResources().getStringArray(R.array.latitude_unit)));
        localLongitudeUnit.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1,
                android.R.id.text1, getContext().getResources().getStringArray(R.array.longitude_unit)));

        try {
            cities = new Cities(getContext());
            String[] provincesArray = cities.getProvinceArray();
            if (provincesArray.length > 0) {
                spinnerLocationProvince.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, provincesArray));
                // 读取配置中的值
                selectedProvince = provincesArray[0];
                for(int i=0; i<provincesArray.length; i++){
                    if(selectedProvince.equals(provincesArray[i])){
                        spinnerLocationProvince.setSelection(i,true);
                        break;
                    }
                }
                String[] citiesArray = cities.getCitiesArray(selectedProvince);
                if (citiesArray.length > 0) {
                    spinnerLocationCity.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, citiesArray));
                    // 读取配置中的值
                    selectedCity = citiesArray[0];
                    for(int i=0; i<citiesArray.length; i++){
                        if(selectedCity.equals(citiesArray[i])){
                            spinnerLocationCity.setSelection(i,true);
                            break;
                        }
                    }
                    LocationInfo locationInfo = cities.getLocationInfoByProvinceCity(selectedProvince, selectedCity);
                    if (locationInfo != null) {
                        cityLongitude = locationInfo.getLongitude();
                        cityLatitude = locationInfo.getLatitude();
                        localLatitude.setText(locationInfo.getLatitude()+"");
                        localLongitude.setText(locationInfo.getLongitude()+"");
                        localLatitudeUnit.setSelection(0);
                        localLongitudeUnit.setSelection(0);
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        spinnerLocationProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProvince = (String)spinnerLocationProvince.getItemAtPosition(position);
                String[] citiesArray = cities.getCitiesArray(selectedProvince);
                if (citiesArray.length > 0) {
                    spinnerLocationCity.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, citiesArray));
                    selectedCity = citiesArray[0];

                    if (!selectedCity.isEmpty()) {
                        LocationInfo locationInfo = cities.getLocationInfoByProvinceCity(selectedProvince, selectedCity);
                        if (locationInfo != null) {
                            cityLongitude = locationInfo.getLongitude();
                            cityLatitude = locationInfo.getLatitude();
                            localLatitude.setText(locationInfo.getLatitude()+"");
                            localLongitude.setText(locationInfo.getLongitude()+"");
                            localLatitudeUnit.setSelection(0);
                            localLongitudeUnit.setSelection(0);
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerLocationCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 读取配置中的值
                selectedCity = (String)spinnerLocationCity.getItemAtPosition(position);
                if (!selectedCity.isEmpty()) {
                    LocationInfo locationInfo = cities.getLocationInfoByProvinceCity(selectedProvince, selectedCity);
                    if (locationInfo != null) {
                        cityLongitude = locationInfo.getLongitude();
                        cityLatitude = locationInfo.getLatitude();
                        localLatitude.setText(locationInfo.getLatitude()+"");
                        localLongitude.setText(locationInfo.getLongitude()+"");
                        localLatitudeUnit.setSelection(0);
                        localLongitudeUnit.setSelection(0);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

}
