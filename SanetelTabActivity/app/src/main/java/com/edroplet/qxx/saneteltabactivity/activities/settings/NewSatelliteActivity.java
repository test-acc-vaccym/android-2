package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterFloat;
import com.edroplet.qxx.saneteltabactivity.utils.JsonLoad;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;
import com.edroplet.qxx.saneteltabactivity.view.EDropletDialogBuilder;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class NewSatelliteActivity extends AppCompatActivity {
    @BindId(R.id.name_detail)
    private CustomEditText satelliteNameView;
    @BindId(R.id.longitude_detail)
    private CustomEditText satelliteLongitudeView;
    @BindId(R.id.polarization_detail)
    private Spinner satellitePolarizationView;
    @BindId(R.id.beacon_detail)
    private CustomEditText satelliteBeaconView;
    @BindId(R.id.threshold_detail)
    private CustomEditText satelliteThresholdView;
    @BindId(R.id.symbol_rate_detail)
    private CustomEditText satelliteSymbolRateView;
    @BindId(R.id.comment_detail)
    private CustomEditText satelliteCommentView;
    @BindId(R.id.carrier_detail)
    private CustomEditText satelliteCarrierView;

    private boolean inputError;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_satellite);

        // 初始化
        ViewInject.inject(this, this);
        mContext = this;

        satelliteLongitudeView.setFilters(new InputFilter[]{ new InputFilterFloat("-180.000", "180.000")});
        satelliteThresholdView.setFilters(new InputFilter[]{new InputFilterFloat(0, 10.0f)});
        satelliteBeaconView.setFilters(new InputFilter[]{new InputFilterFloat(0, 40000)});
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
