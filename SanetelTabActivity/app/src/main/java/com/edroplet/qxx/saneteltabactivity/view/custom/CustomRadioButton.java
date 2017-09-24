package com.edroplet.qxx.saneteltabactivity.view.custom;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.utils.ChangeTypeFace;

/**
 * Created by qxs on 2017/9/21.
 */

public class CustomRadioButton extends AppCompatRadioButton {
    public CustomRadioButton(Context context, AttributeSet attributeSet, int defStyle){
        super(context,attributeSet,defStyle);
        init();
    }
    public CustomRadioButton(Context context){
        super(context);
        init();
    }
    public CustomRadioButton(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }
    private void init() {
        if (!isInEditMode()) {
            setTypeface(ChangeTypeFace.getSimHei(this.getContext()));
        }
    }
    public void toggleChecked(){
        if (isChecked()){
            setChecked(false);
        }else {
            setChecked(true);
        }
    }
}
