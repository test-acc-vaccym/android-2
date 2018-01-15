package com.edroplet.sanetel.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.adapters.SpinnerAdapter2;
import com.edroplet.sanetel.beans.SatelliteInfo;
import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.utils.InputFilterFloat;
import com.edroplet.sanetel.utils.JsonLoad;
import com.edroplet.sanetel.view.custom.CustomEditText;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewSatelliteActivity extends AppCompatActivity {
    @BindView(R.id.name_detail)
     CustomEditText satelliteNameView;
    @BindView(R.id.longitude_detail)
     CustomEditText satelliteLongitudeView;
    @BindView(R.id.polarization_detail)
     Spinner satellitePolarizationView;
    @BindView(R.id.beacon_detail)
     CustomEditText satelliteBeaconView;
    @BindView(R.id.threshold_detail)
     CustomEditText satelliteThresholdView;
    @BindView(R.id.symbol_rate_detail)
     CustomEditText satelliteSymbolRateView;
    @BindView(R.id.comment_detail)
     CustomEditText satelliteCommentView;
    @BindView(R.id.carrier_detail)
     CustomEditText satelliteCarrierView;

    @BindArray(R.array.satellites_polarization)
    String[] polarizationArray;

    private boolean inputError;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_satellite);

        // 初始化
        ButterKnife.bind(this);

        mContext = this;

        satelliteLongitudeView.setFilters(new InputFilter[]{ new InputFilterFloat(InputFilterFloat.longitudeMin, InputFilterFloat.longitudeMax)});
        satelliteThresholdView.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.thresholdMin, InputFilterFloat.thresholdMax)});
        satelliteSymbolRateView.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.dvbMin,InputFilterFloat.dvbMax)});
        satelliteSymbolRateView.setMinMax(InputFilterFloat.dvbMin,InputFilterFloat.dvbMax);
        satelliteBeaconView.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.beaconMin,InputFilterFloat.beaconMax)});
        satelliteBeaconView.setMinMax(InputFilterFloat.beaconMin,InputFilterFloat.beaconMax);
        satelliteCarrierView.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.carrierMin,InputFilterFloat.carrierMax)});
        satelliteCarrierView.setMinMax(InputFilterFloat.carrierMin,InputFilterFloat.carrierMax);

        satelliteBeaconView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView tv = (TextView)v;
                if (!hasFocus){
                    if (tv.length() > 0 &&  ConvertUtil.convertToFloat(tv.getText().toString(),0.0f)< 10750){
                        tv.setText("");
                        // tv.setError(getString(R.string.value_error));
                        inputError = true;
                    }
                }
            }
        });
        satelliteSymbolRateView.setFilters(new InputFilter[]{new InputFilterFloat(0, 30000)});
        satelliteSymbolRateView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView tv = (TextView)v;
                if (!hasFocus){
                    if (tv.length() > 0 &&  ConvertUtil.convertToFloat(tv.getText().toString(),0.0f)< 6000){
                        tv.setText("");
                        // tv.setError(getString(R.string.value_error));
                        inputError = true;
                    }
                }
            }
        });

        findViewById(R.id.satellites_new_button_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputError){
                    // RandomDialog randomDialog = new RandomDialog(mContext);
                    // randomDialog.onConfirm();
                }
                doCommit();
            }
        });

        findViewById(R.id.satellites_new_button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 设置极化方式可选值
        satellitePolarizationView.setAdapter(new SpinnerAdapter2(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, polarizationArray));
    }

    private boolean doCommit(){
        JsonLoad jl = new JsonLoad(NewSatelliteActivity.this, SatelliteInfo.satelliteJsonFile);
        try {
            ArrayList<SatelliteInfo> satellites = jl.loadSatellite();
            SatelliteInfo newSatellite = new SatelliteInfo(String.valueOf(satellites.size()),satelliteNameView.getText().toString(),
                    satellitePolarizationView.getSelectedItem().toString(), satelliteLongitudeView.getText().toString(),
                    satelliteBeaconView.getText().toString(), satelliteThresholdView.getText().toString(),
                    satelliteSymbolRateView.getText().toString(), satelliteCommentView.getText().toString(),satelliteCarrierView.getText().toString(),null);
            satellites.add(newSatellite);
            jl.saveSatellites(satellites);

            Bundle bundle = new Bundle();
            bundle.putSerializable(SatelliteInfo.objectKey,newSatellite);
            bundle.putInt(SatelliteInfo.positionKey,satellites.size() - 1);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
