package com.edroplet.qxx.saneteltabactivity.activities.functions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;

public class MonitorHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_help);
        Toolbar toolbar =
        (Toolbar)
        findViewById(R.id.monitor_help_toolbar);
        toolbar.setTitle(R.string.functions_monitor_help_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
