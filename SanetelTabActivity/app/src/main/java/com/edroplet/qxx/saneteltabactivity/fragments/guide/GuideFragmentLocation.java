package com.edroplet.qxx.saneteltabactivity.fragments.guide;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.adapters.SpinnerAdapter2;
import com.edroplet.qxx.saneteltabactivity.beans.Cities;
import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.GalleryOnTime;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterFloat;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;

import java.util.Timer;

/**
 * Created by qxs on 2017/9/19.
 */

public class GuideFragmentLocation extends Fragment {

    private static int[] cityImages = {R.mipmap.city1, R.mipmap.city2, R.mipmap.city3};
    private static final String LOCATION_PROVINCE_KEY = "LOCATION_PROVINCE_KEY";
    private static final String LOCATION_CITY_KEY = "LOCATION_CITY_KEY";

    // 定时器
    Timer timer = new Timer();

    private Cities cities;
    private String selectedProvince;
    private String selectedCity;

    private CustomRadioButton crbDbCity;
    private CustomRadioButton crbNewCity;

    private Spinner spinnerLocationProvince;
    private Spinner spinnerLocationCity;

    private CustomRadioGroupWithCustomRadioButton customRadioGroupWithCustomRadioButton;

    private CustomEditText newProvince;
    private CustomEditText newCity;
    private CustomEditText newLongitude;
    private Spinner newLongitudeUnit;
    private CustomEditText newLatitude;
    private Spinner newLatitudeUnit;

    private CustomButton thirdButton;

