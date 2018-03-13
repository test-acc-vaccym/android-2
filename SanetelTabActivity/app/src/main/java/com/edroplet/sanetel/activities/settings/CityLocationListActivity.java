package com.edroplet.sanetel.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.adapters.CitiesRecyclerViewAdapter;
import com.edroplet.sanetel.adapters.SpinnerAdapter2;
import com.edroplet.sanetel.beans.Cities;
import com.edroplet.sanetel.beans.LocationInfo;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.JsonLoad;
import com.edroplet.sanetel.utils.RandomDialog;
import com.edroplet.sanetel.view.ViewInject;
import com.edroplet.sanetel.view.annotation.BindId;
import com.edroplet.sanetel.view.custom.CustomButton;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * An activity representing a list of CityLocations. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CityLocationDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CityLocationListActivity extends AppCompatActivity /*implements View.OnClickListener*/{
    public static final int NEW_CITY_REQUEST_CODE = 10010;
    public static final int CITY_DETAIL_REQUEST_CODE = 10011;
    private CitiesRecyclerViewAdapter citiesRecyclerViewAdapter;
    private Cities cities;
    private RecyclerView recyclerView;
    private boolean isShowSelect = false;

    @BindId(R.id.recover_city)
    private CustomButton recoveryCity;

    @BindId(R.id.city_select_button)
    private CustomButton citySelectButton;

    @BindId(R.id.city_list_select_province)
    Spinner cityListSelectProvince;

    String selectedProvince;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int position = 0;
        String id = "";
        LocationInfo locationInfo = null;
        if (null != data) {
            if (data.hasExtra(LocationInfo.objectKey)) {
                locationInfo = data.getParcelableExtra(LocationInfo.objectKey);
            }
            if (data.hasExtra(LocationInfo.positionKey)) {
                position = data.getIntExtra(LocationInfo.positionKey, 0);
            }
            if (data.hasExtra(LocationInfo.JSON_ID_KEY)){
                id = data.getStringExtra(LocationInfo.JSON_ID_KEY);
            }
        }
        switch (requestCode){
            case CITY_DETAIL_REQUEST_CODE:
                if (RESULT_OK == resultCode && id != null && id.length() > 0 && locationInfo != null) {
                    cities.update(id, locationInfo);
                    citiesRecyclerViewAdapter.setmValues(cities.getLocationInfosByProvince(selectedProvince));
//                    List<LocationInfo> newList = citiesRecyclerViewAdapter.getValues();
//                    int i = 0;
//                    for (LocationInfo info: newList) {
//                        if (info.getName().equals(locationInfo.getName())) {
//                            newList.set(i, locationInfo);
//                            break;
//                        }
//                        i++;
//                    }
//                    citiesRecyclerViewAdapter.setmValues(newList);
                    citiesRecyclerViewAdapter.notifyDataSetChanged();
                    // 保存到文件
                    JsonLoad js = new JsonLoad(this, LocationInfo.citiesJsonFile);
                    ArrayList<LocationInfo> al = new ArrayList<LocationInfo>();
                    al.addAll(cities.getITEMS());
                    try {
                        js.saveCities(al);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case NEW_CITY_REQUEST_CODE:
                if (resultCode == RESULT_OK && locationInfo != null) {
                    //  刷新当前activity界面数据
                    cities.addItem(locationInfo,true);
                    cityListSelectProvince.setAdapter(new SpinnerAdapter2(this,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1, cities.getProvinceArray()));
                    cityListSelectProvince.setSelection(Arrays.asList(cities.getProvinceArray()).indexOf(selectedProvince));
                    citiesRecyclerViewAdapter.setmValues(cities.getLocationInfosByProvince(selectedProvince));
                    // citiesRecyclerViewAdapter.setmValues(citiesRecyclerViewAdapter.getValues());
                    citiesRecyclerViewAdapter.notifyDataSetChanged();

                }
                // }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                // NavUtils.navigateUpTo(this, new Intent(this, SatelliteListActivity.class));
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        ViewInject.inject(this, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.city_list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_navigate_before_white);
            // ab.setTitle(R.string.satellite_toolbar_title);
            // 设置居中的时候不能含有原标题
            ab.setDisplayShowTitleEnabled(false);
        }

        try {
            cities = new Cities(this);
            String[] provinceArray = cities.getProvinceArray();
            if (provinceArray.length > 0) selectedProvince = provinceArray[0];
            cityListSelectProvince.setAdapter(new SpinnerAdapter2(this, android.R.layout.simple_list_item_1, android.R.id.text1, provinceArray));
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            finish();
        }

        cityListSelectProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProvince = (String)cityListSelectProvince.getItemAtPosition(position);
                List<LocationInfo> infoList = cities.getLocationInfosByProvince(selectedProvince);
                if (infoList != null) {
                    citiesRecyclerViewAdapter.setmValues(infoList);
                }
                citiesRecyclerViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.city_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        findViewById(R.id.add_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(CityLocationListActivity.this, NewCityActivity.class), NEW_CITY_REQUEST_CODE);
            }
        });

        findViewById(R.id.delete_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Map<Integer, Boolean> map = citiesRecyclerViewAdapter.getMap();
                int size = map.size();
                boolean hasCheckedAny = false;
                final List<Integer> checkedPosition = new ArrayList<>();
                for (int i = size - 1; i >= 0; i--) {
                    if (map.get(i)) {
                        hasCheckedAny = true;
                        checkedPosition.add(i);
                    }
                }

                if (isShowSelect && hasCheckedAny){
                    String confirmDelete = String.format(getString(R.string.confirm_delete_message), "所选项？");
                    final RandomDialog dialogBuilder = new RandomDialog(CityLocationListActivity.this);
                    dialogBuilder.onConfirm(confirmDelete, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            List<LocationInfo> items = citiesRecyclerViewAdapter.getValues();
                            for (int i: checkedPosition) {
                                if (map.get(i)) {
                                    LocationInfo item = items.get(i);
                                    citiesRecyclerViewAdapter.deleteItem(item);
                                    cities.deleteItem(item);
                                }
                            }

                            try {
                                // 修改文件
                                JsonLoad js = new JsonLoad(view.getContext(), LocationInfo.citiesJsonFile);
                                ArrayList<LocationInfo> al = new ArrayList<LocationInfo>();
                                // 修改整个文件所以这样
                                al.addAll(cities.getITEMS());
                                js.saveCities(al);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // 现在只修改当前视图，所以这样
                            toggleState();
                            // 数据库可能已经更新，所以需要重新更新spinner
                            String[] provinceArray = cities.getProvinceArray();
                            boolean isClear = true;
                            for (String pro:provinceArray){
                                if (pro.equals(selectedProvince)){
                                    isClear = false;
                                    break;
                                }
                            }
                            if (isClear && provinceArray.length > 0) {
                                selectedProvince = provinceArray[0];
                                cityListSelectProvince.setAdapter(new SpinnerAdapter2(CityLocationListActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        android.R.id.text1,
                                        provinceArray));

                                citiesRecyclerViewAdapter.setmValues(cities.getLocationInfosByProvince(selectedProvince));
                                citiesRecyclerViewAdapter.initMap();
                                cityListSelectProvince.setSelection(0);
                            }else if (isClear && provinceArray.length == 0){
                                citiesRecyclerViewAdapter.setmValues(null);
                                Toast.makeText(CityLocationListActivity.this,"EMPTY!", Toast.LENGTH_LONG);
                            }else {
                                citiesRecyclerViewAdapter.setmValues(cities.getLocationInfosByProvince(selectedProvince));
                            }
                            citiesRecyclerViewAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(0);
                            // citiesRecyclerViewAdapter.notifyAll();
                            dialogBuilder.getDialogBuilder().dismiss();
                        }
                    }, getString(R.string.operate_confirm_ok));
                } else {
                    toggleState();
                    citiesRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });

        citySelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CustomButton)view).getText() == getString(R.string.select_all)){
                    citiesRecyclerViewAdapter.fillMap();
                    ((CustomButton)view).setText(R.string.select_none);
                }else {
                    citiesRecyclerViewAdapter.initMap();
                    ((CustomButton)view).setText(R.string.select_all);
                }
                citiesRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.city_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.city_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        recoveryCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SystemServices.copyAssetsFiles2FileDir(CityLocationListActivity.this, LocationInfo.citiesJsonFile);

                CustomSP.putBoolean(getApplicationContext(), CustomSP.firstReadCities, true);
