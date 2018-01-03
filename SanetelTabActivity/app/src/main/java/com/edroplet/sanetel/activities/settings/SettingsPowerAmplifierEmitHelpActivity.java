package com.edroplet.sanetel.activities.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.sanetel.R;

public class SettingsPowerAmplifierEmitHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_power_amplifier_emit_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.amplifier_help_toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
