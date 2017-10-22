package com.edroplet.qxx.saneteltabactivity.activities.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;

public class MainWifiSettingHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wifi_setting_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.guide_wifi_setting_help_toolbar);
        toolbar.setTitle(R.string.main_wifi_setting_help_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
