package com.edroplet.qxx.saneteltabactivity.view.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.edroplet.qxx.saneteltabactivity.utils.ChangeTypeFace;

/**
 * Created by qxs on 2017/9/21.
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
    }
}
