package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.adapters.SpinnerAdapter2;
import com.edroplet.qxx.saneteltabactivity.beans.PolarizationMode;
import com.edroplet.qxx.saneteltabactivity.beans.Protocol;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.beans.Satellites;
import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterFloat;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;


public class ReferenceSatelliteActivity extends AppCompatActivity {
    @BindId(R.id.settings_reference_toolbar)
    private Toolbar referenceToolbar;

    @BindId(R.id.pop_dialog_tv_second)
    private CustomTextView secondLine;

    @BindId(R.id.pop_dialog_tv_third_end)
    private CustomTextView thirdEnd;

    @BindId(R.id.reference_satellite_select_radio_group)
    private RadioGroup referenceSatelliteSelectGroup;

    @BindId(R.id.reference_satellite_select_satellites)
    private Spinner satellitesSpinner;

    @BindId(R.id.reference_satellite_select_satellites_polarization)
    private Spinner satellitesPolarizationSpinner;

    @BindId(R.id.settings_reference_longitude)
    private CustomTextView longitude;
    @BindId(R.id.settings_reference_beacon)
    private CustomTextView beacon;
    @BindId(R.id.settings_reference_ag)
    private CustomTextView agThreshold;
    @BindId(R.id.settings_reference_dvb)
    private CustomTextView dvbSymbolRate;
    @BindId(R.id.settings_reference_carrier)
    private CustomTextView tvCarrier;
    @BindId(R.id.pop_dialog_third_button)
    private CustomButton thirdButton;

    private static final String KEY_REFERENCE_SATELLITE_SEARCHING_MODE = "KEY_REFERENCE_SATELLITE_SEARCHING_MODE";
    private static final String KEY_REFERENCE_SATELLITE_SEARCHING_SATELLITE = "KEY_REFERENCE_SATELLITE_SEARCHING_SATELLITE";
    private static final String KEY_REFERENCE_SATELLITE_SEARCHING_SATELLITE_POLARIZATION = "KEY_REFERENCE_SATELLITE_SEARCHING_SATELLITE_POLARIZATION";
    private static final String KEY_REFERENCE_SATELLITE_SEARCHING_LONGITUDE = "KEY_REFERENCE_SATELLITE_SEARCHING_LONGITUDE";
    private static final String KEY_REFERENCE_SATELLITE_SEARCHING_BEACON = "KEY_REFERENCE_SATELLITE_SEARCHING_BEACON";
    private static final String KEY_REFERENCE_SATELLITE_SEARCHING_AG = "KEY_REFERENCE_SATELLITE_SEARCHING_AG";
    private static final String KEY_REFERENCE_SATELLITE_SEARCHING_DVB = "KEY_REFERENCE_SATELLITE_SEARCHING_DVB";
    private static final String KEY_REFERENCE_SATELLITE_SEARCHING_CARRIER = "KEY_REFERENCE_SATELLITE_SEARCHING_CARRIER";

    static SparseIntArray mapReferenceSatellite = new SparseIntArray(2);

    void initReferenceSatellite(){
        mapReferenceSatellite.put(0, R.id.reference_satellite_select_mode_direct);
        mapReferenceSatellite.put(1, R.id.reference_satellite_select_mode_reference);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_satellite);

        ViewInject.inject(this, this);
        initReferenceSatellite();
        initView();

