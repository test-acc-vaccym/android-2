package com.edroplet.qxx.saneteltabactivity.activities.functions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.adapters.MainViewPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;
import com.edroplet.qxx.saneteltabactivity.control.OperateBarControl;
import com.edroplet.qxx.saneteltabactivity.control.StatusBarControl;
import com.edroplet.qxx.saneteltabactivity.fragments.manual.AngleCalculateFragment;
import com.edroplet.qxx.saneteltabactivity.fragments.manual.LocationControlFragment;
import com.edroplet.qxx.saneteltabactivity.fragments.manual.SpeedControlFragment;
import com.edroplet.qxx.saneteltabactivity.utils.BottomNavigationViewHelper;

public class ManualActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private MainViewPagerAdapter adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_speed_manual:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_location_manual:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_calculate_manual:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (menuItem != null){
                menuItem.setChecked(false);
            }else {
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
            }
            menuItem = bottomNavigationView.getMenu().getItem(position);
            menuItem.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };
    public static String POSITION = "position";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        Bundle bundle = this.getIntent().getExtras();

        StatusBarControl.setupToolbar(this, R.id.main_content_toolbar);
        OperateBarControl.setupOperatorBar(this);

        viewPager = (ViewPager) findViewById(R.id.manual_viewpager);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
        setupViewPager(viewPager);
        //禁止ViewPager滑动
        //        viewPager.setOnTouchListener(new View.OnTouchListener() {
        //            @Override
        //            public boolean onTouch(View v, MotionEvent event) {
        //                return true;
        //            }
        //        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.manual_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        NestedScrollView sv = (NestedScrollView) findViewById(R.id.speed_info_scroll);
        if (sv != null)
            sv.smoothScrollTo(0,0);



        int position = bundle.getInt(POSITION);
        if (position < 0){
            position = 0;
        }else if (position >= adapter.getCount()){
            position = adapter.getCount() - 1;
        }
        viewPager.setCurrentItem(position);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MainViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(SpeedControlFragment.newInstance(new AntennaInfo()));
        adapter.addFragment(LocationControlFragment.newInstance(new AntennaInfo()));
        adapter.addFragment(AngleCalculateFragment.newInstance(new AntennaInfo()));
        viewPager.setAdapter(adapter);
    }
}
