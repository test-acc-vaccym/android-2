package com.edroplet.qxx.saneteltabactivity.activities.administrator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.functions.MonitorHelpActivity;
import com.edroplet.qxx.saneteltabactivity.adapters.MainViewPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.control.OperateBarControl;
import com.edroplet.qxx.saneteltabactivity.fragments.administrator.AdministratorFragmentAntennaIncriminate;
import com.edroplet.qxx.saneteltabactivity.fragments.administrator.AdministratorFragmentAntennaType;
import com.edroplet.qxx.saneteltabactivity.fragments.administrator.AdministratorFragmentBandSelect;
import com.edroplet.qxx.saneteltabactivity.fragments.administrator.AdministratorFragmentIPSettings;
import com.edroplet.qxx.saneteltabactivity.fragments.administrator.AdministratorFragmentLNBFrequencyChannel;
import com.edroplet.qxx.saneteltabactivity.fragments.administrator.AdministratorFragmentNetworkProtocolSettings;
import com.edroplet.qxx.saneteltabactivity.fragments.administrator.AdministratorFragmentRecoveryFactory;
import com.edroplet.qxx.saneteltabactivity.fragments.administrator.AdministratorFragmentSearchingRange;
import com.edroplet.qxx.saneteltabactivity.fragments.administrator.AdministratorFragmentSerialProtocolSettings;
import com.edroplet.qxx.saneteltabactivity.fragments.administrator.AdministratorFragmentWifiSettings;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomFAB;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomViewPager;

public class AdministratorSettingsActivity extends AppCompatActivity {
    public static final String AdministratorSettingsPosition = "position";

    private static final @IdRes int[] TITLE = {R.string.administrator_antenna_incriminate,
            R.string.administrator_recovery_factory,R.string.administrator_antenna_type,
            R.string.administrator_wifi_name, R.string.administrator_ip_settings,
            R.string.administrator_network_protocol, R.string.administrator_serial_protocol,
            R.string.administrator_band_select, R.string.administrator_lnb_frequency_channel,
            R.string.administrator_searching_range};

    @BindId(R.id.administrator_settings_viewpager)
    private CustomViewPager viewPager;

    private MenuItem menuItem;

    @BindId(R.id.administrator_settings_fab)
    private CustomFAB customFAB;

    @BindId(R.id.administrator_settings_toolbar)
    private Toolbar toolbar;


    private int startPosition;


    private int COUNT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_settings);

        ViewInject.inject(this, this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0){
                    customFAB.setVisibility(View.VISIBLE);
                }else {
                    customFAB.setVisibility(View.INVISIBLE);
                }
                toolbar.setTitle(TITLE[position]);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        //禁止ViewPager滑动
        viewPager.setScanScroll(false);

        setupViewPager(viewPager);

        if(savedInstanceState == null){

            startPosition = getIntent().getIntExtra(AdministratorSettingsPosition, 0);
            if (startPosition >= COUNT) {
                startPosition = COUNT - 1;
            }
        }

        OperateBarControl.setupOperatorBar(this);

        customFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdministratorSettingsActivity.this, MonitorHelpActivity.class);
                startActivity(intent);
            }
        });

        viewPager.setCurrentItem(startPosition);
    }

    private void setupViewPager(ViewPager viewPager) {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        // 天线标定
        adapter.addFragment(AdministratorFragmentAntennaIncriminate.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        adapter.addFragment(AdministratorFragmentRecoveryFactory.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.administrator_setting_recovery_factory_third_button), getString(R.string.settings_to_be_working)));
        adapter.addFragment(AdministratorFragmentAntennaType.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        adapter.addFragment(AdministratorFragmentWifiSettings.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        adapter.addFragment(AdministratorFragmentIPSettings.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        adapter.addFragment(AdministratorFragmentNetworkProtocolSettings.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        adapter.addFragment(AdministratorFragmentSerialProtocolSettings.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        adapter.addFragment(AdministratorFragmentBandSelect.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        adapter.addFragment(AdministratorFragmentLNBFrequencyChannel.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        adapter.addFragment(AdministratorFragmentSearchingRange.newInstance(false,null,
                true, getString(R.string.administrator_setting_searching_range_second_line), true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));

        viewPager.setAdapter(adapter);
        COUNT = adapter.getCount();
    }

}
