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
import com.edroplet.sanetel.control.OperateBarControl;
import com.edroplet.sanetel.control.StatusBarControl;
import com.edroplet.sanetel.fragments.guide.GuideFragmentDestination;
import com.edroplet.sanetel.fragments.guide.GuideFragmentExplode;
import com.edroplet.sanetel.fragments.guide.GuideFragmentLocation;
import com.edroplet.sanetel.fragments.guide.GuideFragmentLocker;
import com.edroplet.sanetel.fragments.guide.GuideFragmentSaving;
import com.edroplet.sanetel.fragments.guide.GuideFragmentSearchModeSetting;
import com.edroplet.sanetel.fragments.guide.GuideFragmentSearching;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.SystemServices;
import com.edroplet.sanetel.view.ViewInject;
import com.edroplet.sanetel.view.annotation.BindId;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomFAB;

import java.util.ArrayList;

import static com.edroplet.sanetel.activities.guide.FollowMeActivity.FOLLOWME_PAGES_INDEX.INDEX_EXPLODE;
import static com.edroplet.sanetel.activities.main.MainActivity.defaultDeviceName;
import static com.edroplet.sanetel.fragments.functions.FunctionsFragmentMonitor.ACTION_RECEIVE_MONITOR_INFO;
import static com.edroplet.sanetel.fragments.functions.FunctionsFragmentMonitor.KEY_RECEIVE_MONITOR_INFO_DATA;
import static com.edroplet.sanetel.utils.CustomSP.WifiSettingsNameKey;

