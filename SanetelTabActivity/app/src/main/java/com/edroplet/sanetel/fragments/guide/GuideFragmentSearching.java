package com.edroplet.sanetel.fragments.guide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.activities.guide.GuideActivity;
import com.edroplet.sanetel.beans.AntennaInfo;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.view.TimerFragment;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomTextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.edroplet.sanetel.fragments.functions.manual.LocationControlFragment.KEY_PREPARE_AZIMUTH;
import static com.edroplet.sanetel.fragments.functions.manual.LocationControlFragment.KEY_PREPARE_PITCH;
import static com.edroplet.sanetel.fragments.functions.manual.LocationControlFragment.KEY_PREPARE_POLARIZATION;

/**
 * Created by qxs on 2017/9/19.
 * 寻星操作
 * 4.13.1	自动寻星指令
 * ……app中使用》3.5、寻星操作流程
 */

public class GuideFragmentSearching extends TimerFragment {

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

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;
    @BindView(R.id.pop_dialog_tv_first)
    CustomTextView firstLine;
    @BindView(R.id.pop_dialog_tv_second)
    CustomTextView secondLine;
    @BindView(R.id.pop_dialog_tv_third_start)
    CustomTextView thirdStart;
    @BindView(R.id.pop_dialog_tv_third_end)
    CustomTextView thirdEnd;

    @BindView(R.id.follow_me_searching_antenna_info)
    LinearLayout linearLayoutAntennaInfo;


    @BindView(R.id.antenna_info_tv_prepare_azimuth)
    CustomTextView prepareAzimuth;
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

    @BindView(R.id.tv_antenna_info)
    CustomTextView infoHead;

    @BindArray(R.array.guide_searching_state_button_array)
    String[] searchingButtonTextArray;

    Context context;
    Unbinder unbinder;

    private  static final int  STATE_READY = 0;
    private  static final int  STATE_SEARCHING = 1;
    private  static final int  STATE_TIMEOUT = 2;
    private  static final int  STATE_ERROR = 3;
    private  static final int  STATE_COMPLETE= 4;
    private  static final int  STATE_LOCKED= 5;

    SparseIntArray mapAntennaStateSearchingState = new SparseIntArray();

    //用 @IntDef "包住" 常量；
    // @Retention 定义策略
    // 声明构造器
    @IntDef({STATE_READY, STATE_SEARCHING, STATE_TIMEOUT, STATE_ERROR, STATE_COMPLETE, STATE_LOCKED})
    @Retention(RetentionPolicy.SOURCE)
    private @interface SearchingState{}

    private @SearchingState int state = STATE_READY;

    PopDialog popDialog = new PopDialog();

    View view;
    static int startTime = 0;

