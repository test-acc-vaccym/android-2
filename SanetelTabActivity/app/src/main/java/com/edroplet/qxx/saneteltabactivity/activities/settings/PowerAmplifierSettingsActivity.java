package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.adapters.MainViewPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.adapters.SectionsPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.control.OperateBarControl;
import com.edroplet.qxx.saneteltabactivity.control.StatusBarControl;
import com.edroplet.qxx.saneteltabactivity.fragments.settings.SettingsFragmentAmplifierInterfere;
import com.edroplet.qxx.saneteltabactivity.fragments.settings.SettingsFragmentAmplifierManufacture;
import com.edroplet.qxx.saneteltabactivity.fragments.settings.SettingsFragmentAmplifierOscillator;
import com.edroplet.qxx.saneteltabactivity.fragments.settings.SettingsFragmentAmplifiereEmit;
import com.edroplet.qxx.saneteltabactivity.utils.hashMapUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class PowerAmplifierSettingsActivity extends AppCompatActivity {
    public static Toolbar toolbar;
    private static LinkedHashMap<String, String> map = new LinkedHashMap<>();
    private FloatingActionButton fab;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private MainViewPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private String snackHelpMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  始终保持竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_power_amplifier);
        StatusBarControl.setupToolbar(this,R.id.content_toolbar);
        initView();
        StatusBarControl.setTitle(hashMapUtils.getElementFromLinkHashMap(map,0).getKey());
        setupFab();

    }


    public PowerAmplifierSettingsActivity initView(){
        map.put("功放厂家","更换参数，点击▲ 设置永久生效。");
        map.put("功放本振","更换参数，点击▲ 设置永久生效。");
        map.put("邻星干扰","更换参数，点击▲ 设置永久生效。");
        map.put("发射开关","更换参数，点击▲ 设置永久生效。");

        mSectionsPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(SettingsFragmentAmplifierManufacture.newInstance(null));
        mSectionsPagerAdapter.addFragment(SettingsFragmentAmplifierOscillator.newInstance(null));
        mSectionsPagerAdapter.addFragment(SettingsFragmentAmplifierInterfere.newInstance(null));
        mSectionsPagerAdapter.addFragment(SettingsFragmentAmplifiereEmit.newInstance(null));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.amplifier_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        if (tabLayout.getChildCount()>4)
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        else
            tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Map.Entry<String , String > entry = hashMapUtils.getElementFromLinkHashMap(map,tab.getPosition());
                // 这儿使用getSupportedActionBar没有用
                if (entry != null && toolbar != null)
                    toolbar.setTitle(entry.getKey());
                if (tab.getPosition() == 3){
                    fab.setVisibility(View.VISIBLE);
                }else {
                    fab.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        // 初始化快捷键
        OperateBarControl.setupOperatorBar(this);
        return this;
    }

    public PowerAmplifierSettingsActivity setupFab(){
        fab = (FloatingActionButton) findViewById(R.id.amplifier_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PowerAmplifierSettingsActivity.this, SettingsPowerAmplifierEmitHelpActivity.class));
//                if (snackHelpMessage == null || snackHelpMessage.isEmpty()) {
//                    snackHelpMessage = "Replace with your own action";
//                }
//                Snackbar.make(view, snackHelpMessage, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        return this;
    }

    public PowerAmplifierSettingsActivity setHelpMessage(String message){
        this.snackHelpMessage = message;
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
}
