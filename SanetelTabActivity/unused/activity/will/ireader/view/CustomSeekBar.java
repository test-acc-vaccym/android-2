package com.will.ireader.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

/**
 * Created by qxs on 2017/11/2.
 */

public class CustomSeekBar extends AppCompatSeekBar {
    public CustomSeekBar(Context context) {
        super(context);
    }
    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    Drawable mThumb;
    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
        mThumb = thumb;
    }
    public Drawable getSeekBarThumb() {
        return mThumb;
    }

}
