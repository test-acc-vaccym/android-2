package com.edroplet.sanetel.fragments.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.adapters.SpinnerAdapter2;
import com.edroplet.sanetel.beans.Cities;
import com.edroplet.sanetel.beans.LocationInfo;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.GalleryOnTime;
import com.edroplet.sanetel.utils.InputFilterFloat;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.utils.sscanf.Sscanf;
import com.edroplet.sanetel.view.BroadcastReceiverFragment;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;
import com.edroplet.sanetel.view.custom.CustomRadioButton;
import com.edroplet.sanetel.view.custom.CustomRadioGroupWithCustomRadioButton;

import java.util.Arrays;
import java.util.Timer;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 位置输入
 */

public class GuideFragmentLocation extends BroadcastReceiverFragment {
    public static String LocationGetPositionAction = "com.edroplet.sanetel.GetPositionAction";
    public static String LocationGetPositionData = "com.edroplet.sanetel.GetPositionData";

    private static int[] cityImages = {R.mipmap.city1, R.mipmap.city2, R.mipmap.city3};

    // 定时器
    Timer timer;

    private Cities cities;
    private String selectedProvince;
    private String selectedCity;

    @BindView(R.id.follow_me_spinner_province)
    Spinner spinnerLocationProvince;

    @BindView(R.id.follow_me_spinner_city)
    Spinner spinnerLocationCity;

    @BindView(R.id.city_select_group)
    RadioGroup citySelectGroup;

    @BindView(R.id.city_detail_province)
    CustomEditText newProvince;
    @BindView(R.id.city_detail_name)
    CustomEditText newCity;
    @BindView(R.id.city_detail_longitude)
    CustomEditText newLongitude;
    @BindView(R.id.longitude_unit)
    Spinner newLongitudeUnit;

    @BindView(R.id.city_detail_latitude)
    CustomEditText newLatitude;
    @BindView(R.id.latitude_unit)
    Spinner newLatitudeUnit;

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindArray(R.array.guide_locate_state_array)
    String[] locateStateArray;
    @BindArray(R.array.guide_locate_second_line_array)
    String[] locateSecondLineArray;

    @BindString(R.string.follow_me_message_click)
    String messageClickString;
    @BindString(R.string.follow_me_forever)
    String foreverString;
    @BindString(R.string.setting_button_text)
    String settingString;

    private Context context;

    @BindView(R.id.destination_frameLayout_location)
    FrameLayout layoutLocation;

    @BindArray(R.array.gnss_state_array)
    String[] gnssStateString;

    Unbinder unbinder;
    View view;

    SparseIntArray mapCitySelectArray = new SparseIntArray(2);
    int[] citySelectIds = {R.id.follow_me_location_db_city, R.id.follow_me_location_new_city};

