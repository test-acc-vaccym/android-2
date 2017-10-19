package com.edroplet.qxx.saneteltabactivity.fragments.guide;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.ImageUtil;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

/**
 * Created by qxs on 2017/9/19.
 */

public class GuideFragmentLocation extends Fragment {
    public static GuideFragmentLocation newInstance(boolean showFirst, String firstLine, boolean showSecond, String secondLine, boolean showThird, String thirdLineStart, int icon, String thirdLineEnd) {
        Bundle args = new Bundle();
        GuideFragmentLocation fragment = new GuideFragmentLocation();
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
        final View view = inflater.inflate(R.layout.fragment_follow_me_location, null);
        Context context = getContext();
        // 位置输入
        // 泡泡弹框
        CustomTextView firstLine = view.findViewById(R.id.pop_dialog_tv_first);
        Bundle bundle = getArguments();
        if (bundle != null) {
            boolean showFirst = getArguments().getBoolean("showFirst", false);
            boolean showSecond = getArguments().getBoolean("showSecond", false);
            boolean showThird = getArguments().getBoolean("showThird", false);
            if (showFirst) {
                String first = getArguments().getString("first", null);
                if (first != null && first.length() > 0) {
                    firstLine.setText(first);
                    firstLine.setTextColor(Color.RED);
                    firstLine.setVisibility(View.VISIBLE);
                }
            } else {
                // firstLine.setLayoutParams(new LinearLayout.LayoutParams(0,0,0));
                // 不占位
                firstLine.setVisibility(View.GONE);
            }

            CustomTextView secondLine = view.findViewById(R.id.pop_dialog_tv_second);
            if (showSecond) {
                String second = getArguments().getString("second", null);
                if (second != null && second.length() > 0) {
                    secondLine.setText(second);
                }
            } else {
                secondLine.setLayoutParams(new LinearLayout.LayoutParams(0,0,0));
            }

            if (showThird) {
                CustomTextView thirdStart = view.findViewById(R.id.pop_dialog_tv_third_start);
                String start = getArguments().getString("start", null);
                if (start != null && start.length() > 0) {
                    thirdStart.setText(start);
                    thirdStart.setVisibility(View.VISIBLE);
                } else {
                    thirdStart.setVisibility(View.GONE);
                }

                StatusButton thirdButton = view.findViewById(R.id.pop_dialog_third_button);
                int icon = getArguments().getInt("icon", -1);
                if (icon >= 0) {
                    thirdButton.setCompoundDrawables((ImageUtil.bitmapToDrawable(
                            ImageUtil.textAsBitmap(context,context.getString(
                                    R.string.setting_string_button),
                                    ImageUtil.sp2px(context,24)))) ,
                            null,null,null);
                    thirdButton.setVisibility(View.VISIBLE);
                } else {
                    thirdButton.setVisibility(View.GONE);
                }


                CustomTextView thirdEnd = view.findViewById(R.id.pop_dialog_tv_third_end);
                String end = getArguments().getString("end", null);
                if (end != null && end.length() > 0) {
                    thirdEnd.setText(end);
                    thirdEnd.setVisibility(View.VISIBLE);
                } else {
                    thirdEnd.setVisibility(View.GONE);
                }
            } else {
                LinearLayout ll = view.findViewById(R.id.pop_dialog_third);
                ll.setLayoutParams(new LinearLayout.LayoutParams(0,0,0));
                // ll.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }
}
