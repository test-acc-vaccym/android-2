package com.edroplet.sanetel.view.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.edroplet.sanetel.utils.ChangeTypeFace;
import com.edroplet.sanetel.utils.InputFilterFloat;

/**
 * Created by qxs on 2017/9/21.
 * 自定义edit text控件
 */

public class CustomEditText extends AppCompatEditText {
    public CustomEditText(Context context, AttributeSet set, int defStyle){
        super(context,set,defStyle);
        init();
    }
    public CustomEditText(Context context, AttributeSet set){
        super(context,set);
        init();
    }
    public CustomEditText(Context context){
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setTypeface(ChangeTypeFace.getSimHei(this.getContext()));
        }
        // 3dp or 1.5倍
        setLineSpacing(3,1.3f);

    }

    public void setMinMax(final double min, final double max){
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    String s = getText().toString();
                    if (s == null || s.length() == 0) return;
                    Double d = Double.parseDouble(s);
                    if (d < min || d > max) {
                        setText("");
                    }
                }
            }
        });
    }

}
