package com.edroplet.qxx.saneteltabactivity.view.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.edroplet.qxx.saneteltabactivity.beans.FlowTagConfig;
import com.edroplet.qxx.saneteltabactivity.utils.ChangeTypeFace;

/**
 * Created by qxs on 2017/9/21.
 * http://blog.csdn.net/chenzheng8975/article/details/55511498
 */

public class CustomButton extends AppCompatButton {
    //    private int mLineSpacing;//行间距
    //    private int mTagSpacing;//各个标签之间的距离

    public CustomButton(Context context, AttributeSet set, int defStyle){
        super(context,set,defStyle);
        init(context,set,defStyle);
    }
    public CustomButton(Context context, AttributeSet set){
        super(context,set);
        init(context,set, 0);
    }
    public CustomButton(Context context){
        super(context);
        init(context, null, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        if (!isInEditMode()) {
            setTypeface(ChangeTypeFace.getSimHei(this.getContext()));
        }
        //获取属性
        //        FlowTagConfig config = new FlowTagConfig(context, attrs);
        //        mLineSpacing = config.getLineSpacing();
        //        mTagSpacing = config.getTagSpacing();
    }
}