//                try {
//                    cities = new Cities(CityLocationListActivity.this,true);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                cities.clear();
                finish();
                // citiesRecyclerViewAdapter.notifyDataSetChanged();
                // SystemServices.restartAPP(CityLocationListActivity.this, 1000);
            }
        });

    }

    private void toggleState(){
        citiesRecyclerViewAdapter.setShowBox();
        if (isShowSelect) {
            citySelectButton.setVisibility(View.GONE);
            isShowSelect = false;
        }
        else {
            isShowSelect = true;
            citySelectButton.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        try {
            if (null == cities || cities.getItemCounts() == 0)
            cities = new Cities(this);
            // citiesRecyclerViewAdapter = new CitiesRecyclerViewAdapter(cities.getITEMS(), this);
            citiesRecyclerViewAdapter = new CitiesRecyclerViewAdapter(cities.getLocationInfosByProvince(selectedProvince), this);
            /** 亦可在item中处理 */
            citiesRecyclerViewAdapter.setRecyclerViewOnItemClickListener(new CitiesRecyclerViewAdapter.RecyclerViewOnItemClickListener(){
                @Override
                public void onItemClickListener(View view, int position) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(CityLocationDetailFragment.CITY_ARG_ITEM_ID, citiesRecyclerViewAdapter.getValues().get(position).getName());
                        CityLocationDetailFragment fragment = new CityLocationDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.city_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, CityLocationDetailActivity.class);
                        intent.putExtra(CityLocationDetailFragment.CITY_ARG_ITEM_ID, citiesRecyclerViewAdapter.getValues().get(position).getName());
                        startActivityForResult(intent, CityLocationListActivity.CITY_DETAIL_REQUEST_CODE);
                    }
                }

                @Override
                public boolean onItemLongClickListener(View view, int position) {
                    // toggleState();
                    isShowSelect = true;
                    citySelectButton.setVisibility(View.VISIBLE);
                    citiesRecyclerViewAdapter.setShowBox();
                    citiesRecyclerViewAdapter.initMap();
                    citiesRecyclerViewAdapter.notifyDataSetChanged();
                    Toast.makeText(view.getContext(), "长按了" + citiesRecyclerViewAdapter.getValues().get(position).getName(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            //为RecyclerView添加默认动画效果，测试不写也可以
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(citiesRecyclerViewAdapter);
        }catch (JSONException je){
            je.printStackTrace();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

}
