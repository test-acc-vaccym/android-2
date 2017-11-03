package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterMinMax;
import com.edroplet.qxx.saneteltabactivity.utils.JsonLoad;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;

import java.util.ArrayList;

public class NewCityActivity extends AppCompatActivity {

    @BindId(R.id.city_new_provence)
    private CustomEditText cityProvenceView;
    @BindId(R.id.city_new_name)
    private CustomEditText cityNameView;
    @BindId(R.id.city_new_longitude)
    private CustomEditText cityLongitudeView;
    @BindId(R.id.city_new_latitude)
    private CustomEditText cityLatitudeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_city);
        // 初始化
        ViewInject.inject(this, this);
        cityLongitudeView.setFilters(new InputFilter[]{ new InputFilterMinMax("-180", "180")});
        cityLatitudeView.setFilters(new InputFilter[]{ new InputFilterMinMax("-90", "90")});

        findViewById(R.id.city_new_button_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonLoad jl = new JsonLoad(NewCityActivity.this, LocationInfo.citiesJsonFile);
                try {
                    ArrayList<LocationInfo> cities = jl.loadCities();
                    LocationInfo newLocation = new LocationInfo(cityProvenceView.getText().toString(), cityNameView.getText().toString(),
                            ConvertUtil.convertToFloat(cityLatitudeView.getText().toString(), 0),
                            ConvertUtil.convertToFloat(cityLongitudeView.getText().toString(), 0));
                    cities.add(newLocation);
                    jl.saveCities(cities);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("city",newLocation);
                    bundle.putInt("position",cities.size() - 1);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(CityLocationListActivity.NEW_CITY_REQUEST_CODE, intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
                finish();
            }
        });

        findViewById(R.id.city_new_button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}