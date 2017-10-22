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
    private FloatingActionButton fab;

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
                    Toast.makeText(this, getString(R.string.follow_me_first_page), Toast.LENGTH_SHORT).show();
                }else{
                    mViewPager.setCurrentItem(now - 1);
                }
                break;
            case R.id.follow_me_bottom_nav_next:
                StatusBarControl.setTitle(getString(R.string.follow_me_bottom_nav_next));
                if (now == count - 1) {
                    Toast.makeText(this, getString(R.string.follow_me_last_page), Toast.LENGTH_SHORT).show();
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
        fab = (FloatingActionButton) findViewById(R.id.follow_me_explode_fab);
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
                mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_folder),
                        false, null, true, getString(R.string.follow_me_message_click), 0, getString(R.string.antenna_state_exploded), null));
                break;
            case AntennaInfo.AntennaStatus.EXPLODED:
                mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_explode),
                        true, getString(R.string.follow_me_antenna_state_explode_second_line),
                        true, getString(R.string.follow_me_antenna_state_explode_third_start),-1, null, null));
                break;
            case AntennaInfo.AntennaStatus.PAUSE:
                mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_pause),
                        false, null, true, getString(R.string.follow_me_message_click), 0, getString(R.string.antenna_state_exploded), null));
                break;
            case AntennaInfo.AntennaStatus.EXPLODING:
                mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(true,getString(R.string.follow_me_explode_exploding),
                        false, null, true, getString(R.string.follow_me_message_click), 1,  getString(R.string.antenna_state_paused), null));
                break;
            case AntennaInfo.AntennaStatus.SEARCHING:
                mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(true,getString(R.string.follow_me_antenna_state_searching),
                        false, null, false, null, -1, null, null));
                break;
            case AntennaInfo.AntennaStatus.RECYCLED:
                mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance(true,getString(R.string.follow_me_antenna_state_saving),
                        false, null, true, getString(R.string.follow_me_message_click), 0, getString(R.string.antenna_state_exploded), null));
                break;
        }
        // 位置输入
        // 获取gps定位信息
        int bdState = SystemServices.getBDState();
        switch (bdState){
            case LocationInfo.BDState.NONLOCATED:
                mSectionsPagerAdapter.addFragment(GuideFragmentLocation.newInstance(true, getString(R.string.follow_me_location_state_not_locate),
                        true, getString(R.string.follow_me_location_state_not_locate_second_line),
                        true, getString(R.string.follow_me_message_click), 0, null, getString(R.string.follow_me_forever)));
                break;
            case LocationInfo.BDState.LOCATED:
                mSectionsPagerAdapter.addFragment(GuideFragmentLocation.newInstance(true, getString(R.string.follow_me_location_state_locate),
                        true, getString(R.string.follow_me_location_state_locate_second_line), false, null,  -1, null, null));
                break;
        }

        // 目标星
        mSectionsPagerAdapter.addFragment(GuideFragmentDestination.newInstance(false, null,
                true, getString(R.string.follow_me_destination_second_line),
                true, getString(R.string.follow_me_message_click), 0, null, getString(R.string.follow_me_forever)));

        // 4.3.5.	寻星模式
        mSectionsPagerAdapter.addFragment(GuideFragmentSearchModeSetting.newInstance(false, null,
                true, getString(R.string.follow_me_search_mode_second), true, getString(R.string.follow_me_search_mode_third_start),
                0,  null, getString(R.string.follow_me_search_mode_third_end)));
        // 4.3.6.	寻星操作
        int satelliteStatus = SystemServices.getAntennaState();

        switch (satelliteStatus){
            // 开始寻星
            case 0:
                mSectionsPagerAdapter.addFragment(GuideFragmentSearching.newInstance(false, true, getString(R.string.follow_me_searching_first_line),
                        true, getString(R.string.follow_me_searching_second_line),
                        true, getString(R.string.follow_me_searching_third_start), 1, null, null));
                break;
            // 寻星中
            case 1:
                mSectionsPagerAdapter.addFragment(GuideFragmentSearching.newInstance(true, true, getString(R.string.follow_me_searching_ing_first_line),
                        true, getString(R.string.follow_me_searching_ing_second_line),
                        true, getString(R.string.follow_me_searching_ing_third_start), 2,  null, null));
                break;
            // 锁星
            case 2:
                mSectionsPagerAdapter.addFragment(GuideFragmentSearching.newInstance(true, true, getString(R.string.follow_me_searching_lock_first_line),
                        true, getString(R.string.follow_me_searching_lock_second_line),
                        true, getString(R.string.follow_me_searching_lock_third_start), -1,  null, null));
                break;
            // 重设参数
            case 3:
                mSectionsPagerAdapter.addFragment(GuideFragmentSearching.newInstance(true, true, getString(R.string.follow_me_searching_timeout_first_line),
                        true, getString(R.string.follow_me_searching_timeout_second_line),
                        true, getString(R.string.follow_me_searching_timeout_third_start), 3,
                        null, getString(R.string.follow_me_searching_timeout_third_end)));
                break;
            // 故障报告
            case 4:
                mSectionsPagerAdapter.addFragment(GuideFragmentSearching.newInstance(true, true, getString(R.string.follow_me_searching_error_first_line),
                        true, getString(R.string.follow_me_searching_error_second_line),
                        true, getString(R.string.follow_me_searching_error_third_start), 4,  null, null));
                break;
        }
        // 4.3.7.	锁紧操作
        int lockerState = SystemServices.getLockerState();
        switch (lockerState){
            case 0:
                mSectionsPagerAdapter.addFragment(GuideFragmentLocker.newInstance(true, getString(R.string.follow_me_locker_open_first_line),
                        true, getString(R.string.follow_me_locker_open_second_line),
                        true, getString(R.string.follow_me_locker_open_third_start), -1,  null, null));
                break;
            case 1:
                mSectionsPagerAdapter.addFragment(GuideFragmentLocker.newInstance(true, getString(R.string.follow_me_locker_lock_first_line),
                        true, getString(R.string.follow_me_locker_lock_second_line),
                        true, getString(R.string.follow_me_locker_lock_third_start), -1,  null, null));
                break;
        }
        // 4.3.8.	节能操作
        int savingState = SystemServices.getSavingState();
        switch (savingState){
            case 0:
                mSectionsPagerAdapter.addFragment(GuideFragmentSaving.newInstance(true, getString(R.string.follow_me_saving_open_first_line),
                        true, getString(R.string.follow_me_saving_second_line),
                        true, getString(R.string.follow_me_saving_third_start), 0,  null, getString(R.string.follow_me_saving_third_end)));
                break;
            case 1:
                mSectionsPagerAdapter.addFragment(GuideFragmentSaving.newInstance(true, getString(R.string.follow_me_saving_close_first_line),
                        true, getString(R.string.follow_me_saving_second_line),
                        true, getString(R.string.follow_me_saving_third_start), 0,  null, getString(R.string.follow_me_saving_third_end)));
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
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        // jumpTo(position);
        mViewPager.setCurrentItem(position);
        mViewPager.setCurrentItem(position, true);
    }

    private void  jumpTo(final int index){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.follow_me_view_pager, GuideFragmentExplode.newInstance(false, null, true,"展开中……！", false, getString(R.string.follow_me_message_click), 1, getString(R.string.antenna_state_exploded), null));
        transaction.commit();
    }
    private MenuItem menuItem;

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            switch (position){
                case 0:
                case 1:
                    fab.setVisibility(View.VISIBLE);
                    break;
                default:
                    fab.setVisibility(View.INVISIBLE);
                    break;
            }
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };
}
