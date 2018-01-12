package com.edroplet.sanetel.fragments.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.adapters.SpinnerAdapter2;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.beans.SatelliteInfo;
import com.edroplet.sanetel.beans.Satellites;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.GalleryOnTime;
import com.edroplet.sanetel.utils.InputFilterFloat;
import com.edroplet.sanetel.utils.InputFilterMinMax;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.utils.sscanf.Sscanf;
import com.edroplet.sanetel.view.BroadcastReceiverFragment;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;

import java.util.Arrays;
import java.util.Timer;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 目标星设置
 * 协议 4.10	目标星
 */

public class GuideFragmentDestination extends BroadcastReceiverFragment {
    public static final String GuideDestinationAction = "com.edroplet.sanetel.GuideDestinationAction";
    public static final String GuideDestinationData = "com.edroplet.sanetel.GuideDestinationData";

    private static final String KeySelectDestination = "KeySelectDestination";

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

    @BindView(R.id.name_detail)
    CustomEditText satelliteName;

    @BindView(R.id.polarization_detail)
    Spinner satellitePolarization;

    @BindView(R.id.longitude_detail)
    CustomEditText satelliteLongitude;

    @BindView(R.id.beacon_detail)
    CustomEditText satelliteBeacon;

    @BindView(R.id.threshold_detail)
    CustomEditText satelliteThreshold;

    @BindView(R.id.symbol_rate_detail)
    CustomEditText satelliteDvb;

    @BindView(R.id.carrier_detail)
    CustomEditText satelliteCarrier;

    @BindView(R.id.comment_detail)
    CustomEditText satelliteComment;

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindArray(R.array.satellites_polarization)
    String[] satellitesPolarization;
    private Satellites satellites;
    private String name;
    private String polarization;

    @BindView(R.id.guide_destination_group)
    RadioGroup guideDestinationGroup;

    @BindArray(R.array.satellites_polarization)
    String[] satellitesPolarizationArray;

    SparseIntArray mapSelectDestinationArray = new SparseIntArray(2);
    int[] destinationSelectIds = {R.id.follow_me_destination_satellite_select,R.id.follow_me_destination_satellite_new};

