package com.edroplet.qxx.saneteltabactivity.activities.settings;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.adapters.CitiesRecyclerViewAdapter;
import com.edroplet.qxx.saneteltabactivity.beans.Cities;
import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.JsonLoad;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;
import com.edroplet.qxx.saneteltabactivity.view.custom.SmoothCheckBox;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

    @BindId(R.id.recover_city)
    private CustomButton recoveryCity;

    @BindId(R.id.city_select_button)
    private CustomButton citySelectButton;

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
            if (data.hasExtra("city")) {
                locationInfo = data.getParcelableExtra("city");
            }
            if (data.hasExtra("position")) {
                position = data.getIntExtra("position", 0);
            }
            if (data.hasExtra(LocationInfo.JSON_ID_KEY)){
                id = data.getStringExtra(LocationInfo.JSON_ID_KEY);
            }
        }
        switch (requestCode){
            case CITY_DETAIL_REQUEST_CODE:
                if (id != null && id.length() > 0) {
                    cities.update(id, locationInfo);
                    citiesRecyclerViewAdapter.notifyItemChanged(position);
                }
                break;
            case NEW_CITY_REQUEST_CODE:
                // if(resultCode== Activity.RESULT_OK){
                if (locationInfo != null) {
                    //  刷新当前activity界面数据
                    cities.addItem(locationInfo,true);
                    //RecyclerView列表进行UI数据更新
                    citiesRecyclerViewAdapter.notifyItemInserted(position);
                    //如果在第一项添加模拟数据需要调用 scrollToPosition（0）把列表移动到顶端（可选）
                    recyclerView.scrollToPosition(position);
                    citiesRecyclerViewAdapter.notifyItemChanged(position);
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
            case R.id.delete_city:
                //Map<Integer, Boolean> map = citiesRecyclerViewAdapter.getMap();
                //for (int i = 0; i < map.size(); i++) {
                    // map.put(i, true);
                    citiesRecyclerViewAdapter.setShowBox();
                    citiesRecyclerViewAdapter.notifyDataSetChanged();
                //}
                break;
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
            ab.setHomeAsUpIndicator(R.drawable.back);
            // ab.setTitle(R.string.satellite_toolbar_title);
            // 设置居中的时候不能含有原标题
            ab.setDisplayShowTitleEnabled(false);
        }

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
                String confirmDelete = String.format(getString(R.string.confirm_delete_message),"所选项？");
                final RandomDialog dialogBuilder = new RandomDialog(CityLocationListActivity.this);
                dialogBuilder.onConfirm(confirmDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<Integer, Boolean> map =  citiesRecyclerViewAdapter.getMap();
                        for (int i = 0, j = 0; i < map.size(); i++,j++) {
                            if (map.get(i)) {
                                citiesRecyclerViewAdapter.deleteItem(i - j);
                                cities.deleteItem(i - j);
                            }
                        }

                        try {
                            // 修改文件
                            JsonLoad js = new JsonLoad(view.getContext(), LocationInfo.citiesJsonFile);
                            ArrayList<LocationInfo> al = new ArrayList<LocationInfo>();
                            al.addAll(cities.getITEMS());
                            js.saveCities(al);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        citiesRecyclerViewAdapter.setmValues(cities.getITEMS());
                        citiesRecyclerViewAdapter.initMap();
                        toggleState();
                        citiesRecyclerViewAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(0);
                        citySelectButton.setVisibility(View.INVISIBLE);
                        // citiesRecyclerViewAdapter.notifyAll();
                        dialogBuilder.getDialogBuilder().dismiss();
                    }
                });
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
        citySelectButton.setVisibility(View.INVISIBLE);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        try {
            cities = new Cities(this);
            citiesRecyclerViewAdapter = new CitiesRecyclerViewAdapter(cities.getITEMS(), this);
            /** 亦可在item中处理 */
            citiesRecyclerViewAdapter.setRecyclerViewOnItemClickListener(new CitiesRecyclerViewAdapter.RecyclerViewOnItemClickListener(){
                @Override
                public void onItemClickListener(View view, int position) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(CityLocationDetailFragment.CITY_ARG_ITEM_ID, cities.getITEMS().get(position).getName());
                        CityLocationDetailFragment fragment = new CityLocationDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.city_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, CityLocationDetailActivity.class);
                        intent.putExtra(CityLocationDetailFragment.CITY_ARG_ITEM_ID, cities.getITEMS().get(position).getName());
                        startActivityForResult(intent, CityLocationListActivity.CITY_DETAIL_REQUEST_CODE);
                    }
                }

                @Override
                public boolean onItemLongClickListener(View view, int position) {
                    citySelectButton.setVisibility(View.VISIBLE);
                    citiesRecyclerViewAdapter.setShowBox();
                    citiesRecyclerViewAdapter.notifyDataSetChanged();
                    Toast.makeText(view.getContext(), "长按了" + cities.getITEMS().get(position).getName(), Toast.LENGTH_SHORT).show();
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
