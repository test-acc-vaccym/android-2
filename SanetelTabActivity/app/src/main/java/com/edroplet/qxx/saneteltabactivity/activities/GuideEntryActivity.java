package com.edroplet.qxx.saneteltabactivity.activities;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.edroplet.qxx.saneteltabactivity.R;

public class GuideEntryActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mExplode;
    private Button mLocation;
    private Button mDestination;
    private Button mSearchMode;
    private Button mSearching;
    private Button mLock;
    private Button mSaving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);
        setupToolbar();
        mExplode = (Button) findViewById(R.id.guide_main_button_explode);
        mExplode.setOnClickListener(this);
        // BottomNavigationView manual_navigation = (BottomNavigationView) findViewById(R.id.guide_navigation);
        // manual_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.guide_tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setHomeAsUpIndicator(R.drawable.back);
                // 隐藏ActionBar的标题
                // ab.setDisplayShowTitleEnabled(false);
                ab.setTitle(R.string.follow_me_bottom_nav_main);
                toolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.title_background, null));
                // 使用自定义试图
                // ab.setDisplayShowCustomEnabled(true);
            }

            //关键下面两句话，设置了回退按钮，及点击事件的效果
            ab.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            toolbar.hideOverflowMenu();
        }
    }

    private void setupToolsbar2(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.guide_tool_bar);
        if (toolbar != null) {
            // toolbar.setTitle("");
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                // getSupportActionBar().setTitle("");
                // 返回箭头（默认显示）
                actionBar.setDisplayHomeAsUpEnabled(true);
                // 更改返回图标
                actionBar.setHomeAsUpIndicator(R.drawable.back);
                // 左侧图标点击事件使能
                actionBar.setHomeButtonEnabled(true);
                // 使左上角图标(系统)是否显示
                actionBar.setDisplayShowHomeEnabled(true);
                // 显示标题
                actionBar.setDisplayShowTitleEnabled(false);
                //显示自定义视图
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                // View actionbarLayout = LayoutInflater.from(this).inflate(R.layout.guide_toolbar, null);
                // actionBar.setCustomView(actionbarLayout);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.guide_main_button_explode:
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
