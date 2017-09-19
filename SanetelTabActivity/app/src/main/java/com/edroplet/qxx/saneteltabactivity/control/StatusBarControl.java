package com.edroplet.qxx.saneteltabactivity.control;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.edroplet.qxx.saneteltabactivity.R;

/**
 * Created by qxs on 2017/9/19.
 * 参考 http://blog.csdn.net/dreamintheworld/article/details/39314121/
 */

public class StatusBarControl {
    private static ActionBar actionBar;
    public static void setupToolbar(AppCompatActivity activity, @IdRes int resId){

        Toolbar toolbar = (Toolbar) activity.findViewById(resId);
        activity.setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(ResourcesCompat.getColor(activity.getResources(), R.color.title_background, null));
        //actionBar的设置(使用自定义的设置)
        actionBar = activity.getSupportActionBar();
        if (actionBar != null) {

            // 返回箭头（默认不显示）
            actionBar.setDisplayHomeAsUpEnabled(false);
            // 左侧图标点击事件使能
            actionBar.setHomeButtonEnabled(true);
            // 使左上角图标(系统)是否显示
            actionBar.setDisplayShowHomeEnabled(false);
            // 修改图标
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                actionBar.setHomeAsUpIndicator(R.drawable.back);
            }
            // 显示标题
            actionBar.setDisplayShowTitleEnabled(false);

            // 获取屏幕参数
            //            WindowManager wm = activity.getWindowManager();
            //            DisplayMetrics metric = new DisplayMetrics();
            //            wm.getDefaultDisplay().getMetrics(metric);
            //            int width = metric.widthPixels;     // 屏幕宽度（像素）
            //            int height = metric.heightPixels;   // 屏幕高度（像素）
            //            float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
            //            int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240
            //            int properWidth = (int)(width*0.9);
            //            ActionBar.LayoutParams alp = new ActionBar.LayoutParams(properWidth,ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            //            LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //            View view=inflater.inflate(R.layout.status_bar, null);

            // 使用自定义layout,显示自定义视图
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.status_bar);
        }
        // 一个布局内的所有控件可以获取到焦点
        //   for (int i = 0; i < toolbar.getChildCount(); i++) {
        //       View v = toolbar.getChildAt(i);
        //       v.setFocusable(true);
        //   }
        final AppCompatActivity thisActivity = activity;
        // 使用drawable资源但不为其设置theme主题
        // ab.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.status_background,null));
        //关键下面两句话，设置了回退按钮，及点击事件的效果
        activity.findViewById(android.R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisActivity.finish();
            }
        });
        /*
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisActivity.finish();
            }
        });*/
        toolbar.hideOverflowMenu();
    }

    public static void setTitle(String title){
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }
}
