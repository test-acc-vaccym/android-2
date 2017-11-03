package com.edroplet.qxx.saneteltabactivity.activities.functions;

import android.content.Intent;
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
import com.edroplet.qxx.saneteltabactivity.beans.PresetAngleInfo;
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
    public static String PRESET_AZIMUTH = "azimuth";
    public static String PRESET_PITCH = "pitch";
    public static String PRESET_POLARIZATION = "polarization";
    private float azimuth = 0.000f;
    private float pitch = 0.000f;
    private float polarization = 0.000f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        StatusBarControl.setupToolbar(this, R.id.main_content_toolbar);
        OperateBarControl.setupOperatorBar(this);

        viewPager = (ViewPager) findViewById(R.id.manual_viewpager);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);

        if(bundle != null && bundle.containsKey(PRESET_AZIMUTH))
            azimuth = bundle.getFloat(PRESET_AZIMUTH);
        if(bundle != null && bundle.containsKey(PRESET_PITCH))
            pitch = bundle.getFloat(PRESET_PITCH);
        if(bundle != null && bundle.containsKey(PRESET_POLARIZATION))
            polarization = bundle.getFloat(PRESET_POLARIZATION);

        setupViewPager(viewPager);


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

        PresetAngleInfo presetAngleInfo = new PresetAngleInfo();
        presetAngleInfo.setAzimuth(azimuth).setPitch(pitch).setPolarization(polarization);
        adapter.addFragment(LocationControlFragment.newInstance(presetAngleInfo));

        adapter.addFragment(AngleCalculateFragment.newInstance(new AntennaInfo()));
        viewPager.setAdapter(adapter);
    }
}
