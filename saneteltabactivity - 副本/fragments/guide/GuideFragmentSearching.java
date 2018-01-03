package com.edroplet.qxx.saneteltabactivity.fragments.guide;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.edroplet.qxx.saneteltabactivity.fragments.functions.manual.LocationControlFragment.KEY_PREPARE_AZIMUTH;
import static com.edroplet.qxx.saneteltabactivity.fragments.functions.manual.LocationControlFragment.KEY_PREPARE_PITCH;
import static com.edroplet.qxx.saneteltabactivity.fragments.functions.manual.LocationControlFragment.KEY_PREPARE_POLARIZATION;

/**
 * Created by qxs on 2017/9/19.
 */

public class GuideFragmentSearching extends Fragment {

    // 寻星中
    //case 1:

    // 锁星
    //case 2:

    // 重设参数
    //case 3:

    // 故障报告
    //case 4:


    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;
    
    public static GuideFragmentSearching newInstance(boolean showInfo,boolean showFirst, String firstLine,
                                                     boolean showSecond, String secondLine, boolean showThird,
                                                     String thirdLineStart, int icon, String buttonText, String thirdLineEnd) {

        GuideFragmentSearching fragment = new GuideFragmentSearching();
        fragment.setArguments(setPopupDialogBundle(showInfo,showFirst,firstLine,
                showSecond,secondLine,showThird,thirdLineStart,icon,buttonText,thirdLineEnd));
        return fragment;
    }

    public static Bundle setPopupDialogBundle(boolean showInfo,boolean showFirst, String firstLine,
                                       boolean showSecond, String secondLine, boolean showThird,
                                       String thirdLineStart, int icon, String buttonText, String thirdLineEnd){
        Bundle args = new Bundle();
        args.putBoolean(PopDialog.SHOW_INFO,showInfo);
        args.putBoolean(PopDialog.SHOW_FIRST,showFirst);
        args.putString(PopDialog.FIRST, firstLine);
        args.putBoolean(PopDialog.SHOW_SECOND,showSecond);
        args.putString(PopDialog.SECOND, secondLine);
        args.putBoolean(PopDialog.SHOW_THIRD,showThird);
        args.putString(PopDialog.START, thirdLineStart);
        args.putInt(PopDialog.ICON, icon);
        args.putString(PopDialog.BUTTON_TEXT, buttonText);
        args.putString(PopDialog.END, thirdLineEnd);
        return args;
    }

    @BindView(R.id.follow_me_searching_antenna_info)
    LinearLayout linearLayoutAntennaInfo;

    @BindView(R.id.antenna_info_tv_prepare_azimuth)
    CustomTextView prepareAzmiuth;
    @BindView(R.id.antenna_info_tv_prepare_pitch)
    CustomTextView preparePitch;
    @BindView(R.id.antenna_info_tv_prepare_polarization)
    CustomTextView preparePolarization;
    @BindView(R.id.antenna_info_tv_azimuth)
    CustomTextView azimuth;
    @BindView(R.id.antenna_info_tv_pitch)
    CustomTextView pitch;
    @BindView(R.id.antenna_info_tv_polarization)
    CustomTextView polarization;

    Context context;
    private  static final int  STATE_READY = 0;
    private  static final int  STATE_SEARCHING = 1;
    private  static final int  STATE_TIMEOUT = 2;
    private  static final int  STATE_ERROR = 3;
    private  static final int  STATE_COMPLETE= 4;

    //用 @IntDef "包住" 常量；
    // @Retention 定义策略
    // 声明构造器
    @IntDef({STATE_READY, STATE_SEARCHING, STATE_TIMEOUT, STATE_ERROR, STATE_COMPLETE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SearchingState{}

    private @SearchingState int state = STATE_READY;

    PopDialog popDialog = new PopDialog();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_guide_searching, null);
        if (view == null){
            return null;
        }
        ButterKnife.bind(this, view);
        context = getContext();

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/11/11  开始寻星
                // 首先让天线信息可见
                linearLayoutAntennaInfo.setVisibility(View.VISIBLE);
                // 更新信息
                // 设置预置角度信息
                String defaultVal="0.00";
                prepareAzmiuth.setText(CustomSP.getString(context, KEY_PREPARE_AZIMUTH,defaultVal));
                preparePitch.setText(CustomSP.getString(context, KEY_PREPARE_PITCH,defaultVal));
                preparePolarization.setText(CustomSP.getString(context, KEY_PREPARE_POLARIZATION,defaultVal));

                // 修改pop_dialog的提示信息
                switch (state){
                    case STATE_READY:
                        state = STATE_SEARCHING;
                        Bundle bundle = setPopupDialogBundle(true, true, getString(R.string.follow_me_searching_ing_first_line),
                                true, String.format(getString(R.string.follow_me_searching_ing_second_line), 0),
                                true, getString(R.string.follow_me_searching_ing_third_start), 2, null, null);
                        setPopupDialog(view, bundle);
                        popDialog.show();
                        // TODO: 2017/11/12 天线通信，获取超时,失败的状态
                        return;
                    case STATE_COMPLETE:
                        state = STATE_READY;
                        setPopupDialog(view, setPopupDialogBundle(true, true, getString(R.string.follow_me_searching_lock_first_line),
                                true, String.format(getString(R.string.follow_me_searching_lock_second_line),0) ,
                                true, getString(R.string.follow_me_searching_lock_third_start), -1, null, null));
                        popDialog.show();
                        return;
                    case STATE_SEARCHING:
                        // TODO: 2017/11/12  急停命令
                        return;
                    case STATE_TIMEOUT:
                        setPopupDialog(view, setPopupDialogBundle(true, true, getString(R.string.follow_me_searching_timeout_first_line),
                                true, String.format(getString(R.string.follow_me_searching_timeout_second_line),0),
                                true, getString(R.string.follow_me_searching_timeout_third_start), 3,
                                null, getString(R.string.follow_me_searching_timeout_third_end)));
                        popDialog.show();
                        return;
                    case STATE_ERROR:
                        setPopupDialog(view, setPopupDialogBundle(true, true, getString(R.string.follow_me_searching_error_first_line),
                                true, String.format(getString(R.string.follow_me_searching_error_second_line),0),
                                true, getString(R.string.follow_me_searching_error_third_start), 4, null, null));
                        popDialog.show();
                        return;

                }
            }
        });


        Bundle bundle = getArguments();
        setPopupDialog(view, bundle);
        return popDialog.show();
    }

    private void setPopupDialog(View view, Bundle bundle){
        popDialog.setView(view);
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);
            Context context = getContext();
            popDialog.setContext(context);
            int icon = bundle.getInt(PopDialog.ICON, -1);
            if (icon == 1) {
                popDialog.setButtonText(context, getString(R.string.follow_me_searching_third_button_start));
            } else if (icon == 2){
                popDialog.setButtonText(context, getString(R.string.follow_me_searching_third_button_stop));
            }else if (icon == 3) {
                popDialog.setButtonText(context, getString(R.string.return_string_button));
            }else if (icon == 4){
                // 故障
                popDialog.setSetThirdColor(true);
            }
        }
    }
}
