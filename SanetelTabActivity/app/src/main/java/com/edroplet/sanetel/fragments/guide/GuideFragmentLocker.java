package com.edroplet.sanetel.fragments.guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.view.custom.CustomButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qxs on 2017/9/19.
 */

public class GuideFragmentLocker extends Fragment {
    public static GuideFragmentLocker newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                  String secondLine, boolean showThird, String thirdLineStart,
                                                  int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        GuideFragmentLocker fragment = new GuideFragmentLocker();
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

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_guide_locker, null);
        if (view == null){
            return null;
        }
        ButterKnife.bind(this, view);

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/11/11  设置锁紧
                // send command
            }
        });

        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(getContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);
        }
        return popDialog.show();
    }
}
