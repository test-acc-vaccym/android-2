package com.edroplet.qxx.saneteltabactivity.fragments.guide;

import android.content.Context;
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

/**
 * Created by qxs on 2017/9/19.
 */

public class GuideFragmentSearchModeSetting extends Fragment {

    public static final String KEY_SEARCHING_MODE = "KEY_SEARCHING_MODE";

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_follow_me_search_mode, null);

        if (view == null){
            return null;
        }
        ButterKnife.bind(this, view);

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/11/11 设置寻星模式，发送命令
                if (crbSearchModeBeacon.isChecked()){
                    CustomSP.putInt(getContext(),KEY_SEARCHING_MODE, 0);
                } else {
                    CustomSP.putInt(getContext(), KEY_SEARCHING_MODE, 1);
                }

            }
        });

        // 单选组的选择互斥
        crbSearchModeBeacon.setOnCheckedChangeListener(GuideFragmentLocation.mOnCheckedChangeListener);
        crbSearchModeDvb.setOnCheckedChangeListener(GuideFragmentLocation.mOnCheckedChangeListener);

        if(CustomSP.getInt(getContext(), KEY_SEARCHING_MODE, 0) == 0){
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
