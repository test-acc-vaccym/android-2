package com.edroplet.sanetel.activities.administrator;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.adapters.MainViewPagerAdapter;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentAmplifierManufacturer;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentAmplifierMonitor;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentAmplifierOscillator;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentAntennaIncriminate;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentAntennaType;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentBandSelect;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentFactoryIncriminate;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentIPSettings;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentLNBOscillator;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentNetworkProtocolSettings;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentRecoveryFactory;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentSearchingRange;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentSerialProtocolSettings;
import com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentWifiSettings;

import com.edroplet.sanetel.view.custom.CustomViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdministratorSettingsActivity extends AppCompatActivity {
    public static final String AdministratorSettingsPosition = "position";
    public static final int antennaIncriminatePosition = 0;
    public static final int recoveryFactoryPosition = 1;
    public static final int antennaTypePosition = 2;
    public static final int wifiNamePosition = 3;
    public static final int ipSettingsPosition = 4;
    public static final int networkProtocolPosition = 5;
    public static final int serialProtocolPosition = 6;
    public static final int bandSelectPosition = 7;
    public static final int lnbPosition = 8;
    public static final int searchingRangePosition = 9;
    public static final int amplifierMonitorPosition = 10;
    public static final int amplifierFactoryPosition = 11;
    public static final int amplifierOscillatorPosition = 12;
    public static final int factoryIncriminatePosition = 13;

    private static final @IdRes int[] TITLE = {R.string.administrator_antenna_incriminate,
            R.string.administrator_recovery_factory,R.string.administrator_antenna_type,
            R.string.administrator_wifi_name, R.string.administrator_ip_settings,
            R.string.administrator_network_protocol, R.string.administrator_serial_protocol,
            R.string.administrator_band_select, R.string.main_settings_lnb_oscillator,
            R.string.administrator_searching_range, R.string.administrator_amplifier_monitor,
            R.string.main_settings_amplifier_factory,R.string.main_settings_amplifier_oscillator,
            R.string.administrator_factory_incriminate};

    @BindView(R.id.id_administrator_settings_viewpager)
    CustomViewPager viewPager;

    private MenuItem menuItem;

    @BindView(R.id.id_administrator_settings_toolbar)
    Toolbar toolbar;

    private int startPosition;

    private int COUNT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_settings);

        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //禁止ViewPager滑动
        viewPager.setScanScroll(false);

        viewPager.setAdapter(setupAdapter());

        if(savedInstanceState == null){

            startPosition = getIntent().getIntExtra(AdministratorSettingsPosition, 0);
            if (startPosition >= COUNT) {
                startPosition = COUNT - 1;
            }
        }


        viewPager.setCurrentItem(startPosition);
    }

    private MainViewPagerAdapter setupAdapter() {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        // 0 天线标定
        adapter.addFragment(AdministratorFragmentAntennaIncriminate.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        // 1 恢复出厂
        adapter.addFragment(AdministratorFragmentRecoveryFactory.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.administrator_setting_recovery_factory_third_button), getString(R.string.settings_to_be_working)));
        // 2 天线类型
        adapter.addFragment(AdministratorFragmentAntennaType.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        // 3 WiFi设置
        adapter.addFragment(AdministratorFragmentWifiSettings.newInstance(false,null,
                true, getString(R.string.administrator_setting_wifi_name_second_line),
                true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        // 4 ip设置
        adapter.addFragment(AdministratorFragmentIPSettings.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        // 5 网口协议
        adapter.addFragment(AdministratorFragmentNetworkProtocolSettings.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        // 6 串口协议
        adapter.addFragment(AdministratorFragmentSerialProtocolSettings.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        // 7 波段选择
        adapter.addFragment(AdministratorFragmentBandSelect.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        // 8 LNB本振
        adapter.addFragment(AdministratorFragmentLNBOscillator.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        // 9 巡行范围
        adapter.addFragment(AdministratorFragmentSearchingRange.newInstance(false,null,
                true, getString(R.string.administrator_setting_searching_range_second_line), true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        // 10 功放监控
        adapter.addFragment(AdministratorFragmentAmplifierMonitor.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));
        // 11 功放厂家
        adapter.addFragment(AdministratorFragmentAmplifierManufacturer.newInstance());
        // 12 功放本振
        adapter.addFragment(AdministratorFragmentAmplifierOscillator.newInstance());
        // 13 厂家标定
        adapter.addFragment(AdministratorFragmentFactoryIncriminate.newInstance(false,null,
                false, null, true, getString(R.string.follow_me_message_click),-1,
                getString(R.string.setting_button_text), getString(R.string.settings_to_be_working)));

        COUNT = adapter.getCount();
        return adapter;
    }

}
