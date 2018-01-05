package com.edroplet.sanetel.fragments.guide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.utils.sscanf.Sscanf;
import com.edroplet.sanetel.view.BroadcastReceiverFragment;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomRadioButton;
import com.edroplet.sanetel.view.custom.CustomRadioGroupWithCustomRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.edroplet.sanetel.fragments.guide.GuideFragmentDestination.KEY_DESTINATION_SATELLITE_BEACON_FREQUENCY;
import static com.edroplet.sanetel.fragments.guide.GuideFragmentDestination.KEY_DESTINATION_SATELLITE_DVB;
import static com.edroplet.sanetel.fragments.guide.GuideFragmentDestination.KEY_DESTINATION_SATELLITE_POLARIZATION;
import static com.edroplet.sanetel.utils.CustomSP.KEY_SEARCHING_MODE;

/**
 * Created by qxs on 2017/9/19.
 *  UI 寻星模式
 * 协议 4.6	跟踪模式选择
 * 通信协议在ap中使用 3.4、寻星模式流程
 */

public class GuideFragmentSearchModeSetting extends BroadcastReceiverFragment {
    public static final String SearchModeAction = "com.edroplet.sanetel.SearchModeAction";
    public static final String SearchModeData = "com.edroplet.sanetel.SearchModeData";

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.search_mode_group)
    CustomRadioGroupWithCustomRadioButton searchModeGroup;
    @BindView(R.id.follow_me_search_mode_beacon)
    CustomRadioButton rbSearchModeBeacon;
    @BindView(R.id.follow_me_search_mode_dvb)
    CustomRadioButton rbSearchModeDvb;
    
    int []searchModeIds = {R.id.follow_me_search_mode_beacon,R.id.follow_me_search_mode_dvb};
    SparseIntArray searchModeArray = new SparseIntArray(searchModeIds.length);

    Context context;
    Unbinder unbinder;
    View view;

    public static GuideFragmentSearchModeSetting newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                             String secondLine, boolean showThird, String thirdLineStart,
                                                             int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        GuideFragmentSearchModeSetting fragment = new GuideFragmentSearchModeSetting();
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
        String[]action = {SearchModeAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        context = getContext();
        int i = 0;
        for (int id: searchModeIds){
            searchModeArray.put(i++, id);
        }
        Protocol.sendMessage(context,Protocol.cmdGetTrackMode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guide_search_mode, null);

        if (view == null){
            return null;
        }
        context = getContext();
        ButterKnife.bind(this, view);

        // 4.2.7.	寻星模式
        // 如果卫星只支持DVB，信标模式为灰色；如果卫星只支持信标模式，DVB模式为灰色。
        // 卫星是否支持信标模式和DVB模式，由数据库数据确定。
        // 数据库中，卫星没有水平和垂直、左旋和右旋，或者是信标频率为0，则不支持信标模式；
        // 如果DVB值为零或不在合理范围内，则不支持DVB模式。

        String defaultVal = "0";
        String beacon = CustomSP.getString(context, KEY_DESTINATION_SATELLITE_BEACON_FREQUENCY, defaultVal);
        String dvb = CustomSP.getString(context, KEY_DESTINATION_SATELLITE_DVB, defaultVal);
        String polarization = CustomSP.getString(context,KEY_DESTINATION_SATELLITE_POLARIZATION,defaultVal);
        String carrier = CustomSP.getString(context, KEY_DESTINATION_SATELLITE_DVB, defaultVal);

        if (beacon.equals(defaultVal) && dvb.equals(defaultVal)){
            rbSearchModeBeacon.setChecked(true);
        }else if (polarization.isEmpty() || beacon.equals(defaultVal)){
            // 数据库中，卫星没有水平和垂直、左旋和右旋，或者是信标频率为0，则不支持信标模式
            rbSearchModeBeacon.setClickable(false);
            rbSearchModeBeacon.setTextColor(Color.GRAY);
            rbSearchModeBeacon.setChecked(false);
            rbSearchModeDvb.setChecked(true);
        }else if (dvb.equals(defaultVal) || carrier.equals(defaultVal)){
            // 如果DVB的载波频率和符号率值为零，则不支持DVB模式。
            rbSearchModeDvb.setClickable(false);
            rbSearchModeDvb.setTextColor(Color.GRAY);
            rbSearchModeDvb.setChecked(false);
            rbSearchModeBeacon.setChecked(true);
        }
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = searchModeArray.indexOfKey(searchModeGroup.getCheckedRadioButtonId());
                CustomSP.putInt(getContext(),KEY_SEARCHING_MODE, pos);
                // 2017/11/11 设置寻星模式，发送命令
                Protocol.sendMessage(context,String.format(Protocol.cmdSetTrackMode, pos));
            }
        });

        int storedSearchMode = CustomSP.getInt(getContext(), KEY_SEARCHING_MODE, -1);
        if( storedSearchMode != -1){
            searchModeGroup.check(searchModeArray.get(storedSearchMode));
        }

        Context context = getContext();
        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(context);

        Bundle bundle = getArguments();
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);

            int icon = bundle.getInt(PopDialog.ICON, -1);
            if (icon == 0) {
//                popDialog.setDrawable(ImageUtil.bitmapToDrawable(
//                        ImageUtil.textAsBitmap(context,context.getString(
//                                R.string.triangle_string),
//                                ImageUtil.sp2px(context,24)))) ;
                popDialog.setButtonText(context, getString(R.string.setting_button_text));
            }
        }
        return popDialog.show();
    }

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        String rawData = intent.getStringExtra(SearchModeData);
        String trackMode = "0";
        Object[] objects = Sscanf.scan(rawData,Protocol.cmdGetTrackModeResult,trackMode);
        trackMode = (String) objects[0];
        searchModeGroup.check(searchModeArray.get(Integer.parseInt(trackMode)));
        
        view.invalidate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
