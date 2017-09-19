package com.edroplet.qxx.saneteltabactivity.control;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;

/**
 * Created by qxs on 2017/9/19.
 */

public class StatusBarControl {
    private static ActionBar actionBar;
    public static void setupToolbar(AppCompatActivity activity, @IdRes int resId){

        Toolbar toolbar = (Toolbar) activity.findViewById(resId);
        activity.setSupportActionBar(toolbar);
        /*
        toolbar.setTitle(R.string.main_application_operate);
        */
        //actionBar的设置(使用自定义的设置)
        actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            // 使用自定义layout
            actionBar.setCustomView(R.layout.status_bar);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayHomeAsUpEnabled(true);
            // 修改图标
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                actionBar.setHomeAsUpIndicator(R.drawable.back);
            }
        }
        final AppCompatActivity thisActivity = activity;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisActivity.finish();
            }
        });

    }

    public static void setTitle(String title){
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }
}
