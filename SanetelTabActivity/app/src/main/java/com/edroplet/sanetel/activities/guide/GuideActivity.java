package com.edroplet.sanetel.activities.guide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

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

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.edroplet.sanetel.beans.monitor.MonitorInfo.MonitorInfoAction;
import static com.edroplet.sanetel.beans.monitor.MonitorInfo.MonitorInfoData;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String POSITION="position";

    private Timer timer = new Timer();
    private static final int schedule = 1000; // 一秒钟获取一次状态

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

    private  ArrayList<GuideFragmentExplode> guideFragmentExplode = new ArrayList<>();
    private  ArrayList<GuideFragmentLocation> guideFragmentLocation = new ArrayList<>();

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
    }

    private void initView(){
        mSectionsPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());

        if (mSectionsPagerAdapter.getCount() == 0) {
            // 添加天线界面
            // int antennaState = SystemServices.getAntennaState();
            // switch (antennaState){;
            // case AntennaInfo.AntennaSearchSatellitesStatus.EXPLODED:
            GuideFragmentExplode guideFragmentExplode0 = GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_explode),
                    true, getString(R.string.follow_me_antenna_state_explode_second_line),
                    true, getString(R.string.follow_me_antenna_state_explode_third_start), -1, null, null);
            guideFragmentExplode.add(guideFragmentExplode0);
            //    break;
            // case AntennaInfo.AntennaSearchSatellitesStatus.FOLDED:
            GuideFragmentExplode guideFragmentExplode1 = GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_folder),
                    false, null, true, getString(R.string.follow_me_message_click), 0, getString(R.string.antenna_state_exploded), null);
            guideFragmentExplode.add(guideFragmentExplode1);
            //     break
            // case AntennaInfo.AntennaSearchSatellitesStatus.PAUSE:
            GuideFragmentExplode guideFragmentExplode2 = GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_pause),
                    false, null, true, getString(R.string.follow_me_message_click), 0, getString(R.string.antenna_state_exploded), null);
            guideFragmentExplode.add(guideFragmentExplode2);
            // break;
            // case AntennaInfo.AntennaSearchSatellitesStatus.SEARCHING:
            GuideFragmentExplode guideFragmentExplode3 = GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_searching),
                    false, null, false, null, -1, null, null);
            guideFragmentExplode.add(guideFragmentExplode3);
            // break;
            // case AntennaInfo.AntennaSearchSatellitesStatus.RECYCLED:
            GuideFragmentExplode guideFragmentExplode4 = GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_saving),
                    false, null, true, getString(R.string.follow_me_message_click), 0, getString(R.string.antenna_state_exploded), null);
            guideFragmentExplode.add(guideFragmentExplode4);
            // break;
            // case AntennaInfo.AntennaSearchSatellitesStatus.EXPLODING:
            GuideFragmentExplode guideFragmentExplode5 = GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_explode_exploding),
                    false, null, true, getString(R.string.follow_me_message_click), 1, getString(R.string.antenna_state_paused), null);
            guideFragmentExplode.add(guideFragmentExplode5);
            // break;
            // }

            mSectionsPagerAdapter.addFragment(guideFragmentExplode.get(AntennaInfo.getAntennaState(this)));

            // 位置输入
            // 获取gps定位信息
            int bdState = LocationInfo.getGnssState(this);
            // switch (bdState){
            //    case LocationInfo.GnssState.NOTLOCATED:
            guideFragmentLocation.add(GuideFragmentLocation.newInstance(true, getString(R.string.follow_me_location_state_not_locate),
                    true, getString(R.string.follow_me_location_state_not_locate_second_line),
                    true, getString(R.string.follow_me_message_click), 0, null, getString(R.string.follow_me_forever), false, null));
            //       break;
            //   case LocationInfo.GnssState.LOCATED:
            guideFragmentLocation.add(GuideFragmentLocation.newInstance(true, getString(R.string.follow_me_location_state_locate),
                    false, null, false, null, -1, null, null,
                    true, getString(R.string.follow_me_location_state_locate_forth_line)));
            //       break;
            //}
            mSectionsPagerAdapter.addFragment(guideFragmentLocation.get(bdState));

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
                case 2: // 目标星
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

    private class FollowMeBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 监视信息，包含状态信息
            if (MonitorInfoAction.equals(action)){
                String rawData =intent.getStringExtra(MonitorInfoData);
                MonitorInfo monitorInfo = MonitorInfo.parseMonitorInfo(rawData);
                // 获取状态，更新UI

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

    }
}
