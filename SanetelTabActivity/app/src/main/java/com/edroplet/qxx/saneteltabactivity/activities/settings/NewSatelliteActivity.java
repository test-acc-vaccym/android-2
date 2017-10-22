package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.utils.JsonLoad;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class NewSatelliteActivity extends AppCompatActivity {
    @BindId(R.id.satellites_new_name)
    private CustomEditText satelliteNameView;
    @BindId(R.id.satellites_new_longitude)
    private CustomEditText satelliteLongitudeView;
    @BindId(R.id.satellites_new_polarization)
    private Spinner satellitePolarizationView;
    @BindId(R.id.satellites_new_beacon)
    private CustomEditText satelliteBeaconView;
    @BindId(R.id.satellites_new_threshold)
    private CustomEditText satelliteThresholdView;
    @BindId(R.id.satellites_new_symbol_rate)
    private CustomEditText satelliteSymbolRateView;
    @BindId(R.id.satellites_new_comment)
    private CustomEditText satelliteCommentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_satellite);

        // 初始化
        ViewInject.inject(this, this);

        findViewById(R.id.satellites_new_button_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonLoad jl = new JsonLoad(NewSatelliteActivity.this, SatelliteInfo.satelliteJsonFile);
                try {
                    ArrayList<SatelliteInfo> satellites = jl.loadSatellite();
                    satellites.add(new SatelliteInfo(String.valueOf(satellites.size()),satelliteNameView.getText().toString(),
                            satellitePolarizationView.getSelectedItem().toString(), satelliteLongitudeView.getText().toString(),
                            satelliteBeaconView.getText().toString(), satelliteThresholdView.getText().toString(),
                            satelliteSymbolRateView.getText().toString(), satelliteCommentView.getText().toString()));
                    jl.saveSatellites(satellites);
                }catch (Exception e){
                    e.printStackTrace();
                }
                finish();
            }
        });

        findViewById(R.id.satellites_new_button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
