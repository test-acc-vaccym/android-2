package com.edroplet.sanetel.fragments.guide;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.AntennaInfo;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.view.TimerFragment;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomTextView;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 展开页面，只有一个设置展开
 */

public class GuideFragmentExplode extends TimerFragment {
    private int[] icons = {R.drawable.antenna_exploded, R.drawable.park, R.drawable.searching, R.drawable.recycle, R.drawable.folder};
    public static GuideFragmentExplode newInstance() {
        return new GuideFragmentExplode();
    }

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;
    @BindView(R.id.pop_dialog_tv_first)
    CustomTextView tvFirstLine;
    @BindView(R.id.pop_dialog_tv_second)
    CustomTextView tvSecondLine;
    @BindView(R.id.pop_dialog_tv_third_start)
    CustomTextView tvThirdStart;
    @BindView(R.id.pop_dialog_tv_third_end)
    CustomTextView tvThirdEnd;

    @BindView(R.id.pop_dialog_third)
    LinearLayout thirdLine;

    @BindArray(R.array.antenna_state_array)
    String[] antennaStateArray;

    int antennaState = AntennaInfo.AntennaSearchSatellitesStatus.INIT;
    Context ctx;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_guide_explode, null);
        if (view == null){
            return null;
        }
        unbinder = ButterKnife.bind(this, view);
        ctx = getContext();

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 及时获取
                antennaState = AntennaInfo.getAntennaState(ctx);
                // send command
                if (antennaState == AntennaInfo.AntennaSearchSatellitesStatus.EXPLODED){
                    Protocol.sendMessage(ctx,Protocol.cmdAntennaExplode);
                }
            }
        });
        // 2017/11/11 获取天线状态
        antennaState = AntennaInfo.getAntennaState(ctx);
        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(getContext());

        Bundle bundle = getBundles();
        popDialog.setBundle(bundle);
        popDialog.setSetFirstColor(true);

        int icon = bundle.getInt(PopDialog.ICON, -1);
        if (icon >= 0) {
            popDialog.setDrawable(ContextCompat.getDrawable(getContext(),icons[icon]));
        }
        return popDialog.show();
    }

    @Override
    public void doTimer() {
        super.doTimer();
        int antennaState = AntennaInfo.getAntennaState(getContext());
        tvFirstLine.setVisibility(View.VISIBLE);
        if (antennaState < antennaStateArray.length){
            tvFirstLine.setText(String.format(getString(R.string.follow_me_antenna_first_line), antennaStateArray[antennaState]));
        }
        tvSecondLine.setVisibility(View.GONE);
        thirdLine.setVisibility(View.GONE);
        switch (antennaState){
            case AntennaInfo.AntennaSearchSatellitesStatus.EXPLODED:
                tvFirstLine.setText(String.format(getString(R.string.follow_me_antenna_first_line), antennaStateArray[antennaState]));
                tvSecondLine.setVisibility(View.VISIBLE);
                tvSecondLine.setText(getString(R.string.follow_me_antenna_state_explode_second_line));
                thirdLine.setVisibility(View.VISIBLE);
                tvThirdStart.setText(getString(R.string.follow_me_antenna_state_explode_third_start));
                break;
            case AntennaInfo.AntennaSearchSatellitesStatus.FOLDED:
                thirdLine.setVisibility(View.VISIBLE);
                tvThirdStart.setText(getString(R.string.follow_me_message_click));
                tvThirdEnd.setText(getString(R.string.antenna_state_exploded));
                thirdButton.setCompoundDrawables(ContextCompat.getDrawable(getContext(),icons[0]),null,null,null);
                break;
            case AntennaInfo.AntennaSearchSatellitesStatus.EXPLODING:
                thirdLine.setVisibility(View.VISIBLE);
                tvThirdStart.setText(getString(R.string.follow_me_message_click));
                tvThirdEnd.setText(getString(R.string.antenna_state_paused));
                thirdButton.setCompoundDrawables(ContextCompat.getDrawable(getContext(),icons[1]),null,null,null);
                break;
            case AntennaInfo.AntennaSearchSatellitesStatus.ABNORMAL:
            case AntennaInfo.AntennaSearchSatellitesStatus.PAUSE:
            case AntennaInfo.AntennaSearchSatellitesStatus.SEARCHING:
            case AntennaInfo.AntennaSearchSatellitesStatus.RECYCLED:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }

    Bundle getBundles(){
        Bundle args = new Bundle();
        args.putBoolean(PopDialog.SHOW_FIRST,true);
        args.putString(PopDialog.FIRST, String.format(getString(R.string.follow_me_antenna_first_line), antennaStateArray[antennaState]));
        args.putBoolean(PopDialog.SHOW_SECOND,false);
        args.putString(PopDialog.SECOND, getString(R.string.follow_me_antenna_state_explode_second_line));
        args.putBoolean(PopDialog.SHOW_THIRD,false);
        args.putString(PopDialog.START, getString(R.string.follow_me_antenna_state_explode_third_start));
        args.putInt(PopDialog.ICON, -1);
        args.putString(PopDialog.BUTTON_TEXT, null);
        args.putString(PopDialog.END, null);
        return args;
    }
}
