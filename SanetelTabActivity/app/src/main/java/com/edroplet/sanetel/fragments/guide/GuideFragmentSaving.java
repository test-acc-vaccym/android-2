package com.edroplet.sanetel.fragments.guide;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.LockerInfo;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.view.TimerFragment;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomTextView;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 节能
 */

public class GuideFragmentSaving extends TimerFragment {
    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindString(R.string.follow_me_saving_first_line)
    String first;
    @BindString(R.string.follow_me_saving_second_line)
    String second;
    @BindString(R.string.follow_me_saving_third_start)
    String start;
    @BindString(R.string.follow_me_saving_third_end)
    String end;

    @BindString(R.string.saving_open_button)
    String open;
    @BindString(R.string.saving_close_button)
    String close;

    @BindView(R.id.pop_dialog_tv_first)
    CustomTextView tvFirst;

    @BindArray(R.array.energy_state_array)
    String[] energyStateArray;
    @BindArray(R.array.energy_setting_button_array)
    String[] energyButtonTextArray;

    int[] sendState = {1,0};

    Unbinder unbinder;
    Context context;
    int energyState;

    public static GuideFragmentSaving newInstance() {
        GuideFragmentSaving fragment = new GuideFragmentSaving();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_guide_saving, null);
        if (view == null){
            return null;
        }
        context = getContext();
        unbinder = ButterKnife.bind(this, view);

        energyState = LockerInfo.getLockerState(context);

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // 2017/11/11  设置节能
            // send command
            Protocol.sendMessage(context, String.format(Protocol.cmdSetEnergySave, sendState[energyState]));
            }
        });

        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(context);

        Bundle bundle = getBundle();

        popDialog.setBundle(bundle);
        popDialog.setSetFirstColor(true);
        popDialog.setButtonText(context, energyButtonTextArray[energyState]);

        return popDialog.show();
    }

    @Override
    public void doTimer() {
        super.doTimer();
        energyState = LockerInfo.getLockerState(context);
        tvFirst.setText(String.format(first, energyStateArray[energyState]));
        thirdButton.setText(energyButtonTextArray[energyState]);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }

    Bundle getBundle(){
        Bundle args = new Bundle();
        args.putBoolean(PopDialog.SHOW_FIRST,true);
        args.putString(PopDialog.FIRST, String.format(first, energyStateArray[energyState]));
        args.putBoolean(PopDialog.SHOW_SECOND,true);
        args.putString(PopDialog.SECOND, second);
        args.putBoolean(PopDialog.SHOW_THIRD,true);
        args.putString(PopDialog.START, start);
        args.putString(PopDialog.BUTTON_TEXT, null);
        args.putString(PopDialog.END, end);

        return args;
    }
}
