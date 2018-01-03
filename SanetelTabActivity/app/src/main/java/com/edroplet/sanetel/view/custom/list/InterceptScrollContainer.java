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
    private static final String TAG = "InterceptScrollContainer";

    public InterceptScrollContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public InterceptScrollContainer(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
     * 拦截TouchEvent
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        Log.i(TAG,"onInterceptTouchEvent");
        return true;
        //return super.onInterceptTouchEvent(ev);
    }
}
