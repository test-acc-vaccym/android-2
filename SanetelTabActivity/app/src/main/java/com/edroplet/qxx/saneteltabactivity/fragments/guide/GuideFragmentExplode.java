package com.edroplet.qxx.saneteltabactivity.fragments.guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

/**
 * Created by qxs on 2017/9/19.
 */

public class GuideFragmentExplode extends Fragment {
    private static
    int[] icons = {R.drawable.antenna_exploded, R.drawable.park, R.drawable.searching, R.drawable.recycle, R.drawable.folder};
    public static GuideFragmentExplode newInstance(boolean showFirst, String firstLine, boolean showSecond, String secondLine, boolean showThird, String thirdLineStart, int icon, String thirdLineEnd) {
        Bundle args = new Bundle();
        GuideFragmentExplode fragment = new GuideFragmentExplode();
        args.putBoolean("showFirst",showFirst);
        args.putString("first", firstLine);
        args.putBoolean("showSecond",showSecond);
        args.putString("second", secondLine);
        args.putBoolean("showThird",showThird);
        args.putString("start", thirdLineStart);
        args.putInt("icon", icon);
        args.putString("end", thirdLineEnd);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_follow_me_explode, null);
        CustomTextView firstLine = view.findViewById(R.id.pop_dialog_tv_first);
        boolean showFirst = getArguments().getBoolean("showFirst",false);
        boolean showSecond = getArguments().getBoolean("showSecond",false);
        boolean showThird = getArguments().getBoolean("showThird",false);
        if (showFirst) {
            String first = getArguments().getString("first", null);
            if (first != null && first.length() > 0) {
                firstLine.setText(first);
            }
        }else {
            // firstLine.setLayoutParams(new LinearLayoutCompat.LayoutParams(0,0,0));
            firstLine.setVisibility(View.INVISIBLE);
        }

        CustomTextView secondLine = view.findViewById(R.id.pop_dialog_tv_second);
        if (showSecond) {
            String second = getArguments().getString("second", null);
            if (second != null && second.length() > 0) {
                secondLine.setText(second);
            }
        }else {
            // secondLine.setLayoutParams(new LinearLayoutCompat.LayoutParams(0,0,0));
            secondLine.setVisibility(View.INVISIBLE);
        }

        if (showThird) {
            CustomTextView thirdStart = view.findViewById(R.id.pop_dialog_tv_third_start);
            String start = getArguments().getString("start", null);
            if (start != null && start.length() > 0) {
                thirdStart.setText(start);
            } else {
                // thirdStart.setLayoutParams(new LinearLayoutCompat.LayoutParams(0, 0, 0));
                thirdStart.setVisibility(View.INVISIBLE);
            }

            CustomButton thirdButton = view.findViewById(R.id.pop_dialog_btn_third);
            int icon = getArguments().getInt("icon", -1);
            if (icon > 0) {
                thirdButton.setBackgroundResource(icons[icon]);
            } else {
                // thirdButton.setLayoutParams(new LinearLayoutCompat.LayoutParams(0, 0, 0));
                thirdButton.setVisibility(View.INVISIBLE);
            }


            CustomTextView thirdEnd = view.findViewById(R.id.pop_dialog_tv_third_end);
            String end = getArguments().getString("end", null);
            if (end != null && end.length() > 0) {
                thirdEnd.setText(end);
            } else {
                // thirdEnd.setLayoutParams(new LinearLayoutCompat.LayoutParams(0, 0, 0));
                thirdEnd.setVisibility(View.INVISIBLE);
            }
        }else{
            LinearLayout ll = view.findViewById(R.id.pop_dialog_third);
            ll.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}