        dvbSymbolRate.setInputType(InputType.TYPE_CLASS_NUMBER);
        dvbSymbolRate.setFilters(new InputFilter[]{new InputFilterFloat(0,30000)});
        dvbSymbolRate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView tv = (TextView)v;
                if (!hasFocus){
                    if (tv.length() > 0 &&  ConvertUtil.convertToFloat(tv.getText().toString(),0.0f)< 6000){
                        tv.setText("");
                        // tv.setError(getString(R.string.value_error));
                    }
                }
            }
        });

        agThreshold.setFilters(new InputFilter[]{new InputFilterFloat(0, 10.0f)});
        beacon.setFilters(new InputFilter[]{new InputFilterFloat(0, 40000)});
        beacon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView tv = (TextView)v;
                if (!hasFocus){
                    if (tv.length() > 0 &&  ConvertUtil.convertToFloat(tv.getText().toString(),0.0f)< 10750){
                        tv.setText("");
                        // tv.setError(getString(R.string.value_error));
                    }
                }
            }
        });

        referenceToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                @IdRes int checkedId = referenceSatelliteSelectGroup.getCheckedRadioButtonId();
                int searchMode = mapReferenceSatellite.indexOfValue(checkedId);
                CustomSP.putInt(ReferenceSatelliteActivity.this,
                        KEY_REFERENCE_SATELLITE_SEARCHING_MODE,
                        searchMode);

                CustomSP.putString(ReferenceSatelliteActivity.this,
                        KEY_REFERENCE_SATELLITE_SEARCHING_SATELLITE,
                        selectedSatellite);

                CustomSP.putString(ReferenceSatelliteActivity.this,
                        KEY_REFERENCE_SATELLITE_SEARCHING_SATELLITE_POLARIZATION,
                        selectedPolarization);

                String longitudeVal = longitude.getText().toString();
                CustomSP.putString(ReferenceSatelliteActivity.this,
                        KEY_REFERENCE_SATELLITE_SEARCHING_LONGITUDE, longitudeVal);

                String beaconVal = beacon.getText().toString();
                CustomSP.putString(ReferenceSatelliteActivity.this,
                        KEY_REFERENCE_SATELLITE_SEARCHING_BEACON,
                        beaconVal);

                String agThresholdVal = agThreshold.getText().toString();
                CustomSP.putString(ReferenceSatelliteActivity.this,
                        KEY_REFERENCE_SATELLITE_SEARCHING_AG,
                        agThresholdVal);

                String dvbVal = dvbSymbolRate.getText().toString();
                CustomSP.putString(ReferenceSatelliteActivity.this,
                        KEY_REFERENCE_SATELLITE_SEARCHING_DVB,
                        dvbVal);

                String carrier = tvCarrier.getText().toString();
                CustomSP.putString(ReferenceSatelliteActivity.this,
                        KEY_REFERENCE_SATELLITE_SEARCHING_CARRIER,
                        carrier);
                // TODO: 2017/10/23 设置命令
                // 卫星经度,极化方式,寻星门限,信标频率,载波频率,符号率,寻星方式
                int polarizationMode = PolarizationMode.getMap(ReferenceSatelliteActivity.this).get(selectedPolarization);
                String message = String.format(Protocol.cmdSetRefData, longitudeVal, polarizationMode, agThresholdVal, beaconVal, carrier, dvbVal, searchMode);
                Log.e(ReferenceSatelliteActivity.class.getSimpleName(), message);
                Protocol.sendMessage(ReferenceSatelliteActivity.this, message);
            }
        });

        initData();


        PopDialog popDialog = new PopDialog(this);

        popDialog.setView(findViewById(R.id.settings_reference_satellite_pop));
        popDialog.setContext(this);
        Bundle bundle = new Bundle();
        bundle.putBoolean(PopDialog.SHOW_SECOND, true);
        bundle.putString(PopDialog.SECOND,getString(R.string.settings_reference_message_second_line));
        bundle.putBoolean(PopDialog.SHOW_THIRD, true);
        bundle.putBoolean(PopDialog.SHOW_FORTH, true);

        bundle.putString(PopDialog.START,getString(R.string.follow_me_message_click));
        bundle.putString(PopDialog.END,getString(R.string.settings_reference_message_third_end));
        bundle.putString(PopDialog.FORTH,getString(R.string.settings_reference_message_forth));

        popDialog.setBundle(bundle);
        popDialog.setSetFirstColor(true);
        popDialog.setButtonText(this,getString(R.string.setting_button_text));
        popDialog.show();
    }

    Satellites satellites;
    String selectedSatellite;
    String selectedPolarization;

    private void initData(){
        int pos = CustomSP.getInt(ReferenceSatelliteActivity.this,
                KEY_REFERENCE_SATELLITE_SEARCHING_MODE,
                0);
        referenceSatelliteSelectGroup.check(mapReferenceSatellite.get(pos));

        longitude.setText(CustomSP.getString(ReferenceSatelliteActivity.this,
                KEY_REFERENCE_SATELLITE_SEARCHING_LONGITUDE,
                ""));
        beacon.setText(CustomSP.getString(ReferenceSatelliteActivity.this,
                KEY_REFERENCE_SATELLITE_SEARCHING_BEACON,
                ""));
        agThreshold.setText(CustomSP.getString(ReferenceSatelliteActivity.this,
                KEY_REFERENCE_SATELLITE_SEARCHING_AG,
                ""));
        dvbSymbolRate.setText(CustomSP.getString(ReferenceSatelliteActivity.this,
                KEY_REFERENCE_SATELLITE_SEARCHING_DVB,
                ""));

    }

    private void initView(){
        try {
            satellites = new Satellites(this);
            String[] satelliteNameArray = satellites.getSatelliteNameArray();
            satellitesSpinner.setAdapter(new SpinnerAdapter2(this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    satelliteNameArray));
            // 读取配置中的值
            selectedSatellite = CustomSP.getString(this, KEY_REFERENCE_SATELLITE_SEARCHING_SATELLITE, satelliteNameArray[0]);
            for(int i=0; i<satelliteNameArray.length; i++){
                if(selectedSatellite.equals(satelliteNameArray[i])){
                    satellitesSpinner.setSelection(i,true);
                    break;
                }
            }
            String[] polarizationArray = satellites.getSatellitePolarizationArray(selectedSatellite);
            if (polarizationArray.length > 0) {
                satellitesPolarizationSpinner.setAdapter(new SpinnerAdapter2(this, android.R.layout.simple_list_item_1, android.R.id.text1, polarizationArray));
                // 读取配置中的值
                selectedPolarization = CustomSP.getString(this, KEY_REFERENCE_SATELLITE_SEARCHING_SATELLITE_POLARIZATION, polarizationArray[0]);
                for(int i=0; i<polarizationArray.length; i++){
                    if(selectedPolarization.equals(polarizationArray[i])){
                        satellitesPolarizationSpinner.setSelection(i,true);
                        break;
                    }
                }
                SatelliteInfo satelliteInfo = satellites.getSatelliteInfoBySatelliteNamePolarization(selectedSatellite, selectedPolarization);
                if (satelliteInfo != null) {
                    for(int i=0; i<polarizationArray.length; i++){
                        if(selectedPolarization.equals(polarizationArray[i])){
                            satellitesPolarizationSpinner.setSelection(i,true);
                            break;
                        }
                    }
                    longitude.setText(satelliteInfo.longitude);
                    beacon.setText(satelliteInfo.beacon);
                    agThreshold.setText(satelliteInfo.threshold);
                    // todo dvb数据哪里来
                    dvbSymbolRate.setText(satelliteInfo.symbolRate);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        satellitesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSatellite = (String)satellitesSpinner.getItemAtPosition(position);
                String[] polarizationArray = satellites.getSatellitePolarizationArray(selectedSatellite);
                if (polarizationArray.length > 0) {
                    satellitesPolarizationSpinner.setAdapter(new SpinnerAdapter2(ReferenceSatelliteActivity.this,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            polarizationArray));
                    selectedPolarization = polarizationArray[0];

                    if (!selectedPolarization.isEmpty()) {
                        SatelliteInfo satelliteInfo = satellites.getSatelliteInfoBySatelliteNamePolarization(selectedSatellite, selectedPolarization);
                        if (satelliteInfo != null) {

                            for(int i=0; i<polarizationArray.length; i++){
                                if(selectedPolarization.equals(polarizationArray[i])){
                                    satellitesPolarizationSpinner.setSelection(i,true);
                                    break;
                                }
                            }
                            longitude.setText(satelliteInfo.longitude);
                            beacon.setText(satelliteInfo.beacon);
                            agThreshold.setText(satelliteInfo.threshold);
                            // todo dvb数据哪里来
                            dvbSymbolRate.setText(satelliteInfo.symbolRate);
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        satellitesPolarizationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 读取配置中的值
                selectedPolarization = (String)satellitesPolarizationSpinner.getItemAtPosition(position);
                if (!selectedPolarization.isEmpty()) {
                    SatelliteInfo satelliteInfo = satellites.getSatelliteInfoBySatelliteNamePolarization(selectedSatellite, selectedPolarization);
                    if (satelliteInfo != null) {
                        longitude.setText(satelliteInfo.longitude);
                        beacon.setText(satelliteInfo.beacon);
                        agThreshold.setText(satelliteInfo.threshold);
                        // todo dvb数据哪里来
                        dvbSymbolRate.setText(satelliteInfo.symbolRate);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }
}
