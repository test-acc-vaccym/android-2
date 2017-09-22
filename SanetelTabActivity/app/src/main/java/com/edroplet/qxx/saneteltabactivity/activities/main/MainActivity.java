package com.edroplet.qxx.saneteltabactivity.activities.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.adapters.MainViewPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;
import com.edroplet.qxx.saneteltabactivity.fragments.main.MainFragmentGuide;
import com.edroplet.qxx.saneteltabactivity.fragments.main.MainFragmentMe;
import com.edroplet.qxx.saneteltabactivity.fragments.main.MainFragmentStart;
import com.edroplet.qxx.saneteltabactivity.fragments.manual.AngleCalculateFragment;
import com.edroplet.qxx.saneteltabactivity.fragments.manual.LocationControlFragment;
import com.edroplet.qxx.saneteltabactivity.fragments.manual.SpeedControlFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.main_bottom_nav_guide:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.main_bottom_nav_start:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.main_bottom_nav_me:
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
        setupViewPager(viewPager);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private void setupViewPager(ViewPager viewPager) {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(MainFragmentGuide.newInstance(""));
        adapter.addFragment(MainFragmentStart.newInstance(""));
        adapter.addFragment(MainFragmentMe.newInstance(""));
        viewPager.setAdapter(adapter);
    }


}
