package com.edroplet.sanetel.activities.functions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.adapters.MainViewPagerAdapter;
import com.edroplet.sanetel.beans.AntennaInfo;
import com.edroplet.sanetel.beans.PresetAngleInfo;
import com.edroplet.sanetel.control.OperateBarControl;
import com.edroplet.sanetel.control.StatusBarControl;
import com.edroplet.sanetel.fragments.functions.manual.AngleCalculateFragment;
import com.edroplet.sanetel.fragments.functions.manual.LocationControlFragment;
import com.edroplet.sanetel.fragments.functions.manual.SpeedControlFragment;
import com.edroplet.sanetel.fragments.functions.manual.StepControlFragment;
import com.edroplet.sanetel.utils.BottomNavigationViewHelper;
import com.edroplet.sanetel.utils.CustomSP;

public class ManualActivity extends AppCompatActivity {
    public static final int stepIndex = 0;
    public static final int speedIndex = 1;
    public static final int locationIndex = 2;
    public static final int calculateIndex = 3;

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private MainViewPagerAdapter adapter;
    Context context;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_step_control:
                    viewPager.setCurrentItem(stepIndex);
                    return true;
                case R.id.navigation_speed_manual:
                    viewPager.setCurrentItem(speedIndex);
                    return true;
                case R.id.navigation_location_manual:
                    viewPager.setCurrentItem(locationIndex);
                    return true;
                case R.id.navigation_calculate_manual:
                    viewPager.setCurrentItem(calculateIndex);
                    return true;
            }
            return false;
        }

    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position == locationIndex){
                azimuth = CustomSP.getFloat(context, PRESET_AZIMUTH, azimuth);
                pitch = CustomSP.getFloat(context, PRESET_PITCH, pitch);
                polarization = CustomSP.getFloat(context, PRESET_POLARIZATION, polarization);
            }
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
            if (position == locationIndex){
                azimuth = CustomSP.getFloat(context, PRESET_AZIMUTH, azimuth);
                pitch = CustomSP.getFloat(context, PRESET_PITCH, pitch);
                polarization = CustomSP.getFloat(context, PRESET_POLARIZATION, polarization);
            }
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
        context = this;

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        StatusBarControl.setupToolbar(this, R.id.main_content_toolbar);

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

    @Override
    protected void onResume() {
        super.onResume();
        OperateBarControl.setupOperatorBar(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MainViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(StepControlFragment.newInstance(new AntennaInfo()));
        adapter.addFragment(SpeedControlFragment.newInstance(new AntennaInfo()));

        PresetAngleInfo presetAngleInfo = new PresetAngleInfo();
        presetAngleInfo.setAzimuth(azimuth).setPitch(pitch).setPolarization(polarization);
        adapter.addFragment(LocationControlFragment.newInstance(presetAngleInfo));

        adapter.addFragment(AngleCalculateFragment.newInstance(new AntennaInfo()));
        viewPager.setAdapter(adapter);
    }
}
