package com.edroplet.sanetel.view.custom.list;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by qxs on 2017/11/8.
 * 容器拦截TouchEvent
 */

public class InterceptScrollContainer extends LinearLayout {
    private static final String TAG = "InterceptScroll";

    public InterceptScrollContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptScrollContainer(Context context) {
        super(context);
    }

    /* (non-Javadoc)
     * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
     * 拦截TouchEvent
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG,"onInterceptTouchEvent");
        return true;
        //return super.onInterceptTouchEvent(ev);
    }
}
