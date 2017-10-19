package com.edroplet.qxx.saneteltabactivity.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.LinearLayout;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

/**
 * Created by qxs on 2017/10/19.
 */

public class PopDialog {
    private Context context = null;
    private Bundle bundle= null;
    private View view = null;
    private boolean showInfo = false;
    private boolean showFirst = false;
    private boolean setFirstColor = false;
    private boolean setThirdColor = false;
    private boolean showSecond = false;
    private boolean showThird = false;
    private String first = null;
    private String second = null;
    private String thirdStart = null;
    private String thirdEnd = null;
    private Drawable drawable = null;
    private @ColorInt int firstLineColor = Color.RED;

    public PopDialog setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public PopDialog setDrawable(Drawable drawable) {
        this.drawable = drawable;
        return this;
    }

    public void setSetFirstColor(boolean setFirstColor) {
        this.setFirstColor = setFirstColor;
    }

    public void setSetThirdColor(boolean setFirstColor) {
        this.setFirstColor = setFirstColor;
    }

    public PopDialog setView(View view) {
        this.view = view;
        return this;
    }

    public PopDialog setContext(Context context) {
        this.context = context;
        return this;
    }

    public void setFirstLineColor(@ColorInt int firstLineColor) {
        this.firstLineColor = firstLineColor;
    }

    public PopDialog(){}

    public PopDialog(View view, Context context, Bundle bundle, boolean showInfo, boolean showFirst,
                     boolean showSecond, boolean showThird, String first, String second,
                     String start, Drawable drawable, String end){
        this.view = view;
        this.bundle = bundle;
        this.showInfo = showInfo;
        this.showFirst = showFirst;
        this.showSecond = showSecond;
        this.showThird = showThird;
        this.first = first;
        this.second = second;
        this.thirdStart = start;
        this.thirdEnd = end;
        this.drawable = drawable;
        this.context = context;
    }

    public View show(){
        if (bundle != null){
            showInfo = bundle.getBoolean("showInfo", false);
            showFirst = bundle.getBoolean("showFirst", false);
            showSecond = bundle.getBoolean("showSecond", false);
            showThird = bundle.getBoolean("showThird", false);
            first = bundle.getString("first", null);
            second = bundle.getString("second", null);
            thirdStart = bundle.getString("start", null);
            thirdEnd = bundle.getString("end", null);
        }

        if (view != null) {
            CustomTextView firstLine = view.findViewById(R.id.pop_dialog_tv_first);
            // 是否显示卫星信息
            if (showInfo){
                view.findViewById(R.id.follow_me_searching_satellite_info).setVisibility(View.VISIBLE);
            }else{
                view.findViewById(R.id.follow_me_searching_satellite_info).setVisibility(View.GONE);
            }
            if (showFirst) {
                if (first != null && first.length() > 0) {
                    firstLine.setText(first);
                    if (setFirstColor) {
                        firstLine.setTextColor(firstLineColor);
                    }
                    firstLine.setVisibility(View.VISIBLE);
                }
            } else {
                firstLine.setLayoutParams(new LinearLayout.LayoutParams(0,0,0));
                // firstLine.setVisibility(View.INVISIBLE);
            }

            CustomTextView secondLine = view.findViewById(R.id.pop_dialog_tv_second);
            if (showSecond) {
                if (second != null && second.length() > 0) {
                    secondLine.setText(second);
                    secondLine.setVisibility(View.VISIBLE);
                }
            } else {
                secondLine.setVisibility(View.GONE);
            }

            LinearLayout ll = view.findViewById(R.id.pop_dialog_third);
            if (showThird) {
                ll.setVisibility(View.VISIBLE);
                CustomTextView thirdStartTextView = view.findViewById(R.id.pop_dialog_tv_third_start);
                if (thirdStart != null && thirdStart.length() > 0) {
                    thirdStartTextView.setText(thirdStart);
                    if (setThirdColor){
                        thirdStartTextView.setTextColor(firstLineColor);
                    }
                    thirdStartTextView.setVisibility(View.VISIBLE);
                } else {
                    thirdStartTextView.setVisibility(View.GONE);
                }
                StatusButton thirdButton = view.findViewById(R.id.pop_dialog_third_button);
                if (drawable != null) {
                    thirdButton.setCompoundDrawables(drawable,
                            null,null,null);
                    thirdButton.setVisibility(View.VISIBLE);
                } else {
                    thirdButton.setVisibility(View.GONE);
                }

                CustomTextView thirdEndTextView = view.findViewById(R.id.pop_dialog_tv_third_end);
                if (thirdEnd != null && thirdEnd.length() > 0) {
                    thirdEndTextView.setText(thirdEnd);
                    if (setThirdColor){
                        thirdEndTextView.setTextColor(firstLineColor);
                    }
                    thirdEndTextView.setVisibility(View.VISIBLE);
                } else {
                    thirdEndTextView.setVisibility(View.GONE);
                }
            } else {
                // ll.setLayoutParams(new LinearLayout.LayoutParams(0,0,0));
                ll.setVisibility(View.GONE);
            }
        }
        return view;
    }

}
