package com.edroplet.sanetel.fragments.functions.manual;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.activities.functions.ManualActivity;
import com.edroplet.sanetel.adapters.SpinnerAdapter2;
import com.edroplet.sanetel.beans.AntennaInfo;
import com.edroplet.sanetel.beans.Cities;
import com.edroplet.sanetel.beans.LocationInfo;
import com.edroplet.sanetel.beans.SatelliteInfo;
import com.edroplet.sanetel.beans.Satellites;
import com.edroplet.sanetel.utils.AngleCalculate;
import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.utils.InputFilterFloat;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qxs on 2017/9/19.
 * 对星计算
 */

public class AngleCalculateFragment extends Fragment implements View.OnClickListener {
    // 本地位置
    @BindView(R.id.angle_calculate_spinner_province)
    Spinner spinnerLocationProvince;
    @BindView(R.id.angle_calculate_spinner_city)
    Spinner spinnerLocationCity;

    private Cities cities;
    private String selectedProvince;
    private String selectedCity;

    //
    // 本地位置
    @BindView(R.id.angle_calculate_local_longitude)
    CustomEditText localLongitude;
    @BindView(R.id.angle_calculate_local_latitude)
    CustomEditText localLatitude;
    @BindView(R.id.angle_calculate_local_longitude_unit)
    Spinner localLongitudeUnit;
    @BindView(R.id.angle_calculate_local_latitude_unit)
    Spinner localLatitudeUnit;

    // 卫星选择
    @BindView(R.id.angle_calculate_spinner_satellites)
    Spinner satelliteSelect;
    @BindView(R.id.angle_calculate_spinner_satellites_local)
    Spinner satellitePolarizationSelect;

    private Satellites satellites;
    private String selectedName;
    private String selectedPolarization;

    // 计算结果
    @BindView(R.id.main_control_manual_angle_calculate_tv_setting_azimuth)
    CustomEditText tvAzimuth;
    @BindView(R.id.main_control_manual_angle_calculate_tv_setting_pitch)
    CustomEditText tvPitch;
    @BindView(R.id.main_control_manual_angle_calculate_tv_setting_polarization)
    CustomEditText tvPolarization;

    // 按键
    @BindView(R.id.angle_calculate_operate_calculate)
    CustomButton calculate;
    @BindView(R.id.angle_calculate_operate_use)
    CustomButton use;
    @BindView(R.id.angle_calculate_operate_clear)
    CustomButton clear;

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
        View view = inflater.inflate(R.layout.fragment_functions_control_manual_speed_angle_calculate, null);
        if (view == null){
            return null;
        }
        ButterKnife.bind(this,view);
        AntennaInfo antennaInfo = getArguments().getParcelable("antennaInfo");

        tvAzimuth.setFilters(new InputFilter[]{new InputFilterFloat(0,360,3)});
        tvPitch.setFilters(new InputFilter[]{new InputFilterFloat(-10,90,3)});
        tvPolarization.setFilters(new InputFilter[]{new InputFilterFloat(0,360,3)});

        localLongitude.setOnFocusChangeListener(onInpuFocusChangeListener);
        localLongitudeUnit.setOnFocusChangeListener(onInpuFocusChangeListener);
        localLatitude.setOnFocusChangeListener(onInpuFocusChangeListener);
        localLatitudeUnit.setOnFocusChangeListener(onInpuFocusChangeListener);

        spinnerLocationProvince.setOnFocusChangeListener(onSelectFocusChangeListener);
        spinnerLocationCity.setOnFocusChangeListener(onSelectFocusChangeListener);

        // 按键
        calculate.setOnClickListener(this);
        clear.setOnClickListener(this);
        use.setOnClickListener(this);

        initSatelliteSelect(view);
        initCitySelect(view);
        return view;
    }
    @BindView(R.id.city_select_choice)
    RadioButton choiceSelect;

    @BindView(R.id.city_input_choice)
    RadioButton choiceInput;

    private View.OnFocusChangeListener onInpuFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                choiceInput.setChecked(true);
                choiceSelect.setChecked(false);
            }
        }
    };

    private View.OnFocusChangeListener onSelectFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                choiceInput.setChecked(false);
                choiceSelect.setChecked(true);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.angle_calculate_operate_calculate:
                AngleCalculate angleCalculate = new AngleCalculate();
                tvAzimuth.setText(String.valueOf(angleCalculate.getAzimuth(satelliteLongitude, cityLongitude, cityLatitude)));
                tvPitch.setText(String.valueOf(angleCalculate.getPitch(satelliteLongitude, cityLongitude, cityLatitude)));
                tvPolarization.setText(String.valueOf(angleCalculate.getPolarization(satelliteLongitude, cityLongitude, cityLatitude)));
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
                bundle.putInt(ManualActivity.POSITION, ManualActivity.locationIndex);
                bundle.putFloat(ManualActivity.PRESET_AZIMUTH, azimuth);
                bundle.putFloat(ManualActivity.PRESET_PITCH, pitch);
                bundle.putFloat(ManualActivity.PRESET_POLARIZATION, polarization);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                // getActivity().finish();
                break;
        }
    }

    private float satelliteLongitude = 0.000f;
    private float satelliteLatitude = 0.000f;
    private float satelliteBeacon = 0.000f;
    private float satelliteDvb = 0.000f;
    private float satelliteThreshold = 0.000f;
    private String satellitePolarization = "水平";

    private float cityLongitude = 0.000f;
    private float cityLatitude = 0.000f;

    private void initSatelliteSelect(View view){
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


        localLongitude.setFilters(new InputFilter[]{ new InputFilterFloat("-180", "180")});
        localLatitude.setFilters(new InputFilter[]{ new InputFilterFloat("-90", "90")});

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
