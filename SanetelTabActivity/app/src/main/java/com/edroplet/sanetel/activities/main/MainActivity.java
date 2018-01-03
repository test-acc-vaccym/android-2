package com.edroplet.sanetel.activities.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.activities.functions.FunctionsActivity;
import com.edroplet.sanetel.adapters.MainViewPagerAdapter;
import com.edroplet.sanetel.fragments.main.MainFragmentGuide;
import com.edroplet.sanetel.fragments.main.MainFragmentMe;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.SystemServices;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomTextView;
import com.edroplet.sanetel.view.custom.WeChatRadioGroup;

import static com.edroplet.sanetel.utils.CustomSP.WifiSettingsNameKey;

public class MainActivity extends AppCompatActivity {
    public static String defaultDeviceName = "XWWT-XXX";

    private ViewPager viewPager;
    private WeChatRadioGroup gradualRadioGroup;
    private MenuItem menuItem;
    private CustomTextView guideToolbarTitle;
    private CustomButton guideEnterMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gradualRadioGroup = (WeChatRadioGroup) findViewById(R.id.main_navigation);
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        guideToolbarTitle = (CustomTextView) findViewById(R.id.guide_toolbar_title);
        guideEnterMain = (CustomButton) findViewById(R.id.guide_enter_main);

        setupViewPager(viewPager);
        gradualRadioGroup.setViewPager(viewPager);

        setToolbar();
    }


    private void setupViewPager(ViewPager viewPager) {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        // adapter.addFragment(MainFragmentGuide.newInstance(""));
        // 引导页
        adapter.addFragment(MainFragmentGuide.newInstance(""));
        // 开始使用
        // adapter.addFragment(MainFragmentStart.newInstance(""));
        // 我的设备
        adapter.addFragment(MainFragmentMe.newInstance(""));
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /** 切换以后才修改
                switch (position){
                    case 0:
                        guideToolbarTitle.setText(getString(R.string.follow_me_bottom_nav_main));
                        guideEnterMain.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        guideToolbarTitle.setText(getString(R.string.main_bottom_nav_me));
                        guideEnterMain.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                */
            }

            @Override
            public void onPageSelected(int position) {

                switch (position){
                    case 0:
                        guideToolbarTitle.setText(getString(R.string.follow_me_bottom_nav_main));
                        guideEnterMain.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        guideToolbarTitle.setText(getString(R.string.main_bottom_nav_me));
                        guideEnterMain.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private DialogInterface.OnClickListener mCancelClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            Toast.makeText(MainActivity.this, getString(R.string.cancel_button_prompt), Toast.LENGTH_SHORT).show();
        }
    };


    private DialogInterface.OnClickListener mCancelClickListenerThenContinue = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            Toast.makeText(MainActivity.this, getString(R.string.cancel_button_prompt), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, FunctionsActivity.class));
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 根据上面发送过去的请求吗来区别
        switch (requestCode) {
            case SystemServices.REQUEST_WIFI_CONNECT_HELP:
                SystemServices.checkConnectedSsid(this,
                        CustomSP.getString(this, WifiSettingsNameKey, defaultDeviceName),
                        this, mCancelClickListener, SystemServices.REQUEST_WIFI_CONNECT_MANAGER_DIRECT_IN);
                break;
            case SystemServices.REQUEST_WIFI_CONNECT_MANAGER_DIRECT_IN:
                    Log.e("XXXXSystemServices", "onActivityResult: " + resultCode);
                    String ssid = SystemServices.getConnectWifiSsid(this);
                    if (ssid.toUpperCase().startsWith(SystemServices.XWWT_PREFIX)) {
                        Toast.makeText(this, getString(R.string.main_connected_ssid_prompt) + ssid, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, FunctionsActivity.class));
                    } else {
                        SystemServices.checkConnectedSsid(MainActivity.this,
                                CustomSP.getString(MainActivity.this, WifiSettingsNameKey, defaultDeviceName),
                                MainActivity.this, mCancelClickListener, requestCode);
                    }
                break;
            default:
                break;
        }
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.guide_tool_bar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.guide_enter_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/11/4 判断WIFI连接
                // 点击直接进入，检测wifi连接
                SystemServices.checkConnectedSsid(MainActivity.this,
                        CustomSP.getString(MainActivity.this, WifiSettingsNameKey, defaultDeviceName),
                        MainActivity.this, mCancelClickListenerThenContinue,
                        SystemServices.REQUEST_WIFI_CONNECT_MANAGER_DIRECT_IN);
            }
        });
    }
}