public class FollowMeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String POSITION="position";


    public static enum FOLLOWME_PAGES_INDEX{
        INDEX_EXPLODE,
        INDEX_LOCATION,
        INDEX_DESTINATION,
        INDEX_SEARCH_MODE,
        INDEX_SEARCHING,
        INDEX_LOCKER,
        INDEX_SAVING
    }
    private MainViewPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private int startPosition = 0;
    @BindId(R.id.follow_me_explode_fab)
    private CustomFAB fab;

    private boolean isInited = false;

    private  ArrayList<GuideFragmentExplode> guideFragmentExplode = new ArrayList<GuideFragmentExplode>();
    private  ArrayList<GuideFragmentLocation> guideFragmentLocation = new ArrayList<GuideFragmentLocation>();
    private  ArrayList<GuideFragmentDestination> guideFragmentDestination = new ArrayList<GuideFragmentDestination>();
    private  ArrayList<GuideFragmentSearchModeSetting> guideFragmentSearchModeSetting = new ArrayList<GuideFragmentSearchModeSetting>();
    private  ArrayList<GuideFragmentSearching> guideFragmentSearching = new ArrayList<GuideFragmentSearching>();
    private  ArrayList<GuideFragmentLocker> guideFragmentLocker = new ArrayList<GuideFragmentLocker>();
    private  ArrayList<GuideFragmentSaving> guideFragmentSaving = new ArrayList<GuideFragmentSaving>();

    private  ArrayList<ArrayList> fragments = new ArrayList<ArrayList>();
    @Override
    public void onClick(View view) {
        if (mViewPager == null){
            return;
        }
        int count = mSectionsPagerAdapter.getCount();
        int now = mViewPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.follow_me_bottom_nav_main:
                startActivity(new Intent(FollowMeActivity.this, MainActivity.class));
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
                    startActivity(new Intent(FollowMeActivity.this, FunctionsActivity.class));
                    finish();
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
        ViewInject.inject(this, this);
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

        if (!isInited)
            initView();
        isInited = true;

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
            // case AntennaInfo.AntennaStatus.EXPLODED:
            GuideFragmentExplode guideFragmentExplode0 = GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_explode),
                    true, getString(R.string.follow_me_antenna_state_explode_second_line),
                    true, getString(R.string.follow_me_antenna_state_explode_third_start), -1, null, null);
            guideFragmentExplode.add(guideFragmentExplode0);
            //    break;
            // case AntennaInfo.AntennaStatus.FOLDED:
            GuideFragmentExplode guideFragmentExplode1 = GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_folder),
                    false, null, true, getString(R.string.follow_me_message_click), 0, getString(R.string.antenna_state_exploded), null);
            guideFragmentExplode.add(guideFragmentExplode1);
            //     break
            // case AntennaInfo.AntennaStatus.PAUSE:
            GuideFragmentExplode guideFragmentExplode2 = GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_pause),
                    false, null, true, getString(R.string.follow_me_message_click), 0, getString(R.string.antenna_state_exploded), null);
            guideFragmentExplode.add(guideFragmentExplode2);
            // break;
            // case AntennaInfo.AntennaStatus.SEARCHING:
            GuideFragmentExplode guideFragmentExplode3 = GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_searching),
                    false, null, false, null, -1, null, null);
            guideFragmentExplode.add(guideFragmentExplode3);
            // break;
            // case AntennaInfo.AntennaStatus.RECYCLED:
            GuideFragmentExplode guideFragmentExplode4 = GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_saving),
                    false, null, true, getString(R.string.follow_me_message_click), 0, getString(R.string.antenna_state_exploded), null);
            guideFragmentExplode.add(guideFragmentExplode4);
            // break;
            // case AntennaInfo.AntennaStatus.EXPLODING:
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
            guideFragmentDestination.add(GuideFragmentDestination.newInstance(false, null,
                    true, getString(R.string.follow_me_destination_second_line),
                    true, getString(R.string.follow_me_message_click), 0, null, getString(R.string.follow_me_forever)));
            mSectionsPagerAdapter.addFragment(guideFragmentDestination.get(0));

            // 4.3.5.	寻星模式
            guideFragmentSearchModeSetting.add(GuideFragmentSearchModeSetting.newInstance(true, getString(R.string.follow_me_search_mode_second), false, null,
                    true, getString(R.string.follow_me_search_mode_third_start),
                    0, null, getString(R.string.follow_me_search_mode_third_end)));

            mSectionsPagerAdapter.addFragment(guideFragmentSearchModeSetting.get(0));
            // 4.3.6.	寻星操作
            int satelliteStatus = AntennaInfo.getAntennaState(this);

            //switch (satelliteStatus){
            // 开始寻星
            // case 0:
            guideFragmentSearching.add(GuideFragmentSearching.newInstance(false, true, getString(R.string.follow_me_searching_first_line),
                    true, getString(R.string.follow_me_searching_second_line),
                    true, getString(R.string.follow_me_searching_third_start), 1, null, null));
            //break;

            //break;
            //}
            mSectionsPagerAdapter.addFragment(guideFragmentSearching.get(0));

            // 4.3.7.	锁紧操作
            int lockerState = LockerInfo.getLockerState(this);
            //switch (lockerState){
            //case 0:
            guideFragmentLocker.add(GuideFragmentLocker.newInstance(true, getString(R.string.follow_me_locker_open_first_line),
                    true, getString(R.string.follow_me_locker_open_second_line),
                    true, getString(R.string.follow_me_locker_open_third_start), -1, null, null));
            //break;
            //case 1:
            guideFragmentLocker.add(GuideFragmentLocker.newInstance(true, getString(R.string.follow_me_locker_lock_first_line),
                    true, getString(R.string.follow_me_locker_lock_second_line),
                    true, getString(R.string.follow_me_locker_lock_third_start), -1, null, null));
            //break;
            //}
            mSectionsPagerAdapter.addFragment(guideFragmentLocker.get(lockerState));

            // 4.3.8.	节能操作
            int savingState = SavingInfo.getSavingState(this);
            //switch (savingState){
            //    case 0:
            guideFragmentSaving.add(GuideFragmentSaving.newInstance(true, getString(R.string.follow_me_saving_open_first_line),
                    true, getString(R.string.follow_me_saving_second_line),
                    true, getString(R.string.follow_me_saving_third_start), 0, null, null, true,getString(R.string.follow_me_saving_third_end)));
            //       break;
            //    case 1:
            guideFragmentSaving.add(GuideFragmentSaving.newInstance(true, getString(R.string.follow_me_saving_close_first_line),
                    true, getString(R.string.follow_me_saving_second_line),
                    true, getString(R.string.follow_me_saving_third_start), 0, null, null, true, getString(R.string.follow_me_saving_third_end)));
            //       break;
            //}

            mSectionsPagerAdapter.addFragment(guideFragmentSaving.get(savingState));
        }

        if (!fragments.contains(guideFragmentExplode))
            fragments.add(guideFragmentExplode);
        if (!fragments.contains(guideFragmentLocation))
            fragments.add(guideFragmentLocation);
        if (!fragments.contains(guideFragmentDestination))
            fragments.add(guideFragmentDestination);
        if (!fragments.contains(guideFragmentSearchModeSetting))
            fragments.add(guideFragmentSearchModeSetting);
        if (!fragments.contains(guideFragmentSearching))
            fragments.add(guideFragmentSearching);
        if (!fragments.contains(guideFragmentLocker))
            fragments.add( guideFragmentLocker);
        if (!fragments.contains(guideFragmentSaving))
            fragments.add( guideFragmentSaving);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.follow_me_view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // 指定ViewPage预加载的页数
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        // jumpTo(position);
    }

    private void  jumpTo(){
            FragmentManager fmanager = getSupportFragmentManager();
            FragmentTransaction ftransaction = fmanager.beginTransaction();
            ftransaction.replace(R.id.follow_me_view_pager, (Fragment)fragments.get(startPosition).get(0)).commit();
    }
    private MenuItem menuItem;

    final Context context = FollowMeActivity.this;
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
            final Context context = FollowMeActivity.this;

            switch (position){
                case 0: // 展开
                case 2: // 目标星
                    SystemServices.checkConnectedSsid(context, CustomSP.getString(context,
                            WifiSettingsNameKey, defaultDeviceName), FollowMeActivity.this, mCancelClickListener,
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
            if (ACTION_RECEIVE_MONITOR_INFO.equals(action)){
                String rawData =intent.getStringExtra(KEY_RECEIVE_MONITOR_INFO_DATA);
                MonitorInfo monitorInfo = MonitorInfo.parseMonitorInfo(rawData);
                // 获取状态，更新UI

            }
        }
    }

}
