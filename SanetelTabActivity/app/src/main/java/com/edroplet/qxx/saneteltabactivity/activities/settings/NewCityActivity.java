package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterFloat;
import com.edroplet.qxx.saneteltabactivity.utils.JsonLoad;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;

import java.util.ArrayList;

public class NewCityActivity extends AppCompatActivity {

    @BindId(R.id.city_detail_province)
    private CustomEditText cityProvenceView;
    @BindId(R.id.city_detail_name)
    private CustomEditText cityNameView;
    @BindId(R.id.city_detail_longitude)
    private CustomEditText cityLongitudeView;
    @BindId(R.id.city_detail_latitude)
    private CustomEditText cityLatitudeView;

    @BindId(R.id.latitude_unit)
    private Spinner cityLatitudeUnitView;

    @BindId(R.id.longitude_unit)
    private Spinner cityLongitudeUnitView;

    @BindId(R.id.latitude_unit)
    Spinner spinnerLatitudeUnit;
    int latitudeUnitPosition;
    @BindId(R.id.longitude_unit)
    Spinner spinnerLongitudeUnit;
    int longitudeUnitPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_city);
        // 初始化
        ViewInject.inject(this, this);
        cityLongitudeView.setFilters(new InputFilter[]{ new InputFilterFloat("-180", "180")});
        cityLatitudeView.setFilters(new InputFilter[]{ new InputFilterFloat("-90", "90")});

        findViewById(R.id.city_new_button_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonLoad jl = new JsonLoad(NewCityActivity.this, LocationInfo.citiesJsonFile);
                try {
                    ArrayList<LocationInfo> cities = jl.loadCities();
                    LocationInfo newLocation = new LocationInfo(cityProvenceView.getText().toString(), cityNameView.getText().toString(),
                            ConvertUtil.convertToFloat(cityLatitudeView.getText().toString(), 0),
                            cityLatitudeUnitView.getSelectedItemPosition(),
                            ConvertUtil.convertToFloat(cityLongitudeView.getText().toString(), 0),
                            cityLongitudeUnitView.getSelectedItemPosition());
                    cities.add(newLocation);
                    jl.saveCities(cities);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("city",newLocation);
                    bundle.putInt("position",cities.size() - 1);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
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
