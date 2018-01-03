package com.edroplet.sanetel.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomTextView;

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
    private boolean showForth = false;

    private String first = null;
    private String second = null;
    private String thirdStart = null;
    private String thirdEnd = null;
    private Drawable drawable = null;
    private String forth = null;

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

    public PopDialog(Context context){
        this.context = context;
    }
    // 3排数据
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

    // 四排数据
    public PopDialog(View view, Context context, Bundle bundle, boolean showInfo, boolean showFirst,
                     boolean showSecond, boolean showThird, String first, String second,
                     String start, Drawable drawable, String end, boolean showForth, String forth){
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
        this.showForth = showForth;
        this.forth = forth;
    }

    public static final String START = "start";
    public static final String FIRST = "first";
    public static final String SECOND = "second";
    public static final String END = "end";
    public static final String ICON = "icon";
    public static final String BUTTON_TEXT = "buttonText";
    public static final String SHOW_INFO = "showInfo";
    public static final String SHOW_FIRST = "showFirst";
    public static final String SHOW_SECOND = "showSecond";
    public static final String SHOW_THIRD = "showThird";
    public static final String SHOW_FORTH = "showForth";
    public static final String FORTH = "forth";

    public View show(){
        if (this.bundle != null){
            this.showInfo = bundle.getBoolean(SHOW_INFO, false);
            this.showFirst = bundle.getBoolean(SHOW_FIRST, false);
            this.showSecond = bundle.getBoolean(SHOW_SECOND, false);
            this.showThird = bundle.getBoolean(SHOW_THIRD, false);
            this.showForth = bundle.getBoolean(SHOW_FORTH, false);

            this.first = bundle.getString(FIRST, null);
            this.second = bundle.getString(SECOND, null);
            this.thirdStart = bundle.getString(START, null);
            this.thirdEnd = bundle.getString(END, null);
            this.buttonText = bundle.getString(BUTTON_TEXT,this.buttonText);
            this.forth = bundle.getString(FORTH, null);
        }

        if (this.view != null) {
            CustomTextView firstLine = this.view.findViewById(R.id.pop_dialog_tv_first);
            // 是否显示卫星信息
            LinearLayout satelliteInfo = this.view.findViewById(R.id.follow_me_searching_antenna_info);
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
                    /*
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        thirdButton.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Large);
                    else {
                        thirdButton.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Large);
                    }
                    */
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

            // 第四行
            CustomTextView forthTextView = this.view.findViewById(R.id.pop_dialog_tv_forth);
            if (forthTextView != null && this.forth != null && this.forth.length() > 0) {
                forthTextView.setText(this.forth);
                if (this.setThirdColor){
                    forthTextView.setTextColor(this.firstLineColor);
                }
                forthTextView.setVisibility(View.VISIBLE);
            } else if (forthTextView != null){
                forthTextView.setVisibility(View.GONE);
            }

        }
        return this.view;
    }

}
