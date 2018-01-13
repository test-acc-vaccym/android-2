package com.edroplet.sanetel.activities.guide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.sanetel.R;

public class GuideLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_location_help);
        Toolbar toolbar =
        ((Toolbar) findViewById(R.id.guide_location_toolbar));
        toolbar.setTitle(R.string.follow_me_location_help_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