    CustomButton prevStep;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GuideActivity){
            GuideActivity activity = (GuideActivity)context;
            prevStep = (CustomButton) activity.findViewById(R.id.city_detail_save);
            assert prevStep != null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guide_searching, null);
        if (view == null){
            return null;
        }
        unbinder = ButterKnife.bind(this, view);
        context = getContext();
        mapAntennaStateSearchingState.put(AntennaInfo.AntennaSearchSatellitesStatus.FOLDED, STATE_READY);
        mapAntennaStateSearchingState.put(AntennaInfo.AntennaSearchSatellitesStatus.EXPLODING, STATE_SEARCHING);
        mapAntennaStateSearchingState.put(AntennaInfo.AntennaSearchSatellitesStatus.EXPLODED, STATE_COMPLETE);
        mapAntennaStateSearchingState.put(AntennaInfo.AntennaSearchSatellitesStatus.INIT, STATE_READY);
        mapAntennaStateSearchingState.put(AntennaInfo.AntennaSearchSatellitesStatus.SEARCHING, STATE_SEARCHING);
        mapAntennaStateSearchingState.put(AntennaInfo.AntennaSearchSatellitesStatus.MANUAL, STATE_SEARCHING);
        mapAntennaStateSearchingState.put(AntennaInfo.AntennaSearchSatellitesStatus.LOCKED, STATE_LOCKED);
        mapAntennaStateSearchingState.put(AntennaInfo.AntennaSearchSatellitesStatus.FOLDING, STATE_READY);
        mapAntennaStateSearchingState.put(AntennaInfo.AntennaSearchSatellitesStatus.LOST, STATE_TIMEOUT);
        mapAntennaStateSearchingState.put(AntennaInfo.AntennaSearchSatellitesStatus.ABNORMAL, STATE_ERROR);
        mapAntennaStateSearchingState.put(AntennaInfo.AntennaSearchSatellitesStatus.PAUSE, STATE_READY);
        mapAntennaStateSearchingState.put(AntennaInfo.AntennaSearchSatellitesStatus.RECYCLING, STATE_ERROR);
        mapAntennaStateSearchingState.put(AntennaInfo.AntennaSearchSatellitesStatus.RECYCLED, STATE_ERROR);

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thirdButton.getText().toString().equals(getString(R.string.follow_me_searching_third_button_start))) {
                    //  2017/11/11  发送命令开始寻星
                    Protocol.sendMessage(context, Protocol.cmdSetAutoSearch);
                    isSearching = true;
                }else if (thirdButton.getText().toString().equals(getString(R.string.follow_me_searching_third_button_stop))){
                    // 急停
                    Protocol.sendMessage(context, Protocol.cmdStopSearch);
                    isSearching = false;
                }else if (thirdButton.getText().toString().equals(getString(R.string.return_string_button))){
                    // 跳回
                    Intent intent = new Intent(getActivity(), GuideActivity.class);
                    Bundle bundle = new Bundle();
                    // 点击▲ 跳回位置输入，重设参数
                    prevStep.performClick();
                    prevStep.performClick();
                    /**
                    bundle.putInt(GuideActivity.POSITION, GuideActivity.GUIDE_PAGES_INDEX.INDEX_LOCATION.ordinal());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    isSearching = false;
                    startTime = 0;
                    getActivity().finish();
                     */
                }
                // 首先让天线信息可见
                linearLayoutAntennaInfo.setVisibility(View.VISIBLE);
                infoHead.setVisibility(View.GONE);
                // 更新信息
                // 设置预置角度信息
                String defaultVal="0.00";
                prepareAzimuth.setText(CustomSP.getString(context, KEY_PREPARE_AZIMUTH,defaultVal));
                preparePitch.setText(CustomSP.getString(context, KEY_PREPARE_PITCH,defaultVal));
                preparePolarization.setText(CustomSP.getString(context, KEY_PREPARE_POLARIZATION,defaultVal));

                // 修改pop_dialog的提示信息
                updatePopDialog();
            }
        });


        Bundle bundle = getArguments();
        setPopupDialog(view, bundle);

        return popDialog.show();
    }

    boolean isSearching = false;
    @Override
    public void doTimer() {
        super.doTimer();
        if (isSearching) {
            startTime += 1;
        }
        updatePopDialog();
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

    void updatePopDialog(){
        int antennaState = AntennaInfo.getAntennaState(context);
        state = mapAntennaStateSearchingState.get(antennaState);
        switch (state){
            case STATE_READY:
                linearLayoutAntennaInfo.setVisibility(View.GONE);
                infoHead.setVisibility(View.GONE);
                firstLine.setText(getString(R.string.follow_me_searching_first_line));
                secondLine.setText(getString(R.string.follow_me_searching_second_line));
                thirdStart.setText(getString(R.string.follow_me_searching_third_start));
                thirdButton.setText(searchingButtonTextArray[0]);
                break;

            case STATE_COMPLETE:
                // state = STATE_READY;
                linearLayoutAntennaInfo.setVisibility(View.VISIBLE);
                infoHead.setVisibility(View.GONE);
                firstLine.setText(getString(R.string.follow_me_searching_complete_first_line));
                secondLine.setText(String.format(getString(R.string.follow_me_searching_complete_second_line), startTime));
                thirdStart.setText(getString(R.string.follow_me_searching_complete_third_start));
                thirdButton.setText("");
                break;

            case STATE_SEARCHING:
                // 2017/11/12  急停命令
                // 4.13.5	停止寻星指令
                Protocol.sendMessage(context,Protocol.cmdStopSearch);

                linearLayoutAntennaInfo.setVisibility(View.VISIBLE);
                infoHead.setVisibility(View.GONE);
                firstLine.setText(getString(R.string.follow_me_searching_ing_first_line));
                secondLine.setText(String.format(getString(R.string.follow_me_searching_ing_second_line), startTime));
                thirdStart.setText(getString(R.string.follow_me_searching_ing_third_start));
                thirdButton.setText(searchingButtonTextArray[1]);
                break;

            case STATE_TIMEOUT:
                linearLayoutAntennaInfo.setVisibility(View.VISIBLE);
                infoHead.setVisibility(View.GONE);
                firstLine.setText(getString(R.string.follow_me_searching_timeout_first_line));
                secondLine.setText(String.format(getString(R.string.follow_me_searching_timeout_second_line), startTime));
                thirdStart.setText(getString(R.string.follow_me_searching_timeout_third_start));
                thirdButton.setText(searchingButtonTextArray[2]);
                thirdEnd.setText(getString(R.string.follow_me_searching_timeout_third_end));
                break;

            case STATE_ERROR:
                linearLayoutAntennaInfo.setVisibility(View.GONE);
                infoHead.setVisibility(View.GONE);
                firstLine.setText(getString(R.string.follow_me_searching_error_first_line));
                firstLine.setTextColor(Color.RED);
                secondLine.setText(String.format(getString(R.string.follow_me_searching_error_second_line), startTime));
                thirdButton.setText("");
                thirdStart.setText(getString(R.string.follow_me_searching_error_third_start));
                thirdEnd.setText(getString(R.string.follow_me_searching_error_third_end));
                break;
            case STATE_LOCKED:
                linearLayoutAntennaInfo.setVisibility(View.VISIBLE);
                infoHead.setVisibility(View.GONE);
                firstLine.setText(getString(R.string.follow_me_searching_lock_first_line));
                firstLine.setTextColor(Color.RED);
                secondLine.setText(String.format(getString(R.string.follow_me_searching_lock_second_line), startTime));
                thirdButton.setText("");
                thirdStart.setText(getString(R.string.follow_me_searching_lock_third_start));
                thirdEnd.setText("");
                break;

        }

        view.invalidate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }
}
