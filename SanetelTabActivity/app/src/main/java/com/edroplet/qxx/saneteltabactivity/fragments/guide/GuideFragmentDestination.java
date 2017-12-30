package com.edroplet.qxx.saneteltabactivity.fragments.guide;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.adapters.SpinnerAdapter2;
import com.edroplet.qxx.saneteltabactivity.beans.Protocol;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.beans.Satellites;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.GalleryOnTime;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterFloat;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentLocation.mOnCheckedChangeListener;

/**
 * Created by qxs on 2017/9/19.
 * 目标星设置
 */

public class GuideFragmentDestination extends Fragment {
    private static int[] satellitesImages = {R.mipmap.satellite1, R.mipmap.satellite2, R.mipmap.satellite3};
    public static final String KEY_DESTINATION_SATELLITE_NAME = "KEY_DESTINATION_SATELLITE_NAME";
    public static final String KEY_DESTINATION_SATELLITE_POLARIZATION = "KEY_DESTINATION_SATELLITE_POLARIZATION";
    public static final String KEY_DESTINATION_SATELLITE_BEACON_FREQUENCY = "KEY_DESTINATION_SATELLITE_BEACON_FREQUENCY"; // 信标频率
    public static final String KEY_DESTINATION_SATELLITE_DVB = "KEY_DESTINATION_SATELLITE_DVB"; // DVB值
    public static final String KEY_DESTINATION_SATELLITE_CARRIER = "KEY_DESTINATION_SATELLITE_CARRIER"; // 载波值


    @BindView(R.id.follow_me_destination_spinner_satellites_select)
    Spinner satelliteSelect;

    @BindView(R.id.follow_me_destination_spinner_satellites_polarization_select)
    Spinner satellitePolarizationSelect;

    @BindView(R.id.follow_me_destination_satellite_name)
    CustomEditText satelliteName;

    @BindView(R.id.follow_me_destination_satellite_polarization)
    Spinner satellitePolarization;

    @BindView(R.id.follow_me_destination_satellite_longitude)
    CustomEditText satelliteLongitude;

    @BindView(R.id.follow_me_destination_satellite_beacon)
    CustomEditText satelliteBeacon;

    @BindView(R.id.follow_me_destination_satellite_threshold)
    CustomEditText satelliteThreshold;

    @BindView(R.id.follow_me_destination_satellite_dvb)
    CustomEditText satelliteDvb;

    @BindView(R.id.follow_me_destination_satellite_carrier)
    CustomEditText satelliteCarrier;


    private CustomButton thirdButton;
    private Satellites satellites;
    private String selectedName;
    private String selectedPolarization;
    private CustomRadioGroupWithCustomRadioButton customRadioGroupWithCustomRadioButton;

    // 定时器
    Timer timer = new Timer();

    public static GuideFragmentDestination newInstance(boolean showFirst, String firstLine,
                                                       boolean showSecond, String secondLine,
                                                       boolean showThird, String thirdLineStart,
                                                       int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        GuideFragmentDestination fragment = new GuideFragmentDestination();
        args.putBoolean(PopDialog.SHOW_FIRST,showFirst);
        args.putString(PopDialog.FIRST, firstLine);
        args.putBoolean(PopDialog.SHOW_SECOND,showSecond);
        args.putString(PopDialog.SECOND, secondLine);
        args.putBoolean(PopDialog.SHOW_THIRD,showThird);
        args.putString(PopDialog.START, thirdLineStart);
        args.putInt(PopDialog.ICON, icon);
        args.putString(PopDialog.BUTTON_TEXT, buttonText);
        args.putString(PopDialog.END, thirdLineEnd);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_guide_destination_satellite, null);
        if (view == null){
            return null;
        }
        ButterKnife.bind(this, view);

//        satelliteSelect = view.findViewById(R.id.follow_me_destination_spinner_satellites_select);
//        satellitePolarizationSelect = view.findViewById(R.id.follow_me_destination_spinner_satellites_polarization_select);
//        satelliteName = view.findViewById(R.id.follow_me_destination_satellite_name);
//        satellitePolarization = view.findViewById(R.id.follow_me_destination_satellite_polarization);
//        satelliteLongitude = view.findViewById(R.id.follow_me_destination_satellite_longitude);
//        satelliteBeacon = view.findViewById(R.id.follow_me_destination_satellite_beacon);
//        satelliteThreshold = view.findViewById(R.id.follow_me_destination_satellite_threshold);
//        satelliteDvb = view.findViewById(R.id.follow_me_destination_satellite_dvb);
        satelliteDvb.setFilters(new InputFilter[]{new InputFilterFloat(6000,30000)});

//        thirdButton = view.findViewById(R.id.pop_dialog_third_button);
//        customRadioGroupWithCustomRadioButton = view.findViewById(R.id.rbg_city);

