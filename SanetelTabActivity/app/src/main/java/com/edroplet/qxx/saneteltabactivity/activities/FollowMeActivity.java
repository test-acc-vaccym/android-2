package com.edroplet.qxx.saneteltabactivity.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.adapters.MainViewPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.adapters.SectionsPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.control.OperateBarControl;
import com.edroplet.qxx.saneteltabactivity.control.StatusBarControl;
import com.edroplet.qxx.saneteltabactivity.fragments.followme.GuideFragmentExplode;

import java.util.LinkedHashMap;

public class FollowMeActivity extends AppCompatActivity {

    private MainViewPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static LinkedHashMap<String, String> map = new LinkedHashMap<>();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.follow_me_bottom_nav_main:
                    StatusBarControl.setTitle(getString(R.string.follow_me_bottom_nav_main));
                    return true;
                case R.id.follow_me_bottom_nav_preview:
                    StatusBarControl.setTitle(getString(R.string.follow_me_bottom_nav_preview));
                    return true;
                case R.id.follow_me_bottom_nav_next:
                    StatusBarControl.setTitle(getString(R.string.follow_me_bottom_nav_next));
                    return true;
                case R.id.follow_me_bottom_nav_monitor:
                    StatusBarControl.setTitle(getString(R.string.follow_me_bottom_nav_monitor));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_me);

        OperateBarControl.setupOperatorBar(this);
        StatusBarControl.setupToolbar(this, R.id.follow_me_content_toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.follow_me_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.follow_me_explode_fab);
        if (fab !=null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

        initView();
    }

    private void initView(){

        mSectionsPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance("ahaha"));
        mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance("ahaha1"));
        mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance("ahaha2"));
        mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance("ahaha3"));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.follow_me_view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }
}
