package com.edroplet.qxx.saneteltabactivity.activities.guide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialoger;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;

public class FollowMeActivity extends AppCompatActivity implements View.OnClickListener {
    public static String POSITION="position";
    private MainViewPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public void onClick(View view) {
        if (mViewPager == null){
            return;
        }
        int count = mViewPager.getChildCount();
        count = mViewPager.getAdapter().getCount();
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
            position = bundle.getInt(POSITION);
        }
        OperateBarControl.setupOperatorBar(this);
        StatusBarControl.setupToolbar(this, R.id.follow_me_content_toolbar);
        findViewById(R.id.follow_me_bottom_nav_main).setOnClickListener(this);
        findViewById(R.id.follow_me_bottom_nav_preview).setOnClickListener(this);
        findViewById(R.id.follow_me_bottom_nav_next).setOnClickListener(this);
        findViewById(R.id.follow_me_bottom_nav_monitor).setOnClickListener(this);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.follow_me_explode_fab);
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
        mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(false, null, true,"天线状态为收藏！", true, "请点击", 1, "展开"));
        mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(false, null, true,"展开中……！", false, "请点击", 1, "展开"));
        mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(false, null, true,"天线状态为收藏！", true, "请点击", 1, "展开"));
        mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(true, "天线状态已为展开！", true,"天线面辅瓣放置在软包中," +
                "每个天线辅面配有图标；用户通过主瓣上标注的安装说明，可以轻而易举的完成天线面安装。", true, "安装完成之后，", -1, "可以点击下一步。"));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.follow_me_view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // 指定ViewPage预加载的页数
        mViewPager.setOffscreenPageLimit(4);
        if (position <= 0) {
            position = 0;
        }else if (position >= mViewPager.getChildCount()){
            position = mViewPager.getChildCount() - 1;
        }
        // mViewPager.setCurrentItem(position);
        // mViewPager.setCurrentItem(position, true);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        jumpTo(position);
    }

    private void  jumpTo(final int index){
        if (mViewPager !=  null){
            mViewPager.getAdapter().notifyDataSetChanged();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mViewPager.setCurrentItem(index, true); //Where "2" is the position you want to go
                }
            });
        }
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