        try {
            satellites = new Satellites(getContext());
            String[] satelliteNameArray = satellites.getSatelliteNameArray();
            if (satelliteNameArray.length > 0) {
                satelliteSelect.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, satelliteNameArray));
                // 读取配置中的值
                selectedName = CustomSP.getString(getContext(), KEY_DESTINATION_SATELLITE_NAME, satelliteNameArray[0]);
                for(int i=0; i<satelliteNameArray.length; i++){
                    if(selectedName.equals(satelliteNameArray[i])){
                        satelliteSelect.setSelection(i,true);
                        break;
                    }
                }
                String[] polarizationArray = satellites.getSatellitePolarizationArray(selectedName);
                if (polarizationArray.length > 0) {
                    satellitePolarizationSelect.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, polarizationArray));
                    // 读取配置中的值
                    selectedPolarization = CustomSP.getString(getContext(), KEY_DESTINATION_SATELLITE_POLARIZATION, polarizationArray[0]);
                    for(int i=0; i<polarizationArray.length; i++){
                        if(selectedPolarization.equals(polarizationArray[i])){
                            satellitePolarizationSelect.setSelection(i,true);
                            break;
                        }
                    }
                    SatelliteInfo satelliteInfo = satellites.getSatelliteInfoBySatelliteNamePolarization(selectedName, selectedPolarization);
                    if (satelliteInfo != null) {
                        satelliteName.setText(satelliteInfo.name);
                        for(int i=0; i<polarizationArray.length; i++){
                            if(selectedPolarization.equals(polarizationArray[i])){
                                satellitePolarization.setSelection(i,true);
                                break;
                            }
                        }
                        satelliteLongitude.setText(satelliteInfo.longitude);
                        satelliteBeacon.setText(satelliteInfo.beacon);
                        satelliteThreshold.setText(satelliteInfo.threshold);
                        //  dvb数据
                        satelliteDvb.setText(satelliteInfo.symbolRate);
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        satelliteSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedName = (String)satelliteSelect.getItemAtPosition(position);
                String[] polarizationArray = satellites.getSatellitePolarizationArray(selectedName);
                if (polarizationArray.length > 0) {
                    satellitePolarizationSelect.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, polarizationArray));
                    selectedPolarization = polarizationArray[0];
                    if (selectedName.equals(CustomSP.getString(getContext(), KEY_DESTINATION_SATELLITE_NAME,selectedName)) ){
                        selectedPolarization = CustomSP.getString(getContext(), KEY_DESTINATION_SATELLITE_POLARIZATION, selectedPolarization);
                        for(int i=0; i<polarizationArray.length; i++){
                            if(selectedPolarization.equals(polarizationArray[i])){
                                satellitePolarizationSelect.setSelection(i,true);
                                break;
                            }
                        }
                    }

                    if (!selectedPolarization.isEmpty()) {
                        SatelliteInfo satelliteInfo = satellites.getSatelliteInfoBySatelliteNamePolarization(selectedName, selectedPolarization);
                        if (satelliteInfo != null) {
                            satelliteName.setText(satelliteInfo.name);

                            for(int i=0; i<polarizationArray.length; i++){
                                if(selectedPolarization.equals(polarizationArray[i])){
                                    satellitePolarization.setSelection(i,true);
                                    break;
                                }
                            }
                            satelliteLongitude.setText(satelliteInfo.longitude);
                            satelliteBeacon.setText(satelliteInfo.beacon);
                            satelliteThreshold.setText(satelliteInfo.threshold);
                            // dvb数据
                            satelliteDvb.setText(satelliteInfo.symbolRate);
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        satellitePolarizationSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 读取配置中的值
                selectedPolarization = (String)satellitePolarizationSelect.getItemAtPosition(position);
                if (!selectedPolarization.isEmpty()) {
                    SatelliteInfo satelliteInfo = satellites.getSatelliteInfoBySatelliteNamePolarization(selectedName, selectedPolarization);
                    if (satelliteInfo != null) {
                        satelliteName.setText(satelliteInfo.name);
                        String[] polarizationArray = satellites.getSatellitePolarizationArray(selectedName);
                        for(int i=0; i<polarizationArray.length; i++){
                            if(selectedPolarization.equals(polarizationArray[i])){
                                satellitePolarization.setSelection(i,true);
                                break;
                            }
                        }

                        satelliteLongitude.setText(satelliteInfo.longitude);
                        satelliteBeacon.setText(satelliteInfo.beacon);
                        satelliteThreshold.setText(satelliteInfo.threshold);
                        // dvb数据
                        satelliteDvb.setText(satelliteInfo.symbolRate);
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
                        selectedName = satelliteName.getText().toString();
                        selectedPolarization = satellitePolarization.getSelectedItem().toString();
                        // 添加新城市
                        satellites.addItem(new SatelliteInfo(satellites.getItemCounts()+"",
                                selectedName,
                                selectedPolarization,
                                satelliteLongitude.getText().toString(),
                                satelliteBeacon.getText().toString(),
                                satelliteThreshold.getText().toString(),
                                satelliteDvb.getText().toString(),null), true);
                        try {
                            satellites.save();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                // 保存配置
                CustomSP.putString(getContext(), KEY_DESTINATION_SATELLITE_NAME, selectedName);
                CustomSP.putString(getContext(), KEY_DESTINATION_SATELLITE_POLARIZATION, selectedPolarization);
                // 4.2.7.	寻星模式 需要用到这两个值
                CustomSP.putString(getContext(), KEY_DESTINATION_SATELLITE_BEACON_FREQUENCY, satelliteBeacon.getText().toString());
                CustomSP.putString(getContext(), KEY_DESTINATION_SATELLITE_DVB, satelliteDvb.getText().toString());
                CustomSP.putString(getContext(),KEY_DESTINATION_SATELLITE_CARRIER, satelliteCarrier.getText().toString());
                // 发送设置命令
                Protocol.sendMessage(getContext(), String.format(Protocol.cmdSetTargetState,
                        satelliteBeacon.getText().toString(),satelliteLongitude.getText().toString(),
                        satellitePolarization.toString(), satelliteDvb.getText().toString(),
                        satellitePolarizationSelect, satelliteThreshold.getText().toString()) );
            }
        });

        FrameLayout frameLayout = view.findViewById(R.id.destination_frameLayout_satellite);

        GalleryOnTime galleryOnTime = new GalleryOnTime(getContext());
        galleryOnTime.setFrameLayout(frameLayout);
        galleryOnTime.setImages(satellitesImages);
        galleryOnTime.setImageView();
        timer = galleryOnTime.getTimer();

        CustomRadioButton crbDbSatellite = view.findViewById(R.id.follow_me_destination_satellite_select);
        CustomRadioButton crbNewSatellite = view.findViewById(R.id.follow_me_destination_satellite_new);
        if (crbDbSatellite != null){
            crbDbSatellite.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
        if (crbNewSatellite != null){
            crbNewSatellite.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        Context context = getContext();
        popDialog.setContext(context);

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
