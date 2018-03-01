package com.edroplet.sanetel.activities.settings;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import com.edroplet.sanetel.BaseActivity;
import com.edroplet.sanetel.R;
import com.edroplet.sanetel.adapters.MainViewPagerAdapter;
import com.edroplet.sanetel.fragments.settings.SettingsFragmentAmplifierInterfere;
import com.edroplet.sanetel.fragments.settings.SettingsFragmentAmplifierEmit;
import com.edroplet.sanetel.utils.ImageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PowerAmplifierSettingsActivity extends BaseActivity {
    @BindView(R.id.settings_power_amplifier_bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.content_toolbar)
    Toolbar toolbar;

    private MainViewPagerAdapter mAdapter;

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

    //Fragment 数组
    private final Fragment[] TAB_FRAGMENTS = new Fragment[] {SettingsFragmentAmplifierInterfere.newInstance(null)
            , SettingsFragmentAmplifierEmit.newInstance(null)};

    private int COUNT = TAB_FRAGMENTS.length;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  始终保持竖屏
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        mViewPager.setCurrentItem(startPosition);
    }

    private MenuItem menuItem;
    private void initView(){

        mAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(TAB_FRAGMENTS[0]);
        mAdapter.addFragment(TAB_FRAGMENTS[1]);
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
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Toast.makeText(getBaseContext(),"No Help", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