    public static GuideFragmentLocation newInstance() {
        GuideFragmentLocation fragment = new GuideFragmentLocation();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String[] action = {LocationGetPositionAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        context= getContext();
        Protocol.sendMessage(context, Protocol.cmdGetPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guide_location, null);
        if (view == null){
            return null;
        }
        unbinder = ButterKnife.bind(this, view);

        context = getContext();
        int i = 0;
        for (int id: citySelectIds){
            mapCitySelectArray.put(i++, id);
        }

        initView(view);

        GalleryOnTime galleryOnTime = new GalleryOnTime(context);
        galleryOnTime.setImages(cityImages);
        galleryOnTime.setImageView();
        galleryOnTime.setFrameLayout(layoutLocation);

        timer = galleryOnTime.getTimer();

        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(context);
        // 位置输入
        popDialog.setBundle(getBundle());
        popDialog.setSetFirstColor(true);

        return popDialog.show();
    }

    private static final String KeyCitySelect = "KeyCitySelect";

    boolean[] focusables = {false, true};
    void changFocusable(int pos){
        boolean focusable = focusables[pos];

        newCity.setFocusable(focusable);
        newCity.setFocusableInTouchMode(focusable);
        newCity.setEnabled(focusable);

        newProvince.setFocusable(focusable);
        newProvince.setFocusableInTouchMode(focusable);
        newProvince.setEnabled(focusable);

        newLatitude.setFocusable(focusable);
        newLatitude.setFocusableInTouchMode(focusable);
        newLatitude.setEnabled(focusable);

        newLongitude.setFocusable(focusable);
        newLongitude.setFocusableInTouchMode(focusable);
        newLongitude.setEnabled(focusable);

        newLatitudeUnit.setClickable(focusable);
        newLatitudeUnit.setEnabled(focusable);

        newLongitudeUnit.setClickable(focusable);
        newLongitudeUnit.setEnabled(focusable);

        spinnerLocationCity.setEnabled(!focusable);
        spinnerLocationCity.setClickable(!focusable);
        spinnerLocationProvince.setClickable(!focusable);
        spinnerLocationProvince.setEnabled(!focusable);

        if (focusable) {
            newCity.requestFocus();
        }

    }
    @BindArray(R.array.longitude_unit)
    String[] longitudeArray;
    @BindArray(R.array.latitude_unit)
    String[] latitudeArray;

    String[] citiesArray;
    String[] provincesArray;

    private void initView(View view){
        newLongitudeUnit.setAdapter(new SpinnerAdapter2(context, android.R.layout.simple_list_item_1, android.R.id.text1, longitudeArray));
        newLatitudeUnit.setAdapter(new SpinnerAdapter2(context, android.R.layout.simple_list_item_1, android.R.id.text1, latitudeArray));
        // 初始化单选
        int pos = CustomSP.getInt(context,KeyCitySelect,0);
        citySelectGroup.check(mapCitySelectArray.get(pos));
        changFocusable(pos);

        citySelectGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pos = mapCitySelectArray.indexOfValue(checkedId);
                if (pos <=0) pos = 0;
                else pos = 1;
                changFocusable(pos);
            }
        });
        // 初始化其他

        newLongitude.setFilters(new InputFilter[]{ new InputFilterFloat(InputFilterFloat.longitudeMin, InputFilterFloat.longitudeMax)});
        newLatitude.setFilters(new InputFilter[]{ new InputFilterFloat(InputFilterFloat.latitudeMin, InputFilterFloat.latitudeMax)});

        try {
            cities = new Cities(getContext());
            provincesArray = cities.getProvinceArray();
            if (provincesArray.length > 0) {
                spinnerLocationProvince.setAdapter(new SpinnerAdapter2(context, android.R.layout.simple_list_item_1, android.R.id.text1, provincesArray));
                // 读取配置中的值
                selectedProvince = LocationInfo.getProvince(context);
                for(int i=0; i<provincesArray.length; i++){
                    if(selectedProvince.equals(provincesArray[i])){
                        spinnerLocationProvince.setSelection(i,true);
                        break;
                    }
                }
                citiesArray = cities.getCitiesArray(selectedProvince);
                if (citiesArray != null && citiesArray.length > 0) {
                    spinnerLocationCity.setAdapter(new SpinnerAdapter2(context, android.R.layout.simple_list_item_1, android.R.id.text1, citiesArray));
                    // 读取配置中的值
                    selectedCity = LocationInfo.getName(context);
                    for(int i=0; i<citiesArray.length; i++){
                        if(selectedCity.equals(citiesArray[i])){
                            spinnerLocationCity.setSelection(i,true);
                            break;
                        }
                    }
                    LocationInfo locationInfo = cities.getLocationInfoByProvinceCity(selectedProvince, selectedCity);
                    if (locationInfo != null) {
                        updateLocationUI(locationInfo);
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
                if (null == cities){ try{cities = new Cities(context);}catch (Exception e){e.printStackTrace();return;}}
                String[] citiesArray = cities.getCitiesArray(selectedProvince);
                if (citiesArray != null && citiesArray.length > 0) {
                    spinnerLocationCity.setAdapter(new SpinnerAdapter2(context, android.R.layout.simple_list_item_1, android.R.id.text1, citiesArray));
                    selectedCity = citiesArray[0];
                    if (selectedProvince.equals(LocationInfo.getProvince(context)) ){
                        selectedCity = LocationInfo.getName(context);
                        for(int i=0; i<citiesArray.length; i++){
                            if(selectedCity.equals(citiesArray[i])){
                                spinnerLocationCity.setSelection(i,true);
                                break;
                            }
                        }
                    }

                    if (!selectedCity.isEmpty()) {
                        LocationInfo locationInfo = cities.getLocationInfoByProvinceCity(selectedProvince, selectedCity);
                        if (locationInfo != null) {
                            updateLocationUI(locationInfo);
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
                        updateLocationUI(locationInfo);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @IdRes int checkedId = citySelectGroup.getCheckedRadioButtonId();
                CustomSP.putInt(context,KeyCitySelect, mapCitySelectArray.indexOfValue(checkedId));
                String la = newLatitude.getText().toString();
                String lo = newLongitude.getText().toString();
                switch (checkedId){
                    case R.id.follow_me_location_new_city:
                        selectedProvince = newProvince.getText().toString();
                        selectedCity = newCity.getText().toString();
                        // 添加新城市
                        cities.addItem(new LocationInfo(selectedProvince, selectedCity,
                                ConvertUtil.convertToFloat(la, 0.00f),
                                newLatitudeUnit.getSelectedItemPosition(),
                                ConvertUtil.convertToFloat(lo, 0.00f),
                                newLongitudeUnit.getSelectedItemPosition()), true);
                        try {
                            cities.save();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                // 保存配置
                LocationInfo.setProvince(context,selectedProvince);
                LocationInfo.setName(context,selectedCity);
                // 发送定位指令
                Protocol.sendMessage(context, String.format(Protocol.cmdSetPosition, la, lo));
            }
        });

    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }

    Bundle getBundle(){
        int gnssState = LocationInfo.getGnssState(context);
        Bundle args = new Bundle();
        args.putBoolean(PopDialog.SHOW_FIRST,true);
        args.putString(PopDialog.FIRST, locateStateArray[gnssState]);
        args.putBoolean(PopDialog.SHOW_SECOND,true);
        args.putString(PopDialog.SECOND, locateSecondLineArray[gnssState]);
        args.putBoolean(PopDialog.SHOW_THIRD,true);
        args.putString(PopDialog.START, messageClickString);
        args.putString(PopDialog.BUTTON_TEXT, settingString);
        args.putString(PopDialog.END, foreverString);
        return args;
    }

    void updateLocationUI(LocationInfo locationInfo){
        newProvince.setText(locationInfo.getProvince());
        newCity.setText(locationInfo.getName());
        newLatitude.setText(String.valueOf(locationInfo.getLatitude()));
        newLongitude.setText(String.valueOf(locationInfo.getLongitude()));
        newLatitudeUnit.setSelection(locationInfo.getLatitudeUnitPosition());
        newLongitudeUnit.setSelection(locationInfo.getLongitudeUnitPosition());
    }

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        String rawData = intent.getStringExtra(LocationGetPositionData);
        String latitude = "0";
        String longitude = "0";
        Object[] objects = Sscanf.scan(rawData, Protocol.cmdGetPositionResult, longitude, latitude);
        latitude = (String) objects[0];
        longitude = (String) objects[1];
        newLongitude.setText(longitude);
        newLatitude.setText(latitude);
        if (cities != null){
          LocationInfo li =   cities.getInfoByLatitudeAndLongitude(longitude, latitude);
          if (li != null){
              selectedProvince = li.getProvince();
              int proPos = Arrays.asList(provincesArray).indexOf(selectedProvince);
              if (proPos < 0){
                  proPos = 0;
                  selectedProvince = provincesArray[proPos];
              }
              spinnerLocationProvince.setSelection(proPos);
              String[] cityArray = cities.getCitiesArray(selectedProvince);
              if (cityArray.length > 0) {
                  selectedCity = li.getName();
                  int cityPos = Arrays.asList(cityArray).indexOf(selectedCity);
                  if (cityPos < 0) {
                        cityPos = 0;
                        selectedCity = cityArray[0];
                  }
                  spinnerLocationCity.setSelection(cityPos);
              }
          }
        }
    }
}
