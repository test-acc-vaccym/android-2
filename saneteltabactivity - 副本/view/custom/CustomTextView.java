package com.edroplet.qxx.saneteltabactivity.view.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.edroplet.qxx.saneteltabactivity.utils.ChangeTypeFace;

/**
 * Created by qxs on 2017/9/21.
 */

public class CustomTextView extends AppCompatTextView {
    public CustomTextView(Context context, AttributeSet attributeSet, int defStyle){
        super(context,attributeSet,defStyle);
        init();
    }
    public CustomTextView(Context context){
        super(context);
        init();
    }
    public CustomTextView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }
    private void init() {
        if (!isInEditMode()) {
            setTypeface(ChangeTypeFace.getSimHei(this.getContext()));
        }
        // 3dp or 1.5倍
        setLineSpacing(3,1.5f);
    }
}
