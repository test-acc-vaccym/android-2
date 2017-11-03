package com.edroplet.qxx.saneteltabactivity.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.adapters.MainViewPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;
import com.edroplet.qxx.saneteltabactivity.fragments.main.MainFragmentGuide;
import com.edroplet.qxx.saneteltabactivity.fragments.main.MainFragmentMe;
import com.edroplet.qxx.saneteltabactivity.fragments.main.MainFragmentStart;
import com.edroplet.qxx.saneteltabactivity.fragments.manual.AngleCalculateFragment;
import com.edroplet.qxx.saneteltabactivity.fragments.manual.LocationControlFragment;
import com.edroplet.qxx.saneteltabactivity.fragments.manual.SpeedControlFragment;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;
import com.edroplet.qxx.saneteltabactivity.view.custom.WeChatRadioGroup;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private WeChatRadioGroup gradualRadioGroup;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gradualRadioGroup = (WeChatRadioGroup) findViewById(R.id.main_navigation);
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        setupViewPager(viewPager);
        gradualRadioGroup.setViewPager(viewPager);
        // 进入该界面，检测wifi连接
        SystemServices.checkConnectedSsid(this, MainFragmentGuide.device, this);
    }


    private void setupViewPager(ViewPager viewPager) {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        // adapter.addFragment(MainFragmentGuide.newInstance(""));
        adapter.addFragment(MainFragmentGuide.newInstance(""));
        adapter.addFragment(MainFragmentStart.newInstance(""));
        adapter.addFragment(MainFragmentMe.newInstance(""));
        viewPager.setAdapter(adapter);
    }
    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 根据上面发送过去的请求吗来区别
        switch (requestCode) {
            case SystemServices.REQUEST_WIFI_CONNECT_HELP:
                SystemServices.checkConnectedSsid(this,MainFragmentGuide.device, this);
                break;
            case SystemServices.REQUEST_WIFI_CONNECT_MANAGER:
                Toast.makeText(this, getString(R.string.main_connected_ssid_prompt) + SystemServices.getConnectWifiSsid(this), Toast.LENGTH_SHORT).show();
            default:
                break;
        }
    }
}
