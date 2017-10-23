package com.edroplet.qxx.saneteltabactivity.fragments.guide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.guide.GuideExplodeActivity;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

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
        args.putBoolean(PopDialog.SHOWFIRST,showFirst);
        args.putString(PopDialog.FIRST, firstLine);
        args.putBoolean(PopDialog.SHOWSECOND,showSecond);
        args.putString(PopDialog.SECOND, secondLine);
        args.putBoolean(PopDialog.SHOWTHIRD,showThird);
        args.putString(PopDialog.START, thirdLineStart);
        args.putInt("icon", icon);
        args.putString(PopDialog.BUTTONTEXT, buttonText);
        args.putString(PopDialog.END, thirdLineEnd);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_follow_me_explode, null);
        if (view == null){
            return null;
        }
        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);

            int icon = bundle.getInt("icon", -1);
            if (icon >= 0) {
                popDialog.setDrawable(ContextCompat.getDrawable(getContext(),icons[icon]));
            }
        }
        return popDialog.show();
    }
}
