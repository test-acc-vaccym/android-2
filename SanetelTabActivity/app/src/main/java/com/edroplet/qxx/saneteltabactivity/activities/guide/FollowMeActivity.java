package com.edroplet.qxx.saneteltabactivity.activities.guide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class FollowMeActivity extends AppCompatActivity implements View.OnClickListener {
    public static String POSITION="position";
    private MainViewPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton fab;
    private int startPosition = 0;


    private static List<GuideFragmentExplode> guideFragmentExplode = new List<GuideFragmentExplode>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<GuideFragmentExplode> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] ts) {
            return null;
        }

        @Override
        public boolean add(GuideFragmentExplode guideFragmentExplode) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends GuideFragmentExplode> collection) {
            return false;
        }

        @Override
        public boolean addAll(int i, @NonNull Collection<? extends GuideFragmentExplode> collection) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public GuideFragmentExplode get(int i) {
            return null;
        }

        @Override
        public GuideFragmentExplode set(int i, GuideFragmentExplode guideFragmentExplode) {
            return null;
        }

        @Override
        public void add(int i, GuideFragmentExplode guideFragmentExplode) {

        }

        @Override
        public GuideFragmentExplode remove(int i) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<GuideFragmentExplode> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<GuideFragmentExplode> listIterator(int i) {
            return null;
        }

        @NonNull
        @Override
        public List<GuideFragmentExplode> subList(int i, int i1) {
            return null;
        }
    };
    private static ArrayList<GuideFragmentLocation> guideFragmentLocation = new ArrayList<GuideFragmentLocation>();
    private static ArrayList<GuideFragmentDestination> guideFragmentDestination = new ArrayList<GuideFragmentDestination>();
    private static ArrayList<GuideFragmentSearchModeSetting> guideFragmentSearchModeSetting = new ArrayList<GuideFragmentSearchModeSetting>();
    private static ArrayList<GuideFragmentSearching> guideFragmentSearching = new ArrayList<GuideFragmentSearching>();
    private static ArrayList<GuideFragmentLocker> guideFragmentLocker = new ArrayList<GuideFragmentLocker>();
    private static ArrayList<GuideFragmentSaving> guideFragmentSaving = new ArrayList<GuideFragmentSaving>();

    private static List[] fragments = {guideFragmentExplode, guideFragmentLocation, guideFragmentDestination, guideFragmentSearchModeSetting,
            guideFragmentSearching, guideFragmentLocker, guideFragmentSaving};
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
        // int antennaState = SystemServices.getAntennaState();
        // switch (antennaState){;
        // case AntennaInfo.AntennaStatus.EXPLODED:
        GuideFragmentExplode guideFragmentExplode0 = GuideFragmentExplode.newInstance(true, getString(R.string.follow_me_antenna_state_explode),
                true, getString(R.string.follow_me_antenna_state_explode_second_line),
                true, getString(R.string.follow_me_antenna_state_explode_third_start),-1, null, null);
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
        GuideFragmentExplode guideFragmentExplode3 = GuideFragmentExplode.newInstance(true,getString(R.string.follow_me_antenna_state_searching),
                false, null, false, null, -1, null, null);
        guideFragmentExplode.add(guideFragmentExplode3);
        // break;
        // case AntennaInfo.AntennaStatus.RECYCLED:
        GuideFragmentExplode guideFragmentExplode4 = GuideFragmentExplode.newInstance(true,getString(R.string.follow_me_antenna_state_saving),
                false, null, true, getString(R.string.follow_me_message_click), 0, getString(R.string.antenna_state_exploded), null);
        guideFragmentExplode.add(guideFragmentExplode4);
        // break;
        // case AntennaInfo.AntennaStatus.EXPLODING:
                GuideFragmentExplode guideFragmentExplode5 = GuideFragmentExplode.newInstance(true,getString(R.string.follow_me_explode_exploding),
                        false, null, true, getString(R.string.follow_me_message_click), 1,  getString(R.string.antenna_state_paused), null);
        guideFragmentExplode.add(guideFragmentExplode5);
        // break;
        // }

        mSectionsPagerAdapter.addFragment(guideFragmentExplode.get(SystemServices.getAntennaState()));

        // 位置输入
        // 获取gps定位信息
        int bdState = SystemServices.getBDState();
        // switch (bdState){
        //    case LocationInfo.BDState.NONLOCATED:
                guideFragmentLocation.add(GuideFragmentLocation.newInstance(true, getString(R.string.follow_me_location_state_not_locate),
                        true, getString(R.string.follow_me_location_state_not_locate_second_line),
                        true, getString(R.string.follow_me_message_click), 0, null, getString(R.string.follow_me_forever)));
         //       break;
         //   case LocationInfo.BDState.LOCATED:
                guideFragmentLocation.add(GuideFragmentLocation.newInstance(true, getString(R.string.follow_me_location_state_locate),
                        true, getString(R.string.follow_me_location_state_locate_second_line), false, null,  -1, null, null));
         //       break;
        //}
        mSectionsPagerAdapter.addFragment(guideFragmentLocation.get(bdState));

        // 目标星
        guideFragmentDestination.add(GuideFragmentDestination.newInstance(false, null,
                true, getString(R.string.follow_me_destination_second_line),
                true, getString(R.string.follow_me_message_click), 0, null, getString(R.string.follow_me_forever)));
        mSectionsPagerAdapter.addFragment(guideFragmentDestination.get(0));

        // 4.3.5.	寻星模式
        guideFragmentSearchModeSetting.add(GuideFragmentSearchModeSetting.newInstance(false, null,
                true, getString(R.string.follow_me_search_mode_second), true, getString(R.string.follow_me_search_mode_third_start),
                0,  null, getString(R.string.follow_me_search_mode_third_end)));

        mSectionsPagerAdapter.addFragment(guideFragmentSearchModeSetting.get(0));
        // 4.3.6.	寻星操作
        int satelliteStatus = SystemServices.getAntennaState();

        //switch (satelliteStatus){
            // 开始寻星
            // case 0:
                guideFragmentSearching.add(GuideFragmentSearching.newInstance(false, true, getString(R.string.follow_me_searching_first_line),
                        true, getString(R.string.follow_me_searching_second_line),
                        true, getString(R.string.follow_me_searching_third_start), 1, null, null));
                //break;
            // 寻星中
            //case 1:
        guideFragmentSearching.add(GuideFragmentSearching.newInstance(true, true, getString(R.string.follow_me_searching_ing_first_line),
                        true, getString(R.string.follow_me_searching_ing_second_line),
                        true, getString(R.string.follow_me_searching_ing_third_start), 2,  null, null));
                //break;
            // 锁星
            //case 2:
        guideFragmentSearching.add(GuideFragmentSearching.newInstance(true, true, getString(R.string.follow_me_searching_lock_first_line),
                        true, getString(R.string.follow_me_searching_lock_second_line),
                        true, getString(R.string.follow_me_searching_lock_third_start), -1,  null, null));
                //break;
            // 重设参数
            //case 3:
        guideFragmentSearching.add(GuideFragmentSearching.newInstance(true, true, getString(R.string.follow_me_searching_timeout_first_line),
                        true, getString(R.string.follow_me_searching_timeout_second_line),
                        true, getString(R.string.follow_me_searching_timeout_third_start), 3,
                        null, getString(R.string.follow_me_searching_timeout_third_end)));
                //break;
            // 故障报告
            //case 4:
        guideFragmentSearching.add(GuideFragmentSearching.newInstance(true, true, getString(R.string.follow_me_searching_error_first_line),
                        true, getString(R.string.follow_me_searching_error_second_line),
                        true, getString(R.string.follow_me_searching_error_third_start), 4,  null, null));
                //break;
        //}
        mSectionsPagerAdapter.addFragment(guideFragmentSearching.get(satelliteStatus));

        // 4.3.7.	锁紧操作
        int lockerState = SystemServices.getLockerState();
        //switch (lockerState){
            //case 0:
                guideFragmentLocker.add(GuideFragmentLocker.newInstance(true, getString(R.string.follow_me_locker_open_first_line),
                        true, getString(R.string.follow_me_locker_open_second_line),
                        true, getString(R.string.follow_me_locker_open_third_start), -1,  null, null));
                //break;
            //case 1:
                guideFragmentLocker.add(GuideFragmentLocker.newInstance(true, getString(R.string.follow_me_locker_lock_first_line),
                        true, getString(R.string.follow_me_locker_lock_second_line),
                        true, getString(R.string.follow_me_locker_lock_third_start), -1,  null, null));
                //break;
        //}
        mSectionsPagerAdapter.addFragment(guideFragmentLocker.get(lockerState));

        // 4.3.8.	节能操作
        int savingState = SystemServices.getSavingState();
       //switch (savingState){
        //    case 0:
        guideFragmentSaving.add(GuideFragmentSaving.newInstance(true, getString(R.string.follow_me_saving_open_first_line),
                        true, getString(R.string.follow_me_saving_second_line),
                        true, getString(R.string.follow_me_saving_third_start), 0,  null, getString(R.string.follow_me_saving_third_end)));
         //       break;
        //    case 1:
        guideFragmentSaving.add(GuideFragmentSaving.newInstance(true, getString(R.string.follow_me_saving_close_first_line),
                        true, getString(R.string.follow_me_saving_second_line),
                        true, getString(R.string.follow_me_saving_third_start), 0,  null, getString(R.string.follow_me_saving_third_end)));
         //       break;
        //}
        mSectionsPagerAdapter.addFragment(guideFragmentSaving.get(savingState));

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
        this.startPosition = position;
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        // jumpTo(position);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewPager != null) {
            FragmentManager fmanager = getSupportFragmentManager();
            FragmentTransaction ftransaction = fmanager.beginTransaction();
            ftransaction.replace(R.id.follow_me_view_pager, (Fragment)fragments[startPosition].get(0)).commit();
            // mViewPager.setCurrentItem(startPosition);
        }
    }
}
