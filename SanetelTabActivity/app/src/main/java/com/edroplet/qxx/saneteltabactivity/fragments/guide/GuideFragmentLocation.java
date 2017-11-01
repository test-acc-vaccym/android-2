package com.edroplet.qxx.saneteltabactivity.fragments.guide;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.GalleryOnTime;
import com.edroplet.qxx.saneteltabactivity.utils.ImageUtil;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

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
                                                    int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        GuideFragmentLocation fragment = new GuideFragmentLocation();
        args.putBoolean("showFirst",showFirst);
        args.putString("first", firstLine);
        args.putBoolean("showSecond",showSecond);
        args.putString("second", secondLine);
        args.putBoolean("showThird",showThird);
        args.putString("start", thirdLineStart);
        args.putInt("icon", icon);
        args.putString("buttonText", buttonText);
        args.putString("end", thirdLineEnd);
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
        // 位置输入
        Bundle bundle = getArguments();
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);
            int icon = bundle.getInt("icon", -1);
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
                    case R.id.follow_me_destination_satellite_new:
                        // 添加新城市
                        cities.addItem(new LocationInfo(newProvince.getText().toString(),
                                newCity.getText().toString(),
                                Float.parseFloat(newLatitude.getText().toString()),
                                Float.parseFloat(newLongitude.getText().toString())), true);
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
            for (int i = 0; i < childCount; i++){
                if (edGroup.getChildAt(i).getId() != compoundButton.getId()){
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
        timer.cancel();
        super.onDestroy();
    }
}
