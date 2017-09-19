package com.edroplet.qxx.saneteltabactivity.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.control.OperateBarControl;
import com.edroplet.qxx.saneteltabactivity.control.StatusBarControl;

public class ManualActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_speed_manual:
                    mTextMessage.setText(R.string.main_application_operate_speed);
                    return true;
                case R.id.navigation_location_manual:
                    mTextMessage.setText(R.string.main_application_operate_location);
                    return true;
                case R.id.navigation_calculate_manual:
                    mTextMessage.setText(R.string.main_application_operate_calculate);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        StatusBarControl.setupToolbar(this, R.id.main_content_toolbar);
        OperateBarControl.setupOperatorBar(this);
        // initToolbar();
        mTextMessage = (TextView) findViewById(R.id.custom_val);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.manual_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void  initToolbar(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_content_toolbar);
        setSupportActionBar(toolbar);
        /*
        toolbar.setTitle(R.string.main_application_operate);
        */
        //actionBar的设置(使用自定义的设置)
        ActionBar actionBar = getSupportActionBar();
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
