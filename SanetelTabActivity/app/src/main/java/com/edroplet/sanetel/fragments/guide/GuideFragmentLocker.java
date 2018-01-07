package com.edroplet.sanetel.fragments.guide;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 锁紧操作
 */

public class GuideFragmentLocker extends TimerFragment {
    public static GuideFragmentLocker newInstance() {
        return new GuideFragmentLocker();
    }

    @BindArray(R.array.locker_state_array)
    String[] lockerStateArray;
    int lockerState;

    @BindView(R.id.pop_dialog_tv_first)
    CustomTextView tvFirst;

    Unbinder unbinder;
    Context context;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guide_locker, null);
        if (view == null){
            return null;
        }
        context = getContext();
        unbinder = ButterKnife.bind(this, view);

        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(getContext());
        lockerState = LockerInfo.getLockerState(getContext());
        Bundle bundle = getBundle(true, String.format(getString(R.string.follow_me_locker_lock_first_line), lockerStateArray[lockerState]) ,
                true, getString(R.string.follow_me_locker_lock_second_line),
                true, getString(R.string.follow_me_locker_lock_third_start), -1, null, null);
        popDialog.setBundle(bundle);
        popDialog.setSetFirstColor(true);

        return popDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }

    @Override
    public void doTimer() {
        super.doTimer();
        lockerState = LockerInfo.getLockerState(getContext());
        tvFirst.setText(String.format(getString(R.string.follow_me_locker_lock_first_line), lockerStateArray[lockerState]));
        view.invalidate();
    }

    Bundle  getBundle(boolean showFirst, String firstLine, boolean showSecond,
                      String secondLine, boolean showThird, String thirdLineStart,
                      int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
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
}
