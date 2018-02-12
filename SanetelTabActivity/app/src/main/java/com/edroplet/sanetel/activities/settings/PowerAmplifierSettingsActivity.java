package com.edroplet.sanetel.activities.settings;

import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.fragments.settings.SettingsFragmentAmplifierInterfere;
import com.edroplet.sanetel.fragments.settings.SettingsFragmentAmplifierEmit;
import com.edroplet.sanetel.utils.ImageUtil;
import com.edroplet.sanetel.view.BottomNavigationViewEx;
import com.edroplet.sanetel.view.custom.CustomFAB;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PowerAmplifierSettingsActivity extends AppCompatActivity {
//    @BindView(R.id.amplifier_fab)
//    CustomFAB fab;

    @BindView(R.id.settings_power_amplifier_bottom_navigation)
    BottomNavigationViewEx bottomNavigationView;

    @BindView(R.id.content_toolbar)
    Toolbar toolbar;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    // private MainViewPagerAdapter mSectionsPagerAdapter;

    private MyViewPagerAdapter mAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @BindView(R.id.amplifier_view_pager)
    ViewPager mViewPager;

    private int startPosition;
    public static final String positionKey = "position";
    public static final int interfererPosition = 0;
    public static final int emitPosition = 1;
    //Tab 文字
    private final int[] TAB_TITLES = new int[]{
            R.string.main_settings_amplifier_interfere,
            R.string.main_settings_amplifier_emit,
    };
    //Tab 图片
    private final int[] TAB_IMGS = new int[]{
            R.drawable.tab_image_selector,
            R.drawable.tab_image_selector};
    //Fragment 数组
    private final Fragment[] TAB_FRAGMENTS = new Fragment[] {SettingsFragmentAmplifierInterfere.newInstance(null)
            , SettingsFragmentAmplifierEmit.newInstance(null)};

    private int COUNT = TAB_FRAGMENTS.length;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  始终保持竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_power_amplifier);

        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        if (savedInstanceState == null){
            startPosition = getIntent().getIntExtra(positionKey, 0);
            if (startPosition >= COUNT) {
                startPosition = COUNT - 1;
            }
        }
        bottomNavigationView.setItemHeight(ImageUtil.dip2px(this, 40));
        bottomNavigationView.enableAnimation(false);
        bottomNavigationView.enableItemShiftingMode(false);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.setTextSize(20);
        bottomNavigationView.setIconSize(0,0);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // fab.setVisibility(View.INVISIBLE);
                        switch (item.getItemId()) {
                            case R.id.settings_power_amplifier_bottom_navigation_interfere:
                                mViewPager.setCurrentItem(interfererPosition);
                                break;
                            case R.id.settings_power_amplifier_bottom_navigation_emit:
                                mViewPager.setCurrentItem(emitPosition);
                                // fab.setVisibility(View.VISIBLE);
                        }
                        return false;
                    }
                });
        bottomNavigationView.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(startPosition);
        // setupFab();
    }

    private MenuItem menuItem;
    public PowerAmplifierSettingsActivity initView(){

        mAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                toolbar.setTitle(TAB_TITLES[position]);
            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return this;
    }

    public PowerAmplifierSettingsActivity setupFab(){
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(PowerAmplifierSettingsActivity.this, SettingsPowerAmplifierEmitHelpActivity.class));
//            }
//        });
        return this;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            // TODO 显示帮助文档
            Toast.makeText(getBaseContext(),"No Help", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * @description: 设置添加Tab
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, int[] tabTitlees, int[] tabImgs){
        int count = tabImgs.length;
        if (count > tabTitlees.length){
            count = tabTitlees.length;
        }
        for (int i = 0; i < count; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = inflater.inflate(R.layout.settings_power_amplifier_tab_custom,null);
            tab.setCustomView(view);

            TextView tvTitle = view.findViewById(R.id.tv_tab);
            tvTitle.setText(tabTitlees[i]);
            ImageView imgTab = view.findViewById(R.id.img_tab);
            imgTab.setImageResource(tabImgs[i]);
            tabLayout.addTab(tab);
        }
    }

    /**
     * @description: ViewPager 适配器
     */
    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TAB_FRAGMENTS[position];
        }

        @Override
        public int getCount() {
            return TAB_FRAGMENTS.length;
        }
    }
}
