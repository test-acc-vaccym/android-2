package com.edroplet.sanetel.activities.guide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.activities.functions.FunctionsActivity;
import com.edroplet.sanetel.activities.main.MainActivity;
import com.edroplet.sanetel.adapters.MainViewPagerAdapter;
import com.edroplet.sanetel.beans.AntennaInfo;
import com.edroplet.sanetel.beans.LocationInfo;
import com.edroplet.sanetel.beans.LockerInfo;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.beans.SavingInfo;
import com.edroplet.sanetel.beans.monitor.MonitorInfo;
import com.edroplet.sanetel.control.StatusBarControl;
import com.edroplet.sanetel.fragments.guide.GuideFragmentDestination;
import com.edroplet.sanetel.fragments.guide.GuideFragmentExplode;
import com.edroplet.sanetel.fragments.guide.GuideFragmentLocation;
import com.edroplet.sanetel.fragments.guide.GuideFragmentLocker;
import com.edroplet.sanetel.fragments.guide.GuideFragmentSaving;
import com.edroplet.sanetel.fragments.guide.GuideFragmentSearchModeSetting;
import com.edroplet.sanetel.fragments.guide.GuideFragmentSearching;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomFAB;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.edroplet.sanetel.beans.monitor.MonitorInfo.MonitorInfoAction;
import static com.edroplet.sanetel.beans.monitor.MonitorInfo.MonitorInfoData;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String POSITION="position";

    private Timer timer = new Timer();
    private static final int schedule = 1000; // 一秒钟获取一次状态
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    // 发送监视指令，获取监视信息
                    Protocol.sendMessage(context,Protocol.cmdGetSystemState);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    public enum GUIDE_PAGES_INDEX{
        INDEX_EXPLODE,
        INDEX_LOCATION,
        INDEX_DESTINATION,
        INDEX_SEARCH_MODE,
        INDEX_SEARCHING,
        INDEX_LOCKER,
        INDEX_SAVING
    }
    private MainViewPagerAdapter mSectionsPagerAdapter;
    @BindView(R.id.follow_me_view_pager)
    ViewPager mViewPager;

    private int startPosition = 0;
    @BindView(R.id.follow_me_explode_fab)
    CustomFAB fab;

    private boolean isInit = false;

    GuideBroadcast guideBroadcast;

    @Override
    public void onClick(View view) {
        if (mViewPager == null){
            return;
        }
        int count = mSectionsPagerAdapter.getCount();
        int now = mViewPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.follow_me_bottom_nav_main:
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
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
                    StatusBarControl.setTitle(getString(R.string.follow_me_bottom_nav_done));
                    // Toast.makeText(this, getString(R.string.follow_me_last_page), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(GuideActivity.this, FunctionsActivity.class));
                    finish();
                }else{
                    mViewPager.setCurrentItem(now + 1);
                }
                break;
            case R.id.follow_me_bottom_nav_monitor:
                StatusBarControl.setTitle(getString(R.string.follow_me_bottom_nav_monitor));
                startActivity(new Intent(GuideActivity.this, FunctionsActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_me);
        ButterKnife.bind(this);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        int position = 0;
        if (bundle != null) {
            position = bundle.getInt(POSITION);
        }
        // OperateBarControl.setupOperatorBar(this);
        StatusBarControl.setupToolbar(this, R.id.follow_me_content_toolbar);
        findViewById(R.id.follow_me_bottom_nav_main).setOnClickListener(this);
        findViewById(R.id.follow_me_bottom_nav_preview).setOnClickListener(this);
        findViewById(R.id.follow_me_bottom_nav_next).setOnClickListener(this);
        findViewById(R.id.follow_me_bottom_nav_monitor).setOnClickListener(this);

        if (fab !=null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPage = mViewPager.getCurrentItem();
                    Intent intent;
                    switch (currentPage) {
                        case 0:
                            intent = new Intent(GuideActivity.this, GuideExplodeActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(GuideActivity.this, GuideLocationActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            });

        if (!isInit)
            initView();
        isInit = true;

        int viewCount = mSectionsPagerAdapter.getCount();
        if (position <= 0) {
            position = 0;
        }else if (position >= viewCount){
            if (viewCount > 0 )
                position =  viewCount - 1;
        }
        this.startPosition = position;
        // 初始化广播
        initBroadcast();
        // 开启定时任务
        timer.schedule(timerTask,0,schedule);
    }

    void initBroadcast(){
        // 注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MonitorInfo.MonitorInfoAction);
        guideBroadcast = new GuideBroadcast();
        registerReceiver(guideBroadcast, intentFilter);
    }

    private void initView(){
        mSectionsPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());

        if (mSectionsPagerAdapter.getCount() == 0) {
            // 添加天线界面
            mSectionsPagerAdapter.addFragment(GuideFragmentExplode.newInstance());

            // 位置输入
            mSectionsPagerAdapter.addFragment(GuideFragmentLocation.newInstance());

            // 目标星
            mSectionsPagerAdapter.addFragment(GuideFragmentDestination.newInstance(false, null,
                    true, getString(R.string.follow_me_destination_second_line),
                    true, getString(R.string.follow_me_message_click), 0, null, getString(R.string.follow_me_forever)));

            // 4.3.5.	寻星模式
            mSectionsPagerAdapter.addFragment(GuideFragmentSearchModeSetting.newInstance(true, getString(R.string.follow_me_search_mode_second), false, null,
                    true, getString(R.string.follow_me_search_mode_third_start),
                    0, null, getString(R.string.follow_me_search_mode_third_end)));
            // 4.3.6.	寻星操作
            mSectionsPagerAdapter.addFragment(GuideFragmentSearching.newInstance(false, true, getString(R.string.follow_me_searching_first_line),
                    true, getString(R.string.follow_me_searching_second_line),
                    true, getString(R.string.follow_me_searching_third_start), 1, null, null));

            // 4.3.7.	锁紧操作
            mSectionsPagerAdapter.addFragment(GuideFragmentLocker.newInstance());

            // 4.3.8.	节能操作
            mSectionsPagerAdapter.addFragment(GuideFragmentSaving.newInstance());
        }

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // 指定ViewPage预加载的页数
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    private MenuItem menuItem;

    final Context context = GuideActivity.this;
    final DialogInterface.OnClickListener mCancelClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            Toast.makeText(context, context.getString(R.string.cancel_button_prompt), Toast.LENGTH_SHORT).show();
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            switch (position){
                case 0: // 展开
                case 1: // 位置输入
                    fab.setVisibility(View.VISIBLE);
                    break;
                default:
                    fab.setVisibility(View.INVISIBLE);
                    break;
            }

            if (position == mSectionsPagerAdapter.getCount() - 1){
                ((CustomButton)findViewById(R.id.follow_me_bottom_nav_next)).setText(R.string.follow_me_bottom_nav_done);
            }else {
                ((CustomButton)findViewById(R.id.follow_me_bottom_nav_next)).setText(R.string.follow_me_bottom_nav_next);
            }

            startPosition = position;
        }

        @Override
        public void onPageSelected(int position) {
            /** 吴鹏说进入后就不要判断了
            final Context context = GuideActivity.this;

            switch (position){
                case 0: // 展开
                case 2: // 目标星
                    SystemServices.checkConnectedSsid(context, CustomSP.getString(context,
                            WifiSettingsNameKey, defaultDeviceName), GuideActivity.this, mCancelClickListener,
             SystemServices.REQUEST_WIFI_CONNECT_MANAGER);
                    break;
            }
             */

        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewPager != null) {
            mViewPager.setCurrentItem(startPosition);
        }
    }

    private class GuideBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 监视信息，包含状态信息
            if (MonitorInfoAction.equals(action)){
                String rawData =intent.getStringExtra(MonitorInfoData);
                // 更新本地缓存数据
                MonitorInfo.parseMonitorInfo(context, rawData);
                // 获取状态，更新UI
                // 更新statusBar, 在StatusButton中实现
                // 在每个fragment中实现
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (guideBroadcast != null){
            unregisterReceiver(guideBroadcast);
            guideBroadcast = null;
        }
    }
}
