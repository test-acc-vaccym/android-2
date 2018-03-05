package com.edroplet.sanetel.activities.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.sanetel.R;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainMeAboutActivity extends AppCompatActivity{
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_me_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_me_about_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
