package com.edroplet.qxx.saneteltabactivity.activities.guide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.functions.FunctionsActivity;
import com.edroplet.qxx.saneteltabactivity.adapters.MainViewPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;
import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.control.OperateBarControl;
import com.edroplet.qxx.saneteltabactivity.control.StatusBarControl;
import com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentDestination;
import com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentExplode;
import com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentLocation;
import com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentLocker;
import com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentSaving;
import com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentSearchModeSetting;
import com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentSearching;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;

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
                    // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    int currentPage = mViewPager.getCurrentItem();
                    Intent intent;
                    switch (currentPage) {
                        case 0:
                            intent = new Intent(FollowMeActivity.this, GuideExplodeActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(FollowMeActivity.this, GuideLocationActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            });

        initView(position);
    }

    private void initView(int position){
        mSectionsPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        // 添加天线界面
        int state = SystemServices.getAntennaState();
        switch (state){
            case AntennaInfo.AntennaStatus.FOLDED:
                mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(false, null, true,"天线状态为收藏！", true, "请点击", 0, "展开"));
                break;
            case AntennaInfo.AntennaStatus.EXPLODED:
                mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(true, "天线状态已为展开！    ", true,"     天线面辅瓣放置在软包中," +
                        "每个天线辅面配有图标；用户通过主瓣上标注的安装说明，可以轻而易举的完成天线面安装。", true, "     安装完成之后,可以点击下一步。",-1, null));
                // mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(false, null, true,"天线状态为展开！", true, "请点击", 4, "收藏"));
                break;
            case AntennaInfo.AntennaStatus.PAUSE:
                mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(false, null, true,"天线状态为暂停！", true, "请点击", 0, "展开"));
                break;
            case AntennaInfo.AntennaStatus.EXPLODING:
                mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(false, null, true,"展开中……！", true, "请点击", 1, "暂停"));
                break;
            case AntennaInfo.AntennaStatus.SEARCHING:
                mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(false, "", true,"天线状态为寻星中……！", false, null, -1, null));
                break;
            case AntennaInfo.AntennaStatus.RECYCLED:
                mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(false, null, true,"天线状态为节能模式！", true, "请点击", 0, "展开"));
                break;
        }
        // 位置输入
        // 获取gps定位信息
        int bdState = SystemServices.getBDState();
        switch (bdState){
            case LocationInfo.BDState.NONLOCATED:
                mSectionsPagerAdapter.addFragment(GuideFragmentLocation.newInstance(false, null, true,"GPS/BD状态：未定位！", true, "点击", 0, "永久生效。新城市将保持于数据库。"));
                break;
            case LocationInfo.BDState.LOCATED:
                mSectionsPagerAdapter.addFragment(GuideFragmentLocation.newInstance(false, null, true,"GPS/BD状态：已定位！", true, "**当地经纬度展现在文本框中，直接点击下一步。**", -1, null));
                break;
        }
        // 目标星
        mSectionsPagerAdapter.addFragment(GuideFragmentDestination.newInstance(false, null, true,"请您从数据库中选择目标星；如果数据库没有目标星，请输入卫星参数。新卫星将保持于数据库。", true, "点击", -1, "永久生效。"));

        // 4.3.5.	寻星模式
        mSectionsPagerAdapter.addFragment(GuideFragmentSearchModeSetting.newInstance(true, getString(R.string.follow_me_search_mode_third_start), false, null, true,
                getString(R.string.follow_me_search_mode_third_end), -1, getString(R.string.follow_me_search_mode_second)));
        // 4.3.6.	寻星操作
        int satelliteStatus = SystemServices.getAntennaState();

        switch (satelliteStatus){
            // 开始寻星
            case 0:
                mSectionsPagerAdapter.addFragment(GuideFragmentSearching.newInstance(false, true, getString(R.string.follow_me_searching_first_line), true, getString(R.string.follow_me_searching_second_line),
                        true, getString(R.string.follow_me_searching_third_start), 0, getString(R.string.follow_me_searching_third_end)));
                break;
            // 寻星中
            case 1:
                mSectionsPagerAdapter.addFragment(GuideFragmentSearching.newInstance(true, true, getString(R.string.follow_me_searching_ing_first_line), true, getString(R.string.follow_me_searching_ing_second_line),
                        true, getString(R.string.follow_me_searching_ing_third_start), 0, null));
                break;
            // 锁星
            case 2:
                mSectionsPagerAdapter.addFragment(GuideFragmentSearching.newInstance(true, true, getString(R.string.follow_me_searching_lock_first_line), true, getString(R.string.follow_me_searching_lock_second_line),
                        true, getString(R.string.follow_me_searching_lock_third_start), -1, null));
                break;
            // 重设参数
            case 3:
                mSectionsPagerAdapter.addFragment(GuideFragmentSearching.newInstance(true, true, getString(R.string.follow_me_searching_timeout_first_line), true, getString(R.string.follow_me_searching_timeout_second_line),
                        true, getString(R.string.follow_me_searching_timeout_third_start), 3, getString(R.string.follow_me_searching_timeout_third_end)));
                break;
            // 故障报告
            case 4:
                mSectionsPagerAdapter.addFragment(GuideFragmentSearching.newInstance(true, true, getString(R.string.follow_me_searching_error_first_line), true, getString(R.string.follow_me_searching_error_second_line),
                        true, getString(R.string.follow_me_searching_error_third_start), -1, null));
                break;
        }
        // 4.3.7.	锁紧操作
        int lockerState = SystemServices.getLockerState();
        switch (lockerState){
            case 0:
                mSectionsPagerAdapter.addFragment(GuideFragmentLocker.newInstance(true, getString(R.string.follow_me_locker_open_first_line), true, getString(R.string.follow_me_locker_open_second_line),
                        true, getString(R.string.follow_me_locker_open_third_start), -1, null));
                break;
            case 1:
                mSectionsPagerAdapter.addFragment(GuideFragmentLocker.newInstance(true, getString(R.string.follow_me_locker_lock_first_line), true, getString(R.string.follow_me_locker_lock_second_line),
                        true, getString(R.string.follow_me_locker_lock_third_start), -1, null));
                break;
        }
        // 4.3.8.	节能操作
        int savingState = SystemServices.getSavingState();
        switch (savingState){
            case 0:
                mSectionsPagerAdapter.addFragment(GuideFragmentSaving.newInstance(true, getString(R.string.follow_me_saving_open_first_line), true, getString(R.string.follow_me_saving_second_line),
                        true, getString(R.string.follow_me_saving_third_start), 0, getString(R.string.follow_me_saving_third_end)));
                break;
            case 1:
                mSectionsPagerAdapter.addFragment(GuideFragmentSaving.newInstance(true, getString(R.string.follow_me_saving_close_first_line), true, getString(R.string.follow_me_saving_second_line),
                        true, getString(R.string.follow_me_saving_third_start), 0, getString(R.string.follow_me_saving_third_end)));
                break;
        }

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
        // mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        // jumpTo(position);
        mViewPager.setCurrentItem(position);
    }

    private void  jumpTo(final int index){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.follow_me_view_pager, GuideFragmentExplode.newInstance(false, null, true,"展开中……！", false, "请点击", 1, "展开"));
        transaction.commit();
//        if (mViewPager !=  null){
//            mViewPager.getAdapter().notifyDataSetChanged();
//            new Handler().post(new Runnable() {
//                @Override
//                public void run() {
//                    mViewPager.setCurrentItem(index, true); //Where "2" is the position you want to go
//                }
//            });
//        }
    }
    private MenuItem menuItem;

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };
}
