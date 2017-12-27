package com.edroplet.qxx.saneteltabactivity.fragments.guide;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentDestination.KEY_DESTINATION_SATELLITE_BEACON_FREQUENCY;
import static com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentDestination.KEY_DESTINATION_SATELLITE_DVB;
import static com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentDestination.KEY_DESTINATION_SATELLITE_POLARIZATION;
import static com.edroplet.qxx.saneteltabactivity.utils.CustomSP.KEY_SEARCHING_MODE;

/**
 * Created by qxs on 2017/9/19.
 */

public class GuideFragmentSearchModeSetting extends Fragment {
    public static final int Mode_Beacon = 0;
    public static final int Mode_DVB = 1;

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.follow_me_search_mode_beacon)
    CustomRadioButton crbSearchModeBeacon;

    @BindView(R.id.follow_me_search_mode_dvb)
    CustomRadioButton crbSearchModeDvb;

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
    Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_follow_me_search_mode, null);

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
            crbSearchModeBeacon.setChecked(true);
        }else if (polarization.isEmpty() || beacon.equals(defaultVal)){
            // 数据库中，卫星没有水平和垂直、左旋和右旋，或者是信标频率为0，则不支持信标模式
            crbSearchModeBeacon.setClickable(false);
            crbSearchModeBeacon.setTextColor(Color.GRAY);
            crbSearchModeBeacon.setChecked(false);
            crbSearchModeDvb.setChecked(true);
        }else if (dvb.equals(defaultVal) || carrier.equals(defaultVal)){
            // 如果DVB的载波频率和符号率值为零，则不支持DVB模式。
            crbSearchModeDvb.setClickable(false);
            crbSearchModeDvb.setTextColor(Color.GRAY);
            crbSearchModeDvb.setChecked(false);
            crbSearchModeBeacon.setChecked(true);
        }
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/11/11 设置寻星模式，发送命令
                if (crbSearchModeBeacon.isChecked()){
                    CustomSP.putInt(getContext(),KEY_SEARCHING_MODE, Mode_Beacon);
                } else {
                    CustomSP.putInt(getContext(), KEY_SEARCHING_MODE, Mode_DVB);
                }
            }
        });

        // 单选组的选择互斥
        crbSearchModeBeacon.setOnCheckedChangeListener(GuideFragmentLocation.mOnCheckedChangeListener);
        crbSearchModeDvb.setOnCheckedChangeListener(GuideFragmentLocation.mOnCheckedChangeListener);

        if(CustomSP.getInt(getContext(), KEY_SEARCHING_MODE, Mode_Beacon) == Mode_Beacon){
            crbSearchModeBeacon.setChecked(true);
        }else {
            crbSearchModeDvb.setChecked(true);
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
}
