package com.edroplet.qxx.saneteltabactivity.activities.administrator;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.settings.AdministratorLoginActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.AntennaRestartActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.LowNoiseBlockOscillatorActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.PowerAmplifierSettingsActivity;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;

import static com.edroplet.qxx.saneteltabactivity.activities.administrator.AdministratorSettingsActivity.AdministratorSettingsPosition;

public class AdministratorActivity extends AppCompatActivity implements View.OnClickListener{

    @BindId(R.id.administrator_antenna_incriminate)
    private CustomButton administratorAntennaIncriminate;

    @BindId(R.id.administrator_antenna_type)
    private CustomButton administratorAntennaType;

    @BindId(R.id.administrator_band_select)
    private CustomButton administratorBandSelect;

    @BindId(R.id.administrator_ip_settings)
    private CustomButton administratorIpSettings;

    @BindId(R.id.administrator_lnb_frequency_channel)
    private CustomButton administratorLnbFrequencyChannle;

    @BindId(R.id.administrator_network_protocol)
    private CustomButton administratorNetworkProtocol;

    @BindId(R.id.administrator_serial_protocol)
    private CustomButton administratorSerialProtocol;

    @BindId(R.id.administrator_searching_range)
    private CustomButton administratorSearhingRange;

    @BindId(R.id.administrator_recovery_factory)
    private CustomButton administratorRecoveryFactory;

    @BindId(R.id.administrator_wifi_name)
    private CustomButton administratorWifiName;

    @BindId(R.id.administrator_amplifier_monitor)
    private CustomButton administratorAmplifierMonitor;

    @BindId(R.id.administrator_toolbar)
    private Toolbar administratorToolbar;

    @BindId(R.id.administrator_amplifier_factory)
    private CustomButton administratorAmplifierFactory;

    @BindId(R.id.administrator_amplifier_oscillator)
    private CustomButton administratorAmplifierOscillator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        ViewInject.inject(this,this);

        findViewById(R.id.main_settings_antenna_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdministratorActivity.this, AntennaRestartActivity.class));
            }
        });

        administratorToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        administratorAntennaIncriminate.setOnClickListener(this);
        administratorRecoveryFactory.setOnClickListener(this);
        administratorAntennaType.setOnClickListener(this);
        administratorWifiName.setOnClickListener(this);
        administratorIpSettings.setOnClickListener(this);
        administratorNetworkProtocol.setOnClickListener(this);
        administratorSerialProtocol.setOnClickListener(this);
        administratorBandSelect.setOnClickListener(this);
        administratorLnbFrequencyChannle.setOnClickListener(this);
        administratorSearhingRange.setOnClickListener(this);
        administratorAmplifierMonitor.setOnClickListener(this);
        administratorAmplifierFactory.setOnClickListener(this);
        administratorAmplifierOscillator.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AdministratorSettingsActivity.class);
        switch (v.getId()){
            case R.id.administrator_amplifier_factory:
                intent.putExtra(AdministratorSettingsPosition, AdministratorSettingsActivity.amplifierFactoryPosition);
                break;
            case R.id.administrator_amplifier_oscillator:
                intent.putExtra(AdministratorSettingsPosition, AdministratorSettingsActivity.amplifierOscillatorPosition);
                break;
            case R.id.administrator_antenna_incriminate:
                intent.putExtra(AdministratorSettingsPosition, AdministratorSettingsActivity.antennaIncriminatePosition);
                break;
            case R.id.administrator_recovery_factory:
                intent.putExtra(AdministratorSettingsPosition, AdministratorSettingsActivity.recoveryFactoryPosition);
                break;
            case R.id.administrator_antenna_type:
                intent.putExtra(AdministratorSettingsPosition, AdministratorSettingsActivity.antennaTypePosition);
                break;
            case R.id.administrator_wifi_name:
                intent.putExtra(AdministratorSettingsPosition, AdministratorSettingsActivity.wifiNamePosition);
                break;
            case R.id.administrator_ip_settings:
                intent.putExtra(AdministratorSettingsPosition, AdministratorSettingsActivity.ipSettingsPosition);
                break;
            case R.id.administrator_network_protocol:
                intent.putExtra(AdministratorSettingsPosition, AdministratorSettingsActivity.networkProtocolPosition);
                break;
            case R.id.administrator_serial_protocol:
                intent.putExtra(AdministratorSettingsPosition, AdministratorSettingsActivity.serialProtocolPosition);
                break;
            case R.id.administrator_band_select:
                intent.putExtra(AdministratorSettingsPosition, AdministratorSettingsActivity.bandSelectPosition);
                break;
            case R.id.administrator_lnb_frequency_channel:
                intent.putExtra(AdministratorSettingsPosition, AdministratorSettingsActivity.lnbPosition);
                break;
            case R.id.administrator_searching_range:
                intent.putExtra(AdministratorSettingsPosition, AdministratorSettingsActivity.searchingRangePosition);
                break;
            case R.id.administrator_amplifier_monitor:
                intent.putExtra(AdministratorSettingsPosition, AdministratorSettingsActivity.amplifierMonitorPosition);
                break;
            default:
                break;
        }

        if (intent != null){
            startActivity(intent);
        }
    }
}