    public static GuideFragmentLocation newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                    String secondLine, boolean showThird, String thirdLineStart,
                                                    int icon, String buttonText, String thirdLineEnd,
                                                    boolean showForth, String forth) {
        Bundle args = new Bundle();
        GuideFragmentLocation fragment = new GuideFragmentLocation();
        args.putBoolean(PopDialog.SHOW_FIRST,showFirst);
        args.putString(PopDialog.FIRST, firstLine);
        args.putBoolean(PopDialog.SHOW_SECOND,showSecond);
        args.putString(PopDialog.SECOND, secondLine);
        args.putBoolean(PopDialog.SHOW_THIRD,showThird);
        args.putString(PopDialog.START, thirdLineStart);
        args.putInt(PopDialog.ICON, icon);
        args.putString(PopDialog.BUTTON_TEXT, buttonText);
        args.putString(PopDialog.END, thirdLineEnd);
        args.putBoolean(PopDialog.SHOW_FORTH,showForth);
        args.putString(PopDialog.FORTH, forth);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_follow_me_location, null);
        if (view == null){
            return null;
        }
        
        initView(view);

        GalleryOnTime galleryOnTime = new GalleryOnTime(getContext());
        galleryOnTime.setFrameLayout((FrameLayout) view.findViewById(R.id.destination_frameLayout_location));
        galleryOnTime.setImages(cityImages);
        galleryOnTime.setImageView();
        timer = galleryOnTime.getTimer();

        crbDbCity = view.findViewById(R.id.follow_me_location_db_city);
        crbNewCity = view.findViewById(R.id.follow_me_location_new_city);
        if (crbNewCity != null){
            crbNewCity.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
        if (crbDbCity != null){
            crbDbCity.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }

        Context context = getContext();

        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(context);
        // 位置输入
        Bundle bundle = getArguments();
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);
            int icon = bundle.getInt(PopDialog.ICON, -1);
            if (icon >= 0) {
                popDialog.setButtonText(context, getString(R.string.setting_button_text));
            }
        }
        return popDialog.show();
    }

    private void initView(View view){

        // 初始化控件
        thirdButton = view.findViewById(R.id.pop_dialog_third_button);
        spinnerLocationCity = view.findViewById(R.id.follow_me_spinner_city);
        spinnerLocationProvince = view.findViewById(R.id.follow_me_spinner_province);

        customRadioGroupWithCustomRadioButton = view.findViewById(R.id.rbg_city);
        newCity = view.findViewById(R.id.follow_me_location_et_new_city);
        newProvince = view.findViewById(R.id.follow_me_location_et_new_province);
        newLongitude = view.findViewById(R.id.follow_me_location_et_new_longitude);
        newLatitude = view.findViewById(R.id.follow_me_location_et_new_latitude);
        newLongitudeUnit = view.findViewById(R.id.follow_me_location_spinner_new_longitude_nit);
        newLatitudeUnit = view.findViewById(R.id.follow_me_location_spinner_new_latitude_unit);

        newLongitude.setFilters(new InputFilter[]{ new InputFilterFloat("-180.0", "180.0")});
        newLatitude.setFilters(new InputFilter[]{ new InputFilterFloat("-90.0", "90.0")});

        try {
            cities = new Cities(getContext());
            String[] provincesArray = cities.getProvinceArray();
            if (provincesArray.length > 0) {
                spinnerLocationProvince.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, provincesArray));
                // 读取配置中的值
                selectedProvince = CustomSP.getString(getContext(), LOCATION_PROVINCE_KEY, provincesArray[0]);
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
                    selectedCity = CustomSP.getString(getContext(), LOCATION_CITY_KEY, citiesArray[0]);
                    for(int i=0; i<citiesArray.length; i++){
                        if(selectedCity.equals(citiesArray[i])){
                            spinnerLocationCity.setSelection(i,true);
                            break;
                        }
                    }
                    LocationInfo locationInfo = cities.getLocationInfoByProvinceCity(selectedProvince, selectedCity);
                    if (locationInfo != null) {
                        newProvince.setText(locationInfo.getProvince());
                        newCity.setText(locationInfo.getName());
                        newLatitude.setText(locationInfo.getLatitude()+"");
                        newLongitude.setText(locationInfo.getLongitude()+"");
                        newLatitudeUnit.setSelection(0);
                        newLongitudeUnit.setSelection(0);
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
                    if (selectedProvince.equals(CustomSP.getString(getContext(), LOCATION_PROVINCE_KEY,selectedProvince)) ){
                        selectedCity = CustomSP.getString(getContext(), LOCATION_CITY_KEY, selectedCity);
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
                            newProvince.setText(locationInfo.getProvince());
                            newCity.setText(locationInfo.getName());
                            newLatitude.setText(locationInfo.getLatitude()+"");
                            newLongitude.setText(locationInfo.getLongitude()+"");
                            newLatitudeUnit.setSelection(0);
                            newLongitudeUnit.setSelection(0);
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
                        newProvince.setText(locationInfo.getProvince());
                        newCity.setText(locationInfo.getName());
                        newLatitude.setText(locationInfo.getLatitude()+"");
                        newLongitude.setText(locationInfo.getLongitude()+"");
                        newLatitudeUnit.setSelection(0);
                        newLongitudeUnit.setSelection(0);
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
                @IdRes int checkedId = customRadioGroupWithCustomRadioButton.getCheckedRadioButtonId();
                switch (checkedId){
                    case R.id.follow_me_location_new_city:
                        selectedProvince = newProvince.getText().toString();
                        selectedCity = newCity.getText().toString();
                        // 添加新城市
                        cities.addItem(new LocationInfo(selectedProvince, selectedCity,
                                ConvertUtil.convertToFloat(newLatitude.getText().toString(), 0.00f),
                                newLatitudeUnit.getSelectedItem().toString(),
                                ConvertUtil.convertToFloat(newLongitude.getText().toString(), 0.00f),
                                newLongitudeUnit.getSelectedItem().toString()), true);
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
                CustomSP.putString(getContext(), LOCATION_PROVINCE_KEY, selectedProvince);
                CustomSP.putString(getContext(), LOCATION_CITY_KEY, selectedCity);
            }
        });

    }
    public static CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            ViewParent vp = compoundButton.getParent();
            CustomRadioGroupWithCustomRadioButton edGroup = (CustomRadioGroupWithCustomRadioButton) vp;
            int childCount = edGroup.getChildCount();
            int buttonId = compoundButton.getId();
            if (b){
                edGroup.setCheckedId(buttonId);
            }
            for (int i = 0; i < childCount; i++){
                int childId = edGroup.getChildAt(i).getId();
                if (childId != buttonId){
                    CustomRadioButton rdButton =  (CustomRadioButton)edGroup.getChildAt(i);
                    if (b && rdButton.isChecked()){
                        rdButton.setChecked(false);
                    }
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        super.onDestroy();
    }
}
