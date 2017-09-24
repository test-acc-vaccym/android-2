package com.edroplet.qxx.saneteltabactivity.activities.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.functions.FunctionsActivity;
import com.edroplet.qxx.saneteltabactivity.adapters.MainViewPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.control.OperateBarControl;
import com.edroplet.qxx.saneteltabactivity.control.StatusBarControl;
import com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentExplode;

public class FollowMeActivity extends AppCompatActivity implements View.OnClickListener {

    private MainViewPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public void onClick(View view) {
        if (mViewPager == null){
            return;
        }
        int count = mViewPager.getChildCount();
        int now = mViewPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.follow_me_bottom_nav_main:
                startActivity(new Intent(FollowMeActivity.this, GuideEntryActivity.class));
                StatusBarControl.setTitle(getString(R.string.follow_me_bottom_nav_main));
                finish();
                break;
            case R.id.follow_me_bottom_nav_preview:
                StatusBarControl.setTitle(getString(R.string.follow_me_bottom_nav_preview));
                if (now == 0) {
                    Toast.makeText(this, "Already the FIRST one", Toast.LENGTH_SHORT).show();
                }else{
                    mViewPager.setCurrentItem(now - 1);
                }
                break;
            case R.id.follow_me_bottom_nav_next:
                StatusBarControl.setTitle(getString(R.string.follow_me_bottom_nav_next));
                if (now == count - 1) {
                    Toast.makeText(this, "Already the LAST one", Toast.LENGTH_SHORT).show();
                }else{
                    mViewPager.setCurrentItem(now + 1);
                }
                break;
            case R.id.follow_me_bottom_nav_monitor:
                StatusBarControl.setTitle(getString(R.string.follow_me_bottom_nav_monitor));
                startActivity(new Intent(FollowMeActivity.this, FunctionsActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_me);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        int position = 0;
        if (bundle != null) {
            position = bundle.getInt("position");
            if (position <= 0) {
                position = 0;
            }
        }
        OperateBarControl.setupOperatorBar(this);
        StatusBarControl.setupToolbar(this, R.id.follow_me_content_toolbar);
        findViewById(R.id.follow_me_bottom_nav_main).setOnClickListener(this);
        findViewById(R.id.follow_me_bottom_nav_preview).setOnClickListener(this);
        findViewById(R.id.follow_me_bottom_nav_next).setOnClickListener(this);
        findViewById(R.id.follow_me_bottom_nav_monitor).setOnClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.follow_me_explode_fab);
        if (fab !=null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

        initView(position);
    }

    private void initView(int position){
        mSectionsPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance("ahaha"));
        mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance("ahaha1"));
        mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance("ahaha2"));
        mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance("ahaha3"));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.follow_me_view_pager);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(position);
    }

    private MenuItem menuItem;

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
//            if (menuItem != null){
//                menuItem.setChecked(false);
//            }
//            menuItem.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };
}
