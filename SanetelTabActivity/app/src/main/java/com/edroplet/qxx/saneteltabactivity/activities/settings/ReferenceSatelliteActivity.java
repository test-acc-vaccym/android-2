package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

public class ReferenceSatelliteActivity extends AppCompatActivity {
    @BindId(R.id.settings_reference_toolbar)
    private Toolbar referenceToolbar;

    @BindId(R.id.pop_dialog_tv_second)
    private CustomTextView secondLine;

    @BindId(R.id.pop_dialog_tv_third_end)
    private CustomTextView thirdEnd;

    @BindId(R.id.reference_satellite_select_radio_group)
    private RadioButton satelliteSelect;

    @BindId(R.id.reference_satellite_select_satellites)
    private Spinner satellitesSpinner;

    @BindId(R.id.reference_satellite_select_satellites_polarization)
    private Spinner satellitesPolarizationSpinner;

    @BindId(R.id.settings_reference_latitude)
    private CustomTextView latitude;
    @BindId(R.id.settings_reference_beacon)
    private CustomTextView beacon;
    @BindId(R.id.settings_reference_ag)
    private CustomTextView agThrehold;
    @BindId(R.id.settings_reference_dvb)
    private CustomTextView dvbSymbolRate;

    @BindId(R.id.pop_dialog_third_button)
    private CustomButton thirdButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_satellite);

        ViewInject.inject(this, this);

        referenceToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/10/23 设置命令
            }
        });
        PopDialog popDialog = new PopDialog(this);

        popDialog.setView(findViewById(R.id.settings_reference_satellite_pop));
        Bundle bundle = new Bundle();
        bundle.putBoolean(PopDialog.SHOWSECOND, true);
        bundle.putString(PopDialog.SECOND,getString(R.string.settings_reference_message_second_line));
        bundle.putBoolean(PopDialog.SHOWTHIRD, true);

        bundle.putString(PopDialog.START,getString(R.string.follow_me_message_click));
        bundle.putString(PopDialog.END,getString(R.string.settings_reference_message_third_end));
        popDialog.setBundle(bundle);
        popDialog.setSetFirstColor(true);
        popDialog.setButtonText(this,getString(R.string.setting_button_text));
        popDialog.show();
        
    }
}
