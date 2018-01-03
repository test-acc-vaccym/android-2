package com.edroplet.qxx.saneteltabactivity.activities.guide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;

public class GuideExplodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_explode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.guide_explode_toolbar);
        toolbar.setTitle(R.string.follow_me_explode_help_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
