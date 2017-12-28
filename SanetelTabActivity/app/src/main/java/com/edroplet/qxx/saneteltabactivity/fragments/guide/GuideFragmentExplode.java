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
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;
import com.edroplet.qxx.saneteltabactivity.beans.Protocol;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qxs on 2017/9/19.
 */

public class GuideFragmentExplode extends Fragment {
    private int[] icons = {R.drawable.antenna_exploded, R.drawable.park, R.drawable.searching, R.drawable.recycle, R.drawable.folder};
    public static GuideFragmentExplode newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                   String secondLine, boolean showThird, String thirdLineStart,
                                                   int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        GuideFragmentExplode fragment = new GuideFragmentExplode();
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

    int antennaState = AntennaInfo.AntennaStatus.INIT;
     Context ctx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_guide_explode, null);
        if (view == null){
            return null;
        }
        ButterKnife.bind(this, view);
        ctx = getContext();

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 及时获取
                antennaState = AntennaInfo.getAntennaState(ctx);
                // send command
                if (antennaState == AntennaInfo.AntennaStatus.EXPLODED){
                    Protocol.sendMessage(ctx,Protocol.cmdAntennaExplode);
                }
            }
        });
        // TODO: 2017/11/11 获取天线状态
        antennaState = AntennaInfo.getAntennaState(ctx);
        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);

            int icon = bundle.getInt(PopDialog.ICON, -1);
            if (icon >= 0) {
                popDialog.setDrawable(ContextCompat.getDrawable(getContext(),icons[icon]));
            }
        }
        return popDialog.show();
    }
}
