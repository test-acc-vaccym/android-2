package com.edroplet.qxx.saneteltabactivity.activities.guide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.edroplet.qxx.saneteltabactivity.R;

public class GuideLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_location);
        ((Toolbar) findViewById(R.id.guide_location_toolbar)).setTitle(R.string.follow_me_location_help_title);
    }
}
