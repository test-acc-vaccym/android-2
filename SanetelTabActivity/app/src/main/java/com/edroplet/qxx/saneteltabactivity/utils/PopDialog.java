package com.edroplet.qxx.saneteltabactivity.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
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
    private String buttonText = null;

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public void setSetFirstColor(boolean setFirstColor) {
        this.setFirstColor = setFirstColor;
    }

    public void setSetThirdColor(boolean setFirstColor) {
        this.setFirstColor = setFirstColor;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setFirstLineColor(@ColorInt int firstLineColor) {
        this.firstLineColor = firstLineColor;
    }

    public void setButtonText(Context context, String buttonText) {
        this.context = context;
        this.buttonText = buttonText;
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
        if (this.bundle != null){
            this.showInfo = bundle.getBoolean("showInfo", false);
            this.showFirst = bundle.getBoolean("showFirst", false);
            this.showSecond = bundle.getBoolean("showSecond", false);
            this.showThird = bundle.getBoolean("showThird", false);
            this.first = bundle.getString("first", null);
            this.second = bundle.getString("second", null);
            this.thirdStart = bundle.getString("start", null);
            this.thirdEnd = bundle.getString("end", null);
            this.buttonText = bundle.getString("buttonText",this.buttonText);
        }

        if (this.view != null) {
            CustomTextView firstLine = this.view.findViewById(R.id.pop_dialog_tv_first);
            // 是否显示卫星信息
            LinearLayout satelliteInfo = this.view.findViewById(R.id.follow_me_searching_satellite_info);
            if (satelliteInfo != null) {
                if (this.showInfo) {
                    satelliteInfo.setVisibility(View.VISIBLE);
                } else {
                    satelliteInfo.setVisibility(View.GONE);
                }
            }
            if (this.showFirst) {
                if (this.first != null && this.first.length() > 0) {
                    firstLine.setText(first);
                    if (this.setFirstColor) {
                        firstLine.setTextColor(this.firstLineColor);
                    }
                    firstLine.setVisibility(View.VISIBLE);
                }
            } else {
                firstLine.setLayoutParams(new LinearLayout.LayoutParams(0,0,0));
                // firstLine.setVisibility(View.INVISIBLE);
            }

            CustomTextView secondLine = this.view.findViewById(R.id.pop_dialog_tv_second);
            if (this.showSecond) {
                if (this.second != null && this.second.length() > 0) {
                    secondLine.setText(this.second);
                    secondLine.setVisibility(View.VISIBLE);
                }
            } else {
                secondLine.setVisibility(View.GONE);
            }

            LinearLayout ll = this.view.findViewById(R.id.pop_dialog_third);
            if (this.showThird) {
                ll.setVisibility(View.VISIBLE);
                CustomTextView thirdStartTextView = this.view.findViewById(R.id.pop_dialog_tv_third_start);
                if (this.thirdStart != null && this.thirdStart.length() > 0) {
                    thirdStartTextView.setText(this.thirdStart);
                    if (this.setThirdColor){
                        thirdStartTextView.setTextColor(this.firstLineColor);
                    }
                    thirdStartTextView.setVisibility(View.VISIBLE);
                } else {
                    thirdStartTextView.setVisibility(View.GONE);
                }
                CustomButton thirdButton = this.view.findViewById(R.id.pop_dialog_third_button);
                if (this.buttonText != null || this.drawable != null) {
                    if (this.drawable != null) {
                        this.drawable.setBounds(0, 0, this.drawable.getMinimumWidth(), this.drawable.getMinimumHeight());
                    }
                    thirdButton.setCompoundDrawables(this.drawable,
                            null,null,null);
                    thirdButton.setText(this.buttonText);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        thirdButton.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Large);
                    else {
                        thirdButton.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Large);
                    }
                    thirdButton.setTextColor(ContextCompat.getColor(context, R.color.button_text));
                    thirdButton.setVisibility(View.VISIBLE);
                } else {
                    thirdButton.setVisibility(View.GONE);
                }

                CustomTextView thirdEndTextView = this.view.findViewById(R.id.pop_dialog_tv_third_end);
                if (this.thirdEnd != null && this.thirdEnd.length() > 0) {
                    thirdEndTextView.setText(this.thirdEnd);
                    if (this.setThirdColor){
                        thirdEndTextView.setTextColor(this.firstLineColor);
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
        return this.view;
    }

}