    Context context;
    Unbinder unbinder;
    View view;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String[] action = {GuideDestinationAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        context = getContext();
        Protocol.sendMessage(context, Protocol.cmdGetTargetState);

        int n = 0;
        for (int id:destinationSelectIds){
            mapSelectDestinationArray.put(n++, id);
        }
    }

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        String rawData = intent.getStringExtra(GuideDestinationData);
        SatelliteInfo destinationSatelliteInfo = new SatelliteInfo();
        Object[] objects = Sscanf.scan(rawData,Protocol.cmdGetTargetStateResult,destinationSatelliteInfo.longitude,destinationSatelliteInfo.polarization,destinationSatelliteInfo.threshold,destinationSatelliteInfo.beacon,destinationSatelliteInfo.carrier,destinationSatelliteInfo.symbolRate);
        int pos = Arrays.asList(satellitesPolarization).indexOf((String)objects[0]);
        if (pos < 0 ){
            pos = 0;
        }
        String longitudeString = (String)objects[0];
        satelliteName.setText(satellites.getNameByLongitude(longitudeString));
        satelliteLongitude.setText(longitudeString);
        satellitePolarization.setSelection(pos,true);
        satelliteThreshold.setText((String)objects[1]);
        satelliteBeacon.setText((String)objects[2]);
        satelliteCarrier.setText((String)objects[3]);
        satelliteDvb.setText((String)objects[4]);
        view.invalidate();
    }

    boolean[] focusableArray = {false, true};

    void changeFocusable(int pos){
        boolean focusable = focusableArray[pos];

        satelliteName.setFocusableInTouchMode(focusable);
        satelliteName.setFocusable(focusable);
        satelliteName.setEnabled(focusable);

        satelliteLongitude.setFocusableInTouchMode(focusable);
        satelliteLongitude.setFocusable(focusable);
        satelliteLongitude.setEnabled(focusable);

        satellitePolarization.setFocusableInTouchMode(focusable);
        satellitePolarization.setFocusable(focusable);
        satellitePolarization.setEnabled(focusable);

        satelliteDvb.setFocusableInTouchMode(focusable);
        satelliteDvb.setFocusable(focusable);
        satelliteDvb.setEnabled(focusable);

        satelliteCarrier.setFocusableInTouchMode(focusable);
        satelliteCarrier.setFocusable(focusable);
        satelliteCarrier.setEnabled(focusable);

        satelliteThreshold.setFocusableInTouchMode(focusable);
        satelliteThreshold.setFocusable(focusable);
        satelliteThreshold.setEnabled(focusable);

        satelliteBeacon.setFocusableInTouchMode(focusable);
        satelliteBeacon.setFocusable(focusable);
        satelliteBeacon.setEnabled(focusable);

        satelliteComment.setFocusableInTouchMode(focusable);
        satelliteComment.setFocusable(focusable);
        satelliteComment.setEnabled(focusable);

        satellitePolarization.setClickable(focusable);
        satellitePolarization.setEnabled(focusable);
        satellitePolarization.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, satellitesPolarizationArray));

        satellitePolarizationSelect.setClickable(!focusable);
        satellitePolarizationSelect.setEnabled(!focusable);
        satelliteSelect.setEnabled(!focusable);
        satelliteSelect.setClickable(!focusable);

        if (focusable) {
            satelliteName.requestFocus();
        }
        view.invalidate();
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guide_destination_satellite, null);
        if (view == null){
            return null;
        }
        unbinder = ButterKnife.bind(this, view);

        final int pos = CustomSP.getInt(context,KeySelectDestination,0);
        changeFocusable(pos);
        guideDestinationGroup.check(mapSelectDestinationArray.get(pos));

        guideDestinationGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pos = mapSelectDestinationArray.indexOfValue(checkedId);
                if (pos <= 0) pos = 0;
                else pos = 1;
                changeFocusable(pos);
            }
        });
        // 限制输入
        satelliteLongitude.setFilters(new InputFilter[]{ new InputFilterFloat(InputFilterFloat.longitudeMin, InputFilterFloat.longitudeMax)});
        satelliteDvb.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.dvbMin,InputFilterFloat.dvbMax)});
        satelliteThreshold.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.thresholdMin,InputFilterFloat.thresholdMax)});
        satelliteBeacon.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.beaconMin,InputFilterFloat.beaconMax)});
        satelliteCarrier.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.carrierMin,InputFilterFloat.carrierMax)});

        try {
            satellites = new Satellites(context);
            String[] satelliteNameArray = satellites.getSatelliteNameArray();
            if (satelliteNameArray.length > 0) {
                satelliteSelect.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, satelliteNameArray));
                // 读取配置中的值
                name = CustomSP.getString(getContext(), KEY_DESTINATION_SATELLITE_NAME, satelliteNameArray[0]);
                for(int i=0; i<satelliteNameArray.length; i++){
                    if(name.equals(satelliteNameArray[i])){
                        satelliteSelect.setSelection(i,true);
                        break;
                    }
                }
                String[] polarizationArray = satellites.getSatellitePolarizationArray(name);
                if (polarizationArray.length > 0) {
                    satellitePolarizationSelect.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, polarizationArray));
                    // 读取配置中的值
                    polarization = CustomSP.getString(getContext(), KEY_DESTINATION_SATELLITE_POLARIZATION, polarizationArray[0]);
                    for(int i=0; i<polarizationArray.length; i++){
                        if(polarization.equals(polarizationArray[i])){
                            satellitePolarizationSelect.setSelection(i,true);
                            break;
                        }
                    }
                    SatelliteInfo satelliteInfo = satellites.getSatelliteInfoBySatelliteNamePolarization(name, polarization);
                    if (satelliteInfo != null) {
                        updateSattelitesUI(satelliteInfo);
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        satelliteSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                name = (String)satelliteSelect.getItemAtPosition(position);
                String[] polarizationArray = satellites.getSatellitePolarizationArray(name);
                if (polarizationArray.length > 0) {
                    satellitePolarizationSelect.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, polarizationArray));
                    polarization = polarizationArray[0];
                    if (name.equals(CustomSP.getString(getContext(), KEY_DESTINATION_SATELLITE_NAME,name)) ){
                        polarization = CustomSP.getString(getContext(), KEY_DESTINATION_SATELLITE_POLARIZATION, polarization);
                        for(int i=0; i<polarizationArray.length; i++){
                            if(polarization.equals(polarizationArray[i])){
                                satellitePolarizationSelect.setSelection(i,true);
                                break;
                            }
                        }
                    }

                    if (!polarization.isEmpty()) {
                        SatelliteInfo satelliteInfo = satellites.getSatelliteInfoBySatelliteNamePolarization(name, polarization);
                        if (satelliteInfo != null) {
                            updateSattelitesUI(satelliteInfo);
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
                polarization = (String)satellitePolarizationSelect.getItemAtPosition(position);
                if (!polarization.isEmpty()) {
                    SatelliteInfo satelliteInfo = satellites.getSatelliteInfoBySatelliteNamePolarization(name, polarization);
                    if (satelliteInfo != null) {
                        updateSattelitesUI(satelliteInfo);
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
                @IdRes int checkedId = guideDestinationGroup.getCheckedRadioButtonId();
                CustomSP.putInt(context,KeySelectDestination, mapSelectDestinationArray.indexOfValue(checkedId));

                switch (checkedId){
                    case R.id.follow_me_destination_satellite_new:
                        name = satelliteName.getText().toString();
                        polarization = satellitePolarization.getSelectedItem().toString();
                        // 添加新城市
                        satellites.addItem(new SatelliteInfo(satellites.getItemCounts()+"",
                                name,
                                polarization,
                                satelliteLongitude.getText().toString(),
                                satelliteBeacon.getText().toString(),
                                satelliteThreshold.getText().toString(),
                                satelliteDvb.getText().toString(),
                                satelliteComment.getText().toString(),
                                satelliteCarrier.getText().toString(),
                                null), true);
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
                CustomSP.putString(getContext(), KEY_DESTINATION_SATELLITE_NAME, name);
                CustomSP.putString(getContext(), KEY_DESTINATION_SATELLITE_POLARIZATION, polarization);
                // 4.2.7.	寻星模式 需要用到这两个值
                CustomSP.putString(getContext(), KEY_DESTINATION_SATELLITE_BEACON_FREQUENCY, satelliteBeacon.getText().toString());
                CustomSP.putString(getContext(), KEY_DESTINATION_SATELLITE_DVB, satelliteDvb.getText().toString());
                CustomSP.putString(getContext(),KEY_DESTINATION_SATELLITE_CARRIER, satelliteCarrier.getText().toString());
                // 发送设置命令
                Protocol.sendMessage(getContext(), String.format(Protocol.cmdSetTargetState,
                        satelliteBeacon.getText().toString(),satelliteLongitude.getText().toString(),
                        satellitePolarization.getSelectedItem().toString(), satelliteDvb.getText().toString(),
                        satellitePolarizationSelect, satelliteThreshold.getText().toString()) );
            }
        });

        FrameLayout frameLayout = view.findViewById(R.id.destination_frameLayout_satellite);

        GalleryOnTime galleryOnTime = new GalleryOnTime(getContext());
        galleryOnTime.setFrameLayout(frameLayout);
        galleryOnTime.setImages(satellitesImages);
        galleryOnTime.setImageView();
        timer = galleryOnTime.getTimer();

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
        if (unbinder != null)  unbinder.unbind();
    }

    // 更新界面
    void updateSattelitesUI(SatelliteInfo satelliteInfo){

        satelliteName.setText(satelliteInfo.name);
        String[] polarizationArray = satellites.getSatellitePolarizationArray(name);
        for(int i=0; i<polarizationArray.length; i++){
            if(polarization.equals(polarizationArray[i])){
                satellitePolarization.setSelection(i,true);
                break;
            }
        }

        satelliteLongitude.setText(satelliteInfo.longitude);
        satelliteBeacon.setText(satelliteInfo.beacon);
        satelliteThreshold.setText(satelliteInfo.threshold);
        // dvb数据
        satelliteDvb.setText(satelliteInfo.symbolRate);

        satelliteCarrier.setText(satelliteInfo.carrier);
        satelliteComment.setText(satelliteInfo.comment);
    }
}
