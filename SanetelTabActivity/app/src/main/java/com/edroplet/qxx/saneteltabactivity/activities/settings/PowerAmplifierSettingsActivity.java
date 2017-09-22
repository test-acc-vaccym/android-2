package com.edroplet.qxx.saneteltabactivity.activities.settings;

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
import com.edroplet.qxx.saneteltabactivity.adapters.SectionsPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.control.OperateBarControl;
import com.edroplet.qxx.saneteltabactivity.control.StatusBarControl;
import com.edroplet.qxx.saneteltabactivity.utils.hashMapUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class PowerAmplifierSettingsActivity extends AppCompatActivity {
    public static Toolbar toolbar;
    private static LinkedHashMap<String, String> map = new LinkedHashMap<>();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private String snackHelpMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanetel_tab);
        StatusBarControl.setupToolbar(this,R.id.content_toolbar);
        initView();
        StatusBarControl.setTitle(hashMapUtils.getElemntFromLinkHashMap(map,0).getKey());
//        setupFab();

    }


    public PowerAmplifierSettingsActivity initView(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        map.put("功放厂家","更换参数，点击▲ 设置永久生效。");
        map.put("功放本振","更换参数，点击▲ 设置永久生效。");
        map.put("邻星干扰","更换参数，点击▲ 设置永久生效。");
        map.put("发射开关","更换参数，点击▲ 设置永久生效。");
        mSectionsPagerAdapter.setTab(map);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
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
                Map.Entry<String , String > entry = hashMapUtils.getElemntFromLinkHashMap(map,tab.getPosition());
                // 这儿使用getSupportedActionBar没有用
                if (entry != null && toolbar != null)
                    toolbar.setTitle(entry.getKey());
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (snackHelpMessage == null || snackHelpMessage.isEmpty()) {
                    snackHelpMessage = "Replace with your own action";
                }
                Snackbar.make(view, snackHelpMessage, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return this;
    }

    public PowerAmplifierSettingsActivity setHelpMessage(String message){
        this.snackHelpMessage = message;
        return this;
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sanetel_tab, menu);
        return true;
    }
    */

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
