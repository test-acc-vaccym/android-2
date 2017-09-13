package com.sanetel.control.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qxs on 2017/8/15.
 *     class desc: 引导页面适配器
 */

public class WelcomePageAdapter extends PagerAdapter {

    // 界面列表
    private List<View> views;
    private AppCompatActivity activity;

    public WelcomePageAdapter(List<View> views, AppCompatActivity activity){
        this.views = views;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
        container.removeView(views.get(position));
    }

    //页面view
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position),0);
        return views.get(position);
    }


}
